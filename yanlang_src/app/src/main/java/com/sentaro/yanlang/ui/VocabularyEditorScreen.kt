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

@Composable
internal fun CustomVocabularyLibraryScreen(
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
internal fun CustomVocabularyEditorScreen(
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
                    containerColor = Color(0xFFF2F2F2),
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
