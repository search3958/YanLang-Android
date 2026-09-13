package com.sentaro.yanlang.ui
import android.widget.Toast

import android.util.Log

import com.sentaro.yanlang.R

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.ui.graphics.Brush
import coil.compose.rememberAsyncImagePainter
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.Article
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Style
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Whatshot
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.sentaro.yanlang.data.AppState
import com.sentaro.yanlang.data.AuthRepository
import com.sentaro.yanlang.data.CreditInfo
import com.sentaro.yanlang.data.ComprehensionQuestion
import com.sentaro.yanlang.data.ComprehensionQuestionType
import com.sentaro.yanlang.data.LearningDocument
import com.sentaro.yanlang.data.LearningEngine
import com.sentaro.yanlang.data.LearningRepository
import com.sentaro.yanlang.data.LearningStep
import com.sentaro.yanlang.data.LearningToken
import com.sentaro.yanlang.data.LearningApiException
import com.sentaro.yanlang.data.SupabaseLearningEngine
import com.sentaro.yanlang.data.TokenKind
import com.sentaro.yanlang.data.VocabularyRepository
import com.sentaro.yanlang.data.VocabularyEntry
import com.sentaro.yanlang.data.VocabularyLanguage
import com.sentaro.yanlang.data.VocabularyTranslation
import com.sentaro.yanlang.data.RewardedCreditAdManager
import com.sentaro.yanlang.domain.reward.RewardCreditCoordinator
import com.sentaro.yanlang.domain.reward.VerificationPendingException
import com.sentaro.yanlang.data.CustomVocabularyBook
import com.sentaro.yanlang.data.CustomVocabularyEntry
import com.sentaro.yanlang.data.CustomVocabularyRepository
import com.sentaro.yanlang.BuildConfig
import com.google.firebase.analytics.FirebaseAnalytics
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.platform.LocalUriHandler
import android.view.HapticFeedbackConstants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.delay
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.compose.ui.platform.LocalLifecycleOwner
import java.util.UUID
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

private const val CREDIT_EXHAUSTION_MARKER = "1日のトークン上限"

private fun findLearningApiException(error: Throwable?): LearningApiException? {
    var current = error
    while (current != null) {
        if (current is LearningApiException) {
            return current
        }
        current = current.cause
    }
    return null
}

private fun responseIndicatesCreditExhaustion(responseText: String?): Boolean {
    if (responseText.isNullOrBlank()) {
        println("[YanLangApp] Credit exhaustion check skipped: empty response")
        return false
    }

    val errorValue = runCatching {
        org.json.JSONObject(responseText).opt("error")?.toString().orEmpty()
    }.getOrElse {
        println("[YanLangApp] Credit exhaustion JSON parse failed; falling back to raw response")
        ""
    }

    val matched = errorValue.contains(CREDIT_EXHAUSTION_MARKER) ||
        responseText.contains(CREDIT_EXHAUSTION_MARKER)
    println("[YanLangApp] Credit exhaustion check: matched=$matched marker=$CREDIT_EXHAUSTION_MARKER")
    return matched
}

internal enum class CreditRewardPhase {
    LOADING_AD,
    RESETTING_CREDITS,
}

@Composable
internal fun hapticAction(action: () -> Unit): () -> Unit {
    val hapticFeedback = LocalHapticFeedback.current
    return {
        hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
        action()
    }
}

@Composable
fun YanLangApp(
    repository: LearningRepository,
    authRepository: AuthRepository = remember { AuthRepository() },
    engine: LearningEngine? = null,
) {
    val actualEngine = engine ?: remember { SupabaseLearningEngine(authRepository = authRepository) }
    val stateViewModel: YanLangViewModel = viewModel(
        factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T =
                YanLangViewModel(repository) as T
        },
    )
    val appState = stateViewModel.appState
    // AnimatedContentによって保持される画面が、ナビゲーションターゲットを変更せずに設定変更を観測できるように、安定したState参照を保持する。
    val wordPronunciationEnabledState = rememberUpdatedState(appState.wordPronunciationEnabled)
    var navigationHistory by remember {
        mutableStateOf(listOf(LearningStep.LIBRARY))
    }
    var aiBusy by remember { mutableStateOf(false) }
    var aiError by remember { mutableStateOf<String?>(null) }
    var aiErrorDetails by remember { mutableStateOf<String?>(null) }
    var completedMenuDocument by remember { mutableStateOf<LearningDocument?>(null) }
    var vocabularyFile by rememberSaveable { mutableStateOf<String?>(null) }
    var vocabularyTestFile by rememberSaveable { mutableStateOf<String?>(null) }
    var customVocabularyScreen by rememberSaveable { mutableStateOf<String?>(null) }
    var vocabularyEntryIndex by rememberSaveable { mutableStateOf<Int?>(null) }
    var vocabularyLanguageCode by rememberSaveable { mutableStateOf("ja") }
    var vocabularyExitTick by rememberSaveable { mutableStateOf(0) }
    var librarySection by rememberSaveable { mutableStateOf(0) }
    var rootTab by rememberSaveable { mutableStateOf(0) }
    var singlePageMode by rememberSaveable { mutableStateOf(false) }
    var creditInfo by remember { mutableStateOf<CreditInfo?>(null) }
    var showCreditEmptyDialog by rememberSaveable { mutableStateOf(false) }
    var creditRewardBusy by remember { mutableStateOf(false) }
    var creditRewardPhase by remember { mutableStateOf<CreditRewardPhase?>(null) }
    var creditRewardResult by remember { mutableStateOf<String?>(null) }
    var creditRewardSuccess by remember { mutableStateOf(false) }
    val rewardedCreditAdManager = RewardedCreditAdManager.shared
    val rewardCreditCoordinator = remember(authRepository) { RewardCreditCoordinator(authRepository, rewardedCreditAdManager) }
    val context = LocalContext.current
    val activity = context as? android.app.Activity
    var showExampleSentences by rememberSaveable { mutableStateOf(false) }
    val appScope = rememberCoroutineScope()

    val refreshCredits: () -> Unit = {
        appScope.launch {
            val refreshed = authRepository.fetchCredits().getOrNull()
            creditInfo = refreshed
            if (refreshed == null) {
                println("[YanLangApp] Credit refresh failed")
            } else {
                println("[YanLangApp] Credit refresh success: remainingTokens=${refreshed.remainingTokens}")
            }
        }
    }

    fun startRewardedCreditReset() {
        println("[YanLangApp] Rewarded credit reset requested")
        if (creditRewardBusy) {
            println("[YanLangApp] Rewarded credit reset ignored: already busy")
            return
        }

        val userId = authRepository.currentUserId?.trim()
        println("[YanLangApp] Reward user UUID available=${!userId.isNullOrBlank()}")
        if (userId.isNullOrBlank()) {
            creditRewardSuccess = false
            creditRewardResult = context.getString(R.string.credit_reward_missing_user)
            println("[YanLangApp] Rewarded credit reset failed: authenticated user is missing")
            return
        }

        creditRewardBusy = true
        creditRewardPhase = null
        creditRewardResult = null
        creditRewardSuccess = false

        appScope.launch {
            try {
                val result = rewardCreditCoordinator.run(
                    context = activity ?: context,
                    userId = userId,
                    onLoadingAd = {
                        creditRewardPhase = CreditRewardPhase.LOADING_AD
                        println("[YanLangApp] Rewarded ad is not cached; showing ad-loading dialog")
                    },
                    onVerifying = {
                        creditRewardPhase = CreditRewardPhase.RESETTING_CREDITS
                        println("[YanLangApp] Credit reset request/polling dialog shown")
                    },
                )

                result.onSuccess { status ->
                    if (status.status == RewardCreditCoordinator.VERIFIED_STATUS) {
                        creditRewardSuccess = true
                        creditRewardResult = context.getString(R.string.credit_reward_success_message)
                        println("[YanLangApp] Waiting 1 second before refreshing credit display")
                        delay(1_000L)
                        refreshCredits()
                        println("[YanLangApp] Rewarded credit reset completed by server remaining=${status.remainingTokens}")
                    } else {
                        creditRewardSuccess = false
                        creditRewardResult = when (status.status) {
                            RewardCreditCoordinator.EXPIRED_STATUS -> context.getString(R.string.credit_reward_expired_message)
                            RewardCreditCoordinator.FAILED_STATUS -> context.getString(R.string.credit_reward_verification_failed)
                            else -> context.getString(R.string.credit_reward_verification_pending)
                        }
                        println("[YanLangApp] Rewarded credit reset was not verified: status=${status.status}")
                    }
                }.onFailure { error ->
                    throw error
                }
            } catch (error: Throwable) {
                creditRewardSuccess = false
                when (error) {
                    is com.sentaro.yanlang.data.AdUnavailableException -> {
                        Toast.makeText(
                            context.applicationContext,
                            context.getString(R.string.credit_reward_ad_unavailable),
                            Toast.LENGTH_SHORT,
                        ).show()
                        creditRewardResult = context.getString(R.string.credit_reward_ad_unavailable_message)
                        println("[YanLangApp] Rewarded credit reset failed: ad unavailable")
                    }
                    is VerificationPendingException -> {
                        creditRewardResult = context.getString(R.string.credit_reward_verification_pending)
                        println("[YanLangApp] Rewarded credit reset failed: verification polling exhausted")
                    }
                    else -> {
                        creditRewardResult = context.getString(R.string.credit_reward_generic_error)
                        println("[YanLangApp] Rewarded credit reset failed: ${error.message}")
                    }
                }
            } finally {
                creditRewardBusy = false
                creditRewardPhase = null
                println("[YanLangApp] Rewarded credit reset workflow finished")
            }
        }
    }

    fun handleAiFailure(error: Throwable, fallbackMessage: String) {
        val apiError = findLearningApiException(error)
        val creditExhausted = apiError?.isCreditExhausted == true ||
            apiError?.rawResponse?.let(::responseIndicatesCreditExhaustion) == true

        if (creditExhausted) {
            showCreditEmptyDialog = true
            aiError = null
            aiErrorDetails = null
            println("[YanLangApp] Showing empty credit dialog: marker detected")
            refreshCredits()
        } else {
            aiError = error.message ?: fallbackMessage
            aiErrorDetails = apiError?.rawResponse
            println("[YanLangApp] AI operation failed: ${error.message}")
        }
    }

    androidx.compose.runtime.LaunchedEffect(Unit) {
        refreshCredits()
    }

    val lifecycleOwner = LocalLifecycleOwner.current
    androidx.compose.runtime.DisposableEffect(lifecycleOwner) {
        val observer = object : DefaultLifecycleObserver {
            override fun onResume(owner: LifecycleOwner) {
                refreshCredits()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
    var onboardingCompleted by remember {
        mutableStateOf(
            context.getSharedPreferences(ONBOARDING_PREFS, android.content.Context.MODE_PRIVATE)
                .getBoolean(ONBOARDING_COMPLETED, false),
        )
    }
    val analyticsPreferences = remember {
        context.getSharedPreferences(ANALYTICS_PREFS, android.content.Context.MODE_PRIVATE)
    }
    var analyticsConsentGranted by remember {
        mutableStateOf(analyticsPreferences.getBoolean(ANALYTICS_CONSENT_GRANTED, false))
    }
    val firebaseAnalytics = remember { FirebaseAnalytics.getInstance(context) }
    androidx.compose.runtime.LaunchedEffect(analyticsConsentGranted) {
        firebaseAnalytics.setAnalyticsCollectionEnabled(analyticsConsentGranted)
    }
    var customVocabularyBooks by remember { mutableStateOf(CustomVocabularyRepository.load(context)) }
    val activeDocument = appState.documents.firstOrNull {
        it.id == appState.activeDocumentId
    }
    val nativeLanguage = nativeLanguageName(
        appState.nativeLanguageCode,
        appState.customNativeLanguage,
    )

    fun commit(newState: AppState, recordNavigation: Boolean = true) {
        if (recordNavigation) {
            val stepChanged = newState.currentStep != appState.currentStep
            val documentChanged = newState.activeDocumentId != appState.activeDocumentId
            if (newState.currentStep == LearningStep.LIBRARY ||
                newState.activeDocumentId == null
            ) {
                navigationHistory = listOf(LearningStep.LIBRARY)
            } else if (stepChanged || documentChanged) {
                navigationHistory = navigationHistory + newState.currentStep
            }
        }
        stateViewModel.commit(newState)
    }

    fun updateDocument(transform: (LearningDocument) -> LearningDocument) {
        val id = appState.activeDocumentId ?: return
        stateViewModel.updateDocument(id, transform)
    }

    fun navigate(step: LearningStep, recordNavigation: Boolean = true) {
        aiError = null
        aiErrorDetails = null
        val id = appState.activeDocumentId
        commit(
            appState.copy(
                documents = appState.documents.map { document ->
                    if (document.id == id) {
                        document.copy(
                            step = step,
                            updatedAt = System.currentTimeMillis(),
                        )
                    } else {
                        document
                    }
                },
                currentStep = step,
            ),
            recordNavigation = recordNavigation,
        )
    }

    fun navigateToLibrary() {
        if (aiBusy) return
        singlePageMode = false
        completedMenuDocument = null
        commit(
            appState.copy(
                activeDocumentId = null,
                currentStep = LearningStep.LIBRARY,
            ),
            recordNavigation = false,
        )
        navigationHistory = listOf(LearningStep.LIBRARY)
    }

    fun recordActivity() {
        val today = SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date())
        if (today !in appState.activityDates) {
            stateViewModel.commit(appState.copy(activityDates = appState.activityDates + today))
        }
    }

    fun navigateBack() {
        if (aiBusy) return
        if (navigationHistory.size <= 1) {
            navigateToLibrary()
        } else {
            val previousHistory = navigationHistory.dropLast(1)
            val previous = previousHistory.last()
            navigationHistory = previousHistory
            if (previous == LearningStep.LIBRARY) {
                commit(
                    appState.copy(
                        activeDocumentId = null,
                        currentStep = previous,
                    ),
                    recordNavigation = false,
                )
            } else {
                navigate(previous, recordNavigation = false)
            }
        }
    }

    fun navigateToPreviousLearningStep() {
        if (aiBusy) return
        val previous = when (appState.currentStep) {
            LearningStep.LIBRARY,
            LearningStep.EDITOR,
            -> return
            LearningStep.WORDBOOK -> LearningStep.EDITOR
            LearningStep.WORD_CHECK -> LearningStep.WORDBOOK
            LearningStep.CONNECTOR_CHECK -> LearningStep.WORD_CHECK
            LearningStep.FINAL_TRANSLATION -> LearningStep.CONNECTOR_CHECK
            LearningStep.COMPREHENSION_CHECK -> LearningStep.FINAL_TRANSLATION
        }
        navigate(previous, recordNavigation = false)
    }

    BackHandler(enabled = showExampleSentences || appState.currentStep != LearningStep.LIBRARY || vocabularyFile != null || customVocabularyScreen != null) {
        if (showExampleSentences) {
            showExampleSentences = false
        } else if (vocabularyEntryIndex != null) vocabularyEntryIndex = null
        else if (customVocabularyScreen != null) customVocabularyScreen = null
        else if (vocabularyTestFile != null) vocabularyTestFile = null
        else if (vocabularyFile != null) vocabularyFile = null
        else if (singlePageMode) navigateToLibrary() else navigateBack()
    }

    Surface(modifier = Modifier.fillMaxSize()) {
        CreditRewardDialogs(
            showCreditEmptyDialog = showCreditEmptyDialog,
            rewardBusy = creditRewardBusy,
            rewardPhase = creditRewardPhase,
            rewardResult = creditRewardResult,
            rewardSuccess = creditRewardSuccess,
            onDismissEmpty = {
                if (!creditRewardBusy) {
                    showCreditEmptyDialog = false
                    println("[YanLangApp] Empty credit dialog dismissed")
                }
            },
            onStartReward = {
                showCreditEmptyDialog = false
                startRewardedCreditReset()
                println("[YanLangApp] Rewarded ad reset requested from empty credit dialog")
            },
            onCloseEmpty = {
                showCreditEmptyDialog = false
                println("[YanLangApp] Empty credit dialog closed")
            },
            onDismissResult = {
                creditRewardResult = null
                println("[YanLangApp] Reward result dialog dismissed")
            },
        )

        if (!onboardingCompleted) {
            OnboardingFlow(
                initialLanguageCode = appState.nativeLanguageCode,
                initialCustomLanguage = appState.customNativeLanguage,
                onLanguageComplete = { code, customLanguage ->
                    commit(
                        appState.copy(
                            nativeLanguageCode = code,
                            customNativeLanguage = customLanguage,
                        ),
                        recordNavigation = false,
                    )
                },
                onConsent = {
                    commit(
                        appState.copy(
                            nativeLanguageCode = appState.nativeLanguageCode,
                            customNativeLanguage = appState.customNativeLanguage,
                        ),
                        recordNavigation = false,
                    )
                    context.getSharedPreferences(
                        ONBOARDING_PREFS,
                        android.content.Context.MODE_PRIVATE,
                    ).edit().putBoolean(ONBOARDING_COMPLETED, true).apply()
                    onboardingCompleted = true
                    analyticsConsentGranted = true
                    analyticsPreferences.edit().putBoolean(ANALYTICS_CONSENT_GRANTED, true).apply()
                    firebaseAnalytics.setAnalyticsCollectionEnabled(true)
                    appScope.launch {
                        authRepository.signInAnonymously().onSuccess { userId ->
                            println("[YanLangApp] Signed in anonymously: $userId")
                        }.onFailure { e ->
                            println("[YanLangApp] Failed to sign in: ${e.message}")
                        }
                    }
                },
                modifier = Modifier.fillMaxSize(),
            )
        } else if (showExampleSentences) {
            ExampleSentenceScreen(
                onBack = {
                    showExampleSentences = false
                    println("[YanLangApp] Example sentence screen closed")
                },
                onSelect = { targetLanguage, title, text ->
                    val documentId = appState.activeDocumentId
                    if (documentId == null) {
                        println("[YanLangApp] Example sentence selection failed: active document is missing")
                    } else {
                        updateDocument {
                            it.copy(
                                targetLanguage = targetLanguage,
                                title = title,
                                sourceText = text.take(MAX_SOURCE_LENGTH),
                                finalScore = null,
                                finalMistakes = emptyList(),
                                comprehensionQuestions = emptyList(),
                                comprehensionScore = null,
                            )
                        }
                        showExampleSentences = false
                        println("[YanLangApp] Example sentence applied: language=$targetLanguage title=$title")
                    }
                },
            )
        } else {
        val vocabularyRoute = vocabularyTestFile?.let { "test:$it" }
            ?: vocabularyFile?.let { "asset:$it" }
            ?: customVocabularyScreen
        val screenTargetState = Triple(appState.currentStep, activeDocument?.id, vocabularyRoute)
        AnimatedContent(
            targetState = screenTargetState,
            transitionSpec = {
                val isForward = when {
                    targetState.first.ordinal != initialState.first.ordinal ->
                        targetState.first.ordinal > initialState.first.ordinal
                    targetState.third != initialState.third -> targetState.third != null
                    else -> targetState.second != null
                }
                val easing = CubicBezierEasing(0.3f, 0.01f, 0.6f, 1f)
                if (isForward) {
                    slideInHorizontally(
                        animationSpec = tween(300, easing = easing),
                        initialOffsetX = { it },
                    ) togetherWith slideOutHorizontally(
                        animationSpec = tween(300, easing = easing),
                        targetOffsetX = { -it / 2 },
                    )
                } else {
                    slideInHorizontally(
                        animationSpec = tween(300, easing = easing),
                        initialOffsetX = { -it },
                    ) togetherWith slideOutHorizontally(
                        animationSpec = tween(300, easing = easing),
                        targetOffsetX = { it },
                    )
                }
            },
            label = "screen",
        ) { contentState ->
            val (step, documentId, targetVocabularyRoute) = contentState
            // 遷移ターゲットを最新の外部状態の代わりに使用し、出力コンテンツが実際に離れている画面のままにする。
            val activeDocument = appState.documents.firstOrNull { it.id == documentId }
            val outgoingScrimAlpha by animateFloatAsState(
                targetValue = if (contentState == screenTargetState) 0f else 0.5f,
                animationSpec = tween(300, easing = CubicBezierEasing(0.3f, 0.01f, 0.6f, 1f)),
                label = "outgoingScrim",
            )
            Box {
            when {
                step == LearningStep.LIBRARY && targetVocabularyRoute?.startsWith("test:") == true -> {
                    val targetVocabularyFile = targetVocabularyRoute.removePrefix("test:")
                    val vocabulary = remember(targetVocabularyFile) {
                        VocabularyRepository.load(context, targetVocabularyFile)
                    }
                    FixedVocabularyTestScreen(
                        entries = vocabulary.second,
                        languageCode = vocabularyLanguageCode,
                        nativeLanguageCode = appState.nativeLanguageCode,
                        onBack = { vocabularyTestFile = null },
                    )
                }
                step == LearningStep.LIBRARY && targetVocabularyRoute?.startsWith("asset:") == true -> {
                    val targetVocabularyFile = targetVocabularyRoute.removePrefix("asset:")
                    val vocabulary = remember(targetVocabularyFile) {
                        VocabularyRepository.load(context, targetVocabularyFile)
                    }
                    VocabularyLibraryScreen(
                        languages = vocabulary.first,
                        entries = vocabulary.second,
                        themeName = vocabularyThemeName(targetVocabularyFile),
                        initialIndex = 0,
                        languageCode = vocabularyLanguageCode,
                        nativeLanguageCode = appState.nativeLanguageCode,
                        wordPronunciationEnabled = appState.wordPronunciationEnabled,
                        onBack = { vocabularyEntryIndex = null; vocabularyTestFile = null; vocabularyFile = null; vocabularyExitTick++ },
                        onStartTest = {
                            val file = vocabularyFile
                            if (file.isNullOrBlank()) {
                                println("YanLangApp: fixed vocabulary test start failed: vocabularyFile is missing")
                            } else {
                                vocabularyTestFile = file
                                println("YanLangApp: fixed vocabulary test started: $file")
                            }
                        },
                        modifier = Modifier.fillMaxSize(),
                    )
                }
                step == LearningStep.LIBRARY && targetVocabularyRoute?.startsWith("view:") == true -> {
                    val bookId = targetVocabularyRoute.removePrefix("view:")
                    val book = customVocabularyBooks.firstOrNull { it.id == bookId }
                    if (book == null) {
                        androidx.compose.runtime.LaunchedEffect(bookId) { customVocabularyScreen = null }
                    } else {
                        CustomVocabularyLibraryScreen(
                            book = book,
                            onBack = { customVocabularyScreen = null; vocabularyExitTick++ },
                            onEdit = { customVocabularyScreen = "edit:${book.id}" },
                            onDelete = {
                                customVocabularyBooks = customVocabularyBooks.filterNot { it.id == book.id }
                                CustomVocabularyRepository.save(context, customVocabularyBooks)
                                customVocabularyScreen = null
                            },
                            modifier = Modifier.fillMaxSize(),
                        )
                    }
                }
                step == LearningStep.LIBRARY &&
                    (targetVocabularyRoute == "new" || targetVocabularyRoute?.startsWith("edit:") == true) -> {
                    val editingId = targetVocabularyRoute?.removePrefix("edit:")
                    val initialBook = customVocabularyBooks.firstOrNull { it.id == editingId }
                        ?: CustomVocabularyBook()
                    CustomVocabularyEditorScreen(
                        initialBook = initialBook,
                        isNew = targetVocabularyRoute == "new",
                        onBack = { customVocabularyScreen = null },
                        onSave = { saved ->
                            customVocabularyBooks =
                                if (customVocabularyBooks.any { it.id == saved.id }) {
                                    customVocabularyBooks.map { if (it.id == saved.id) saved else it }
                                } else {
                                    listOf(saved) + customVocabularyBooks
                            }
                            CustomVocabularyRepository.save(context, customVocabularyBooks)
                            customVocabularyScreen = "view:${saved.id}"
                        },
                        modifier = Modifier.fillMaxSize(),
                    )
                }
                step == LearningStep.LIBRARY || activeDocument == null -> {
                    LibraryScreen(
                        documents = appState.documents,
                        onCreate = {
                            val id = UUID.randomUUID().toString()
                            val document = LearningDocument(id = id)
                            commit(
                                appState.copy(
                                    documents = listOf(document) + appState.documents,
                                    activeDocumentId = id,
                                    currentStep = LearningStep.EDITOR,
                                ),
                            )
                        },
                        onSelect = { document ->
                            if (document.comprehensionScore != null) {
                                completedMenuDocument = document
                            } else {
                                commit(
                                    appState.copy(
                                        activeDocumentId = document.id,
                                        currentStep = document.step,
                                        activityDates = if (document.step == LearningStep.WORDBOOK) {
                                            appState.activityDates + todayKey()
                                        } else {
                                            appState.activityDates
                                        },
                                    ),
                                )
                            }
                        },
                        onStartStep = { document, step, onePage ->
                            singlePageMode = onePage
                            commit(
                                appState.copy(
                                    activeDocumentId = document.id,
                                    currentStep = step,
                                    activityDates = if (step == LearningStep.WORDBOOK) {
                                        appState.activityDates + todayKey()
                                    } else {
                                        appState.activityDates
                                    },
                                ),
                            )
                        },
                        onDelete = { document ->
                            val latestState = stateViewModel.appState
                            commit(
                                latestState.copy(
                                    documents = latestState.documents.filterNot {
                                        it.id == document.id
                                    },
                                ),
                            )
                        },
                        onRestore = { document ->
                            val latestState = stateViewModel.appState
                            commit(
                                latestState.copy(
                                    documents = listOf(document) +
                                        latestState.documents.filterNot { it.id == document.id },
                                ),
                            )
                        },
                        onOpenVocabulary = { file, language ->
                            recordActivity()
                            vocabularyTestFile = null
                            vocabularyFile = file
                            vocabularyLanguageCode = language
                        },
                        customVocabularyBooks = customVocabularyBooks,
                        onOpenCustomVocabulary = { book ->
                            recordActivity()
                            vocabularyTestFile = null
                            customVocabularyScreen = "view:${book.id}"
                        },
                        onCreateVocabulary = { customVocabularyScreen = "new" },
                        returnToLongTextKey = vocabularyExitTick,
                        vocabularyLanguageCode = vocabularyLanguageCode,
                        librarySection = librarySection,
                        onSectionChange = { librarySection = it },
                        rootTab = rootTab,
                        onRootTabChange = { rootTab = it },
                        nativeLanguageCode = appState.nativeLanguageCode,
                        customNativeLanguage = appState.customNativeLanguage,
                        wordPronunciationEnabledState = wordPronunciationEnabledState,
                        onNativeLanguageChange = { code, custom ->
                            commit(
                                appState.copy(
                                    nativeLanguageCode = code,
                                    customNativeLanguage = custom,
                                ),
                                recordNavigation = false,
                            )
                        },
                        onWordPronunciationEnabledChange = { enabled ->
                            Log.d("YanLangSettings", "Word pronunciation setting changed: enabled=$enabled")
                            commit(
                                appState.copy(wordPronunciationEnabled = enabled),
                                recordNavigation = false,
                            )
                        },
                        activityDates = appState.activityDates,
                        creditInfo = creditInfo,
                        onRefreshCredits = refreshCredits,
                        onWatchRewardedAd = {
                            Log.i("YanLangReward", "YANLANG_APP_CALLBACK_ENTERED")
                            startRewardedCreditReset()
                        },
                    )
                }

                step == LearningStep.EDITOR -> {
                    EditorScreen(
                        document = activeDocument,
                        onBack = ::navigateBack,
                        onTopBarBack = ::navigateToLibrary,
                        isProcessing = aiBusy,
                        errorMessage = aiError,
                        errorDetails = aiErrorDetails,
                        onChange = { targetLanguage, title, source ->
                            updateDocument {
                                it.copy(
                                    targetLanguage = targetLanguage,
                                    title = title,
                                    sourceText = source.take(MAX_SOURCE_LENGTH),
                                    finalScore = null,
                                    finalMistakes = emptyList(),
                                    comprehensionQuestions = emptyList(),
                                    comprehensionScore = null,
                                )
                            }
                        },
                        onOpenExamples = {
                            showExampleSentences = true
                            println("[YanLangApp] Opening example sentence screen")
                        },
                        onComplete = {
                            aiBusy = true
                            aiError = null
                            aiErrorDetails = null
                            val source = activeDocument.sourceText
                            val documentId = activeDocument.id
                            appScope.launch {
                                runCatching {
                                    withContext(Dispatchers.IO) {
                                        actualEngine.analyze(
                                            source = source,
                                            targetLanguage = activeDocument.targetLanguage,
                                            nativeLanguage = nativeLanguage,
                                        )
                                    }
                                }.onSuccess { tokens ->
                                    commit(
                                        appState.copy(
                                            documents = appState.documents.map { document ->
                                                if (document.id == documentId) {
                                                    document.copy(
                                                        tokens = tokens,
                                                        wordbookIndex = 0,
                                                        wordbookRevealed = false,
                                                        revealedWordIds = emptySet(),
                                                        finalScore = null,
                                                        finalMistakes = emptyList(),
                                                        comprehensionQuestions = emptyList(),
                                                        comprehensionScore = null,
                                                        step = LearningStep.WORDBOOK,
                                                        updatedAt = System.currentTimeMillis(),
                                                    )
                                                } else {
                                                    document
                                                }
                                            },
                                            currentStep = LearningStep.WORDBOOK,
                                            activityDates = appState.activityDates + todayKey(),
                                        ),
                                    )
                                }.onFailure { error ->
                                    handleAiFailure(error, "AI解析に失敗しました")
                                }
                                aiBusy = false
                            }
                        },
                    )
                }

                step == LearningStep.WORDBOOK -> {
                    WordbookScreen(
                        document = activeDocument,
                        singlePageMode = singlePageMode,
                        onBack = ::navigateToPreviousLearningStep,
                        onTopBarBack = ::navigateToLibrary,
                        onReveal = { tokenId ->
                            updateDocument {
                                val revealed = it.revealedWordIds
                                it.copy(
                                    wordbookRevealed = false,
                                    revealedWordIds = if (tokenId in revealed) {
                                        revealed - tokenId
                                    } else {
                                        revealed + tokenId
                                    },
                                )
                            }
                        },
                        onMove = { index ->
                            updateDocument {
                                it.copy(
                                    wordbookIndex = index.coerceIn(
                                        0,
                                        (it.tokens.size - 1).coerceAtLeast(0),
                                    ),
                                    wordbookRevealed = false,
                                )
                            }
                        },
                         onComplete = {
                             if (singlePageMode) navigateToLibrary()
                             else navigate(LearningStep.WORD_CHECK)
                         },
                         wordPronunciationEnabled = appState.wordPronunciationEnabled,
                     )
                }

                step == LearningStep.WORD_CHECK -> {
                    WordCheckScreen(
                        document = activeDocument,
                        singlePageMode = singlePageMode,
                        engine = actualEngine,
                        onBack = ::navigateToPreviousLearningStep,
                        onTopBarBack = ::navigateToLibrary,
                        onTokenChange = { changed ->
                            updateDocument { document ->
                                document.copy(
                                    tokens = document.tokens.map {
                                        if (it.id == changed.id) changed else it
                                    },
                                )
                            }
                        },
                        onSkipKnown = {
                            updateDocument { document ->
                                document.copy(
                                    tokens = document.tokens.map { token ->
                                        if (token.kind == TokenKind.WORD &&
                                            token.isCorrect == null
                                        ) {
                                            token.copy(
                                                answer = token.translation,
                                                isCorrect = true,
                                            )
                                        } else {
                                            token
                                        }
                                    },
                                )
                            }
                        },
                        onReset = {
                            updateDocument { document ->
                                document.copy(
                                    tokens = document.tokens.map { token ->
                                        if (token.kind == TokenKind.WORD) {
                                            token.copy(answer = "", isCorrect = null)
                                        } else {
                                            token
                                        }
                                    },
                                )
                            }
                        },
                        onComplete = {
                            if (singlePageMode) {
                                navigateToLibrary()
                            } else if (activeDocument.connectorTokens.isEmpty()) {
                                navigate(LearningStep.FINAL_TRANSLATION)
                            } else {
                                navigate(LearningStep.CONNECTOR_CHECK)
                            }
                        },
                    )
                }

                step == LearningStep.CONNECTOR_CHECK -> {
                    RunnerConnectorCheckScreen(
                        document = activeDocument,
                        singlePageMode = singlePageMode,
                        onBack = ::navigateToPreviousLearningStep,
                        onTopBarBack = ::navigateToLibrary,
                        onTokenChange = { changed ->
                            updateDocument { document ->
                                document.copy(
                                    tokens = document.tokens.map {
                                        if (it.id == changed.id) changed else it
                                    },
                                )
                            }
                        },
                        onReset = {
                            updateDocument { document ->
                                document.copy(
                                    tokens = document.tokens.map { token ->
                                        if (token.kind == TokenKind.CONNECTOR) {
                                            token.copy(answer = "", isCorrect = null)
                                        } else {
                                            token
                                        }
                                    },
                                )
                            }
                        },
                        onComplete = {
                            if (singlePageMode) navigateToLibrary()
                            else navigate(LearningStep.FINAL_TRANSLATION)
                        },
                    )
                }

                step == LearningStep.FINAL_TRANSLATION -> {
                    FinalTranslationScreen(
                        document = activeDocument,
                        singlePageMode = singlePageMode,
                        onBack = ::navigateToPreviousLearningStep,
                        onTopBarBack = ::navigateToLibrary,
                        isProcessing = aiBusy,
                        errorMessage = aiError,
                        errorDetails = aiErrorDetails,
                        onTranslationChange = { value ->
                            updateDocument {
                                it.copy(
                                    finalTranslation = value,
                                    finalScore = null,
                                    finalMistakes = emptyList(),
                                    comprehensionQuestions = emptyList(),
                                    comprehensionScore = null,
                                )
                            }
                        },
                        onRetry = {
                            updateDocument {
                                it.copy(
                                    finalTranslation = "",
                                    finalScore = null,
                                    finalMistakes = emptyList(),
                                )
                            }
                        },
                        onCheck = {
                            aiBusy = true
                            aiError = null
                            aiErrorDetails = null
                            val documentId = activeDocument.id
                            val source = activeDocument.sourceText
                            val translation = activeDocument.finalTranslation
                            appScope.launch {
                                runCatching {
                                    withContext(Dispatchers.IO) {
                                        actualEngine.evaluateTranslation(
                                            source,
                                            translation,
                                            nativeLanguage,
                                        )
                                    }
                                }.onSuccess { (score, mistakes) ->
                                    updateDocument {
                                        if (it.id == documentId) {
                                            it.copy(
                                                finalScore = score,
                                                finalMistakes = mistakes,
                                            )
                                        } else {
                                            it
                                        }
                                    }
                                }.onFailure { error ->
                                    handleAiFailure(error, "AI採点に失敗しました")
                                }
                                aiBusy = false
                            }
                        },
                        onFinish = {
                            if (singlePageMode) {
                                navigateToLibrary()
                            } else {
                                aiBusy = true
                                aiError = null
                                aiErrorDetails = null
                                val documentId = activeDocument.id
                                val source = activeDocument.sourceText
                                appScope.launch {
                                    runCatching {
                                    withContext(Dispatchers.IO) {
                                        actualEngine.generateComprehensionQuestions(
                                            source,
                                            nativeLanguage,
                                        )
                                    }
                                    }.onSuccess { questions ->
                                        commit(
                                            appState.copy(
                                                documents = appState.documents.map { document ->
                                                    if (document.id == documentId) {
                                                        document.copy(
                                                            comprehensionQuestions = questions,
                                                            comprehensionScore = null,
                                                            step = LearningStep.COMPREHENSION_CHECK,
                                                            updatedAt = System.currentTimeMillis(),
                                                        )
                                                    } else {
                                                        document
                                                    }
                                                },
                                                currentStep = LearningStep.COMPREHENSION_CHECK,
                                            ),
                                        )
                                    }.onFailure { error ->
                                        handleAiFailure(error, "理解チェックを作成できませんでした")
                                    }
                                    aiBusy = false
                                }
                            }
                        },
                    )
                }

                else -> {
                    ComprehensionCheckScreen(
                        document = activeDocument,
                        onBack = ::navigateToPreviousLearningStep,
                        onTopBarBack = ::navigateToLibrary,
                        isProcessing = aiBusy,
                        errorMessage = aiError,
                        errorDetails = aiErrorDetails,
                        onAnswerChange = { questionId, answer ->
                            updateDocument { document ->
                                document.copy(
                                    comprehensionQuestions =
                                        document.comprehensionQuestions.map { question ->
                                            if (question.id == questionId) {
                                                question.copy(
                                                    answer = answer,
                                                    isCorrect = null,
                                                    correctAnswer = "",
                                                    feedback = "",
                                                )
                                            } else {
                                                question
                                            }
                                        },
                                    comprehensionScore = null,
                                )
                            }
                        },
                        onCheck = {
                            aiBusy = true
                            aiError = null
                            aiErrorDetails = null
                            val documentId = activeDocument.id
                            val source = activeDocument.sourceText
                            val questions = activeDocument.comprehensionQuestions
                            appScope.launch {
                                runCatching {
                                    withContext(Dispatchers.IO) {
                                        actualEngine.evaluateComprehension(
                                            source,
                                            questions,
                                            nativeLanguage,
                                        )
                                    }
                                }.onSuccess { (score, checkedQuestions) ->
                                    updateDocument {
                                        if (it.id == documentId) {
                                            it.copy(
                                                comprehensionQuestions = checkedQuestions,
                                                comprehensionScore = score,
                                            )
                                        } else {
                                            it
                                        }
                                    }
                                }.onFailure { error ->
                                    handleAiFailure(error, "回答を確認できませんでした")
                                }
                                aiBusy = false
                            }
                        },
                        onFinish = {
                            commit(
                                appState.copy(
                                    documents = appState.documents.map { document ->
                                        if (document.id == activeDocument.id) {
                                            document.copy(
                                                step = LearningStep.COMPREHENSION_CHECK,
                                                updatedAt = System.currentTimeMillis(),
                                            )
                                        } else {
                                            document
                                        }
                                    },
                                    activeDocumentId = null,
                                    currentStep = LearningStep.LIBRARY,
                                    activityDates = appState.activityDates + todayKey(),
                                ),
                            )
                            refreshCredits()
                        },
                    )
                }
            }
            if (contentState != screenTargetState && outgoingScrimAlpha > 0f) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = outgoingScrimAlpha)),
                )
            }
            }
        }
    }
    completedMenuDocument?.let { document ->
        CompletedDocumentMenu(
            document = document,
            onDismiss = { completedMenuDocument = null },
            onStartStep = { step ->
                completedMenuDocument = null
                singlePageMode = true
                commit(
                    appState.copy(
                        activeDocumentId = document.id,
                        currentStep = step,
                        activityDates = if (step == LearningStep.WORDBOOK) {
                            appState.activityDates + todayKey()
                        } else {
                            appState.activityDates
                        },
                        documents = appState.documents.map { current ->
                            if (current.id != document.id) current else when (step) {
                                LearningStep.WORD_CHECK -> current.copy(
                                    tokens = current.tokens.map { token ->
                                        if (token.kind == TokenKind.WORD) token.copy(answer = "", isCorrect = null) else token
                                    },
                                )
                                LearningStep.CONNECTOR_CHECK -> current.copy(
                                    tokens = current.tokens.map { token ->
                                        if (token.kind == TokenKind.CONNECTOR) token.copy(answer = "", isCorrect = null) else token
                                    },
                                )
                                LearningStep.FINAL_TRANSLATION -> current.copy(
                                    finalTranslation = "",
                                    finalScore = null,
                                    finalMistakes = emptyList(),
                                )
                                else -> current
                            }
                        },
                    ),
                )
            },
            onRedo = {
                completedMenuDocument = null
                singlePageMode = false
                commit(
                    appState.copy(
                        activeDocumentId = document.id,
                        currentStep = LearningStep.WORDBOOK,
                        activityDates = appState.activityDates + todayKey(),
                        documents = appState.documents.map { current ->
                            if (current.id != document.id) current else current.copy(
                                tokens = current.tokens.map { it.copy(answer = "", isCorrect = null) },
                                wordbookIndex = 0,
                                wordbookRevealed = false,
                                revealedWordIds = emptySet(),
                                finalTranslation = "",
                                finalScore = null,
                                finalMistakes = emptyList(),
                                comprehensionQuestions = emptyList(),
                                comprehensionScore = null,
                                step = LearningStep.WORDBOOK,
                            )
                        },
                    ),
                )
            },
        )
    }
    }

    }
