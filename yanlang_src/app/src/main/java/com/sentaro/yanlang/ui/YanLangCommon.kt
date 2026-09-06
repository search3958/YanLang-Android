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
internal fun AnimatedPrimaryButton(
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
internal fun LearningScaffold(
    title: String,
    onBack: () -> Unit,
    onTopBarBack: () -> Unit,
    completeLabel: String,
    completeEnabled: Boolean,
    playCompleteButtonAnimation: Boolean = false,
    onComplete: () -> Unit,
    showBottomBack: Boolean = true,
    showTopBar: Boolean = true,
    topBarActions: @Composable (androidx.compose.foundation.layout.RowScope.() -> Unit)? = null,
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
                        actions = topBarActions ?: {},
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
internal fun SourceCard(
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

internal fun highlightedSource(document: LearningDocument): AnnotatedString {
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

internal fun translatedPhrase(
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
internal fun yanLangTextFieldColors() = TextFieldDefaults.colors(
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
internal fun inlineAnswerTextFieldColors() = TextFieldDefaults.colors(
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

internal val ScreenBackground = Color(0xFFF6F6F6)
internal val HeaderBackground = Color(0xFFF6F6F6)
internal val InputBackground = Color(0xFFF6F6F6)
internal val AccentGreen = Color(0xFF0900FF)
internal val PlaceholderGray = Color(0x88000000)

internal fun todayKey(): String =
    SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date())

internal fun currentStreak(activityDates: Set<String>): Int {
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


internal fun nativeLanguageName(code: String, custom: String): String = when (code) {
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
