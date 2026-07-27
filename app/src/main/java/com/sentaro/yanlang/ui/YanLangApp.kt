package com.sentaro.yanlang.ui

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
import androidx.compose.runtime.CompositionLocalProvider
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
import androidx.compose.ui.platform.LocalContext
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
    val osUiLanguageCode = context.resources.configuration.locales[0].language
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

    CompositionLocalProvider(LocalUiLanguageCode provides osUiLanguageCode) {
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
    }
}

private const val ONBOARDING_PREFS = "yanlang_onboarding"
private const val ONBOARDING_COMPLETED = "completed"

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
        LottieAnimation(composition, iterations = 1, modifier = Modifier.fillMaxSize(), contentScale = ContentScale.Crop)
        Box(Modifier.fillMaxSize()) {
            androidx.compose.animation.AnimatedVisibility(
                visible,
                enter = fadeIn(tween(100)) + slideInVertically(tween(220)) { it / 3 },
                modifier = Modifier.align(Alignment.Center),
            ) {
                Text(l("はじめまして"), style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, color = Color.White)
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
                    Text(l("続行"), fontWeight = FontWeight.Bold)
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
    val languages = listOf("ja" to "日本語", "en" to "English", "ko-kr" to "한국어 (대한민국)", "ko-kp" to "조선말 (조선민주주의인민공화국)", "zh-CN" to "中文（简体）", "zh-TW" to "中文（繁體）", "other" to l("その他"))
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
        Text(l("母国語を教えてください"), style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(8.dp))
        Text(l("あなたに合った訳や問題を表示します"), color = Color(0xFF666666))
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
            TextField(customLanguage, { onLanguageChange("other", it) }, placeholder = { Text(l("母国語を入力")) }, singleLine = true, modifier = Modifier.fillMaxWidth(), shape = SmoothCornerShape(18.dp), colors = yanLangTextFieldColors())
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
                Text(l("完了"), fontWeight = FontWeight.Bold)
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
                        if (rootTab == 1) l("継続の力") else l("設定"),
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
                            Icon(Icons.AutoMirrored.Filled.Article, contentDescription = l("長文"), modifier = Modifier.size(22.dp), tint = if (librarySection == 0) AccentGreen else Color.Black.copy(alpha = 0.5f))
                            Spacer(Modifier.width(6.dp))
                            Text(l("長文"), fontWeight = FontWeight.Bold, fontSize = 20.sp, color = Color.Black.copy(alpha = if (librarySection == 0) 1f else 0.5f))
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
                            Icon(Icons.Default.Style, contentDescription = l("単語"), modifier = Modifier.size(22.dp), tint = if (librarySection == 1) AccentGreen else Color.Black.copy(alpha = 0.5f))
                            Spacer(Modifier.width(6.dp))
                            Text(l("単語"), fontWeight = FontWeight.Bold, fontSize = 20.sp, color = Color.Black.copy(alpha = if (librarySection == 1) 1f else 0.5f))
                        }
                        if (librarySection == 1) Box(Modifier.padding(top = 4.dp).width(42.dp).height(3.dp).background(AccentGreen, SmoothCornerShape(999.dp)))
                    }
                }
                Spacer(Modifier.height(8.dp))
                SearchField(
                    value = documentQuery,
                    onValueChange = { documentQuery = it },
                    placeholder = if (librarySection == 0) l("タイトル・本文を検索") else l("テーマ・単語を検索"),
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
                    Text(l(if (isRootNavigationAction) "教材" else "新規"), fontWeight = FontWeight.Bold)
                }
                LibraryBottomItem(
                    selected = rootTab == 1,
                    icon = Icons.Default.CalendarMonth,
                    label = l("継続の力"),
                    onClick = { onRootTabChange(1) },
                )
                LibraryBottomItem(
                    selected = rootTab == 2,
                    icon = Icons.Default.Settings,
                    label = l("設定"),
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
                                    l("続きから再開しますか？"),
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onPrimary,
                                )
                                Spacer(Modifier.height(4.dp))
                                Text(
                                    resumeDocument.title.ifBlank { l("無題の教材") },
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
            title = { Text(l("教材を削除しますか？")) },
            text = {
                Text(
                    "「${document.title.ifBlank { "無題の教材" }}」を削除します。\n\n" +
                        "削除後はスナックバーが表示されている間だけ元に戻せます。\n" +
                        "スナックバーが消えると復元できません。",
                )
            },
            dismissButton = {
                TextButton(onClick = { pendingDelete = null }) {
                    Text(l("キャンセル"))
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
                    Text(l("削除"))
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
        "ko-kr" to "한국어 (대한민국)",
        "ko-kp" to "조선말 (조선민주주의인민공화국)",
        "zh-CN" to "中文（简体）",
        "zh-TW" to "中文（繁體）",
        "other" to l("その他"),
    )
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 18.dp),
    ) {
        Text(
            l("母国語"),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
        )
        Spacer(Modifier.height(6.dp))
        Text(
            l("AIの訳・問題・フィードバックと単語帳の言語候補に使用します。"),
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
                placeholder = { Text(l("母国語を入力")) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                shape = SmoothCornerShape(20.dp),
                colors = yanLangTextFieldColors(),
            )
        }
        Spacer(Modifier.height(28.dp))
        Text(
            "${l("バージョン")} ${BuildConfig.VERSION_NAME}",
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(Modifier.height(8.dp))
        SettingsExternalLink(
            label = l("About me"),
            url = "https://search3958.github.io/",
            onOpen = uriHandler::openUri,
        )
        Spacer(Modifier.height(4.dp))
        SettingsExternalLink(
            label = l("他のアプリ一覧"),
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
    val uiLanguageCode = LocalUiLanguageCode.current
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
                    Text(l("連続記録"), color = Color.White.copy(alpha = 0.78f))
                    Text(
                        when (LocalUiLanguageCode.current) {
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
                    l(day),
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
            l("学習を完了するか単語帳を開いた日に色がつきます。"),
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
            l("最初の教材を作りましょう"),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
        )
        Spacer(Modifier.height(8.dp))
        Text(
            l("短い文章を貼り付けるだけで、\n単語から全文まで順番に学べます。"),
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
    else -> code
}

private fun vocabularyLanguageFlag(code: String): String = when (code) {
    "ja" -> "🇯🇵"
    "ko-kr" -> "🇰🇷"
    "ko-kp" -> "🇰🇵"
    "zh", "zh-CN" -> "🇨🇳"
    "zh-TW" -> "🇨🇳"
    "en" -> "🇬🇧"
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
            IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = l("戻る")) }
            Text(l(themeName), style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        }
        Spacer(Modifier.height(6.dp))
        if (entries.isEmpty()) {
            Text(l("単語データがありません"))
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
                            Text("${l("母国語")}: $nativeText", style = MaterialTheme.typography.titleLarge, textAlign = TextAlign.Center, modifier = if (entry.id in revealed) Modifier else Modifier.blur(11.dp))
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
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = l("戻る"))
            }
            Text(
                book.title.ifBlank { l("無題の単語帳") },
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f),
            )
            IconButton(onClick = onEdit) {
                Icon(Icons.Default.Edit, contentDescription = l("編集"))
            }
            IconButton(onClick = { confirmDelete = true }) {
                Icon(Icons.Default.Delete, contentDescription = l("削除"), tint = MaterialTheme.colorScheme.error)
            }
        }
        if (entries.isEmpty()) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(l("単語がありません。編集から追加してください。"), color = MaterialTheme.colorScheme.onSurfaceVariant)
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
            title = { Text(l("単語帳を削除しますか？")) },
            text = { Text("「${book.title.ifBlank { "無題の単語帳" }}」と登録した単語をすべて削除します。この操作は元に戻せません。") },
            dismissButton = {
                TextButton(onClick = { confirmDelete = false }) { Text(l("キャンセル")) }
            },
            confirmButton = {
                TextButton(onClick = { confirmDelete = false; onDelete() }) {
                    Text(l("削除"), color = MaterialTheme.colorScheme.error)
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
                title = { Text(if (isNew) l("新しい単語帳") else l("単語帳を編集"), fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = l("戻る"))
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
                        Text(l("保存"), fontWeight = FontWeight.Bold)
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
                    label = l("単語帳の名前"),
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
                                label = l("単語"),
                                singleLine = true,
                            )
                            CompactVocabularyField(
                                value = entry.pronunciation,
                                onValueChange = { value -> entries = entries.map { if (it.id == entry.id) it.copy(pronunciation = value) else it } },
                                label = l("読み方（任意）"),
                                singleLine = true,
                            )
                            CompactVocabularyField(
                                value = entry.meaning,
                                onValueChange = { value -> entries = entries.map { if (it.id == entry.id) it.copy(meaning = value) else it } },
                                label = l("意味"),
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
                    Text(l("単語を追加"), fontWeight = FontWeight.Bold)
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
        Text(l("自分の単語帳"), style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        if (filteredCustomBooks.isEmpty()) {
            Text(
                if (customBooks.isEmpty()) l("「新規」から自分だけの単語帳を作れます") else l("一致する単語帳がありません"),
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
                            Text(book.title.ifBlank { l("無題の単語帳") }, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
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
        Text(l("テーマから選ぶ"), style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        Text(l("言語を選択"), style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
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
        Text(l("学びたいテーマを選択してください"), color = MaterialTheme.colorScheme.onSurfaceVariant)
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
                        Text(l(label), modifier = Modifier.padding(20.dp), style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
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
    LearningStep.EDITOR -> l("文章入力から再開")
    LearningStep.WORDBOOK -> l("単語帳から再開")
    LearningStep.WORD_CHECK -> l("単語確認から再開")
    LearningStep.CONNECTOR_CHECK -> l("接続詞確認から再開")
    LearningStep.FINAL_TRANSLATION -> l("長文翻訳から再開")
    LearningStep.COMPREHENSION_CHECK -> l("理解チェックから再開")
    LearningStep.LIBRARY -> l("学習を再開")
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
                        document.title.ifBlank { l("無題の教材") },
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                    )
                    Spacer(Modifier.height(5.dp))
                    Text(
                        document.sourceText.ifBlank { l("本文を入力してください") },
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
                    ) { Text(l("単語帳")) }
                    Button(
                        onClick = hapticAction(onClick),
                        modifier = Modifier.weight(1f),
                    ) { Text(l("続きから")) }
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
                    document.title.ifBlank { l("無題の教材") },
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                )
                Spacer(Modifier.height(6.dp))
                Text(l("学習する項目を選択"), color = MaterialTheme.colorScheme.onSurfaceVariant)
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
                    ) { Text(l(label)) }
                }
                Button(
                    onClick = hapticAction(onRedo),
                    modifier = Modifier.fillMaxWidth().padding(top = 6.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer,
                        contentColor = MaterialTheme.colorScheme.onErrorContainer,
                    ),
                ) { Text(l("やり直す")) }
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
            title = l("学習カードを作成中"),
            message = l("文章の意味を読み取り、単語と接続表現に分けています。"),
        )
    }
    LearningScaffold(
        title = l("文を入力"),
        onBack = onBack,
        onTopBarBack = onTopBarBack,
        completeLabel = if (isProcessing) l("解析中…") else l("進む"),
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
                l("学習したい言語・タイトル・本文を入力すると続行できます"),
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
                placeholder = { Text(l("学習したい言語")) },
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
                        l("タイトル欄"),
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
                placeholder = { Text(l("ここに学習する文章を貼り付けます")) },
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
        title = l("単語帳"),
        onBack = onBack,
        onTopBarBack = onTopBarBack,
        completeLabel = if (singlePageMode) l("完了") else l("進む"),
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
                    l("クリックで表示  スワイプで次へ"),
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
        title = l("単語確認"),
        onBack = onBack,
        onTopBarBack = onTopBarBack,
        showTopBar = !isInputFocused,
        completeLabel = if (singlePageMode) l("完了") else l("進む"),
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
                    l("単語を訳しましょう"),
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
                            l("知っている単語をスキップ"),
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
                placeholder = { Text(l("訳を入力")) },
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
        title = l("接続詞の確認"),
        onBack = onBack,
        onTopBarBack = onTopBarBack,
        completeLabel = if (singlePageMode) l("完了") else l("進む"),
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
                l("接続詞を選択"),
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
                l("全文翻訳を採点中")
            } else {
                l("理解チェックを作成中")
            },
            message = if (document.finalScore == null) {
                l("原文と入力した全文の意味を照らし合わせています。")
            } else {
                l("原文から内容理解を確認する質問を作っています。")
            },
        )
    }
    LearningScaffold(
        title = l("長文翻訳"),
        onBack = onBack,
        onTopBarBack = onTopBarBack,
        completeLabel = when {
            singlePageMode && document.finalScore != null -> l("確認へ")
            isProcessing && document.finalScore != null -> l("問題を作成中…")
            isProcessing -> l("AIで採点中…")
            document.finalScore == null -> l("AIで採点する")
            else -> l("確認へ")
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
                l("翻訳してください"),
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
                    placeholder = { Text(l("文章全体の意味を入力してください")) },
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
                        Text(l("この翻訳をやり直す"))
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
            title = l("回答を確認中"),
            message = l("原文と回答の意味を照らし合わせています。"),
        )
    }

    LearningScaffold(
        title = l("理解チェック"),
        onBack = onBack,
        onTopBarBack = onTopBarBack,
        completeLabel = when {
            isProcessing -> l("回答を確認中…")
            checked -> l("学習を完了")
            else -> l("回答を確認")
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
                l("文章の内容を確認"),
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
                        placeholder = { Text(l("回答を入力")) },
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
    "ko-kr" -> "한국어 (한국)"
    "ko-kp" -> "조선말 (조선민주주의인민공화국)"
    "zh", "zh-CN" -> "中文（简体）"
    "zh-TW" -> "中文（繁體）"
    else -> custom.ifBlank { "the learner's native language" }
}

private val LocalUiLanguageCode = staticCompositionLocalOf { "ja" }

@Composable
internal fun l(japanese: String): String =
    localized(LocalUiLanguageCode.current, japanese)

private fun localized(code: String, japanese: String): String {
    val table = when (code) {
        "ko" -> koreanUi
        "zh" -> chineseUi
        "es" -> spanishUi
        "ja" -> emptyMap()
        else -> englishUi
    }
    return table[japanese] ?: japanese
}

private val englishUi = mapOf(
    "はじめまして" to "Nice to meet you", "続行" to "Continue",
    "母国語を教えてください" to "What is your native language?",
    "あなたに合った訳や問題を表示します" to "We'll tailor translations and exercises for you",
    "長文" to "Texts", "単語" to "Words", "新規" to "New", "教材" to "Lessons",
    "継続の力" to "Progress", "設定" to "Settings", "母国語" to "Native language",
    "連続記録" to "Current streak", "日" to "Sun", "月" to "Mon", "火" to "Tue",
    "水" to "Wed", "木" to "Thu", "金" to "Fri", "土" to "Sat",
    "自分の単語帳" to "My wordbooks", "テーマから選ぶ" to "Choose a theme",
    "言語を選択" to "Choose a language",
    "学びたいテーマを選択してください" to "Choose a theme to study",
    "文を入力" to "Enter a text", "解析中…" to "Analyzing…", "進む" to "Continue",
    "学習したい言語" to "Language to learn", "タイトル欄" to "Title",
    "ここに学習する文章を貼り付けます" to "Paste the text you want to learn here",
    "学習したい言語・タイトル・本文を入力すると続行できます" to
        "Enter the language, title, and text to continue",
    "単語帳" to "Wordbook", "単語確認" to "Word check", "接続詞の確認" to "Connector check",
    "長文翻訳" to "Full translation", "理解チェック" to "Comprehension check",
    "完了" to "Done", "訳を入力" to "Enter translation",
    "文章全体の意味を入力してください" to "Enter the meaning of the full text",
    "回答を入力" to "Enter your answer", "この翻訳をやり直す" to "Retry this translation",
    "保存" to "Save", "単語を追加" to "Add word", "戻る" to "Back",
    "キャンセル" to "Cancel", "削除" to "Delete", "編集" to "Edit",
    "継続は力なり" to "Consistency is strength",
    "日進月歩 ・ 一日一善" to "Steady progress · One good deed a day",
    "母国語を入力" to "Enter your native language",
    "バージョン" to "Version", "About me" to "About me", "他のアプリ一覧" to "More apps",
    "その他" to "Other", "タイトル・本文を検索" to "Search titles and texts",
    "テーマ・単語を検索" to "Search themes and words",
    "AIの訳・問題・フィードバックとアプリの表示言語に使用します。" to
        "Used for AI translations, questions, feedback, and the app language.",
    "AIの訳・問題・フィードバックと単語帳の言語候補に使用します。" to
        "Used for AI translations, questions, feedback, and to hide your native language from wordbook choices.",
    "学習を完了するか単語帳を開いた日に色がつきます。" to
        "Days are colored when you finish a lesson or open a wordbook.",
    "単語データがありません" to "No vocabulary data",
    "数字" to "Numbers", "数え方" to "Ordinals", "果物" to "Fruit", "野菜" to "Vegetables",
    "色" to "Colors", "電子製品" to "Electronics", "数学用語" to "Math terms",
    "文法用語" to "Grammar terms", "生物用語" to "Biology terms", "科学用語" to "Science terms",
    "主要国名" to "Countries", "衣類" to "Clothing", "家具" to "Furniture",
    "手荷物" to "Luggage", "方向" to "Directions", "香りと視覚感情" to "Scents and visual emotions",
    "感情" to "Emotions",
    "教材を削除しますか？" to "Delete this lesson?", "単語帳を削除しますか？" to "Delete this wordbook?",
    "単語がありません。編集から追加してください。" to "No words yet. Add some from Edit.",
    "続きから" to "Resume", "学習する項目を選択" to "Choose what to study",
    "学習カードを作成中" to "Creating learning cards",
    "文章の意味を読み取り、単語と接続表現に分けています。" to
        "Reading the text and splitting it into words and connecting expressions.",
    "回答を確認中" to "Checking your answer",
    "原文と回答の意味を照らし合わせています。" to "Comparing the meaning of the source and your answer.",
    "続きから再開しますか？" to "Resume where you left off?", "無題の教材" to "Untitled lesson",
    "本文を入力してください" to "Enter the text", "最初の教材を作りましょう" to "Create your first lesson",
    "短い文章を貼り付けるだけで、\n単語から全文まで順番に学べます。" to
        "Paste a short text and learn it step by step, from words to the full passage.",
    "新しい単語帳" to "New wordbook", "単語帳を編集" to "Edit wordbook",
    "単語帳の名前" to "Wordbook name", "読み方（任意）" to "Pronunciation (optional)", "意味" to "Meaning",
    "無題の単語帳" to "Untitled wordbook", "「新規」から自分だけの単語帳を作れます" to "Create your own wordbook from New",
    "一致する単語帳がありません" to "No matching wordbooks",
    "クリックで表示  スワイプで次へ" to "Tap to reveal · Swipe for next",
    "単語を訳しましょう" to "Translate the words", "知っている単語をスキップ" to "Skip words you know",
    "接続詞を選択" to "Choose the connector", "翻訳してください" to "Translate the text",
    "全文翻訳を採点中" to "Grading your translation", "理解チェックを作成中" to "Creating comprehension questions",
    "原文と入力した全文の意味を照らし合わせています。" to "Comparing the source text with your translation.",
    "原文から内容理解を確認する質問を作っています。" to "Creating questions that check your understanding.",
    "確認へ" to "Review", "問題を作成中…" to "Creating questions…", "AIで採点中…" to "AI is grading…",
    "AIで採点する" to "Grade with AI", "回答を確認中…" to "Checking answers…",
    "学習を完了" to "Finish lesson", "回答を確認" to "Check answers", "文章の内容を確認" to "Check your understanding",
    "文章入力から再開" to "Resume text entry", "単語帳から再開" to "Resume wordbook",
    "単語確認から再開" to "Resume word check", "接続詞確認から再開" to "Resume connectors",
    "長文翻訳から再開" to "Resume translation", "理解チェックから再開" to "Resume comprehension",
    "学習を再開" to "Resume learning", "やり直す" to "Start over", "接続詞確認" to "Connector check",
    "全文翻訳" to "Full translation", "開始" to "Start", "もう一度プレイ" to "Play again",
    "最終スコア" to "Final score",
)

private val koreanUi = mapOf(
    "はじめまして" to "처음 뵙겠습니다", "続行" to "계속",
    "母国語を教えてください" to "모국어를 알려 주세요",
    "あなたに合った訳や問題を表示します" to "당신에게 맞는 번역과 문제를 보여 드립니다",
    "長文" to "장문", "単語" to "단어", "新規" to "새로 만들기", "教材" to "학습 자료",
    "継続の力" to "꾸준함의 힘", "設定" to "설정", "母国語" to "모국어",
    "連続記録" to "연속 기록", "日" to "일", "月" to "월", "火" to "화",
    "水" to "수", "木" to "목", "金" to "금", "土" to "토",
    "自分の単語帳" to "내 단어장", "テーマから選ぶ" to "주제로 선택",
    "言語を選択" to "언어 선택", "学びたいテーマを選択してください" to "학습할 주제를 선택하세요",
    "文を入力" to "문장 입력", "解析中…" to "분석 중…", "進む" to "계속",
    "学習したい言語" to "학습할 언어", "タイトル欄" to "제목",
    "ここに学習する文章を貼り付けます" to "학습할 글을 여기에 붙여 넣으세요",
    "学習したい言語・タイトル・本文を入力すると続行できます" to "언어, 제목, 본문을 입력하면 계속할 수 있습니다",
    "単語帳" to "단어장", "単語確認" to "단어 확인", "接続詞の確認" to "연결 표현 확인",
    "長文翻訳" to "전체 번역", "理解チェック" to "이해도 확인", "完了" to "완료",
    "訳を入力" to "번역 입력", "文章全体の意味を入力してください" to "전체 문장의 의미를 입력하세요",
    "回答を入力" to "답변 입력", "この翻訳をやり直す" to "이 번역 다시 하기",
    "保存" to "저장", "単語を追加" to "단어 추가", "戻る" to "뒤로",
    "キャンセル" to "취소", "削除" to "삭제", "編集" to "편집",
    "継続は力なり" to "꾸준함은 힘이다", "日進月歩 ・ 一日一善" to "날마다 발전 · 하루 한 가지 선행",
    "母国語を入力" to "모국어 입력",
    "バージョン" to "버전", "About me" to "About me", "他のアプリ一覧" to "다른 앱",
    "その他" to "기타", "タイトル・本文を検索" to "제목·본문 검색",
    "テーマ・単語を検索" to "주제·단어 검색",
    "AIの訳・問題・フィードバックとアプリの表示言語に使用します。" to
        "AI 번역, 문제, 피드백과 앱 표시 언어에 사용합니다.",
    "AIの訳・問題・フィードバックと単語帳の言語候補に使用します。" to
        "AI 번역, 문제, 피드백과 단어장 언어 후보에서 모국어를 숨기는 데 사용합니다.",
    "学習を完了するか単語帳を開いた日に色がつきます。" to
        "학습을 완료하거나 단어장을 연 날에 색이 표시됩니다.",
    "単語データがありません" to "단어 데이터가 없습니다",
    "数字" to "숫자", "数え方" to "서수", "果物" to "과일", "野菜" to "채소",
    "色" to "색", "電子製品" to "전자제품", "数学用語" to "수학 용어",
    "文法用語" to "문법 용어", "生物用語" to "생물 용어", "科学用語" to "과학 용어",
    "主要国名" to "주요 국가", "衣類" to "의류", "家具" to "가구",
    "手荷物" to "수하물", "方向" to "방향", "香りと視覚感情" to "향기와 시각 감정",
    "感情" to "감정",
    "教材を削除しますか？" to "이 학습 자료를 삭제할까요?", "単語帳を削除しますか？" to "단어장을 삭제할까요?",
    "単語がありません。編集から追加してください。" to "단어가 없습니다. 편집에서 추가하세요.",
    "続きから" to "이어서", "学習する項目を選択" to "학습 항목 선택",
    "学習カードを作成中" to "학습 카드 생성 중",
    "文章の意味を読み取り、単語と接続表現に分けています。" to "글의 의미를 읽고 단어와 연결 표현으로 나누고 있습니다.",
    "回答を確認中" to "답변 확인 중", "原文と回答の意味を照らし合わせています。" to "원문과 답변의 의미를 비교하고 있습니다.",
    "続きから再開しますか？" to "이어서 학습할까요?", "無題の教材" to "제목 없는 학습 자료",
    "本文を入力してください" to "본문을 입력하세요", "最初の教材を作りましょう" to "첫 학습 자료를 만들어 보세요",
    "短い文章を貼り付けるだけで、\n単語から全文まで順番に学べます。" to "짧은 글을 붙여 넣고 단어부터 전체 글까지 순서대로 학습하세요.",
    "新しい単語帳" to "새 단어장", "単語帳を編集" to "단어장 편집", "単語帳の名前" to "단어장 이름",
    "読み方（任意）" to "발음(선택)", "意味" to "뜻", "無題の単語帳" to "제목 없는 단어장",
    "「新規」から自分だけの単語帳を作れます" to "새로 만들기에서 나만의 단어장을 만들 수 있습니다",
    "一致する単語帳がありません" to "일치하는 단어장이 없습니다",
    "クリックで表示  スワイプで次へ" to "탭하여 표시 · 스와이프하여 다음",
    "単語を訳しましょう" to "단어를 번역해 보세요", "知っている単語をスキップ" to "아는 단어 건너뛰기",
    "接続詞を選択" to "연결 표현 선택", "翻訳してください" to "번역해 보세요",
    "全文翻訳を採点中" to "전체 번역 채점 중", "理解チェックを作成中" to "이해도 문제 생성 중",
    "原文と入力した全文の意味を照らし合わせています。" to "원문과 입력한 번역의 의미를 비교하고 있습니다.",
    "原文から内容理解を確認する質問を作っています。" to "내용 이해를 확인할 문제를 만들고 있습니다.",
    "確認へ" to "확인", "問題を作成中…" to "문제 생성 중…", "AIで採点中…" to "AI 채점 중…",
    "AIで採点する" to "AI로 채점", "回答を確認中…" to "답변 확인 중…", "学習を完了" to "학습 완료",
    "回答を確認" to "답변 확인", "文章の内容を確認" to "글의 내용 확인", "やり直す" to "다시 시작",
    "接続詞確認" to "연결 표현 확인", "全文翻訳" to "전체 번역",
    "開始" to "시작", "もう一度プレイ" to "다시 플레이", "最終スコア" to "최종 점수",
)

private val chineseUi = mapOf(
    "はじめまして" to "初次见面", "続行" to "继续",
    "母国語を教えてください" to "请告诉我们您的母语",
    "あなたに合った訳や問題を表示します" to "我们会为您显示合适的翻译和练习",
    "長文" to "长文", "単語" to "单词", "新規" to "新建", "教材" to "教材",
    "継続の力" to "坚持的力量", "設定" to "设置", "母国語" to "母语",
    "連続記録" to "连续记录", "日" to "日", "月" to "一", "火" to "二",
    "水" to "三", "木" to "四", "金" to "五", "土" to "六",
    "自分の単語帳" to "我的单词本", "テーマから選ぶ" to "按主题选择",
    "言語を選択" to "选择语言", "学びたいテーマを選択してください" to "请选择学习主题",
    "文を入力" to "输入文章", "解析中…" to "分析中…", "進む" to "继续",
    "学習したい言語" to "要学习的语言", "タイトル欄" to "标题",
    "ここに学習する文章を貼り付けます" to "在此粘贴要学习的文章",
    "学習したい言語・タイトル・本文を入力すると続行できます" to "输入语言、标题和正文后即可继续",
    "単語帳" to "单词本", "単語確認" to "单词检查", "接続詞の確認" to "连接表达检查",
    "長文翻訳" to "全文翻译", "理解チェック" to "理解检查", "完了" to "完成",
    "訳を入力" to "输入翻译", "文章全体の意味を入力してください" to "请输入全文含义",
    "回答を入力" to "输入答案", "この翻訳をやり直す" to "重新翻译",
    "保存" to "保存", "単語を追加" to "添加单词", "戻る" to "返回",
    "キャンセル" to "取消", "削除" to "删除", "編集" to "编辑",
    "継続は力なり" to "坚持就是力量", "日進月歩 ・ 一日一善" to "日进月步 · 日行一善",
    "母国語を入力" to "输入母语",
    "バージョン" to "版本", "About me" to "About me", "他のアプリ一覧" to "更多应用",
    "その他" to "其他", "タイトル・本文を検索" to "搜索标题和正文",
    "テーマ・単語を検索" to "搜索主题和单词",
    "AIの訳・問題・フィードバックとアプリの表示言語に使用します。" to
        "用于 AI 翻译、问题、反馈和应用显示语言。",
    "AIの訳・問題・フィードバックと単語帳の言語候補に使用します。" to
        "用于 AI 翻译、问题、反馈，并从单词本语言选项中隐藏母语。",
    "学習を完了するか単語帳を開いた日に色がつきます。" to
        "完成学习或打开单词本的日期会显示颜色。",
    "単語データがありません" to "没有单词数据",
    "数字" to "数字", "数え方" to "序数", "果物" to "水果", "野菜" to "蔬菜",
    "色" to "颜色", "電子製品" to "电子产品", "数学用語" to "数学术语",
    "文法用語" to "语法术语", "生物用語" to "生物术语", "科学用語" to "科学术语",
    "主要国名" to "主要国家", "衣類" to "服装", "家具" to "家具",
    "手荷物" to "行李", "方向" to "方向", "香りと視覚感情" to "气味与视觉情绪",
    "感情" to "情绪",
    "教材を削除しますか？" to "删除此教材？", "単語帳を削除しますか？" to "删除此单词本？",
    "単語がありません。編集から追加してください。" to "还没有单词，请从编辑中添加。",
    "続きから" to "继续", "学習する項目を選択" to "选择学习项目",
    "学習カードを作成中" to "正在创建学习卡片",
    "文章の意味を読み取り、単語と接続表現に分けています。" to "正在理解文章并拆分为单词和连接表达。",
    "回答を確認中" to "正在检查答案", "原文と回答の意味を照らし合わせています。" to "正在比较原文和答案的含义。",
    "続きから再開しますか？" to "继续上次的学习？", "無題の教材" to "无标题教材",
    "本文を入力してください" to "请输入正文", "最初の教材を作りましょう" to "创建第一个教材",
    "短い文章を貼り付けるだけで、\n単語から全文まで順番に学べます。" to "粘贴一段短文，从单词到全文逐步学习。",
    "新しい単語帳" to "新单词本", "単語帳を編集" to "编辑单词本", "単語帳の名前" to "单词本名称",
    "読み方（任意）" to "读音（可选）", "意味" to "含义", "無題の単語帳" to "无标题单词本",
    "「新規」から自分だけの単語帳を作れます" to "从新建创建自己的单词本", "一致する単語帳がありません" to "没有匹配的单词本",
    "クリックで表示  スワイプで次へ" to "点击显示 · 滑动到下一张", "単語を訳しましょう" to "翻译单词",
    "知っている単語をスキップ" to "跳过已知单词", "接続詞を選択" to "选择连接表达", "翻訳してください" to "请翻译",
    "全文翻訳を採点中" to "正在批改全文翻译", "理解チェックを作成中" to "正在创建理解问题",
    "原文と入力した全文の意味を照らし合わせています。" to "正在比较原文和输入的翻译。",
    "原文から内容理解を確認する質問を作っています。" to "正在创建检查内容理解的问题。",
    "確認へ" to "查看", "問題を作成中…" to "正在创建问题…", "AIで採点中…" to "AI 正在评分…",
    "AIで採点する" to "使用 AI 评分", "回答を確認中…" to "正在检查答案…", "学習を完了" to "完成学习",
    "回答を確認" to "检查答案", "文章の内容を確認" to "检查文章内容", "やり直す" to "重新开始",
    "接続詞確認" to "连接表达检查", "全文翻訳" to "全文翻译",
    "開始" to "开始", "もう一度プレイ" to "再玩一次", "最終スコア" to "最终得分",
)

private val spanishUi = mapOf(
    "はじめまして" to "Encantado de conocerte", "続行" to "Continuar",
    "母国語を教えてください" to "¿Cuál es tu lengua materna?",
    "あなたに合った訳や問題を表示します" to "Mostraremos traducciones y ejercicios para ti",
    "長文" to "Textos", "単語" to "Palabras", "新規" to "Nuevo", "教材" to "Lecciones",
    "継続の力" to "Progreso", "設定" to "Ajustes", "母国語" to "Lengua materna",
    "連続記録" to "Racha actual", "日" to "D", "月" to "L", "火" to "M",
    "水" to "X", "木" to "J", "金" to "V", "土" to "S",
    "自分の単語帳" to "Mis vocabularios", "テーマから選ぶ" to "Elegir un tema",
    "言語を選択" to "Elegir idioma", "学びたいテーマを選択してください" to "Elige un tema para estudiar",
    "文を入力" to "Introduce un texto", "解析中…" to "Analizando…", "進む" to "Continuar",
    "学習したい言語" to "Idioma que quieres aprender", "タイトル欄" to "Título",
    "ここに学習する文章を貼り付けます" to "Pega aquí el texto que quieres aprender",
    "学習したい言語・タイトル・本文を入力すると続行できます" to "Introduce el idioma, el título y el texto para continuar",
    "単語帳" to "Vocabulario", "単語確認" to "Repaso de palabras",
    "接続詞の確認" to "Repaso de conectores", "長文翻訳" to "Traducción completa",
    "理解チェック" to "Comprensión", "完了" to "Listo", "訳を入力" to "Escribe la traducción",
    "文章全体の意味を入力してください" to "Escribe el significado del texto completo",
    "回答を入力" to "Escribe tu respuesta", "この翻訳をやり直す" to "Reintentar esta traducción",
    "保存" to "Guardar", "単語を追加" to "Añadir palabra", "戻る" to "Atrás",
    "キャンセル" to "Cancelar", "削除" to "Eliminar", "編集" to "Editar",
    "継続は力なり" to "La constancia da fuerza",
    "日進月歩 ・ 一日一善" to "Progreso diario · Una buena acción al día",
    "母国語を入力" to "Introduce tu lengua materna",
    "バージョン" to "Versión", "About me" to "About me", "他のアプリ一覧" to "Más aplicaciones",
    "その他" to "Otro", "タイトル・本文を検索" to "Buscar títulos y textos",
    "テーマ・単語を検索" to "Buscar temas y palabras",
    "AIの訳・問題・フィードバックとアプリの表示言語に使用します。" to
        "Se usa para traducciones, preguntas y comentarios de la IA, y para el idioma de la app.",
    "AIの訳・問題・フィードバックと単語帳の言語候補に使用します。" to
        "Se usa para la IA y para ocultar tu lengua materna de las opciones del vocabulario.",
    "学習を完了するか単語帳を開いた日に色がつきます。" to
        "Se colorean los días en que terminas una lección o abres un vocabulario.",
    "単語データがありません" to "No hay datos de vocabulario",
    "数字" to "Números", "数え方" to "Ordinales", "果物" to "Frutas", "野菜" to "Verduras",
    "色" to "Colores", "電子製品" to "Electrónica", "数学用語" to "Términos matemáticos",
    "文法用語" to "Términos gramaticales", "生物用語" to "Términos de biología",
    "科学用語" to "Términos científicos", "主要国名" to "Países", "衣類" to "Ropa",
    "家具" to "Muebles", "手荷物" to "Equipaje", "方向" to "Direcciones",
    "香りと視覚感情" to "Aromas y emociones visuales", "感情" to "Emociones",
    "教材を削除しますか？" to "¿Eliminar esta lección?", "単語帳を削除しますか？" to "¿Eliminar este vocabulario?",
    "単語がありません。編集から追加してください。" to "Aún no hay palabras. Añádelas desde Editar.",
    "続きから" to "Continuar", "学習する項目を選択" to "Elige qué estudiar",
    "学習カードを作成中" to "Creando tarjetas de estudio",
    "文章の意味を読み取り、単語と接続表現に分けています。" to "Leyendo el texto y separándolo en palabras y conectores.",
    "回答を確認中" to "Comprobando la respuesta",
    "原文と回答の意味を照らし合わせています。" to "Comparando el significado del original y tu respuesta.",
    "続きから再開しますか？" to "¿Continuar donde lo dejaste?", "無題の教材" to "Lección sin título",
    "本文を入力してください" to "Introduce el texto", "最初の教材を作りましょう" to "Crea tu primera lección",
    "短い文章を貼り付けるだけで、\n単語から全文まで順番に学べます。" to "Pega un texto corto y aprende paso a paso, desde palabras hasta el texto completo.",
    "新しい単語帳" to "Nuevo vocabulario", "単語帳を編集" to "Editar vocabulario",
    "単語帳の名前" to "Nombre del vocabulario", "読み方（任意）" to "Pronunciación (opcional)",
    "意味" to "Significado", "無題の単語帳" to "Vocabulario sin título",
    "「新規」から自分だけの単語帳を作れます" to "Crea tu propio vocabulario desde Nuevo",
    "一致する単語帳がありません" to "No hay vocabularios coincidentes",
    "クリックで表示  スワイプで次へ" to "Toca para mostrar · Desliza para seguir",
    "単語を訳しましょう" to "Traduce las palabras", "知っている単語をスキップ" to "Omitir palabras conocidas",
    "接続詞を選択" to "Elige el conector", "翻訳してください" to "Traduce el texto",
    "全文翻訳を採点中" to "Evaluando la traducción", "理解チェックを作成中" to "Creando preguntas de comprensión",
    "原文と入力した全文の意味を照らし合わせています。" to "Comparando el original con tu traducción.",
    "原文から内容理解を確認する質問を作っています。" to "Creando preguntas para comprobar tu comprensión.",
    "確認へ" to "Revisar", "問題を作成中…" to "Creando preguntas…", "AIで採点中…" to "La IA está evaluando…",
    "AIで採点する" to "Evaluar con IA", "回答を確認中…" to "Comprobando respuestas…",
    "学習を完了" to "Finalizar lección", "回答を確認" to "Comprobar respuestas",
    "文章の内容を確認" to "Comprueba tu comprensión", "やり直す" to "Empezar de nuevo",
    "接続詞確認" to "Repaso de conectores", "全文翻訳" to "Traducción completa",
    "開始" to "Comenzar", "もう一度プレイ" to "Jugar de nuevo", "最終スコア" to "Puntuación final",
)

private const val MAX_SOURCE_LENGTH = 300
