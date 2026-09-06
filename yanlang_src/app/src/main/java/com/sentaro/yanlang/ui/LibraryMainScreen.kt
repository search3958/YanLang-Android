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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun LibraryScreen(
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
    creditInfo: CreditInfo? = null,
     onRefreshCredits: () -> Unit = {},
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
                item {
                    CreditCard(info = creditInfo, onRefresh = onRefreshCredits)
                }
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
