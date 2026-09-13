package com.sentaro.yanlang.data

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.util.Log
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.libraries.ads.mobile.sdk.MobileAds
import com.google.android.libraries.ads.mobile.sdk.common.AdLoadCallback
import com.google.android.libraries.ads.mobile.sdk.common.AdRequest
import com.google.android.libraries.ads.mobile.sdk.common.FullScreenContentError
import com.google.android.libraries.ads.mobile.sdk.common.LoadAdError
import com.google.android.libraries.ads.mobile.sdk.initialization.InitializationConfig
import com.google.android.libraries.ads.mobile.sdk.rewarded.RewardedAd
import com.google.android.libraries.ads.mobile.sdk.rewarded.RewardedAdEventCallback
import com.google.android.libraries.ads.mobile.sdk.rewarded.ServerSideVerificationOptions
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeoutOrNull
import kotlin.coroutines.resume

class AdUnavailableException(message: String) : IllegalStateException(message)

class RewardedCreditAdManager private constructor() {
    companion object {
        private const val TAG = "RewardedCreditAdManager"
        private const val PROD_AD_UNIT_ID = "ca-app-pub-6151036058675874/1682643484"
        private const val ADMOB_APP_ID = "ca-app-pub-6151036058675874~9074048345"
        private const val LOAD_TIMEOUT_MS = 20_000L
        private const val LOAD_FAILURE_COOLDOWN_MS = 10_000L

        val shared: RewardedCreditAdManager = RewardedCreditAdManager()

        val adUnitId: String
            get() = PROD_AD_UNIT_ID
    }

    @Volatile private var initialized = false
    @Volatile private var initializationStarted = false
    @Volatile private var cachedRewardedAd: RewardedAd? = null
    @Volatile private var loadInProgress = false
    @Volatile private var nextLoadAllowedAtMs = 0L

/** プリロードとオンデマンド表示リクエストで共有される単一フライトのハンドル。 */
@Volatile private var loadResult: CompletableDeferred<Result<RewardedAd>>? = null
    private val managerScope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

    private val _adAvailable = MutableStateFlow(false)
    val adAvailable: StateFlow<Boolean> = _adAvailable.asStateFlow()

/** 適切なときにバックグラウンドプリロードを開始する。失敗したリクエストは自動的に再試行されず、短いクールダウンがAdMobをNO_FILLリクエストの嵐から保護する。 */
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
            return
        }

        Log.i(TAG, "GMA_INITIALIZE_START appId=$ADMOB_APP_ID adUnit=$PROD_AD_UNIT_ID")
        managerScope.launch(Dispatchers.IO) {
            try {
                MobileAds.initialize(
                    appContext,
                    InitializationConfig.Builder(ADMOB_APP_ID).build(),
                ) {
                    initialized = true
                    _adAvailable.value = cachedRewardedAd != null
                    Log.i(TAG, "GMA_INITIALIZED unit=$adUnitId adAvailable=${_adAvailable.value}")
                    preload()
                }
            } catch (error: Throwable) {
                initialized = false
                initializationStarted = false
                Log.e(TAG, "GMA_INITIALIZATION_FAILED", error)
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
        if (!available) Log.e(TAG, "GMS_UNAVAILABLE code=$result")
        else Log.d(TAG, "GMS_AVAILABLE")
        return available
    }

    fun isAdAvailable(): Boolean {
        val available = initialized && cachedRewardedAd != null
        _adAvailable.value = available
        return available
    }

    fun preload() {
        synchronized(this) {
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
            val now = System.currentTimeMillis()
            if (now < nextLoadAllowedAtMs) {
                Log.d(TAG, "PRELOAD_SKIPPED cooldown remaining=${nextLoadAllowedAtMs - now}ms")
                return
            }
            startLoadLocked("PRELOAD")
        }
    }

    suspend fun show(
        context: Context,
        customData: String,
        userId: String,
    ): Result<Unit> = withContext(Dispatchers.Main.immediate) {
        Log.i(TAG, "REWARDED_SHOW_REQUEST adAvailable=${_adAvailable.value} initialized=$initialized cachedRewardedAd=${cachedRewardedAd != null} inProgress=$loadInProgress")

        val activity = context.findActivity()
        if (activity == null || activity.isFinishing || activity.isDestroyed) {
            val message = "広告を表示できる画面がありません"
            Log.e(TAG, "REWARDED_SHOW_FAILED $message")
            return@withContext Result.failure(AdUnavailableException(message))
        }
        if (!initialized) {
            val message = "広告サービスを初期化できていません"
            Log.e(TAG, "REWARDED_SHOW_FAILED $message")
            return@withContext Result.failure(AdUnavailableException(message))
        }
        if (customData.isBlank() || userId.isBlank()) {
            val message = "広告認証情報が不足しています"
            Log.e(TAG, "REWARDED_SHOW_FAILED $message")
            return@withContext Result.failure(IllegalArgumentException(message))
        }

        val ad = acquireAdForShow().getOrElse { error ->
            return@withContext Result.failure(error)
        }

        try {
            ad.setServerSideVerificationOptions(ServerSideVerificationOptions(userId, customData))
            Log.d(TAG, "SSV_CONFIGURED customDataPresent=${customData.isNotBlank()} userIdPresent=${userId.isNotBlank()}")
        } catch (error: Throwable) {
            Log.e(TAG, "SSV_CONFIG_FAILED", error)
            synchronized(this@RewardedCreditAdManager) {
                cachedRewardedAd = null
                _adAvailable.value = false
            }
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
                    settle(Result.failure(AdUnavailableException("広告を利用できませんでした")))
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
                settle(Result.failure(AdUnavailableException("広告を利用できませんでした")))
                preload()
            }

            continuation.invokeOnCancellation {
                Log.d(TAG, "REWARDED_SHOW_CANCELLED")
            }
        }
    }

    private suspend fun acquireAdForShow(): Result<RewardedAd> {
        cachedRewardedAd?.let { ad ->
            cachedRewardedAd = null
            _adAvailable.value = false
            Log.i(TAG, "REWARDED_USING_PRELOADED_AD")
            return Result.success(ad)
        }

        val load = synchronized(this) {
            if (loadInProgress) {
                loadResult
            } else {
                val now = System.currentTimeMillis()
                if (now < nextLoadAllowedAtMs) {
                    Log.w(TAG, "REWARDED_LOAD_COOLDOWN active remaining=${nextLoadAllowedAtMs - now}ms")
                    null
                } else {
                    startLoadLocked("ON_DEMAND")
                    loadResult
                }
            }
        }

        if (load == null) {
            return Result.failure(AdUnavailableException("広告を利用できませんでした"))
        }

        val result = withTimeoutOrNull(LOAD_TIMEOUT_MS + 1_000L) { load.await() }
            ?: Result.failure(AdUnavailableException("広告を利用できませんでした"))

        val ad = result.getOrNull()
        if (ad != null) {
            _adAvailable.value = false
            Log.i(TAG, "REWARDED_ON_DEMAND_USING_LOADED_AD")
        }
        return result
    }

    private fun startLoadLocked(reason: String) {
        loadInProgress = true
        val deferred = CompletableDeferred<Result<RewardedAd>>()
        loadResult = deferred
        Log.i(TAG, "REWARDED_LOAD_REQUEST unit=$adUnitId production=true reason=$reason")

        try {
            RewardedAd.load(
                AdRequest.Builder(adUnitId).build(),
                object : AdLoadCallback<RewardedAd> {
                    override fun onAdLoaded(ad: RewardedAd) {
                        finishLoad(Result.success(ad), null)
                    }

                    override fun onAdFailedToLoad(error: LoadAdError) {
                        finishLoad(
                            Result.failure(AdUnavailableException("広告を利用できませんでした")),
                            loadError = error,
                        )
                    }
                },
            )
        } catch (error: Throwable) {
            finishLoad(
                Result.failure(AdUnavailableException("広告を利用できませんでした")),
                exception = error,
            )
        }
    }

    private fun finishLoad(
        result: Result<RewardedAd>,
        loadError: LoadAdError? = null,
        exception: Throwable? = null,
    ) {
        synchronized(this) {
            loadInProgress = false
            if (result.isSuccess) {
                cachedRewardedAd = result.getOrNull()
                nextLoadAllowedAtMs = 0L
                _adAvailable.value = cachedRewardedAd != null
            } else {
                cachedRewardedAd = null
                _adAvailable.value = false
                nextLoadAllowedAtMs = System.currentTimeMillis() + LOAD_FAILURE_COOLDOWN_MS
            }
            loadResult?.complete(result)
            loadResult = null
        }

        if (result.isSuccess) {
            Log.i(TAG, "REWARDED_LOAD_SUCCESS adAvailable=${_adAvailable.value}")
        } else if (loadError != null) {
            Log.e(TAG, "REWARDED_LOAD_FAILED code=${loadError.code} message=${loadError.message} adAvailable=false cooldown=${LOAD_FAILURE_COOLDOWN_MS}ms")
        } else {
            Log.e(TAG, "REWARDED_LOAD_EXCEPTION cooldown=${LOAD_FAILURE_COOLDOWN_MS}ms", exception)
        }
    }

    private fun Context.findActivity(): Activity? {
        var current: Context = this
        while (current is ContextWrapper) {
            if (current is Activity) return current
            current = current.baseContext
        }
        return current as? Activity
    }
}
