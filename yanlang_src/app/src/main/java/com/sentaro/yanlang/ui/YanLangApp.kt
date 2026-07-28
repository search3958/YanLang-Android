package com.sentaro.yanlang.ui

import com.sentaro.yanlang.R

import androidx.activity.compose.BackHandler
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
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.ui.graphics.Brush
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
import java.util.UUID
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Composable
private fun hapticAction(action: () -> Unit): () -> Unit {
    val hapticFeedback = LocalHapticFeedback.current
    return {
        hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
        action()
    }
}

@Composable
fun YanLangApp(
    repository: LearningRepository,
    engine: LearningEngine = remember { SupabaseLearningEngine() },
) {
    val stateViewModel: YanLangViewModel = viewModel(
        factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T =
                YanLangViewModel(repository) as T
        },
    )
    val appState = stateViewModel.appState
    var navigationHistory by remember {
        mutableStateOf(listOf(LearningStep.LIBRARY))
    }
    var aiBusy by remember { mutableStateOf(false) }
    var aiError by remember { mutableStateOf<String?>(null) }
    var aiErrorDetails by remember { mutableStateOf<String?>(null) }
    var completedMenuDocument by remember { mutableStateOf<LearningDocument?>(null) }
    var vocabularyFile by rememberSaveable { mutableStateOf<String?>(null) }
    var customVocabularyScreen by rememberSaveable { mutableStateOf<String?>(null) }
    var vocabularyEntryIndex by rememberSaveable { mutableStateOf<Int?>(null) }
    var vocabularyLanguageCode by rememberSaveable { mutableStateOf("ja") }
    var vocabularyExitTick by rememberSaveable { mutableStateOf(0) }
    var librarySection by rememberSaveable { mutableStateOf(0) }
    var rootTab by rememberSaveable { mutableStateOf(0) }
    var singlePageMode by rememberSaveable { mutableStateOf(false) }
    val appScope = rememberCoroutineScope()
    val context = LocalContext.current
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

    BackHandler(enabled = appState.currentStep != LearningStep.LIBRARY || vocabularyFile != null || customVocabularyScreen != null) {
        if (vocabularyEntryIndex != null) vocabularyEntryIndex = null
        else if (customVocabularyScreen != null) customVocabularyScreen = null
        else if (vocabularyFile != null) vocabularyFile = null
        else if (singlePageMode) navigateToLibrary() else navigateBack()
    }

    Surface(modifier = Modifier.fillMaxSize()) {
        if (!onboardingCompleted) {
            OnboardingFlow(
                initialLanguageCode = appState.nativeLanguageCode,
                initialCustomLanguage = appState.customNativeLanguage,
                onComplete = { code, customLanguage ->
                    commit(
                        appState.copy(
                            nativeLanguageCode = code,
                            customNativeLanguage = customLanguage,
                        ),
                        recordNavigation = false,
                    )
                    context.getSharedPreferences(
                        ONBOARDING_PREFS,
                        android.content.Context.MODE_PRIVATE,
                    ).edit().putBoolean(ONBOARDING_COMPLETED, true).apply()
                    onboardingCompleted = true
                },
                modifier = Modifier.fillMaxSize(),
            )
        } else {
        val vocabularyRoute = vocabularyFile?.let { "asset:$it" } ?: customVocabularyScreen
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
            // Use the transition target instead of the latest outer state so the
            // outgoing content remains the screen that is actually leaving.
            val activeDocument = appState.documents.firstOrNull { it.id == documentId }
            val outgoingScrimAlpha by animateFloatAsState(
                targetValue = if (contentState == screenTargetState) 0f else 0.5f,
                animationSpec = tween(300, easing = CubicBezierEasing(0.3f, 0.01f, 0.6f, 1f)),
                label = "outgoingScrim",
            )
            Box {
            when {
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
                        onBack = { vocabularyEntryIndex = null; vocabularyFile = null; vocabularyExitTick++ },
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
                            vocabularyFile = file
                            vocabularyLanguageCode = language
                        },
                        customVocabularyBooks = customVocabularyBooks,
                        onOpenCustomVocabulary = { book ->
                            recordActivity()
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
                        onNativeLanguageChange = { code, custom ->
                            commit(
                                appState.copy(
                                    nativeLanguageCode = code,
                                    customNativeLanguage = custom,
                                ),
                                recordNavigation = false,
                            )
                        },
                        activityDates = appState.activityDates,
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
                        onComplete = {
                            aiBusy = true
                            aiError = null
                            aiErrorDetails = null
                            val source = activeDocument.sourceText
                            val documentId = activeDocument.id
                            appScope.launch {
                                runCatching {
                                    withContext(Dispatchers.IO) {
                                        engine.analyze(
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
                                    aiError = error.message ?: "AI解析に失敗しました"
                                    aiErrorDetails = (error as? LearningApiException)?.rawResponse
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
                    )
                }

                step == LearningStep.WORD_CHECK -> {
                    RunnerWordCheckScreen(
                        document = activeDocument,
                        singlePageMode = singlePageMode,
                        engine = engine,
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
                                        engine.evaluateTranslation(
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
                                    aiError = error.message ?: "AI採点に失敗しました"
                                    aiErrorDetails = (error as? LearningApiException)?.rawResponse
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
                                            engine.generateComprehensionQuestions(
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
                                        aiError = error.message
                                            ?: "理解チェックを作成できませんでした"
                                        aiErrorDetails = (error as? LearningApiException)?.rawResponse
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
                                        engine.evaluateComprehension(
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
                                    aiError = error.message
                                        ?: "回答を確認できませんでした"
                                    aiErrorDetails = (error as? LearningApiException)?.rawResponse
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

    if (onboardingCompleted && !analyticsConsentGranted) {
        AnalyticsConsentDialog(
            onConsent = {
                analyticsPreferences.edit().putBoolean(ANALYTICS_CONSENT_GRANTED, true).apply()
                analyticsConsentGranted = true
            },
        )
    }
}

private const val ONBOARDING_PREFS = "yanlang_onboarding"
private const val ONBOARDING_COMPLETED = "completed"
private const val ANALYTICS_PREFS = "yanlang_analytics"
private const val ANALYTICS_CONSENT_GRANTED = "consent_granted"
private const val MAX_SOURCE_LENGTH = 300

@Composable
private fun OnboardingFlow(
    initialLanguageCode: String,
    initialCustomLanguage: String,
    onComplete: (String, String) -> Unit,
    modifier: Modifier = Modifier,
) {
    var page by rememberSaveable { mutableStateOf(0) }
    var code by rememberSaveable { mutableStateOf("") }
    var custom by rememberSaveable { mutableStateOf("") }
    AnimatedContent(
        targetState = page,
        transitionSpec = { fadeIn(tween(220)) togetherWith fadeOut(tween(160)) },
        label = "onboardingPage",
        modifier = modifier,
    ) { currentPage ->
        if (currentPage == 0) {
            WelcomeOnboardingScreen(
                onContinue = { page = 1 },
                modifier = Modifier.fillMaxSize(),
            )
        } else {
            LanguageOnboardingScreen(
                languageCode = code,
                customLanguage = custom,
                onLanguageChange = { newCode, newCustom ->
                    code = newCode
                    custom = newCustom
                },
                onComplete = { onComplete(code, custom) },
                modifier = Modifier.fillMaxSize(),
            )
        }
    }

}

@Composable
private fun WelcomeOnboardingScreen(
    onContinue: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val composition by rememberLottieComposition(LottieCompositionSpec.Asset("welcome.json"))
    val view = LocalView.current
    var visible by remember { mutableStateOf(false) }
    androidx.compose.runtime.LaunchedEffect(composition) {
        delay(composition?.duration?.toLong()?.coerceAtLeast(1L) ?: 2900L)
        visible = true
        delay(50L)
        view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS)
    }
    Box(modifier.background(Color(0xFFF6F6F6))) {
        LottieAnimation(
            composition,
            iterations = 1,
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer {
                    scaleX = 1.02f
                    scaleY = 1.02f
                },
            contentScale = ContentScale.Crop,
        )
        Box(Modifier.fillMaxSize()) {
            androidx.compose.animation.AnimatedVisibility(
                visible,
                enter = fadeIn(tween(100)) + slideInVertically(tween(220)) { it / 3 },
                modifier = Modifier.align(Alignment.Center),
            ) {
                Text(stringResource(R.string.ui_000), style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, color = Color.White)
            }
            androidx.compose.animation.AnimatedVisibility(
                visible,
                enter = fadeIn(tween(100)),
                modifier = Modifier.align(Alignment.BottomCenter).padding(28.dp),
            ) {
                Button(
                    onClick = {
                        view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP)
                        onContinue()
                    },
                    modifier = Modifier.height(56.dp).wrapContentWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = Color.Black),
                    shape = SmoothCornerShape(999.dp),
                ) {
                    Text(stringResource(R.string.ui_001), fontWeight = FontWeight.Bold)
                    Spacer(Modifier.width(8.dp))
                    Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null)
                }
            }
        }
    }
}

@Composable
private fun LanguageOnboardingScreen(
    languageCode: String,
    customLanguage: String,
    onLanguageChange: (String, String) -> Unit,
    onComplete: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val languages = listOf("ja" to "日本語", "en" to "English", "ru" to "Русский", "ko-kr" to "한국어 (한국)", "ko-kp" to "조선말 (조선및 연변)", "zh-CN" to "中文（简体）", "zh-TW" to "中文（繁體）", "other" to stringResource(R.string.ui_052))
    val canComplete = languageCode.isNotBlank() &&
        (languageCode != "other" || customLanguage.isNotBlank())
    val composition by rememberLottieComposition(LottieCompositionSpec.Asset("bg-animation.json"))
    val view = LocalView.current
    var animationTrigger by remember { mutableStateOf(0) }
    Column(
        modifier.background(Color(0xFFF6F6F6)).verticalScroll(rememberScrollState()).padding(28.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(Modifier.weight(1f, true))
        Text(stringResource(R.string.ui_002), style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(8.dp))
        Text(stringResource(R.string.ui_003), color = Color(0xFF666666))
        Spacer(Modifier.height(28.dp))
        languages.forEach { (itemCode, label) ->
            val selected = itemCode == languageCode
            Card(
                onClick = {
                    view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP)
                    onLanguageChange(itemCode, if (itemCode == "other") customLanguage else "")
                    if (animationTrigger == 0) animationTrigger = 1
                },
                modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp),
                shape = SmoothCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = if (selected) Color(0xFFE8E8FF) else Color.White),
                border = if (selected) androidx.compose.foundation.BorderStroke(2.dp, Color(0xFF0900FF)) else null,
            ) {
                Row(Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Text(label, fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium)
                    Spacer(Modifier.weight(1f))
                    if (selected) Icon(Icons.Default.CheckCircle, null, tint = Color(0xFF0900FF))
                }
            }
        }
        if (languageCode == "other") {
            TextField(customLanguage, { onLanguageChange("other", it) }, placeholder = { Text(stringResource(R.string.ui_048)) }, singleLine = true, modifier = Modifier.fillMaxWidth(), shape = SmoothCornerShape(18.dp), colors = yanLangTextFieldColors())
        }
        Spacer(Modifier.height(18.dp))
        Box(Modifier.height(56.dp).wrapContentWidth().clip(SmoothCornerShape(999.dp)).background(Color(0xFFDDDDDD))) {
            if (languageCode.isNotBlank() && animationTrigger > 0) {
                androidx.compose.runtime.key(animationTrigger) {
                    LottieAnimation(composition, iterations = 1, modifier = Modifier.matchParentSize(), contentScale = ContentScale.FillBounds)
                }
            }
            Button(onClick = {
                view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP)
                onComplete()
            }, enabled = canComplete, modifier = Modifier.wrapContentWidth().fillMaxHeight(), shape = SmoothCornerShape(999.dp), contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 22.dp), colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent, contentColor = Color.White)) {
                Text(stringResource(R.string.ui_035), fontWeight = FontWeight.Bold)
                Spacer(Modifier.width(8.dp))
                Icon(Icons.Default.Check, contentDescription = null)
            }
        }
        Spacer(Modifier.weight(1f, true))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LibraryScreen(
    documents: List<LearningDocument>,
    onCreate: () -> Unit,
    onSelect: (LearningDocument) -> Unit,
    onStartStep: (LearningDocument, LearningStep, Boolean) -> Unit,
    onDelete: (LearningDocument) -> Unit,
    onRestore: (LearningDocument) -> Unit,
    onOpenVocabulary: (String, String) -> Unit,
    customVocabularyBooks: List<CustomVocabularyBook>,
    onOpenCustomVocabulary: (CustomVocabularyBook) -> Unit,
    onCreateVocabulary: () -> Unit,
    returnToLongTextKey: Int,
    vocabularyLanguageCode: String = "ja",
    librarySection: Int = 0,
    onSectionChange: (Int) -> Unit = {},
    rootTab: Int = 0,
    onRootTabChange: (Int) -> Unit = {},
    nativeLanguageCode: String = "ja",
    customNativeLanguage: String = "",
    onNativeLanguageChange: (String, String) -> Unit = { _, _ -> },
    activityDates: Set<String> = emptySet(),
) {
    var pendingDelete by remember { mutableStateOf<LearningDocument?>(null) }
    var documentQuery by rememberSaveable { mutableStateOf("") }
    var playNewButtonAnimation by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    androidx.compose.runtime.LaunchedEffect(Unit) {
        kotlinx.coroutines.delay(2_000)
        playNewButtonAnimation = true
    }
    val resumeDocument = documents
        .filter {
            it.comprehensionScore == null &&
                (it.title.isNotBlank() || it.sourceText.isNotBlank() || it.tokens.isNotEmpty())
        }
        .maxByOrNull { it.updatedAt }

    Box(modifier = Modifier.fillMaxSize().background(HeaderBackground)) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.2f)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0x400900FF),
                            Color(0x000900FF),
                        )
                    )
                )
        )
        Scaffold(
        containerColor = Color.Transparent,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            if (rootTab != 0) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.Transparent)
                        .windowInsetsPadding(WindowInsets.statusBars)
                        .height(72.dp)
                        .padding(horizontal = 22.dp),
                    contentAlignment = Alignment.CenterStart,
                ) {
                    Text(
                        if (rootTab == 1) stringResource(R.string.ui_008) else stringResource(R.string.ui_009),
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                    )
                }
            } else {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Transparent)
                    .windowInsetsPadding(WindowInsets.statusBars)
                    .height(108.dp),
                contentAlignment = Alignment.Center,
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Row(horizontalArrangement = Arrangement.Center) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                        ) { onSectionChange(0) },
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.AutoMirrored.Filled.Article, contentDescription = stringResource(R.string.ui_004), modifier = Modifier.size(22.dp), tint = if (librarySection == 0) AccentGreen else Color.Black.copy(alpha = 0.5f))
                            Spacer(Modifier.width(6.dp))
                            Text(stringResource(R.string.ui_004), fontWeight = FontWeight.Bold, fontSize = 20.sp, color = Color.Black.copy(alpha = if (librarySection == 0) 1f else 0.5f))
                        }
                        if (librarySection == 0) Box(Modifier.padding(top = 4.dp).width(42.dp).height(3.dp).background(AccentGreen, SmoothCornerShape(999.dp)))
                    }
                    Spacer(Modifier.width(24.dp))
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                        ) { onSectionChange(1) },
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Style, contentDescription = stringResource(R.string.ui_005), modifier = Modifier.size(22.dp), tint = if (librarySection == 1) AccentGreen else Color.Black.copy(alpha = 0.5f))
                            Spacer(Modifier.width(6.dp))
                            Text(stringResource(R.string.ui_005), fontWeight = FontWeight.Bold, fontSize = 20.sp, color = Color.Black.copy(alpha = if (librarySection == 1) 1f else 0.5f))
                        }
                        if (librarySection == 1) Box(Modifier.padding(top = 4.dp).width(42.dp).height(3.dp).background(AccentGreen, SmoothCornerShape(999.dp)))
                    }
                }
                Spacer(Modifier.height(8.dp))
                SearchField(
                    value = documentQuery,
                    onValueChange = { documentQuery = it },
                    placeholder = if (librarySection == 0) stringResource(R.string.ui_053) else stringResource(R.string.ui_054),
                    modifier = Modifier.padding(horizontal = 20.dp),
                )
                }
            }
            }
        },
        bottomBar = {
            val isRootNavigationAction = rootTab != 0
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .windowInsetsPadding(WindowInsets.navigationBars)
                    .padding(start = 18.dp, end = 18.dp, top = 14.dp, bottom = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom,
            ) {
                AnimatedPrimaryButton(
                    onClick = hapticAction {
                        if (rootTab != 0) {
                            onRootTabChange(0)
                        } else if (librarySection == 0) {
                            onCreate()
                        } else {
                            onCreateVocabulary()
                        }
                    },
                    modifier = Modifier.height(52.dp),
                    playAnimation = playNewButtonAnimation,
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(
                        horizontal = 22.dp,
                    ),
                ) {
                    Icon(
                        if (isRootNavigationAction) Icons.Default.Book else Icons.Default.Add,
                        contentDescription = null,
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(stringResource(if (isRootNavigationAction) R.string.ui_007 else R.string.ui_006), fontWeight = FontWeight.Bold)
                }
                LibraryBottomItem(
                    selected = rootTab == 1,
                    icon = Icons.Default.CalendarMonth,
                    label = stringResource(R.string.ui_008),
                    onClick = { onRootTabChange(1) },
                )
                LibraryBottomItem(
                    selected = rootTab == 2,
                    icon = Icons.Default.Settings,
                    label = stringResource(R.string.ui_009),
                    onClick = { onRootTabChange(2) },
                )
            }
        },
    ) { padding ->
        if (rootTab == 1) {
            StreakScreen(
                activityDates = activityDates,
                modifier = Modifier.fillMaxSize().padding(padding),
            )
        } else if (rootTab == 2) {
            SettingsScreen(
                nativeLanguageCode = nativeLanguageCode,
                customNativeLanguage = customNativeLanguage,
                onLanguageChange = onNativeLanguageChange,
                modifier = Modifier.fillMaxSize().padding(padding),
            )
        } else if (librarySection == 1) {
            VocabularyCategoryMenu(
                onSelect = onOpenVocabulary,
                initialLanguageCode = vocabularyLanguageCode,
                searchQuery = documentQuery,
                customBooks = customVocabularyBooks,
                onSelectCustom = onOpenCustomVocabulary,
                nativeLanguageCode = nativeLanguageCode,
                modifier = Modifier.fillMaxSize().padding(padding),
            )
        } else if (documents.isEmpty()) {
            EmptyLibrary(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
            )
        } else {
            val normalizedQuery = documentQuery.trim().lowercase()
            val filteredDocuments = documents.filter { document ->
                normalizedQuery.isBlank() || document.title.lowercase().contains(normalizedQuery) || document.sourceText.lowercase().contains(normalizedQuery)
            }
            Column(Modifier.fillMaxSize().padding(padding)) {
                LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(
                    start = 20.dp,
                    end = 20.dp,
                    top = 18.dp,
                    bottom = 24.dp,
                ),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                if (resumeDocument != null) {
                    item {
                        Card(
                            onClick = hapticAction { onSelect(resumeDocument) },
                            modifier = Modifier.fillMaxWidth(),
                            shape = SmoothCornerShape(22.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = AccentGreen,
                            ),
                            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                        ) {
                            Column(Modifier.padding(18.dp)) {
                                Text(
                                    stringResource(R.string.ui_085),
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onPrimary,
                                )
                                Spacer(Modifier.height(4.dp))
                                Text(
                                    resumeDocument.title.ifBlank { stringResource(R.string.ui_086) },
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.onPrimary,
                                )
                                Spacer(Modifier.height(2.dp))
                                Text(
                                    resumeDocument.step.resumeLabel(),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f),
                                )
                            }
                        }
                    }
                }
                items(filteredDocuments, key = { it.id }) { document ->
                    DocumentCard(
                        document = document,
                        onClick = { onSelect(document) },
                        onStartStep = { step -> onStartStep(document, step, false) },
                        onDelete = { pendingDelete = document },
                    )
            }
        }
        }
    }
    }
}

/*
private const val ONBOARDING_PREFS = "yanlang_onboarding"
private const val ONBOARDING_COMPLETED = "completed"

private enum class OnboardingPage { WELCOME, LANGUAGE }

@Composable
private fun OnboardingFlow(
    initialLanguageCode: String,
    initialCustomLanguage: String,
    onComplete: (String, String) -> Unit,
    modifier: Modifier = Modifier,
) {
    var page by rememberSaveable { mutableStateOf(OnboardingPage.WELCOME) }
    var languageCode by rememberSaveable(initialLanguageCode) { mutableStateOf(initialLanguageCode) }
    var customLanguage by rememberSaveable { mutableStateOf(initialCustomLanguage) }

    AnimatedContent(
        targetState = page,
        transitionSpec = { fadeIn(tween(220)) togetherWith fadeOut(tween(160)) },
        label = "onboardingPage",
        modifier = modifier,
    ) { currentPage ->
        when (currentPage) {
            OnboardingPage.WELCOME -> WelcomeOnboardingScreen(
                onContinue = { page = OnboardingPage.LANGUAGE },
                modifier = Modifier.fillMaxSize(),
            )
            OnboardingPage.LANGUAGE -> LanguageOnboardingScreen(
                languageCode = languageCode,
                customLanguage = customLanguage,
                onLanguageChange = { code, custom ->
                    languageCode = code
                    customLanguage = custom
                },
                onComplete = { onComplete(languageCode, customLanguage) },
                modifier = Modifier.fillMaxSize(),
            )
        }
    }
}

@Composable
private fun WelcomeOnboardingScreen(
    onContinue: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val composition by rememberLottieComposition(LottieCompositionSpec.Asset("welcome.json"))
    var showGreeting by remember { mutableStateOf(false) }
    val greetingOffset = remember { androidx.compose.animation.core.Animatable(0f) }

    androidx.compose.runtime.LaunchedEffect(composition) {
        val durationMs = composition?.duration?.toLong()?.coerceAtLeast(1L) ?: 2900L
        delay(durationMs)
        showGreeting = true
        greetingOffset.snapTo(0f)
        greetingOffset.animateTo(5f, tween(55))
        greetingOffset.animateTo(-3f, tween(55))
        greetingOffset.animateTo(1f, tween(45))
        greetingOffset.animateTo(0f, tween(45))
    }

    Box(
        modifier = modifier.background(Color(0xFFF6F6F6)),
        contentAlignment = Alignment.Center,
    ) {
        LottieAnimation(
            composition = composition,
            iterations = 1,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Fit,
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 28.dp, vertical = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom,
        ) {
            androidx.compose.animation.AnimatedVisibility(
                visible = showGreeting,
                enter = fadeIn(tween(100)),
            ) {
                Text(
                    text = "はじめまして",
                    modifier = Modifier.graphicsLayer { translationX = greetingOffset.value },
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                )
            }
            Spacer(Modifier.height(28.dp))
            Button(
                onClick = onContinue,
                modifier = Modifier.fillMaxWidth().height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color.Black,
                ),
                shape = SmoothCornerShape(18.dp),
            ) {
                Text("続行", fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun LanguageOnboardingScreen(
    languageCode: String,
    customLanguage: String,
    onLanguageChange: (String, String) -> Unit,
    onComplete: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val languages = listOf(
        "ja" to "日本語",
        "en" to "English",
        "ru" to "Русский",
        "ko-kr" to "한국어",
        "zh-CN" to "中文（简体）",
        "zh-TW" to "中文（繁體）",
        "other" to "その他",
    )
    val canComplete = languageCode != "other" || customLanguage.isNotBlank()
    val buttonComposition by rememberLottieComposition(
        LottieCompositionSpec.Asset("bg-animation.json"),
    )
    var playButtonAnimation by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .background(Color(0xFFF6F6F6))
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 28.dp, vertical = 48.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(Modifier.weight(1f, fill = true))
        Text("母国語を教えてください", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(8.dp))
        Text("あなたに合った訳や問題を表示します", color = Color(0xFF666666))
        Spacer(Modifier.height(28.dp))
        languages.forEach { (code, label) ->
            val selected = languageCode == code
            Card(
                onClick = { onLanguageChange(code, if (code == "other") customLanguage else "") },
                modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp),
                shape = SmoothCornerShape(18.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (selected) Color(0xFFE8E8FF) else Color.White,
                ),
                border = if (selected) androidx.compose.foundation.BorderStroke(2.dp, Color(0xFF0900FF)) else null,
            ) {
                Row(Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Text(label, fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium)
                    Spacer(Modifier.weight(1f))
                    if (selected) Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color(0xFF0900FF))
                }
            }
        }
        if (languageCode == "other") {
            TextField(
                value = customLanguage,
                onValueChange = { onLanguageChange("other", it) },
                placeholder = { Text("母国語を入力") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                shape = SmoothCornerShape(18.dp),
                colors = yanLangTextFieldColors(),
            )
            Spacer(Modifier.height(10.dp))
        }
        Spacer(Modifier.height(18.dp))
        Box(
            modifier = Modifier.fillMaxWidth().height(56.dp).clip(SmoothCornerShape(18.dp))
                .background(if (canComplete) Color(0xFF0900FF) else Color(0xFFBDBDBD)),
        ) {
            if (playButtonAnimation && canComplete) {
                LottieAnimation(buttonComposition, iterations = 1, modifier = Modifier.matchParentSize(), contentScale = ContentScale.FillBounds)
            }
            Button(
                onClick = { playButtonAnimation = true; onComplete() },
                enabled = canComplete,
                modifier = Modifier.fillMaxSize(),
                shape = SmoothCornerShape(18.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent, contentColor = Color.White),
            ) { Text("完了", fontWeight = FontWeight.Bold) }
        }
        Spacer(Modifier.weight(1f, fill = true))
    }
}
*/

    pendingDelete?.let { document ->
        AlertDialog(
            onDismissRequest = { pendingDelete = null },
            title = { Text(stringResource(R.string.ui_076)) },
            text = {
                Text(
                    "「${document.title.ifBlank { "無題の教材" }}」を削除します。\n\n" +
                        "削除後はスナックバーが表示されている間だけ元に戻せます。\n" +
                        "スナックバーが消えると復元できません。",
                )
            },
            dismissButton = {
                TextButton(onClick = { pendingDelete = null }) {
                    Text(stringResource(R.string.ui_043))
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        pendingDelete = null
                        onDelete(document)
                        scope.launch {
                            val result = snackbarHostState.showSnackbar(
                                message = "教材を削除しました。",
                                actionLabel = "元に戻す",
                                withDismissAction = true,
                            )
                            if (result == androidx.compose.material3.SnackbarResult.ActionPerformed) {
                                onRestore(document)
                            }
                        }
                    },
                ) {
                    Text(stringResource(R.string.ui_044))
                }
            },
        )
    }
}

@Composable
private fun LibraryBottomItem(
    selected: Boolean,
    icon: ImageVector,
    label: String,
    onClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .clip(SmoothCornerShape(18.dp))
            .clickable(onClick = hapticAction(onClick))
            .padding(horizontal = 14.dp, vertical = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = if (selected) AccentGreen else Color.Black.copy(alpha = 0.62f),
            modifier = Modifier.size(25.dp),
        )
        Spacer(Modifier.height(3.dp))
        Text(
            label,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
            color = if (selected) AccentGreen else Color.Black.copy(alpha = 0.68f),
        )
    }
}

@Composable
private fun SettingsScreen(
    nativeLanguageCode: String,
    customNativeLanguage: String,
    onLanguageChange: (String, String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val uriHandler = LocalUriHandler.current
    val languages = listOf(
        "ja" to "日本語",
        "en" to "English",
        "ru" to "Русский",
        "ko-kr" to "한국어 (한국)",
        "ko-kp" to "조선말 (조선및 연변)",
        "zh-CN" to "中文（简体）",
        "zh-TW" to "中文（繁體）",
        "other" to stringResource(R.string.ui_052),
    )
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 18.dp),
    ) {
        Text(
            stringResource(R.string.ui_010),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
        )
        Spacer(Modifier.height(6.dp))
        Text(
            stringResource(R.string.ui_056),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(Modifier.height(16.dp))
        languages.forEach { (code, label) ->
            val selected = nativeLanguageCode == code
            Card(
                onClick = hapticAction {
                    onLanguageChange(code, if (code == "other") customNativeLanguage else "")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp),
                shape = SmoothCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (selected) AccentGreen.copy(alpha = 0.10f) else Color.White,
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(label, fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium)
                    Spacer(Modifier.weight(1f))
                    if (selected) {
                        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = AccentGreen)
                    }
                }
            }
        }
        if (nativeLanguageCode == "other") {
            TextField(
                value = customNativeLanguage,
                onValueChange = { onLanguageChange("other", it) },
                placeholder = { Text(stringResource(R.string.ui_048)) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                shape = SmoothCornerShape(20.dp),
                colors = yanLangTextFieldColors(),
            )
        }
        Spacer(Modifier.height(28.dp))
        Text(
            "${stringResource(R.string.ui_049)} ${BuildConfig.VERSION_NAME}",
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(Modifier.height(8.dp))
        SettingsExternalLink(
            label = stringResource(R.string.ui_050),
            url = "https://search3958.github.io/",
            onOpen = uriHandler::openUri,
        )
        Spacer(Modifier.height(4.dp))
        SettingsExternalLink(
            label = stringResource(R.string.ui_051),
            url = "https://play.google.com/store/apps/dev?id=5714216887541621486",
            onOpen = uriHandler::openUri,
        )
    }
}

@Composable
private fun SettingsExternalLink(
    label: String,
    url: String,
    onOpen: (String) -> Unit,
) {
    TextButton(
        onClick = hapticAction { onOpen(url) },
        modifier = Modifier.fillMaxWidth(),
    ) {
        Text(label)
        Spacer(Modifier.width(6.dp))
        Icon(
            imageVector = Icons.AutoMirrored.Filled.OpenInNew,
            contentDescription = null,
            modifier = Modifier.size(16.dp),
        )
    }
}

@Composable
private fun StreakScreen(
    activityDates: Set<String>,
    modifier: Modifier = Modifier,
) {
    val calendar = remember {
        Calendar.getInstance().apply {
            set(Calendar.DAY_OF_MONTH, 1)
            set(Calendar.HOUR_OF_DAY, 12)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
    }
    val uiLanguageCode = Locale.getDefault().language
    val monthTitle = remember(uiLanguageCode) {
        val locale = when (uiLanguageCode) {
            "en" -> Locale.ENGLISH
            "ko" -> Locale.KOREAN
            "zh" -> Locale.SIMPLIFIED_CHINESE
            "es" -> Locale.forLanguageTag("es")
            else -> Locale.JAPANESE
        }
        SimpleDateFormat(if (uiLanguageCode == "ja" || uiLanguageCode == "zh") "yyyy年 M月" else "MMMM yyyy", locale)
            .format(calendar.time)
    }
    val firstDayOffset = (calendar.get(Calendar.DAY_OF_WEEK) - Calendar.SUNDAY)
    val dayCount = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
    val streak = remember(activityDates) { currentStreak(activityDates) }

    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 16.dp),
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = SmoothCornerShape(28.dp),
            colors = CardDefaults.cardColors(containerColor = AccentGreen),
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 22.dp, vertical = 20.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    Icons.Default.Whatshot,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(38.dp),
                )
                Spacer(Modifier.width(14.dp))
                Column {
                    Text(stringResource(R.string.ui_011), color = Color.White.copy(alpha = 0.78f))
                    Text(
                        when (uiLanguageCode) {
                            "en" -> "$streak days"
                            "ko" -> "${streak}일"
                            "zh" -> "$streak 天"
                            "es" -> "$streak días"
                            else -> "${streak}日"
                        },
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                    )
                }
            }
        }
        Spacer(Modifier.height(22.dp))
        Text(monthTitle, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(14.dp))
        Row(Modifier.fillMaxWidth()) {
            listOf("日", "月", "火", "水", "木", "金", "土").forEach { day ->
                Text(
                    day,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
        Spacer(Modifier.height(8.dp))
        repeat(6) { week ->
            Row(Modifier.fillMaxWidth()) {
                repeat(7) { weekday ->
                    val day = week * 7 + weekday - firstDayOffset + 1
                    if (day in 1..dayCount) {
                        val date = "%04d-%02d-%02d".format(
                            Locale.US,
                            calendar.get(Calendar.YEAR),
                            calendar.get(Calendar.MONTH) + 1,
                            day,
                        )
                        val active = date in activityDates
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .aspectRatio(1f)
                                .padding(4.dp)
                                .background(
                                    if (active) AccentGreen else Color.White,
                                    SmoothCornerShape(14.dp),
                                ),
                            contentAlignment = Alignment.Center,
                        ) {
                            Text(
                                day.toString(),
                                fontWeight = if (active) FontWeight.Bold else FontWeight.Normal,
                                color = if (active) Color.White else Color.Black,
                            )
                        }
                    } else {
                        Spacer(Modifier.weight(1f).aspectRatio(1f))
                    }
                }
            }
        }
        Text(
            stringResource(R.string.ui_057),
            modifier = Modifier.padding(top = 12.dp),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodySmall,
        )
    }
}

@Composable
private fun EmptyLibrary(modifier: Modifier = Modifier) {
    val composition by rememberLottieComposition(
        LottieCompositionSpec.Asset("data.json"),
    )

    Column(
        modifier = modifier.padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        LottieAnimation(
            composition = composition,
            iterations = 1,
            modifier = Modifier.size(168.dp),
        )
        Spacer(Modifier.height(20.dp))
        Text(
            stringResource(R.string.ui_088),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
        )
        Spacer(Modifier.height(8.dp))
        Text(
            stringResource(R.string.ui_089),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )
    }
}

private fun vocabularyLanguageLabel(code: String): String = when (code) {
    "ja" -> "日本語"
    "ko-kr" -> "한국어"
    "ko-kp" -> "조선말"
    "zh" -> "中文（简体）"
    "zh-CN" -> "中文（简体）"
    "zh-TW" -> "中文（繁體）"
    "en" -> "English"
    "ru" -> "Русский"
    else -> code
}

private fun vocabularyLanguageFlag(code: String): String = when (code) {
    "ja" -> "🇯🇵"
    "ko-kr" -> "🇰🇷"
    "ko-kp" -> "🇰🇵"
    "zh", "zh-CN" -> "🇨🇳"
    "zh-TW" -> "🇨🇳"
    "en" -> "🇬🇧"
    "ru" -> "🇷🇺"
    else -> "🌐"
}

private fun vocabularyThemeName(file: String): String = mapOf(
    "numbers.json" to "数字",
    "ordinal_numbers.json" to "数え方",
    "fruits.json" to "果物",
    "vegetables.json" to "野菜",
    "colors.json" to "色",
    "electronics.json" to "電子製品",
    "math_terms.json" to "数学用語",
    "grammar_terms.json" to "文法用語",
    "biology_terms.json" to "生物用語",
    "science_terms.json" to "科学用語",
    "countries.json" to "主要国名",
    "clothing.json" to "衣類",
    "furniture.json" to "家具",
    "luggage.json" to "手荷物",
    "directions.json" to "方向",
    "scents_and_visual_emotions.json" to "香りと視覚感情",
    "emotions.json" to "感情",
)[file] ?: file.removeSuffix(".json")

@Composable
private fun VocabularyLibraryScreen(
    languages: List<VocabularyLanguage>,
    entries: List<VocabularyEntry>,
    themeName: String,
    initialIndex: Int,
    languageCode: String,
    nativeLanguageCode: String,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var revealed by rememberSaveable { mutableStateOf(setOf<String>()) }
    val pagerState = rememberPagerState(initialPage = initialIndex.coerceIn(0, (entries.size - 1).coerceAtLeast(0)), pageCount = { entries.size })

    Column(modifier.background(Color(0xFFF6F6F6)).windowInsetsPadding(WindowInsets.statusBars).padding(horizontal = 20.dp, vertical = 18.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(R.string.ui_042)) }
            Text(themeName, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        }
        Spacer(Modifier.height(6.dp))
        if (entries.isEmpty()) {
            Text(stringResource(R.string.ui_058))
        } else {
            BoxWithConstraints(Modifier.fillMaxWidth().weight(1f)) {
                val verticalPadding = ((maxHeight - 198.dp) / 2).coerceAtLeast(0.dp)
                VerticalPager(state = pagerState, modifier = Modifier.fillMaxSize(), contentPadding = PaddingValues(vertical = verticalPadding), pageSize = PageSize.Fixed(198.dp), pageSpacing = 18.dp) { index ->
                    val entry = entries[index]
                    Card(
                        onClick = hapticAction { revealed = if (entry.id in revealed) revealed - entry.id else revealed + entry.id },
                        modifier = Modifier.fillMaxWidth().height(198.dp), shape = SmoothCornerShape(42.dp), colors = CardDefaults.cardColors(containerColor = Color.White), elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
                    ) {
                        Column(Modifier.fillMaxSize().padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                            val translation = entry.translations[languageCode] ?: entry.translations.values.firstOrNull() ?: VocabularyTranslation("")
                            Text(translation.text, style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
                            Spacer(Modifier.height(18.dp))
                            Text(translation.pronunciation, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurfaceVariant, textAlign = TextAlign.Center)
                            Spacer(Modifier.height(8.dp))
                            val nativeText = entry.translations[nativeLanguageCode]?.text
                                ?: entry.translations["ja"]?.text
                                ?: entry.translations.values.firstOrNull()?.text.orEmpty()
                            Text("${stringResource(R.string.ui_010)}: $nativeText", style = MaterialTheme.typography.titleLarge, textAlign = TextAlign.Center, modifier = if (entry.id in revealed) Modifier else Modifier.blur(11.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CustomVocabularyLibraryScreen(
    book: CustomVocabularyBook,
    onBack: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var revealed by rememberSaveable(book.id) { mutableStateOf(setOf<String>()) }
    var confirmDelete by remember { mutableStateOf(false) }
    val entries = book.entries.filter { it.word.isNotBlank() || it.meaning.isNotBlank() }
    val pagerState = rememberPagerState(pageCount = { entries.size })

    Column(
        modifier
            .background(ScreenBackground)
            .windowInsetsPadding(WindowInsets.statusBars)
            .padding(horizontal = 16.dp, vertical = 10.dp),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(R.string.ui_042))
            }
            Text(
                book.title.ifBlank { stringResource(R.string.ui_095) },
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f),
            )
            IconButton(onClick = onEdit) {
                Icon(Icons.Default.Edit, contentDescription = stringResource(R.string.ui_045))
            }
            IconButton(onClick = { confirmDelete = true }) {
                Icon(Icons.Default.Delete, contentDescription = stringResource(R.string.ui_044), tint = MaterialTheme.colorScheme.error)
            }
        }
        if (entries.isEmpty()) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(stringResource(R.string.ui_078), color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        } else {
            BoxWithConstraints(Modifier.fillMaxWidth().weight(1f)) {
                val verticalPadding = ((maxHeight - 198.dp) / 2).coerceAtLeast(0.dp)
                VerticalPager(
                    state = pagerState,
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(vertical = verticalPadding),
                    pageSize = PageSize.Fixed(198.dp),
                    pageSpacing = 18.dp,
                ) { index ->
                    val entry = entries[index]
                    Card(
                        onClick = hapticAction {
                            revealed = if (entry.id in revealed) revealed - entry.id else revealed + entry.id
                        },
                        modifier = Modifier.fillMaxWidth().height(198.dp),
                        shape = SmoothCornerShape(42.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
                    ) {
                        Column(
                            Modifier.fillMaxSize().padding(22.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                        ) {
                            Text(entry.word, style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
                            if (entry.pronunciation.isNotBlank()) {
                                Spacer(Modifier.height(10.dp))
                                Text(entry.pronunciation, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurfaceVariant, textAlign = TextAlign.Center)
                            }
                            Spacer(Modifier.height(12.dp))
                            Text(
                                entry.meaning,
                                style = MaterialTheme.typography.titleLarge,
                                textAlign = TextAlign.Center,
                                modifier = if (entry.id in revealed) Modifier else Modifier.blur(11.dp),
                            )
                        }
                    }
                }
            }
        }
    }

    if (confirmDelete) {
        AlertDialog(
            onDismissRequest = { confirmDelete = false },
            title = { Text(stringResource(R.string.ui_077)) },
            text = { Text("「${book.title.ifBlank { "無題の単語帳" }}」と登録した単語をすべて削除します。この操作は元に戻せません。") },
            dismissButton = {
                TextButton(onClick = { confirmDelete = false }) { Text(stringResource(R.string.ui_043)) }
            },
            confirmButton = {
                TextButton(onClick = { confirmDelete = false; onDelete() }) {
                    Text(stringResource(R.string.ui_044), color = MaterialTheme.colorScheme.error)
                }
            },
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CustomVocabularyEditorScreen(
    initialBook: CustomVocabularyBook,
    isNew: Boolean,
    onBack: () -> Unit,
    onSave: (CustomVocabularyBook) -> Unit,
    modifier: Modifier = Modifier,
) {
    var title by rememberSaveable(initialBook.id) { mutableStateOf(initialBook.title) }
    var entries by remember(initialBook.id) { mutableStateOf(initialBook.entries.ifEmpty { listOf(CustomVocabularyEntry()) }) }
    val canSave = title.isNotBlank() && entries.any { it.word.isNotBlank() && it.meaning.isNotBlank() }

    Scaffold(
        modifier = modifier,
        containerColor = ScreenBackground,
        topBar = {
            TopAppBar(
                title = { Text(if (isNew) stringResource(R.string.ui_090) else stringResource(R.string.ui_091), fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(R.string.ui_042))
                    }
                },
                actions = {
                    TextButton(
                        enabled = canSave,
                        onClick = {
                            onSave(
                                initialBook.copy(
                                    title = title.trim(),
                                    entries = entries.filter { it.word.isNotBlank() || it.meaning.isNotBlank() },
                                    updatedAt = System.currentTimeMillis(),
                                ),
                            )
                        },
                    ) {
                        Text(stringResource(R.string.ui_040), fontWeight = FontWeight.Bold)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = ScreenBackground),
            )
        },
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding).imePadding(),
            contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            item {
                CompactVocabularyField(
                    value = title,
                    onValueChange = { title = it },
                    label = stringResource(R.string.ui_092),
                    singleLine = true,
                )
            }
            items(entries, key = { it.id }) { entry ->
                val index = entries.indexOfFirst { it.id == entry.id }
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = SmoothCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(start = 10.dp, top = 8.dp, end = 4.dp, bottom = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(5.dp)) {
                            CompactVocabularyField(
                                value = entry.word,
                                onValueChange = { value -> entries = entries.map { if (it.id == entry.id) it.copy(word = value) else it } },
                                label = stringResource(R.string.ui_005),
                                singleLine = true,
                            )
                            CompactVocabularyField(
                                value = entry.pronunciation,
                                onValueChange = { value -> entries = entries.map { if (it.id == entry.id) it.copy(pronunciation = value) else it } },
                                label = stringResource(R.string.ui_093),
                                singleLine = true,
                            )
                            CompactVocabularyField(
                                value = entry.meaning,
                                onValueChange = { value -> entries = entries.map { if (it.id == entry.id) it.copy(meaning = value) else it } },
                                label = stringResource(R.string.ui_094),
                                singleLine = false,
                            )
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            IconButton(
                                enabled = index > 0,
                                onClick = {
                                    entries = entries.toMutableList().apply {
                                        add(index - 1, removeAt(index))
                                    }
                                },
                            ) { Icon(Icons.Default.KeyboardArrowUp, contentDescription = "上へ") }
                            IconButton(
                                onClick = { entries = entries.filterNot { it.id == entry.id } },
                            ) { Icon(Icons.Default.Delete, contentDescription = "単語を削除", tint = MaterialTheme.colorScheme.error) }
                            IconButton(
                                enabled = index < entries.lastIndex,
                                onClick = {
                                    entries = entries.toMutableList().apply {
                                        add(index + 1, removeAt(index))
                                    }
                                },
                            ) { Icon(Icons.Default.KeyboardArrowDown, contentDescription = "下へ") }
                        }
                    }
                }
            }
            item {
                FilledTonalButton(
                    onClick = { entries = entries + CustomVocabularyEntry() },
                    modifier = Modifier.fillMaxWidth().height(46.dp),
                    shape = SmoothCornerShape(16.dp),
                ) {
                    Icon(Icons.Default.Add, contentDescription = null)
                    Spacer(Modifier.width(6.dp))
                    Text(stringResource(R.string.ui_041), fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun CompactVocabularyField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    singleLine: Boolean,
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        label = { Text(label) },
        singleLine = singleLine,
        minLines = if (singleLine) 1 else 2,
        shape = SmoothCornerShape(12.dp),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color(0xFFF4F4F4),
            unfocusedContainerColor = Color(0xFFF4F4F4),
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            focusedPlaceholderColor = PlaceholderGray,
            unfocusedPlaceholderColor = PlaceholderGray,
        ),
    )
}

@Composable
private fun VocabularyCategoryMenu(
    onSelect: (String, String) -> Unit,
    initialLanguageCode: String = "ja",
    searchQuery: String = "",
    customBooks: List<CustomVocabularyBook> = emptyList(),
    onSelectCustom: (CustomVocabularyBook) -> Unit = {},
    nativeLanguageCode: String = "ja",
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val languages = remember(nativeLanguageCode) {
        VocabularyRepository.load(context, "numbers.json").first
            .filterNot { it.code == nativeLanguageCode }
    }
    val preferences = remember { context.getSharedPreferences("yanlang_preferences", 0) }
    var languageCode by rememberSaveable(nativeLanguageCode) {
        mutableStateOf(
            (preferences.getString("vocabulary_language", initialLanguageCode)
                ?: initialLanguageCode).takeIf { saved ->
                languages.any { it.code == saved }
            } ?: languages.firstOrNull()?.code.orEmpty(),
        )
    }
    Column(modifier.padding(20.dp).verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        val normalizedQuery = searchQuery.trim().lowercase()
        val filteredCustomBooks = customBooks.filter { book ->
            normalizedQuery.isBlank() ||
                book.title.lowercase().contains(normalizedQuery) ||
                book.entries.any { entry ->
                    entry.word.lowercase().contains(normalizedQuery) ||
                        entry.meaning.lowercase().contains(normalizedQuery) ||
                        entry.pronunciation.lowercase().contains(normalizedQuery)
                }
        }
        Text(stringResource(R.string.ui_019), style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        if (filteredCustomBooks.isEmpty()) {
            Text(
                if (customBooks.isEmpty()) stringResource(R.string.ui_096) else stringResource(R.string.ui_097),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(bottom = 4.dp),
            )
        } else {
            filteredCustomBooks.forEach { book ->
                Card(
                    onClick = hapticAction { onSelectCustom(book) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = SmoothCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Column(Modifier.weight(1f)) {
                            Text(book.title.ifBlank { stringResource(R.string.ui_095) }, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                            Text(
                                "${book.entries.count { it.word.isNotBlank() || it.meaning.isNotBlank() }}語",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                        Text("›", fontSize = 28.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }
        }
        Spacer(Modifier.height(8.dp))
        Text(stringResource(R.string.ui_020), style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        Text(stringResource(R.string.ui_021), style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
        androidx.compose.foundation.lazy.LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(languages) { language ->
                val label = vocabularyLanguageLabel(language.code)
                val colors = if (language.code == languageCode) ButtonDefaults.buttonColors()
                else ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = Color.Black)
                Button(
                    onClick = {
                        languageCode = language.code
                        preferences.edit().putString("vocabulary_language", language.code).apply()
                    },
                    shape = SmoothCornerShape(999.dp),
                    colors = colors,
                ) { Text("${vocabularyLanguageFlag(language.code)} $label") }
            }
        }
        Text(stringResource(R.string.ui_022), color = MaterialTheme.colorScheme.onSurfaceVariant)
        val themes = listOf(
            "数字" to "numbers.json",
            "数え方" to "ordinal_numbers.json",
            "果物" to "fruits.json",
            "野菜" to "vegetables.json",
            "色" to "colors.json",
            "電子製品" to "electronics.json",
            "数学用語" to "math_terms.json",
            "文法用語" to "grammar_terms.json",
            "生物用語" to "biology_terms.json",
            "科学用語" to "science_terms.json",
            "主要国名" to "countries.json",
            "衣類" to "clothing.json",
            "家具" to "furniture.json",
            "手荷物" to "luggage.json",
            "方向" to "directions.json",
            "香りと視覚感情" to "scents_and_visual_emotions.json",
            "感情" to "emotions.json"
        )
        val filteredThemes = themes.filter { (label, file) ->
            normalizedQuery.isBlank() || label.lowercase().contains(normalizedQuery) ||
                VocabularyRepository.load(context, file).second.any { entry ->
                    entry.translations.values.any { translation ->
                        translation.text.lowercase().contains(normalizedQuery) ||
                            translation.pronunciation.lowercase().contains(normalizedQuery)
                    }
                }
        }
        filteredThemes.chunked(2).forEach { rowThemes ->
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
                rowThemes.forEach { (label, file) ->
                    Card(
                        onClick = hapticAction { onSelect(file, languageCode) },
                        modifier = Modifier.weight(1f).shadow(6.dp, SmoothCornerShape(24.dp), spotColor = Color.Black.copy(alpha = 0.267f), ambientColor = Color.Black.copy(alpha = 0.267f)),
                        shape = SmoothCornerShape(24.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                    ) {
                        Text(label, modifier = Modifier.padding(20.dp), style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    }
                }
                if (rowThemes.size == 1) Spacer(Modifier.weight(1f))
            }
        }
    }
}

@Composable
private fun SearchField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
) {
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier
            .fillMaxWidth()
            .height(44.dp)
            .background(Color.White, SmoothCornerShape(999.dp))
            .padding(horizontal = 16.dp),
        singleLine = true,
        textStyle = MaterialTheme.typography.bodyLarge.copy(color = Color.Black),
        decorationBox = { innerTextField ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Search, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(20.dp))
                Spacer(Modifier.width(8.dp))
                Box(contentAlignment = Alignment.CenterStart) {
                if (value.isEmpty()) Text(placeholder, color = MaterialTheme.colorScheme.onSurfaceVariant)
                innerTextField()
                }
            }
        },
    )
}

@Composable
private fun LearningStep.resumeLabel(): String = when (this) {
    LearningStep.EDITOR -> stringResource(R.string.ui_115)
    LearningStep.WORDBOOK -> stringResource(R.string.ui_116)
    LearningStep.WORD_CHECK -> stringResource(R.string.ui_117)
    LearningStep.CONNECTOR_CHECK -> stringResource(R.string.ui_118)
    LearningStep.FINAL_TRANSLATION -> stringResource(R.string.ui_119)
    LearningStep.COMPREHENSION_CHECK -> stringResource(R.string.ui_120)
    LearningStep.LIBRARY -> stringResource(R.string.ui_121)
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun DocumentCard(
    document: LearningDocument,
    onClick: () -> Unit,
    onStartStep: (LearningStep) -> Unit,
    onDelete: () -> Unit,
) {
    val completed = document.comprehensionScore != null
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(6.dp, SmoothCornerShape(20.dp), spotColor = Color.Black.copy(alpha = 0.267f), ambientColor = Color.Black.copy(alpha = 0.267f))
            .clip(SmoothCornerShape(20.dp))
            .combinedClickable(
                onClick = hapticAction(onClick),
                onLongClick = hapticAction(onDelete),
            ),
        shape = SmoothCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    ) {
        Column(Modifier.padding(18.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        document.title.ifBlank { stringResource(R.string.ui_086) },
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                    )
                    Spacer(Modifier.height(5.dp))
                    Text(
                        document.sourceText.ifBlank { stringResource(R.string.ui_087) },
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                Spacer(Modifier.width(12.dp))
                if (completed) {
                    Icon(
                        Icons.Default.CheckCircle,
                        contentDescription = "完了",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(30.dp),
                    )
                } else {
                    CircularProgressIndicator(
                        progress = { document.progress },
                        modifier = Modifier.size(34.dp),
                        strokeWidth = 4.dp,
                        color = AccentGreen,
                    )
                }
            }
            if (!completed) {
                Spacer(Modifier.height(14.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    FilledTonalButton(
                        onClick = hapticAction { onStartStep(LearningStep.WORDBOOK) },
                        modifier = Modifier.weight(1f),
                    ) { Text(stringResource(R.string.ui_030)) }
                    Button(
                        onClick = hapticAction(onClick),
                        modifier = Modifier.weight(1f),
                    ) { Text(stringResource(R.string.ui_079)) }
                }
            }
        }
    }
}

@Composable
private fun CompletedDocumentMenu(
    document: LearningDocument,
    onDismiss: () -> Unit,
    onStartStep: (LearningStep) -> Unit,
    onRedo: () -> Unit,
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = SmoothCornerShape(28.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
        ) {
            Column(Modifier.padding(22.dp)) {
                Text(
                    document.title.ifBlank { stringResource(R.string.ui_086) },
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                )
                Spacer(Modifier.height(6.dp))
                Text(stringResource(R.string.ui_080), color = MaterialTheme.colorScheme.onSurfaceVariant)
                Spacer(Modifier.height(16.dp))
                listOf(
                    "単語帳" to LearningStep.WORDBOOK,
                    "単語確認" to LearningStep.WORD_CHECK,
                    "接続詞確認" to LearningStep.CONNECTOR_CHECK,
                    "全文翻訳" to LearningStep.FINAL_TRANSLATION,
                ).forEach { (label, step) ->
                    FilledTonalButton(
                        onClick = hapticAction { onStartStep(step) },
                        modifier = Modifier.fillMaxWidth().padding(vertical = 3.dp),
                    ) { Text(label) }
                }
                Button(
                    onClick = hapticAction(onRedo),
                    modifier = Modifier.fillMaxWidth().padding(top = 6.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer,
                        contentColor = MaterialTheme.colorScheme.onErrorContainer,
                    ),
                ) { Text(stringResource(R.string.ui_122)) }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EditorScreen(
    document: LearningDocument,
    onBack: () -> Unit,
    onTopBarBack: () -> Unit,
    isProcessing: Boolean,
    errorMessage: String?,
    errorDetails: String?,
    onChange: (String, String, String) -> Unit,
    onComplete: () -> Unit,
) {
    if (isProcessing) {
        AiTaskDialog(
            title = stringResource(R.string.ui_081),
            message = stringResource(R.string.ui_082),
        )
    }
    LearningScaffold(
        title = stringResource(R.string.ui_023),
        onBack = onBack,
        onTopBarBack = onTopBarBack,
        completeLabel = if (isProcessing) stringResource(R.string.ui_024) else stringResource(R.string.ui_025),
        completeEnabled = document.targetLanguage.isNotBlank() &&
            document.title.isNotBlank() &&
            document.sourceText.isNotBlank() &&
            document.sourceText.length <= MAX_SOURCE_LENGTH &&
            !isProcessing,
        onComplete = onComplete,
        showBottomBack = false,
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .imePadding()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 18.dp, vertical = 20.dp),
        ) {
            Text(
                stringResource(R.string.ui_029),
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Black.copy(alpha = 0.58f),
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            )
            Spacer(Modifier.height(12.dp))
            TextField(
                value = document.targetLanguage,
                onValueChange = {
                    onChange(it, document.title, document.sourceText)
                },
                placeholder = { Text(stringResource(R.string.ui_026)) },
                singleLine = true,
                enabled = !isProcessing,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp),
                textStyle = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                ),
                shape = SmoothCornerShape(999.dp),
                colors = yanLangTextFieldColors(),
            )
            Spacer(Modifier.height(12.dp))
            TextField(
                value = document.title,
                onValueChange = {
                    onChange(document.targetLanguage, it, document.sourceText)
                },
                placeholder = {
                    Text(
                        stringResource(R.string.ui_027),
                        fontWeight = FontWeight.Bold,
                    )
                },
                singleLine = true,
                enabled = !isProcessing,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(72.dp),
                textStyle = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                ),
                shape = SmoothCornerShape(999.dp),
                colors = yanLangTextFieldColors(),
            )
            Spacer(Modifier.height(16.dp))
            TextField(
                value = document.sourceText,
                onValueChange = {
                    if (it.length <= MAX_SOURCE_LENGTH) {
                        onChange(document.targetLanguage, document.title, it)
                    }
                },
                placeholder = { Text(stringResource(R.string.ui_028)) },
                isError = document.sourceText.length > MAX_SOURCE_LENGTH,
                enabled = !isProcessing,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp),
                textStyle = MaterialTheme.typography.bodyLarge.copy(
                    lineHeight = 28.sp,
                ),
                shape = SmoothCornerShape(34.dp),
                colors = yanLangTextFieldColors(),
            )
            if (errorMessage != null) {
                Spacer(Modifier.height(10.dp))
                AiErrorCard(errorMessage, errorDetails)
            }
            Spacer(Modifier.height(16.dp))
        }
    }
}

@Composable
private fun WordbookScreen(
    document: LearningDocument,
    singlePageMode: Boolean,
    onBack: () -> Unit,
    onTopBarBack: () -> Unit,
    onReveal: (String) -> Unit,
    onMove: (Int) -> Unit,
    onComplete: () -> Unit,
) {
    val tokens = document.tokens
    val index = document.wordbookIndex.coerceIn(0, (tokens.size - 1).coerceAtLeast(0))
    val pagerState = rememberPagerState(
        initialPage = index,
        pageCount = { tokens.size },
    )
    val hapticFeedback = LocalHapticFeedback.current

    androidx.compose.runtime.LaunchedEffect(pagerState.currentPage, tokens.size) {
        if (tokens.isNotEmpty() && pagerState.currentPage != document.wordbookIndex) {
                                hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                                onMove(pagerState.currentPage)
        }
    }

    LearningScaffold(
        title = stringResource(R.string.ui_030),
        onBack = onBack,
        onTopBarBack = onTopBarBack,
        completeLabel = if (singlePageMode) stringResource(R.string.ui_035) else stringResource(R.string.ui_025),
        showBottomBack = !singlePageMode,
        completeEnabled = tokens.isNotEmpty(),
        playCompleteButtonAnimation = tokens.isNotEmpty() && index == tokens.lastIndex,
        onComplete = onComplete,
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(top = 34.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Surface(
                shape = SmoothCornerShape(999.dp),
                color = Color.White,
                border = androidx.compose.foundation.BorderStroke(
                    2.dp,
                    Color(0xFFD7D7D7),
                ),
            ) {
                Text(
                    stringResource(R.string.ui_098),
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center,
                )
            }
            Spacer(Modifier.height(20.dp))
            BoxWithConstraints(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
            ) {
                val verticalPadding = ((maxHeight - 198.dp) / 2).coerceAtLeast(0.dp)
                VerticalPager(
                    state = pagerState,
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(
                        start = 22.dp,
                        end = 22.dp,
                        top = verticalPadding,
                        bottom = verticalPadding,
                    ),
                    pageSize = PageSize.Fixed(198.dp),
                    pageSpacing = 18.dp,
                    beyondViewportPageCount = 2,
                    key = { tokens[it].id },
                ) { cardIndex ->
                    val token = tokens[cardIndex]
                    StackedWordCard(
                        token = token,
                        revealed = token.id in document.revealedWordIds ||
                            (cardIndex == index && document.wordbookRevealed),
                        muted = cardIndex != index,
                        onClick = {
                            if (cardIndex == pagerState.currentPage) {
                                onReveal(token.id)
                            }
                        },
                    )
                }
            }
        }
    }
}

@Composable
private fun StackedWordCard(
    token: LearningToken,
    revealed: Boolean,
    muted: Boolean,
    onClick: () -> Unit,
) {
    Card(
        onClick = hapticAction(onClick),
        modifier = Modifier
            .fillMaxWidth()
            .height(198.dp),
        shape = SmoothCornerShape(42.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                token.source,
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = if (muted) Color(0xFFA7A7A7) else Color.Black,
            )
            Spacer(Modifier.height(18.dp))
            Text(
                token.translation,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
                color = if (muted) Color(0xFFA7A7A7) else Color.Black,
                modifier = if (revealed) Modifier else Modifier.blur(11.dp),
            )
        }
    }
}

@Composable
private fun WordCheckScreen(
    document: LearningDocument,
    singlePageMode: Boolean,
    engine: LearningEngine,
    onBack: () -> Unit,
    onTopBarBack: () -> Unit,
    onTokenChange: (LearningToken) -> Unit,
    onSkipKnown: () -> Unit,
    onComplete: () -> Unit,
) {
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current
    val words = document.wordTokens
    var isInputFocused by remember { mutableStateOf(false) }

    LearningScaffold(
        title = stringResource(R.string.ui_031),
        onBack = onBack,
        onTopBarBack = onTopBarBack,
        showTopBar = !isInputFocused,
        completeLabel = if (singlePageMode) stringResource(R.string.ui_035) else stringResource(R.string.ui_025),
        showBottomBack = !singlePageMode,
        completeEnabled = words.all { it.isCorrect != null },
        playCompleteButtonAnimation = words.isNotEmpty() && words.all { it.isCorrect != null },
        onComplete = onComplete,
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
        ) {
            if (!isInputFocused) {
                Text(
                    stringResource(R.string.ui_099),
                    modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 16.dp),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                )
                Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 18.dp, vertical = 8.dp)
                    .clickable(onClick = hapticAction(onSkipKnown)),
                shape = SmoothCornerShape(999.dp),
                color = Color.White,
                border = androidx.compose.foundation.BorderStroke(
                    2.dp,
                    Color(0xFFD5D5D5),
                ),
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 7.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        ResultMark(true)
                        Spacer(Modifier.width(10.dp))
                        Text(
                            stringResource(R.string.ui_100),
                            style = MaterialTheme.typography.bodyLarge,
                        )
                    }
                }
            }
            SourceCard(
                document = document,
                modifier = Modifier.padding(horizontal = 18.dp, vertical = 4.dp),
            )
            LazyColumn(
                state = listState,
                modifier = Modifier.fillMaxSize(),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(
                    start = 20.dp,
                    end = 20.dp,
                    top = 8.dp,
                    bottom = 320.dp,
                ),
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                items(words, key = { it.id }) { token ->
                    val wordIndex = words.indexOfFirst { it.id == token.id }
                    WordAnswerRow(
                        token = token,
                        isLast = wordIndex == words.lastIndex,
                        onFocusChanged = { isInputFocused = it },
                        onAnswerChange = {
                            onTokenChange(token.copy(answer = it, isCorrect = null))
                        },
                        onSubmit = {
                            val checked = token.copy(
                                isCorrect = engine.checkAnswer(token, token.answer),
                            )
                            onTokenChange(checked)
                            val next = (wordIndex + 1).coerceAtMost(words.lastIndex)
                            if (next > wordIndex) {
                                focusManager.moveFocus(FocusDirection.Down)
                                scope.launch { listState.animateScrollToItem(next) }
                            } else {
                                focusManager.clearFocus()
                            }
                        },
                    )
                }
            }
        }
    }
}

@Composable
private fun WordAnswerRow(
    token: LearningToken,
    isLast: Boolean,
    onFocusChanged: (Boolean) -> Unit,
    onAnswerChange: (String) -> Unit,
    onSubmit: () -> Unit,
) {
    val hapticSubmit = hapticAction(onSubmit)
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = SmoothCornerShape(24.dp),
        border = androidx.compose.foundation.BorderStroke(
            2.dp,
            Color(0xFFD5D5D5),
        ),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 7.dp, top = 3.dp, end = 8.dp, bottom = 3.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            ResultMark(token.isCorrect)
            Spacer(Modifier.width(10.dp))
            Text(
                token.source,
                modifier = Modifier.weight(0.38f),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
            )
            TextField(
                value = token.answer,
                onValueChange = onAnswerChange,
                modifier = Modifier
                    .weight(0.62f)
                    .height(56.dp)
                    .onFocusChanged { onFocusChanged(it.isFocused) },
                placeholder = { Text(stringResource(R.string.ui_036)) },
                singleLine = true,
                shape = SmoothCornerShape(13.dp),
                colors = inlineAnswerTextFieldColors(),
                keyboardOptions = KeyboardOptions(
                    imeAction = if (isLast) ImeAction.Done else ImeAction.Next,
                ),
                keyboardActions = KeyboardActions(
                    onNext = { hapticSubmit() },
                    onDone = { hapticSubmit() },
                ),
                trailingIcon = {
                    if (token.answer.isNotBlank() && token.isCorrect == null) {
                        IconButton(onClick = hapticSubmit) {
                            Icon(
                                Icons.Default.Check,
                                contentDescription = "回答を確認",
                            )
                        }
                    }
                },
            )
        }
    }
}

@Composable
private fun ResultMark(result: Boolean?) {
    val color = when (result) {
        true -> AccentGreen
        false -> MaterialTheme.colorScheme.error
        null -> Color(0xFFE0E0E0)
    }
    Box(
        modifier = Modifier
            .size(28.dp)
            .background(color, SmoothCornerShape(999.dp)),
        contentAlignment = Alignment.Center,
    ) {
        when (result) {
            true -> Icon(
                Icons.Default.Check,
                contentDescription = "正解",
                tint = Color.White,
                modifier = Modifier.size(18.dp),
            )
            false -> Icon(
                Icons.Default.Close,
                contentDescription = "不正解",
                tint = Color.White,
                modifier = Modifier.size(18.dp),
            )
            null -> Box(
                Modifier
                    .size(6.dp)
                    .background(color, SmoothCornerShape(999.dp)),
            )
        }
    }
}

@Composable
private fun ConnectorCheckScreen(
    document: LearningDocument,
    singlePageMode: Boolean,
    onBack: () -> Unit,
    onTopBarBack: () -> Unit,
    onTokenChange: (LearningToken) -> Unit,
    onComplete: () -> Unit,
) {
    val connectors = document.connectorTokens
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()
    LearningScaffold(
        title = stringResource(R.string.ui_032),
        onBack = onBack,
        onTopBarBack = onTopBarBack,
        completeLabel = if (singlePageMode) stringResource(R.string.ui_035) else stringResource(R.string.ui_025),
        showBottomBack = !singlePageMode,
        completeEnabled = connectors.all { it.isCorrect != null },
        playCompleteButtonAnimation = connectors.isNotEmpty() && connectors.all { it.isCorrect != null },
        onComplete = onComplete,
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
        ) {
            Text(
                stringResource(R.string.ui_101),
                modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 16.dp),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
            )
            SourceCard(
                document = document,
                modifier = Modifier.padding(horizontal = 18.dp, vertical = 8.dp),
            )
            LazyColumn(
                state = listState,
                modifier = Modifier.fillMaxSize(),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(
                    start = 20.dp,
                    end = 20.dp,
                    top = 8.dp,
                    bottom = 320.dp,
                ),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                items(connectors, key = { it.id }) { token ->
                    val connectorIndex = connectors.indexOfFirst { it.id == token.id }
                    ConnectorCard(
                        token = token,
                        displayPhrase = translatedPhrase(document, token),
                        onSelect = { selected ->
                            onTokenChange(
                                token.copy(
                                    answer = selected,
                                    isCorrect = selected == token.translation,
                                ),
                            )
                            val next = connectorIndex + 1
                            if (next < connectors.size) {
                                scope.launch { listState.animateScrollToItem(next) }
                            }
                        },
                    )
                }
            }
        }
    }
}

@Composable
private fun ConnectorCard(
    token: LearningToken,
    displayPhrase: AnnotatedString,
    onSelect: (String) -> Unit,
) {
    Card(
        shape = SmoothCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
        ),
        border = androidx.compose.foundation.BorderStroke(
            2.dp,
            Color(0xFFD5D5D5),
        ),
    ) {
        Column(Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                ResultMark(token.isCorrect)
                Spacer(Modifier.width(10.dp))
                Text(
                    displayPhrase,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                )
            }
            Spacer(Modifier.height(14.dp))
            token.choices.forEach { choice ->
                val selected = token.answer == choice
                val isCorrectSelection = selected && token.isCorrect == true
                val isWrongSelection = selected && token.isCorrect == false
                val container = when {
                    isCorrectSelection -> AccentGreen
                    isWrongSelection -> MaterialTheme.colorScheme.errorContainer
                    else -> Color(0xFFE8E8E8)
                }
                val contentColor = when {
                    isCorrectSelection -> Color.White
                    isWrongSelection -> MaterialTheme.colorScheme.onErrorContainer
                    else -> Color.Black
                }
                FilledTonalButton(
                    onClick = hapticAction { onSelect(choice) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    shape = SmoothCornerShape(14.dp),
                    colors = ButtonDefaults.filledTonalButtonColors(
                        containerColor = container,
                        contentColor = contentColor,
                    ),
                ) {
                    Text(choice, modifier = Modifier.weight(1f))
                    if (selected) {
                        Icon(
                            imageVector = if (token.isCorrect == true) {
                                Icons.Default.Check
                            } else {
                                Icons.Default.Close
                            },
                            contentDescription = null,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun FinalTranslationScreen(
    document: LearningDocument,
    singlePageMode: Boolean,
    onBack: () -> Unit,
    onTopBarBack: () -> Unit,
    isProcessing: Boolean,
    errorMessage: String?,
    errorDetails: String?,
    onTranslationChange: (String) -> Unit,
    onRetry: () -> Unit,
    onCheck: () -> Unit,
    onFinish: () -> Unit,
) {
    val focusManager = LocalFocusManager.current
    if (isProcessing) {
        AiTaskDialog(
            title = if (document.finalScore == null) {
                stringResource(R.string.ui_103)
            } else {
                stringResource(R.string.ui_104)
            },
            message = if (document.finalScore == null) {
                stringResource(R.string.ui_105)
            } else {
                stringResource(R.string.ui_106)
            },
        )
    }
    LearningScaffold(
        title = stringResource(R.string.ui_033),
        onBack = onBack,
        onTopBarBack = onTopBarBack,
        completeLabel = when {
            singlePageMode && document.finalScore != null -> stringResource(R.string.ui_107)
            isProcessing && document.finalScore != null -> stringResource(R.string.ui_108)
            isProcessing -> stringResource(R.string.ui_109)
            document.finalScore == null -> stringResource(R.string.ui_110)
            else -> stringResource(R.string.ui_107)
        },
        completeEnabled = document.finalTranslation.isNotBlank() && !isProcessing,
        playCompleteButtonAnimation = document.finalScore != null,
        showBottomBack = !singlePageMode,
        onComplete = {
            focusManager.clearFocus()
            if (document.finalScore == null) onCheck() else onFinish()
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .imePadding()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 18.dp),
        ) {
            Text(
                stringResource(R.string.ui_102),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
            )
            Spacer(Modifier.height(8.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = HeaderBackground,
                ),
                shape = SmoothCornerShape(30.dp),
            ) {
                Text(
                    document.sourceText,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp),
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium,
                    lineHeight = 27.sp,
                )
            }
            Spacer(Modifier.height(16.dp))
            if (document.finalScore == null) {
                TextField(
                    value = document.finalTranslation,
                    onValueChange = onTranslationChange,
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text(stringResource(R.string.ui_037)) },
                    minLines = 6,
                    shape = SmoothCornerShape(28.dp),
                    enabled = !isProcessing,
                    colors = yanLangTextFieldColors(),
                )
            } else {
                Text(
                    document.finalTranslation,
                    style = MaterialTheme.typography.headlineSmall,
                    lineHeight = 34.sp,
                    color = Color.Black,
                )
                if (!singlePageMode) {
                    TextButton(onClick = hapticAction(onRetry)) {
                        Text(stringResource(R.string.ui_039))
                    }
                }
            }
            if (errorMessage != null) {
                Spacer(Modifier.height(16.dp))
                AiErrorCard(errorMessage, errorDetails)
            }
            AnimatedVisibility(
                visible = document.finalScore != null,
                enter = fadeIn(),
                exit = fadeOut(),
            ) {
                Column {
                    Spacer(Modifier.height(24.dp))
                    ScoreCard(
                        score = document.finalScore ?: 0,
                    )
                    if (document.finalMistakes.isNotEmpty()) {
                        Spacer(Modifier.height(10.dp))
                        document.finalMistakes.forEach { mistake ->
                            MistakeRow(
                                source = mistake.source,
                                correct = mistake.correct,
                            )
                            Spacer(Modifier.height(8.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ComprehensionCheckScreen(
    document: LearningDocument,
    onBack: () -> Unit,
    onTopBarBack: () -> Unit,
    isProcessing: Boolean,
    errorMessage: String?,
    errorDetails: String?,
    onAnswerChange: (String, String) -> Unit,
    onCheck: () -> Unit,
    onFinish: () -> Unit,
) {
    val questions = document.comprehensionQuestions
    val checked = document.comprehensionScore != null
    val allAnswered = questions.isNotEmpty() && questions.all { it.answer.isNotBlank() }
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()
    if (isProcessing) {
        AiTaskDialog(
            title = stringResource(R.string.ui_083),
            message = stringResource(R.string.ui_084),
        )
    }

    LearningScaffold(
        title = stringResource(R.string.ui_034),
        onBack = onBack,
        onTopBarBack = onTopBarBack,
        completeLabel = when {
            isProcessing -> stringResource(R.string.ui_111)
            checked -> stringResource(R.string.ui_112)
            else -> stringResource(R.string.ui_113)
        },
        completeEnabled = allAnswered && !isProcessing,
        playCompleteButtonAnimation = allAnswered,
        onComplete = {
            if (checked) onFinish() else onCheck()
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
        ) {
            Text(
                stringResource(R.string.ui_114),
                modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 16.dp),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
            )
            SourceCard(
                document = document,
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp),
            )
            LazyColumn(
                state = listState,
                modifier = Modifier.fillMaxSize(),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(
                    start = 20.dp,
                    end = 20.dp,
                    top = 4.dp,
                    bottom = 320.dp,
                ),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                if (checked) {
                    item {
                        ComprehensionScoreCard(
                            score = document.comprehensionScore ?: 0,
                            correctCount = questions.count { it.isCorrect == true },
                            totalCount = questions.size,
                        )
                    }
                }
                items(questions, key = { it.id }) { question ->
                    val questionIndex = questions.indexOfFirst { it.id == question.id }
                    ComprehensionQuestionCard(
                        question = question,
                        enabled = !checked && !isProcessing,
                        onAnswerChange = { onAnswerChange(question.id, it) },
                        onChoiceSelected = {
                            val next = questionIndex + 1
                            if (next < questions.size) {
                                scope.launch { listState.animateScrollToItem(next) }
                            }
                        },
                    )
                }
                if (errorMessage != null) {
                    item { AiErrorCard(errorMessage, errorDetails) }
                }
            }
        }
    }
}

@Composable
private fun ComprehensionQuestionCard(
    question: ComprehensionQuestion,
    enabled: Boolean,
    onAnswerChange: (String) -> Unit,
    onChoiceSelected: () -> Unit = {},
) {
    Card(
        shape = SmoothCornerShape(22.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
        ),
        border = androidx.compose.foundation.BorderStroke(
            2.dp,
            Color(0xFFD5D5D5),
        ),
    ) {
        Column(Modifier.padding(18.dp)) {
            Row(verticalAlignment = Alignment.Top) {
                if (question.isCorrect != null) {
                    ResultMark(question.isCorrect)
                    Spacer(Modifier.width(10.dp))
                }
                Text(
                    question.prompt,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f),
                )
            }
            Spacer(Modifier.height(16.dp))
            when (question.type) {
                ComprehensionQuestionType.MULTIPLE_CHOICE -> {
                    question.choices.forEach { choice ->
                        val selected = question.answer == choice
                        FilledTonalButton(
                            onClick = hapticAction {
                                onAnswerChange(choice)
                                onChoiceSelected()
                            },
                            enabled = enabled,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            shape = SmoothCornerShape(14.dp),
                            colors = ButtonDefaults.filledTonalButtonColors(
                                containerColor = if (selected) {
                                    AccentGreen
                                } else {
                                    Color(0xFFE8E8E8)
                                },
                                contentColor = if (selected) {
                                    Color.White
                                } else {
                                    Color.Black
                                },
                            ),
                        ) {
                            Text(
                                choice,
                                modifier = Modifier.weight(1f),
                                textAlign = TextAlign.Start,
                            )
                            if (selected) {
                                Icon(Icons.Default.Check, contentDescription = "選択中")
                            }
                        }
                    }
                }

                ComprehensionQuestionType.FREE_TEXT -> {
                    TextField(
                        value = question.answer,
                        onValueChange = onAnswerChange,
                        enabled = enabled,
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text(stringResource(R.string.ui_038)) },
                        minLines = 2,
                        shape = SmoothCornerShape(16.dp),
                        colors = yanLangTextFieldColors(),
                    )
                }
            }
            if (question.isCorrect != null) {
                Spacer(Modifier.height(14.dp))
                Surface(
                    color = if (question.isCorrect == true) {
                        MaterialTheme.colorScheme.primaryContainer
                    } else {
                        MaterialTheme.colorScheme.errorContainer
                    },
                    shape = SmoothCornerShape(14.dp),
                ) {
                    Column(Modifier.padding(14.dp)) {
                        Text(
                            if (question.isCorrect == true) "正解です" else "見直しましょう",
                            fontWeight = FontWeight.Bold,
                            color = if (question.isCorrect == true) {
                                MaterialTheme.colorScheme.onPrimaryContainer
                            } else {
                                MaterialTheme.colorScheme.onErrorContainer
                            },
                        )
                        if (question.isCorrect == false &&
                            question.correctAnswer.isNotBlank()
                        ) {
                            Spacer(Modifier.height(4.dp))
                            Text(
                                "答え：${question.correctAnswer}",
                                style = MaterialTheme.typography.bodyMedium,
                            )
                        }
                        if (question.feedback.isNotBlank()) {
                            Spacer(Modifier.height(4.dp))
                            Text(
                                question.feedback,
                                style = MaterialTheme.typography.bodySmall,
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ComprehensionScoreCard(
    score: Int,
    correctCount: Int,
    totalCount: Int,
) {
    Card(
        shape = SmoothCornerShape(999.dp),
        colors = CardDefaults.cardColors(
            containerColor = AccentGreen,
        ),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                Icons.Default.CheckCircle,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(32.dp),
            )
            Spacer(Modifier.width(14.dp))
            Column {
                Text(
                    "理解度 $score%",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                )
                Text(
                    "$totalCount 問中 $correctCount 問正解",
                    color = Color.White,
                )
            }
        }
    }
}

@Composable
private fun ScoreCard(score: Int) {
    Surface(
        color = AccentGreen,
        contentColor = Color.White,
        shape = SmoothCornerShape(999.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            Text(
                "正答率$score%",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
            )
            Spacer(Modifier.width(6.dp))
            Icon(
                Icons.Default.Check,
                contentDescription = null,
                modifier = Modifier.size(18.dp),
            )
        }
    }
}

@Composable
private fun MistakeRow(
    source: String,
    correct: String,
) {
    Card(
        shape = SmoothCornerShape(999.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
        ),
        border = androidx.compose.foundation.BorderStroke(
            2.dp,
            Color(0xFFD5D5D5),
        ),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            ResultMark(false)
            Spacer(Modifier.width(10.dp))
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    source,
                    color = Color.Black,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    "〜$correct",
                    color = Color.Black,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}

@Composable
private fun AiTaskDialog(
    title: String,
    message: String,
) {
    Dialog(onDismissRequest = {}) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = SmoothCornerShape(28.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White,
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 28.dp, vertical = 30.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                val loadingComposition by rememberLottieComposition(
                    LottieCompositionSpec.Asset("loading.json"),
                )
                LottieAnimation(
                    composition = loadingComposition,
                    iterations = LottieConstants.IterateForever,
                    modifier = Modifier.size(100.dp),
                )
                Spacer(Modifier.height(22.dp))
                Text(
                    title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    message,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}

@Composable
@OptIn(ExperimentalFoundationApi::class)
private fun AiErrorCard(
    message: String,
    rawResponse: String?,
) {
    var showDetails by remember(message, rawResponse) { mutableStateOf(false) }
    Surface(
        modifier = Modifier.combinedClickable(
            onClick = {},
            onLongClick = { if (!rawResponse.isNullOrBlank()) showDetails = true },
        ),
        color = MaterialTheme.colorScheme.errorContainer,
        shape = SmoothCornerShape(16.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.Top,
        ) {
            Icon(
                Icons.Default.ErrorOutline,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.error,
                modifier = Modifier.size(24.dp),
            )
            Spacer(Modifier.width(12.dp))
            Column {
                Text(
                    "処理できませんでした",
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onErrorContainer,
                )
                Text(
                    message,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onErrorContainer,
                )
                Text(
                    if (rawResponse.isNullOrBlank()) {
                        "下のボタンからもう一度試せます。"
                    } else {
                        "長押しするとAIサーバーの応答を確認できます。"
                    },
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onErrorContainer,
                )
            }
        }
    }
    if (showDetails && !rawResponse.isNullOrBlank()) {
        AlertDialog(
            onDismissRequest = { showDetails = false },
            title = { Text("AIサーバーの応答") },
            text = {
                Text(
                    rawResponse,
                    modifier = Modifier.verticalScroll(rememberScrollState()),
                    style = MaterialTheme.typography.bodySmall,
                )
            },
            confirmButton = {
                TextButton(onClick = { showDetails = false }) { Text("閉じる") }
            },
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AnimatedPrimaryButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    playAnimation: Boolean,
    useGrayBackground: Boolean = false,
    contentPadding: androidx.compose.foundation.layout.PaddingValues,
    content: @Composable () -> Unit,
) {
    val composition by rememberLottieComposition(
        LottieCompositionSpec.Asset("bg-animation.json"),
    )
    val shape = SmoothCornerShape(999.dp)

    Box(
        modifier = modifier
            .clip(shape)
            .background(if (useGrayBackground) HeaderBackground else AccentGreen),
    ) {
        androidx.compose.runtime.key(playAnimation) {
            if (playAnimation && enabled) {
                LottieAnimation(
                    composition = composition,
                    iterations = 1,
                    modifier = Modifier.matchParentSize(),
                    contentScale = ContentScale.FillBounds,
                )
            }
        }
        Button(
            onClick = onClick,
            enabled = enabled,
            shape = shape,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = Color.White,
                disabledContainerColor = Color.Transparent,
                disabledContentColor = Color.Black.copy(alpha = 0.35f),
            ),
            elevation = ButtonDefaults.buttonElevation(
                defaultElevation = 0.dp,
                pressedElevation = 0.dp,
                focusedElevation = 0.dp,
                hoveredElevation = 0.dp,
            ),
            contentPadding = contentPadding,
        ) {
            content()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LearningScaffold(
    title: String,
    onBack: () -> Unit,
    onTopBarBack: () -> Unit,
    completeLabel: String,
    completeEnabled: Boolean,
    playCompleteButtonAnimation: Boolean = false,
    onComplete: () -> Unit,
    showBottomBack: Boolean = true,
    showTopBar: Boolean = true,
    content: @Composable ColumnScope.(androidx.compose.foundation.layout.PaddingValues) -> Unit,
) {
    Scaffold(
        containerColor = Color.White,
        topBar = {
            if (showTopBar) {
                Column(modifier = Modifier.background(HeaderBackground)) {
                    TopAppBar(
                        title = {
                            Text(
                                title,
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold,
                            )
                        },
                        navigationIcon = {
                            IconButton(
                                onClick = hapticAction(onTopBarBack),
                                modifier = Modifier
                                    .padding(start = 10.dp)
                                    .size(42.dp)
                                    .background(Color(0xFFECECEC), SmoothCornerShape(999.dp)),
                            ) {
                                Icon(
                                    Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "戻る",
                                    tint = Color.Black,
                                )
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = HeaderBackground,
                        ),
                    )
                }
            }
        },
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .windowInsetsPadding(WindowInsets.navigationBars)
                    .padding(horizontal = 20.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                if (showBottomBack) {
                    FloatingActionButton(
                        onClick = hapticAction(onBack),
                        modifier = Modifier.size(56.dp),
                        shape = SmoothCornerShape(999.dp),
                        containerColor = Color(0xFF111111),
                        contentColor = Color.White,
                        elevation = FloatingActionButtonDefaults.elevation(
                            defaultElevation = 0.dp,
                            pressedElevation = 0.dp,
                            focusedElevation = 0.dp,
                            hoveredElevation = 0.dp,
                        ),
                    ) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "戻る",
                        )
                    }
                }
                Spacer(Modifier.weight(1f))
                AnimatedPrimaryButton(
                    onClick = hapticAction(onComplete),
                    enabled = completeEnabled,
                    modifier = Modifier.height(56.dp),
                    playAnimation = playCompleteButtonAnimation,
                    useGrayBackground = !completeEnabled,
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(
                        horizontal = 24.dp,
                    ),
                ) {
                    Text(completeLabel, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.width(8.dp))
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                    )
                if (!showBottomBack) {
                    Spacer(Modifier.weight(1f))
                }
            }
            }
        },
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize()) {
            content(padding)
        }
    }
}

@Composable
private fun SourceCard(
    document: LearningDocument,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = HeaderBackground,
        ),
        shape = SmoothCornerShape(30.dp),
    ) {
        Text(
            highlightedSource(document),
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp),
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Medium,
            lineHeight = 27.sp,
        )
    }
}

private fun highlightedSource(document: LearningDocument): AnnotatedString {
    val completedIds = document.tokens.filter { it.isCorrect != null }.map { it.id }.toSet()
    val builder = AnnotatedString.Builder()
    var cursor = 0
    document.tokens.forEach { token ->
        val position = document.sourceText.indexOf(token.source, startIndex = cursor)
        if (position >= 0) {
            if (position > cursor) builder.append(document.sourceText.substring(cursor, position))
            val start = builder.length
            builder.append(token.source)
            if (token.id in completedIds) {
                builder.addStyle(SpanStyle(color = Color(0x66808080)), start, builder.length)
            }
            cursor = position + token.source.length
        }
    }
    if (cursor < document.sourceText.length) {
        builder.append(document.sourceText.substring(cursor))
    }
    return builder.toAnnotatedString()
}

private fun translatedPhrase(
    document: LearningDocument,
    connector: LearningToken,
): AnnotatedString {
    val index = document.tokens.indexOfFirst { it.id == connector.id }
    val previous = document.tokens
        .take(index.coerceAtLeast(0))
        .lastOrNull { it.kind == TokenKind.WORD }
    return AnnotatedString.Builder().apply {
        if (previous != null) {
            pushStyle(SpanStyle(color = Color(0xFF356A49)))
            append(previous.translation)
            pop()
        }
        append(connector.source)
    }.toAnnotatedString()
}

@Composable
private fun yanLangTextFieldColors() = TextFieldDefaults.colors(
    focusedContainerColor = InputBackground,
    unfocusedContainerColor = InputBackground,
    disabledContainerColor = InputBackground,
    errorContainerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.45f),
    focusedLabelColor = AccentGreen,
    unfocusedLabelColor = AccentGreen,
    focusedPlaceholderColor = PlaceholderGray,
    unfocusedPlaceholderColor = PlaceholderGray,
    cursorColor = Color.Black,
    focusedIndicatorColor = Color.Transparent,
    unfocusedIndicatorColor = Color.Transparent,
    disabledIndicatorColor = Color.Transparent,
    errorIndicatorColor = Color.Transparent,
)

@Composable
private fun inlineAnswerTextFieldColors() = TextFieldDefaults.colors(
    focusedContainerColor = Color.Transparent,
    unfocusedContainerColor = Color.Transparent,
    disabledContainerColor = Color.Transparent,
    focusedIndicatorColor = Color(0xFFD8D8D8),
    unfocusedIndicatorColor = Color(0xFFE2E2E2),
    disabledIndicatorColor = Color(0xFFE2E2E2),
    focusedPlaceholderColor = PlaceholderGray,
    unfocusedPlaceholderColor = PlaceholderGray,
    cursorColor = Color.Black,
)

private val ScreenBackground = Color(0xFFF6F6F6)
private val HeaderBackground = Color(0xFFF6F6F6)
private val InputBackground = Color(0xFFF6F6F6)
private val AccentGreen = Color(0xFF0900FF)
private val PlaceholderGray = Color(0x88000000)

private fun todayKey(): String =
    SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date())

private fun currentStreak(activityDates: Set<String>): Int {
    if (activityDates.isEmpty()) return 0
    val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.US)
    val cursor = Calendar.getInstance()
    if (formatter.format(cursor.time) !in activityDates) {
        cursor.add(Calendar.DAY_OF_YEAR, -1)
    }
    var streak = 0
    while (formatter.format(cursor.time) in activityDates) {
        streak++
        cursor.add(Calendar.DAY_OF_YEAR, -1)
    }
    return streak
}


private fun nativeLanguageName(code: String, custom: String): String = when (code) {
    "ja" -> "日本語"
    "en" -> "English"
    "ru" -> "Русский"
    "ko-kr" -> "한국어 (한국)"
    "ko-kp" -> "조선말 (조선및 연변)"
    "zh", "zh-CN" -> "中文（简体）"
    "zh-TW" -> "中文（繁體）"
    "ru" -> "Русский"
    else -> custom.ifBlank { "the learner's native language" }
}
