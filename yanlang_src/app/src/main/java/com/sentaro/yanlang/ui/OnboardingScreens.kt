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

internal const val ONBOARDING_PREFS = "yanlang_onboarding"
internal const val ONBOARDING_COMPLETED = "completed"
internal const val ANALYTICS_PREFS = "yanlang_analytics"
internal const val ANALYTICS_CONSENT_GRANTED = "consent_granted"
internal const val MAX_SOURCE_LENGTH = 300

@Composable
internal fun OnboardingFlow(
    initialLanguageCode: String,
    initialCustomLanguage: String,
    onLanguageComplete: (String, String) -> Unit,
    onConsent: () -> Unit,
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
        } else if (currentPage == 1) {
            LanguageOnboardingScreen(
                initialLanguageCode = code,
                initialCustomLanguage = custom,
                onLanguageChange = { newCode, newCustom ->
                    code = newCode
                    custom = newCustom
                },
                onComplete = {
                    onLanguageComplete(code, custom)
                    page = 2
                },
                modifier = Modifier.fillMaxSize(),
            )
        } else {
            AnalyticsConsentScreen(
                onConsent = onConsent,
                modifier = Modifier.fillMaxSize(),
            )
        }
    }

}

@Composable
internal fun WelcomeOnboardingScreen(
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
internal fun LanguageOnboardingScreen(
    initialLanguageCode: String,
    initialCustomLanguage: String,
    onLanguageChange: (String, String) -> Unit,
    onComplete: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var languageCode by remember { mutableStateOf(initialLanguageCode) }
    var customLanguage by remember { mutableStateOf(initialCustomLanguage) }
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
                    languageCode = itemCode
                    val newCustom = if (itemCode == "other") customLanguage else ""
                    onLanguageChange(itemCode, newCustom)
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
            TextField(customLanguage, { newCustom ->
                customLanguage = newCustom
                onLanguageChange("other", newCustom)
            }, placeholder = { Text(stringResource(R.string.ui_048)) }, singleLine = true, modifier = Modifier.fillMaxWidth(), shape = SmoothCornerShape(18.dp), colors = yanLangTextFieldColors())
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
