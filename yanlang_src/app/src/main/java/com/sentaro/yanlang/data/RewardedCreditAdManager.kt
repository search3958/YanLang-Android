package com.sentaro.yanlang.data

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.widget.Toast
import android.util.Log
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.libraries.ads.mobile.sdk.MobileAds
import com.google.android.libraries.ads.mobile.sdk.common.AdLoadCallback
import com.google.android.libraries.ads.mobile.sdk.common.AdRequest
import com.google.android.libraries.ads.mobile.sdk.common.FullScreenContentError
import com.google.android.libraries.ads.mobile.sdk.common.LoadAdError
import com.google.android.libraries.ads.mobile.sdk.common.RequestConfiguration
import com.google.android.libraries.ads.mobile.sdk.initialization.InitializationConfig
import com.google.android.libraries.ads.mobile.sdk.rewarded.RewardedAd
import com.google.android.libraries.ads.mobile.sdk.rewarded.RewardedAdEventCallback
import com.google.android.libraries.ads.mobile.sdk.rewarded.ServerSideVerificationOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeoutOrNull
import kotlin.coroutines.resume

class RewardedCreditAdManager private constructor() {
    companion object {
        private const val TAG = "RewardedCreditAdManager"
        private const val PROD_AD_UNIT_ID = "ca-app-pub-6151036058675874/1682643484"
        private const val ADMOB_APP_ID = "ca-app-pub-6151036058675874~9074048345"
        private const val LOAD_TIMEOUT_MS = 20_000L
        private const val RETRY_DELAY_MS = 2_000L

        val shared: RewardedCreditAdManager = RewardedCreditAdManager()

        val adUnitId: String
            get() = PROD_AD_UNIT_ID
    }

    @Volatile private var initialized = false
    @Volatile private var initializationStarted = false
    @Volatile private var cachedRewardedAd: RewardedAd? = null
    @Volatile private var loadInProgress = false
    private val _adAvailable = MutableStateFlow(false)
    val adAvailable: StateFlow<Boolean> = _adAvailable.asStateFlow()

    /**
     * Starts (or continues) the background preload. The caller can use
     * [adAvailable] to decide whether an explicit loading UI is needed.
     */
    fun ensurePreloaded() {
        Log.i(TAG, "ENSURE_PRELOADED requested initialized=$initialized cached=${cachedRewardedAd != null} inProgress=$loadInProgress")
        preload()
    }

    fun initialize(context: Context) {
        if (initializationStarted) {
            Log.d(TAG, "Initialization skipped: already started")
            return
        }

        synchronized(this) {
            if (initializationStarted) {
                Log.d(TAG, "Initialization skipped: already started")
                return
            }
            initializationStarted = true
        }

        val appContext = context.applicationContext
        if (!isGooglePlayServicesAvailable(context)) {
            initializationStarted = false
            notifyUser(appContext, "広告サービスを利用できません")
            return
        }

        // Next-Gen GMA initialization is asynchronous. Keep it off the UI thread.
        // The callback is used as the only signal that initialization completed.
        Log.i(TAG, "GMA_INITIALIZE_START appId=$ADMOB_APP_ID adUnit=$PROD_AD_UNIT_ID")

        CoroutineScope(SupervisorJob() + Dispatchers.IO).launch {
            try {
                val initBuilder = InitializationConfig.Builder(ADMOB_APP_ID)

                MobileAds.initialize(
                    appContext,
                    initBuilder.build(),
                ) {
                    initialized = true
                    _adAvailable.value = cachedRewardedAd != null
                    Log.i(TAG, "GMA_INITIALIZED unit=$adUnitId adAvailable=${_adAvailable.value}")

                    preload()
                }
            } catch (error: Throwable) {
                initialized = false
                Log.e(TAG, "GMA_INITIALIZATION_FAILED", error)
                notifyUser(appContext, "広告サービスの初期化に失敗しました")
            }
        }
    }

    fun isGooglePlayServicesAvailable(context: Context): Boolean {
        val activity = context.findActivity()
        if (activity == null) {
            Log.e(TAG, "GMS_CHECK_FAILED activity unavailable")
            return false
        }
        val result = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(activity)
        val available = result == ConnectionResult.SUCCESS
        if (!available) {
            Log.e(TAG, "GMS_UNAVAILABLE code=$result")
        }
        return available
    }

    fun isAdAvailable(): Boolean {
        val available = initialized && cachedRewardedAd != null
        _adAvailable.value = available
        return available
    }

    fun preload() {
        if (!initialized) {
            Log.d(TAG, "PRELOAD_SKIPPED sdk not initialized")
            return
        }
        if (cachedRewardedAd != null) {
            Log.d(TAG, "PRELOAD_SKIPPED ad already cached")
            return
        }
        if (loadInProgress) {
            Log.d(TAG, "PRELOAD_SKIPPED load already in progress")
            return
        }

        loadInProgress = true
        Log.i(TAG, "REWARDED_LOAD_REQUEST unit=$adUnitId production=true")

        try {
            RewardedAd.load(
                AdRequest.Builder(adUnitId).build(),
                object : AdLoadCallback<RewardedAd> {
                    override fun onAdLoaded(ad: RewardedAd) {
                        loadInProgress = false
                        cachedRewardedAd = ad
                        _adAvailable.value = true
                        Log.i(TAG, "REWARDED_LOAD_SUCCESS adAvailable=${_adAvailable.value}")
                    }

                    override fun onAdFailedToLoad(error: LoadAdError) {
                        loadInProgress = false
                        cachedRewardedAd = null
                        _adAvailable.value = false
                        Log.e(TAG, "REWARDED_LOAD_FAILED code=${error.code} message=${error.message} adAvailable=${_adAvailable.value}")
                        notifyUser(
                            currentContext,
                            "広告を読み込めませんでした: ${error.message}"
                        )
                        scheduleRetry()
                    }
                }
            )
        } catch (error: Throwable) {
            loadInProgress = false
            Log.e(TAG, "REWARDED_LOAD_EXCEPTION", error)
            notifyUser(currentContext, "広告の読み込みに失敗しました")
            scheduleRetry()
        }
    }

    suspend fun show(
        context: Context,
        customData: String,
        userId: String,
    ): Result<Unit> = withContext(Dispatchers.Main.immediate) {
        currentContext = context.applicationContext
        Log.i(TAG, "REWARDED_SHOW_REQUEST adAvailable=${_adAvailable.value} initialized=$initialized cachedRewardedAd=${cachedRewardedAd != null}")

        val activity = context.findActivity()
        if (activity == null || activity.isFinishing || activity.isDestroyed) {
            val message = "広告を表示できる画面がありません"
            Log.e(TAG, "REWARDED_SHOW_FAILED $message")
            notifyUser(context, message)
            return@withContext Result.failure(IllegalStateException(message))
        }

        if (!initialized) {
            val message = "広告サービスを初期化できていません"
            Log.e(TAG, "REWARDED_SHOW_FAILED $message")
            notifyUser(activity, message)
            return@withContext Result.failure(IllegalStateException(message))
        }

        if (customData.isBlank() || userId.isBlank()) {
            val message = "広告認証情報が不足しています"
            Log.e(TAG, "REWARDED_SHOW_FAILED $message")
            notifyUser(activity, message)
            return@withContext Result.failure(IllegalArgumentException(message))
        }

        val ad = cachedRewardedAd ?: loadAdForShow(activity).getOrElse {
            notifyUser(activity, "広告を読み込めませんでした")
            return@withContext Result.failure(it)
        }
        cachedRewardedAd = null

        try {
            ad.setServerSideVerificationOptions(
                ServerSideVerificationOptions(userId, customData)
            )
            Log.d(TAG, "SSV_CONFIGURED userId=$userId customData=$customData")
        } catch (error: Throwable) {
            Log.e(TAG, "SSV_CONFIG_FAILED", error)
            notifyUser(activity, "広告の設定に失敗しました")
            preload()
            return@withContext Result.failure(error)
        }

        suspendCancellableCoroutine { continuation ->
            var settled = false
            var earnedReward = false

            fun settle(result: Result<Unit>) {
                if (settled) return
                settled = true
                if (continuation.isActive) continuation.resume(result)
            }

            ad.adEventCallback = object : RewardedAdEventCallback {
                override fun onAdShowedFullScreenContent() {
                    Log.i(TAG, "REWARDED_SHOWN")
                }

                override fun onAdDismissedFullScreenContent() {
                    Log.i(TAG, "REWARDED_DISMISSED earned=$earnedReward")
                    settle(Result.success(Unit))
                    preload()
                }

                override fun onAdFailedToShowFullScreenContent(error: FullScreenContentError) {
                    Log.e(TAG, "REWARDED_SHOW_FAILED code=${error.code} message=${error.message}")
                    notifyUser(activity, "広告を表示できませんでした: ${error.message}")
                    settle(Result.failure(Exception("広告を表示できませんでした: ${error.message}")))
                    preload()
                }

                override fun onAdImpression() {
                    Log.d(TAG, "REWARDED_IMPRESSION")
                }

                override fun onAdClicked() {
                    Log.d(TAG, "REWARDED_CLICKED")
                }
            }

            try {
                ad.show(activity) { reward ->
                    earnedReward = true
                    Log.i(TAG, "REWARDED_EARNED amount=${reward.amount} type=${reward.type}")
                }
            } catch (error: Throwable) {
                Log.e(TAG, "REWARDED_SHOW_EXCEPTION", error)
                notifyUser(activity, "広告を表示できませんでした")
                settle(Result.failure(error))
                preload()
            }

            continuation.invokeOnCancellation {
                Log.d(TAG, "REWARDED_SHOW_CANCELLED")
            }
        }
    }

    private suspend fun loadAdForShow(context: Context): Result<RewardedAd> {
        Log.d(TAG, "REWARDED_ON_DEMAND_LOAD_START")

        if (loadInProgress) {
            var waited = 0L
            while (loadInProgress && waited < LOAD_TIMEOUT_MS) {
                delay(100L)
                waited += 100L
            }

            cachedRewardedAd?.let {
                cachedRewardedAd = null
                Log.i(TAG, "REWARDED_ON_DEMAND_USING_PRELOADED_AD waited=${waited}ms")
                return Result.success(it)
            }

            if (loadInProgress) {
                loadInProgress = false
                Log.e(TAG, "REWARDED_PRELOAD_TIMEOUT waited=${waited}ms")
            }
        }

        loadInProgress = true

        return try {
            val result = withTimeoutOrNull(LOAD_TIMEOUT_MS) {
                suspendCancellableCoroutine<Result<RewardedAd>> { continuation ->
                    var resumed = false

                    fun resumeOnce(value: Result<RewardedAd>) {
                        if (resumed) return
                        resumed = true
                        loadInProgress = false
                        if (continuation.isActive) continuation.resume(value)
                    }

                    try {
                        RewardedAd.load(
                            AdRequest.Builder(adUnitId).build(),
                            object : AdLoadCallback<RewardedAd> {
                                override fun onAdLoaded(ad: RewardedAd) {
                                    Log.i(TAG, "REWARDED_ON_DEMAND_LOAD_SUCCESS")
                                    resumeOnce(Result.success(ad))
                                }

                                override fun onAdFailedToLoad(error: LoadAdError) {
                                    Log.e(TAG, "REWARDED_ON_DEMAND_LOAD_FAILED code=${error.code} message=${error.message}")
                                    resumeOnce(
                                        Result.failure(
                                            IllegalStateException("広告を読み込めませんでした: ${error.message}")
                                        )
                                    )
                                }
                            }
                        )
                    } catch (error: Throwable) {
                        Log.e(TAG, "REWARDED_ON_DEMAND_LOAD_EXCEPTION", error)
                        resumeOnce(Result.failure(error))
                    }

                    continuation.invokeOnCancellation {
                        loadInProgress = false
                        Log.d(TAG, "REWARDED_ON_DEMAND_LOAD_CANCELLED")
                    }
                }
            }

            if (result != null) {
                result
            } else {
                loadInProgress = false
                Log.e(TAG, "REWARDED_ON_DEMAND_LOAD_TIMEOUT ${LOAD_TIMEOUT_MS}ms")
                notifyUser(context, "広告の読み込みがタイムアウトしました")
                scheduleRetry()
                Result.failure(IllegalStateException("広告の読み込みがタイムアウトしました"))
            }
        } catch (error: Throwable) {
            loadInProgress = false
            Log.e(TAG, "REWARDED_ON_DEMAND_LOAD_ERROR", error)
            notifyUser(context, "広告を読み込めませんでした")
            scheduleRetry()
            Result.failure(error)
        }
    }

    private fun scheduleRetry() {
        if (!initialized || cachedRewardedAd != null || loadInProgress) return
        Thread {
            try {
                Thread.sleep(RETRY_DELAY_MS)
            } catch (_: InterruptedException) {
                return@Thread
            }
            if (!loadInProgress && cachedRewardedAd == null) {
                preload()
            }
        }.start()
    }

    private fun notifyUser(context: Context?, message: String) {
        val safeContext = context ?: return
        android.os.Handler(android.os.Looper.getMainLooper()).post {
            Toast.makeText(safeContext, message, Toast.LENGTH_LONG).show()
        }
    }

    private var currentContext: Context? = null

    private fun Context.findActivity(): Activity? {
        var current: Context = this
        while (current is ContextWrapper) {
            if (current is Activity) return current
            current = current.baseContext
        }
        return current as? Activity
    }
}