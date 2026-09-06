package com.sentaro.yanlang.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import com.sentaro.yanlang.data.LearningToken
import com.sentaro.yanlang.data.TokenKind
import com.sentaro.yanlang.data.VocabularyEntry
import com.sentaro.yanlang.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun FixedVocabularyTestScreen(
    entries: List<VocabularyEntry>,
    languageCode: String,
    nativeLanguageCode: String,
    onBack: () -> Unit,
) {
    val tokens = remember(entries, languageCode, nativeLanguageCode) {
        entries.mapNotNull { entry ->
            val questionText = entry.translations[languageCode]?.text?.takeIf { it.isNotBlank() }
                ?: entry.translations.values.firstOrNull()?.text?.takeIf { it.isNotBlank() }
            val answerText = entry.translations[nativeLanguageCode]?.text?.takeIf { it.isNotBlank() }
                ?: entry.translations["ja"]?.text?.takeIf { it.isNotBlank() }
                ?: entry.translations.values.firstOrNull()?.text?.takeIf { it.isNotBlank() }
            if (questionText == null || answerText == null) {
                null
            } else {
                LearningToken(
                    id = "fixed-vocabulary-${entry.id}",
                    source = questionText,
                    kind = TokenKind.WORD,
                    translation = answerText,
                )
            }
        }
    }

    val haptic = LocalHapticFeedback.current
    Scaffold(
        containerColor = Color.White,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        stringResource(R.string.ui_129),
                        fontWeight = FontWeight.Bold,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.ui_042),
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = Color.Black,
                ),
            )
        },
    ) { padding ->
        if (tokens.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .padding(padding),
                contentAlignment = Alignment.Center,
            ) {
                Text(stringResource(R.string.ui_058), color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        } else {
            YanRunnerGame(
                modifier = Modifier.fillMaxSize().padding(padding),
                instruction = stringResource(R.string.ui_131),
                tokens = tokens,
                choicesFor = { token ->
                    val choices = tokens.asSequence()
                        .filter { it.id != token.id }
                        .map { it.translation }
                        .filter { it.isNotBlank() && it != token.translation }
                        .distinct()
                        .shuffled(kotlin.random.Random(token.id.hashCode()))
                        .take(2)
                        .toMutableList()

                    listOf(
                        *choices.toTypedArray(),
                        token.translation,
                    ).distinct().take(3).shuffled(kotlin.random.Random(token.id.hashCode() xor 0x5A17))
                },
                onGameStart = {
                    println("FixedVocabularyTest: game started, entries=${tokens.size}")
                },
                onAnswer = { token, answer ->
                    val correct = answer == token.translation
                    haptic.performHapticFeedback(
                        if (correct) HapticFeedbackType.LongPress else HapticFeedbackType.TextHandleMove,
                    )
                    println("FixedVocabularyTest: answer=${if (correct) "correct" else "wrong"}, word=${token.source}")
                    correct
                },
                onRunCompleted = {
                    println("FixedVocabularyTest: completed")
                },
            )
        }
    }
}
