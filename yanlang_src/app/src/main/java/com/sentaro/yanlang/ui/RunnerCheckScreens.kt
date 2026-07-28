package com.sentaro.yanlang.ui

import com.sentaro.yanlang.R
import androidx.compose.ui.res.stringResource

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
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
internal fun RunnerWordCheckScreen(
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

@Composable
internal fun RunnerConnectorCheckScreen(
    document: LearningDocument,
    singlePageMode: Boolean,
    onBack: () -> Unit,
    onTopBarBack: () -> Unit,
    onTokenChange: (LearningToken) -> Unit,
    onReset: () -> Unit,
    onComplete: () -> Unit,
) {
    RunnerCheckScreen(
        title = stringResource(R.string.ui_032),
        instruction = stringResource(R.string.ui_101),
        tokens = document.connectorTokens,
        singlePageMode = singlePageMode,
        choicesFor = { token ->
            buildRunnerChoices(
                correct = token.translation,
                candidates = token.choices + document.connectorTokens.map { it.translation },
                seed = token.id.hashCode(),
            )
        },
        answerIsCorrect = { token, answer -> answer == token.translation },
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

@Composable
private fun RunnerCourse(
    modifier: Modifier,
    instruction: String,
    questionNumber: Int,
    questionCount: Int,
    token: LearningToken,
    choices: List<String>,
    selectedAnswer: String?,
    selectedWasCorrect: Boolean?,
    onSkipKnown: (() -> Unit)?,
    onSelect: (String) -> Unit,
) {
    val reaction by animateFloatAsState(
        targetValue = if (selectedAnswer == null) 0f else 1f,
        animationSpec = tween(360, easing = CubicBezierEasing(0.175f, 0.885f, 0.32f, 1.275f)),
        label = "runner-reaction",
    )
    Column(modifier.padding(horizontal = 18.dp, vertical = 14.dp)) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = SmoothCornerShape(22.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = BorderStroke(1.dp, Color(0xFFE8E8E8)),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        ) {
            Column {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 13.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        "Q$questionNumber / $questionCount",
                        color = Color(0xFF777777),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .background(Color(0xFFF0F0F0), SmoothCornerShape(999.dp))
                            .padding(horizontal = 10.dp, vertical = 5.dp),
                    )
                    Spacer(Modifier.width(12.dp))
                    Column {
                        Text(instruction, color = Color.Gray, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        Text(token.source, fontSize = 24.sp, fontWeight = FontWeight.Black)
                    }
                }
                LinearProgressIndicator(
                    progress = { questionNumber.toFloat() / questionCount.coerceAtLeast(1) },
                    modifier = Modifier.fillMaxWidth().height(4.dp),
                    color = RunnerAccent,
                    trackColor = Color(0xFFEDEDED),
                )
            }
        }
        if (onSkipKnown != null) {
            Text(
                stringResource(R.string.ui_100),
                color = Color(0xFF666666),
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(top = 10.dp)
                    .clip(SmoothCornerShape(999.dp))
                    .clickable(onClick = onSkipKnown)
                    .padding(horizontal = 10.dp, vertical = 6.dp),
            )
        }
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(top = 18.dp),
        ) {
            Row(Modifier.fillMaxSize(), horizontalArrangement = Arrangement.SpaceEvenly) {
                repeat(3) {
                    Box(Modifier.width(2.dp).fillMaxSize().background(RunnerTrack.copy(alpha = 0.45f)))
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 58.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                choices.forEach { choice ->
                    val selected = selectedAnswer == choice
                    val color = when {
                        selected && selectedWasCorrect == true -> RunnerGreen
                        selected && selectedWasCorrect == false -> RunnerRed
                        else -> RunnerTrack
                    }
                    Surface(
                        onClick = { onSelect(choice) },
                        modifier = Modifier
                            .weight(1f)
                            .graphicsLayer {
                                translationY = if (selected) -reaction * 18f else 0f
                                scaleX = if (selected && selectedWasCorrect == false) 1f - reaction * 0.06f else 1f
                                scaleY = if (selected && selectedWasCorrect == true) 1f + reaction * 0.08f else 1f
                            },
                        shape = SmoothCornerShape(18.dp),
                        color = color,
                        contentColor = if (selected) Color.White else Color(0xFF555555),
                    ) {
                        Column(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 18.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            Text(choice, textAlign = TextAlign.Center, fontWeight = FontWeight.Bold, maxLines = 3)
                            if (selected) {
                                Spacer(Modifier.height(6.dp))
                                Icon(
                                    if (selectedWasCorrect == true) Icons.Default.Check else Icons.Default.Close,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp),
                                )
                            }
                        }
                    }
                }
            }
            val selectedLane = choices.indexOf(selectedAnswer).takeIf { it >= 0 } ?: 1
            val laneWidthPx = constraints.maxWidth / 3f
            val playerX by animateFloatAsState(
                targetValue = (selectedLane - 1) * laneWidthPx,
                animationSpec = tween(260, easing = CubicBezierEasing(0.16f, 1f, 0.3f, 1f)),
                label = "runner-player-x",
            )
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 42.dp)
                    .graphicsLayer {
                        translationX = playerX
                        scaleX = if (selectedAnswer != null) 1f + reaction * 0.34f else 1f
                        scaleY = if (selectedAnswer != null) 1f - reaction * 0.22f else 1f
                    }
                    .size(30.dp)
                    .background(Color(0xFF111111), SmoothCornerShape(999.dp)),
            )
        }
    }
}
