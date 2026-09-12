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
internal fun WordbookScreen(
    document: LearningDocument,
    singlePageMode: Boolean,
    onBack: () -> Unit,
    onTopBarBack: () -> Unit,
    onReveal: (String) -> Unit,
    onMove: (Int) -> Unit,
    onComplete: () -> Unit,
    showTestButton: Boolean = false,
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
         stageProgress = learningStageProgress(document),
         topBarActions = if (showTestButton) {
             { Text(
                 "確認",
                 fontWeight = FontWeight.Bold,
                 fontSize = 13.sp,
                 color = Color.Black,
             ) }
         } else null,
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
