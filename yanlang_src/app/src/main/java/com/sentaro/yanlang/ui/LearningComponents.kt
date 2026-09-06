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
internal fun LearningStep.resumeLabel(): String = when (this) {
    LearningStep.EDITOR -> stringResource(R.string.ui_115)
    LearningStep.WORDBOOK -> stringResource(R.string.ui_116)
    LearningStep.WORD_CHECK -> stringResource(R.string.ui_117)
    LearningStep.CONNECTOR_CHECK -> stringResource(R.string.ui_118)
    LearningStep.FINAL_TRANSLATION -> stringResource(R.string.ui_119)
    LearningStep.COMPREHENSION_CHECK -> stringResource(R.string.ui_120)
    LearningStep.LIBRARY -> stringResource(R.string.ui_121)
}

@Composable
internal fun StackedWordCard(
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
internal fun WordAnswerRow(
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
internal fun ResultMark(result: Boolean?) {
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
internal fun ConnectorCard(
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
internal fun ComprehensionQuestionCard(
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
internal fun ComprehensionScoreCard(
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
internal fun ScoreCard(score: Int) {
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
internal fun MistakeRow(
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
