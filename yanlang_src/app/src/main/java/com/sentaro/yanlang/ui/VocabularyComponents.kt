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

internal fun vocabularyLanguageLabel(code: String): String = when (code) {
    "ja" -> "日本語"
    "ko-kr" -> "표준어"
    "ko-kp" -> "문화어"
    "zh" -> "简体中文"
    "zh-CN" -> "中简体文"
    "zh-TW" -> "繁體中文"
    "en" -> "English"
    "ru" -> "Русский"
    else -> code
}

internal fun vocabularyLanguageFlag(code: String): String = when (code) {
    "ja" -> "🇯🇵"
    "ko-kr" -> "🇰🇷"
    "ko-kp" -> "🇰🇵"
    "zh", "zh-CN" -> "🇨🇳"
    "zh-TW" -> "🇨🇳"
    "en" -> "🇬🇧"
    "ru" -> "🇷🇺"
    else -> "🌐"
}

@Composable
internal fun vocabularyThemeName(file: String): String = when (file) {
    "numbers.json" -> stringResource(R.string.ui_059)
    "ordinal_numbers.json" -> stringResource(R.string.ui_060)
    "fruits.json" -> stringResource(R.string.ui_061)
    "vegetables.json" -> stringResource(R.string.ui_062)
    "colors.json" -> stringResource(R.string.ui_063)
    "electronics.json" -> stringResource(R.string.ui_064)
    "math_terms.json" -> stringResource(R.string.ui_065)
    "grammar_terms.json" -> stringResource(R.string.ui_066)
    "biology_terms.json" -> stringResource(R.string.ui_067)
    "science_terms.json" -> stringResource(R.string.ui_068)
    "countries.json" -> stringResource(R.string.ui_069)
    "clothing.json" -> stringResource(R.string.ui_070)
    "furniture.json" -> stringResource(R.string.ui_071)
    "luggage.json" -> stringResource(R.string.ui_072)
    "directions.json" -> stringResource(R.string.ui_073)
    "scents_and_visual_emotions.json" -> stringResource(R.string.ui_074)
    "emotions.json" -> stringResource(R.string.ui_075)
    else -> file.removeSuffix(".json")
}

@Composable
internal fun CompactVocabularyField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    singleLine: Boolean,
    containerColor: Color = Color(0xFFF4F4F4),
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
            focusedContainerColor = containerColor,
            unfocusedContainerColor = containerColor,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            focusedPlaceholderColor = PlaceholderGray,
            unfocusedPlaceholderColor = PlaceholderGray,
        ),
    )
}



@Composable
internal fun VocabularyCategoryMenu(
    onSelect: (String, String) -> Unit,
    initialLanguageCode: String = "ja",
    searchQuery: String = "",
    customBooks: List<CustomVocabularyBook> = emptyList(),
    onSelectCustom: (CustomVocabularyBook) -> Unit = {},
    nativeLanguageCode: String = "ja",
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val scrollState = rememberScrollState()

    val languages = remember(nativeLanguageCode) {
        VocabularyRepository.load(context, "numbers.json").first
            .filterNot { it.code == nativeLanguageCode }
    }

    val preferences = remember {
        context.getSharedPreferences("yanlang_preferences", 0)
    }

    var languageCode by rememberSaveable(nativeLanguageCode) {
        mutableStateOf(
            (
                    preferences.getString(
                        "vocabulary_language",
                        initialLanguageCode,
                    ) ?: initialLanguageCode
                    ).takeIf { saved ->
                    languages.any { it.code == saved }
                } ?: languages.firstOrNull()?.code.orEmpty(),
        )
    }

    Box(
        modifier = modifier,
    ) {
        Column(
            modifier = Modifier
                .verticalScroll(scrollState)
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
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

            Text(
                stringResource(R.string.ui_019),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
            )

            if (filteredCustomBooks.isEmpty()) {
                Text(
                    if (customBooks.isEmpty()) {
                        stringResource(R.string.ui_096)
                    } else {
                        stringResource(R.string.ui_097)
                    },
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(bottom = 4.dp),
                )
            } else {
                filteredCustomBooks.forEach { book ->
                    Card(
                        onClick = hapticAction {
                            onSelectCustom(book)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = SmoothCornerShape(20.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White,
                        ),
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 3.dp,
                        ),
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    horizontal = 16.dp,
                                    vertical = 14.dp,
                                ),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Column(
                                modifier = Modifier.weight(1f),
                            ) {
                                Text(
                                    book.title.ifBlank {
                                        stringResource(R.string.ui_095)
                                    },
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                )

                                Text(
                                    "${book.entries.count { entry ->
                                        entry.word.isNotBlank() ||
                                                entry.meaning.isNotBlank()
                                    }}語",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                )
                            }

                            Text(
                                "›",
                                fontSize = 28.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(8.dp))

            Text(
                stringResource(R.string.ui_020),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
            )

            Text(
                stringResource(R.string.ui_021),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )

            androidx.compose.foundation.lazy.LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                items(
                    languages.sortedBy {
                        if (it.code == "ko-kp") 1 else 0
                    },
                ) { language ->
                    val label = vocabularyLanguageLabel(language.code)

                    val colors =
                        if (language.code == languageCode) {
                            ButtonDefaults.buttonColors()
                        } else {
                            ButtonDefaults.buttonColors(
                                containerColor = Color.White,
                                contentColor = Color.Black,
                            )
                        }

                    Button(
                        onClick = {
                            languageCode = language.code

                            preferences
                                .edit()
                                .putString(
                                    "vocabulary_language",
                                    language.code,
                                )
                                .apply()

                            if (language.code == "ko-kp") {
                                scope.launch {
                                    snackbarHostState.showSnackbar(
                                        message = context.getString(
                                            R.string.ui_130,
                                        ),
                                        duration = SnackbarDuration.Short,
                                    )
                                }
                            }
                        },
                        shape = SmoothCornerShape(999.dp),
                        colors = colors,
                    ) {
                        Text(
                            "${vocabularyLanguageFlag(language.code)} $label",
                        )
                    }
                }
            }

            Text(
                stringResource(R.string.ui_022),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )

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
                "感情" to "emotions.json",
            )

            val filteredThemes = themes.filter { (label, file) ->
                normalizedQuery.isBlank() ||
                        label.lowercase().contains(normalizedQuery) ||
                        VocabularyRepository.load(context, file).second.any { entry ->
                            entry.translations.values.any { translation ->
                                translation.text.lowercase().contains(normalizedQuery) ||
                                        translation.pronunciation
                                            .lowercase()
                                            .contains(normalizedQuery)
                            }
                        }
            }

            filteredThemes.chunked(2).forEach { rowThemes ->
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    rowThemes.forEach { (label, file) ->
                        Card(
                            onClick = hapticAction {
                                onSelect(file, languageCode)
                            },
                            modifier = Modifier
                                .weight(1f)
                                .shadow(
                                    6.dp,
                                    SmoothCornerShape(24.dp),
                                    spotColor = Color.Black.copy(alpha = 0.267f),
                                    ambientColor = Color.Black.copy(alpha = 0.267f),
                                ),
                            shape = SmoothCornerShape(24.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = Color.White,
                            ),
                        ) {
                            Text(
                                label,
                                modifier = Modifier.padding(20.dp),
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                            )
                        }
                    }

                    if (rowThemes.size == 1) {
                        Spacer(Modifier.weight(1f))
                    }
                }
            }
        }

        SnackbarHost(
            hostState = snackbarHostState,
        )
    }
}





@Composable
internal fun SearchField(
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
