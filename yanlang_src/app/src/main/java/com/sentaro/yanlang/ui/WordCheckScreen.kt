package com.sentaro.yanlang.ui

import com.sentaro.yanlang.R
import androidx.compose.ui.res.stringResource

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sentaro.yanlang.data.LearningDocument
import com.sentaro.yanlang.data.LearningEngine
import com.sentaro.yanlang.data.LearningToken
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private val RunnerAccent = Color(0xFF0900FF)
private val RunnerGreen = Color(0xFF00A878)
private val RunnerRed = Color(0xFFD32F2F)
private val RunnerTrack = Color(0xFFE8E8E8)

@Composable
internal fun WordCheckScreen(
    document: LearningDocument,
    singlePageMode: Boolean,
    engine: LearningEngine,
    onBack: () -> Unit,
    onTopBarBack: () -> Unit,
    onTokenChange: (LearningToken) -> Unit,
    onSkipKnown: () -> Unit,
    onReset: () -> Unit,
    onComplete: () -> Unit,
) {
    val words = document.wordTokens
     RunnerCheckScreen(
         title = stringResource(R.string.ui_031),
         instruction = stringResource(R.string.ui_099),
         stageProgress = learningStageProgress(document),
         tokens = words,
         singlePageMode = singlePageMode,
         choicesFor = { token ->
             buildRunnerChoices(
                 correct = token.translation,
                 candidates = words.map { it.translation },
                 seed = token.id.hashCode(),
             )
         },
         answerIsCorrect = { token, answer -> engine.checkAnswer(token, answer) },
         onBack = onBack,
         onTopBarBack = onTopBarBack,
         onTokenChange = onTokenChange,
         onReset = onReset,
         onComplete = onComplete,
     )
}

private fun buildRunnerChoices(
    correct: String,
    candidates: List<String>,
    seed: Int,
): List<String> {
    val distractors = candidates
        .filter { it.isNotBlank() && it != correct }
        .distinct()
        .shuffled(kotlin.random.Random(seed))
        .take(2)
        .toMutableList()
    val fallbacks = listOf("どちらでもない", "意味が反対", "文脈に合わない")
    fallbacks.forEach { if (distractors.size < 2 && it != correct) distractors += it }
    return (distractors.take(2) + correct).shuffled(kotlin.random.Random(seed xor 0x51A7))
}

@Composable
private fun RunnerCheckScreen(
    title: String,
    instruction: String,
    stageProgress: LearningStageProgress?,
    tokens: List<LearningToken>,
    singlePageMode: Boolean,
    choicesFor: (LearningToken) -> List<String>,
    answerIsCorrect: (LearningToken, String) -> Boolean,
    onBack: () -> Unit,
    onTopBarBack: () -> Unit,
    onTokenChange: (LearningToken) -> Unit,
    onReset: () -> Unit,
    onComplete: () -> Unit,
) {
    var completedThisRun by remember(tokens.map { it.id }) { mutableStateOf(false) }
    val haptic = LocalHapticFeedback.current
    var isInputFocused by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = Color.White,
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFF6F6F6))
                    .windowInsetsPadding(WindowInsets.statusBars)
                    .padding(horizontal = 14.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                IconButton(
                    onClick = onTopBarBack,
                    modifier = Modifier
                        .size(42.dp)
                        .background(Color(0xFFECECEC), SmoothCornerShape(999.dp)),
                ) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(R.string.ui_042))
                }
                Spacer(Modifier.width(12.dp))
                Text(title, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                Spacer(Modifier.weight(1f))
                if (stageProgress != null) {
                    LearningStageProgressIndicator(stageProgress)
                    Spacer(Modifier.width(6.dp))
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
                if (!singlePageMode) {
                    Surface(
                        onClick = onBack,
                        color = Color(0xFF111111),
                        contentColor = Color.White,
                        shape = SmoothCornerShape(999.dp),
                        modifier = Modifier.size(56.dp),
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(R.string.ui_042))
                        }
                    }
                }
                Spacer(Modifier.weight(1f))
                Button(
                    onClick = onComplete,
                    enabled = completedThisRun || tokens.isEmpty(),
                    shape = SmoothCornerShape(999.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = RunnerAccent),
                    contentPadding = PaddingValues(horizontal = 24.dp, vertical = 16.dp),
                ) {
                    Text(if (singlePageMode) stringResource(R.string.ui_035) else stringResource(R.string.ui_025), fontWeight = FontWeight.Bold)
                    Spacer(Modifier.width(8.dp))
                    Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null)
                }
            }
        },
    ) { padding ->
        if (tokens.isEmpty()) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Text(stringResource(R.string.ui_058), color = Color.Gray)
            }
        } else {
            YanRunnerGame(
                modifier = Modifier.fillMaxSize().padding(padding),
                instruction = instruction,
                tokens = tokens,
                choicesFor = choicesFor,
                onGameStart = {
                    completedThisRun = false
                    onReset()
                },
                onAnswer = { token, answer ->
                    val correct = answerIsCorrect(token, answer)
                    haptic.performHapticFeedback(
                        if (correct) HapticFeedbackType.LongPress else HapticFeedbackType.TextHandleMove,
                    )
                    onTokenChange(token.copy(answer = answer, isCorrect = correct))
                    correct
                },
                onRunCompleted = { completedThisRun = true },
            )
        }
    }
}
