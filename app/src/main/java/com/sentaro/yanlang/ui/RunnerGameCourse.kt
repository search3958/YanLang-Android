package com.sentaro.yanlang.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sentaro.yanlang.data.LearningToken
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin
import kotlin.random.Random

private val GameBlue = Color(0xFF0900FF)
private val GameRed = Color(0xFFD63031)
private val GameGate = Color(0xFFE2E8F0)
private val GameGateText = Color(0xFF718096)
private val GameInk = Color(0xFF2D3436)
private const val PlayerY = 0.62f
private const val PlayerRadius = 51f
private const val PlayerOutlineWidth = 12f
private const val GateHeight = 0.076f
private const val GateGap = 0.50f
private const val VisibleWaves = 3

private enum class GateResult { NORMAL, CORRECT, WRONG }

private data class RunnerWave(
    val tokenIndex: Int,
    val y: Float,
    val choices: List<String>,
    val passed: Boolean = false,
    val selectedLane: Int? = null,
    val result: GateResult = GateResult.NORMAL,
    val reaction: Float = 0f,
    val pull: Float = 0f,
)

private data class TrailPoint(val x: Float, val alpha: Float)
private data class GameParticle(
    val x: Float,
    val y: Float,
    val vx: Float,
    val vy: Float,
    val life: Float,
    val color: Color,
)

@Composable
internal fun YanRunnerGame(
    modifier: Modifier,
    instruction: String,
    tokens: List<LearningToken>,
    choicesFor: (LearningToken) -> List<String>,
    onGameStart: () -> Unit,
    onAnswer: (LearningToken, String) -> Boolean,
    onRunCompleted: () -> Unit,
) {
    var started by remember { mutableStateOf(false) }
    var runId by remember { mutableIntStateOf(0) }
    var boost by remember { mutableStateOf(false) }
    var finished by remember { mutableStateOf(false) }
    var cleared by remember { mutableStateOf(false) }
    var score by remember { mutableIntStateOf(10) }
    var playerX by remember { mutableFloatStateOf(0.5f) }
    var targetX by remember { mutableFloatStateOf(0.5f) }
    var waves by remember { mutableStateOf<List<RunnerWave>>(emptyList()) }
    var nextWaveIndex by remember { mutableIntStateOf(0) }
    var trail by remember { mutableStateOf<List<TrailPoint>>(emptyList()) }
    var particles by remember { mutableStateOf<List<GameParticle>>(emptyList()) }
    var popLife by remember { mutableFloatStateOf(0f) }
    var popCorrect by remember { mutableStateOf(true) }
    var flash by remember { mutableFloatStateOf(0f) }
    var shake by remember { mutableFloatStateOf(0f) }

    fun initialWaves(): List<RunnerWave> {
        var y = PlayerY - GateHeight - GateGap
        return tokens.take(VisibleWaves).mapIndexed { index, token ->
            RunnerWave(index, y.also { y -= GateGap }, choicesFor(token).shuffled())
        }
    }

    fun resetAndStart() {
        onGameStart()
        score = 10
        playerX = 0.5f
        targetX = 0.5f
        trail = emptyList()
        particles = emptyList()
        popLife = 0f
        flash = 0f
        shake = 0f
        finished = false
        cleared = false
        boost = false
        waves = initialWaves()
        nextWaveIndex = min(VisibleWaves, tokens.size)
        runId++
        started = true
    }

    val currentWave = waves.firstOrNull { !it.passed }
    val currentIndex = if (!started) 0 else
        currentWave?.tokenIndex ?: (tokens.lastIndex).coerceAtLeast(0)
    val currentToken = tokens.getOrNull(currentIndex)

    LaunchedEffect(runId, started) {
        if (!started || finished || tokens.isEmpty()) return@LaunchedEffect
        var lastFrame = 0L
        while (!finished) {
            withFrameNanos { frame ->
                val dt = if (lastFrame == 0L) 0f else
                    ((frame - lastFrame) / 1_000_000_000f).coerceAtMost(0.034f)
                lastFrame = frame
                if (dt <= 0f) return@withFrameNanos

                val movement = abs(targetX - playerX)
                playerX += (targetX - playerX) * min(1f, dt * 13.2f)
                playerX = playerX.coerceIn(0.06f, 0.94f)
                val speed = if (boost) 0.49f else 0.19f

                var nextWaves = waves.map { wave ->
                    wave.copy(
                        y = wave.y + speed * dt,
                        reaction = (wave.reaction - dt * 3.3f).coerceAtLeast(0f),
                        pull = (wave.pull - dt * 3f).coerceAtLeast(0f),
                    )
                }

                val active = nextWaves.firstOrNull { !it.passed }
                if (active != null) {
                    val activePosition = nextWaves.indexOfFirst { it.tokenIndex == active.tokenIndex }
                    val lane = (playerX * 3f).toInt().coerceIn(0, 2)
                    val token = tokens[active.tokenIndex]
                    val aimedChoice = active.choices[lane.coerceAtMost(active.choices.lastIndex)]
                    val nearContact = active.y + GateHeight >= PlayerY - 0.18f
                    if (nearContact && aimedChoice == token.translation) {
                        nextWaves = nextWaves.toMutableList().also {
                            it[activePosition] = active.copy(
                                y = active.y,
                                pull = ((active.y + GateHeight - (PlayerY - 0.18f)) / 0.18f)
                                    .coerceIn(0f, 1f),
                            )
                        }
                    }
                    if (active.y + GateHeight >= PlayerY - 0.07f) {
                        val correct = onAnswer(token, aimedChoice)
                        nextWaves = nextWaves.toMutableList().also {
                            it[activePosition] = active.copy(
                                y = active.y,
                                passed = true,
                                selectedLane = lane,
                                result = if (correct) GateResult.CORRECT else GateResult.WRONG,
                                reaction = 1f,
                                pull = if (correct) 1f else 0f,
                            )
                        }
                        score += if (correct) 5 else -3
                        popCorrect = correct
                        popLife = 1f
                        flash = 1f
                        if (!correct) shake = 1f
                        particles = spawnParticles(playerX, correct)

                        if (active.tokenIndex == tokens.lastIndex) {
                            finished = true
                            cleared = true
                            onRunCompleted()
                        } else if (score < 0) {
                            finished = true
                            cleared = false
                        }
                    }
                }

                nextWaves = nextWaves.filter { it.y < 1.12f }
                var localNextIndex = nextWaveIndex
                while (nextWaves.size < VisibleWaves && localNextIndex < tokens.size) {
                    val upstreamY = (nextWaves.minOfOrNull { it.y } ?: 0f) - GateGap
                    nextWaves = nextWaves + RunnerWave(
                        tokenIndex = localNextIndex,
                        y = upstreamY,
                        choices = choicesFor(tokens[localNextIndex]).shuffled(),
                    )
                    localNextIndex++
                }
                nextWaveIndex = localNextIndex
                waves = nextWaves

                val maxTrail = if (boost) 8 else 4
                trail = (
                    listOf(TrailPoint(playerX, if (boost) 0.35f else 0.18f)) +
                        trail.take(maxTrail - 1)
                    ).mapIndexed { index, point ->
                    point.copy(alpha = point.alpha * (1f - index / (maxTrail + 1f)))
                }
                popLife = (popLife - dt * 1.25f).coerceAtLeast(0f)
                flash = (flash - dt * 3.6f).coerceAtLeast(0f)
                shake = (shake - dt * 4.3f).coerceAtLeast(0f)
                particles = particles.mapNotNull {
                    val life = it.life - dt * 1.8f
                    if (life <= 0f) null else it.copy(
                        x = it.x + it.vx * dt,
                        y = it.y + it.vy * dt,
                        vy = it.vy + 0.35f * dt,
                        life = life,
                    )
                }
            }
        }
    }

    val playerScaleX by animateFloatAsState(
        targetValue = when {
            flash > 0.65f && popCorrect -> 1.45f
            flash > 0.65f -> 0.65f
            boost -> 1.18f
            else -> 1f + min(0.16f, abs(targetX - playerX) * 1.7f)
        },
        animationSpec = spring(dampingRatio = 0.48f, stiffness = 520f),
        label = "player-scale-x",
    )
    val playerScaleY by animateFloatAsState(
        targetValue = when {
            flash > 0.65f && popCorrect -> 0.65f
            flash > 0.65f -> 1.4f
            boost -> 0.84f
            else -> 1f
        },
        animationSpec = spring(dampingRatio = 0.48f, stiffness = 520f),
        label = "player-scale-y",
    )

    Box(modifier.background(Color.White)) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(started, finished) {
                    if (started && !finished) {
                        detectDragGestures { change, drag ->
                            change.consume()
                            targetX = (targetX + drag.x / size.width).coerceIn(0.06f, 0.94f)
                        }
                    }
                }
                .pointerInput(started, finished) {
                    if (started && !finished) {
                        detectTapGestures {
                            targetX = (it.x / size.width).coerceIn(0.06f, 0.94f)
                        }
                    }
                },
        ) {
            val sx = if (shake > 0f) (Random.nextFloat() - 0.5f) * 16f * shake else 0f
            val sy = if (shake > 0f) (Random.nextFloat() - 0.5f) * 12f * shake else 0f
            drawRect(Color.White)
            val renderedWaves = if (started) waves else initialWaves()
            renderedWaves.forEach { wave -> drawWave(wave, size.width, size.height, sx, sy) }

            val playerYPx = size.height * PlayerY
            trail.asReversed().forEachIndexed { index, point ->
                drawCircle(
                    color = GameBlue.copy(alpha = point.alpha),
                    radius = PlayerRadius * 0.88f * (1f - index * 0.055f),
                    center = Offset(size.width * point.x + sx, playerYPx + index * 5f + sy),
                )
            }
            particles.forEach {
                drawCircle(
                    color = it.color.copy(alpha = it.life.coerceIn(0f, 1f)),
                    radius = 4f * it.life.coerceAtLeast(0.4f),
                    center = Offset(size.width * it.x + sx, size.height * it.y + sy),
                )
            }
            if (flash > 0f) {
                drawCircle(
                    color = if (popCorrect) GameBlue.copy(alpha = flash * 0.7f) else GameRed.copy(alpha = flash * 0.7f),
                    radius = PlayerRadius + (1f - flash) * 28f,
                    center = Offset(size.width * playerX + sx, playerYPx + sy),
                    style = androidx.compose.ui.graphics.drawscope.Stroke(width = PlayerOutlineWidth),
                )
            }
            drawIntoCanvas { canvas ->
                canvas.save()
                canvas.translate(size.width * playerX + sx, playerYPx + sy)
                canvas.scale(playerScaleX, playerScaleY)
                canvas.drawCircle(Offset.Zero, PlayerRadius, Paint().apply { color = Color.White })
                canvas.drawCircle(
                    Offset.Zero,
                    PlayerRadius - PlayerOutlineWidth,
                    Paint().apply { color = GameBlue },
                )
                canvas.restore()
            }
            if (popLife > 0f) {
                val progress = 1f - popLife
                val alpha = if (progress < 0.58f) min(1f, progress / 0.18f) else
                    (1f - (progress - 0.58f) / 0.42f).coerceIn(0f, 1f)
                drawCenteredText(
                    if (popCorrect) "+5" else "-3",
                    Offset(size.width / 2f, size.height * 0.74f - progress * size.height * 0.23f),
                    (if (popCorrect) GameBlue else GameRed).copy(alpha = alpha),
                    54f,
                )
            }
        }

        GameScoreHud(score, Modifier.align(Alignment.TopCenter).padding(top = 18.dp))
        if (currentToken != null) {
            GameQuestionHud(
                questionNumber = currentIndex + 1,
                questionCount = tokens.size,
                question = currentToken.source,
            )
        }

        if (started && !finished) {
            Surface(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(20.dp)
                    .size(64.dp)
                    .pointerInput(runId) {
                        awaitEachGesture {
                            awaitFirstDown(requireUnconsumed = false)
                            boost = true
                            waitForUpOrCancellation()
                            boost = false
                        }
                    },
                shape = SmoothCornerShape(999.dp),
                color = if (boost) GameInk else Color.White,
                contentColor = if (boost) Color.White else GameInk,
                shadowElevation = if (boost) 4.dp else 10.dp,
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text("BOOST", fontSize = 11.sp, fontWeight = FontWeight.Black)
                }
            }
        }

        AnimatedVisibility(!started, enter = fadeIn(), exit = fadeOut()) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Button(
                    onClick = ::resetAndStart,
                    shape = SmoothCornerShape(999.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = GameInk),
                    contentPadding = PaddingValues(horizontal = 38.dp, vertical = 16.dp),
                ) {
                    Text(l("開始"), fontWeight = FontWeight.Black)
                }
            }
        }

        AnimatedVisibility(started && finished, enter = fadeIn(tween(350)), exit = fadeOut()) {
            Box(
                Modifier.fillMaxSize().background(Color.White.copy(alpha = 0.94f)),
                contentAlignment = Alignment.Center,
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        if (cleared) "CLEAR" else "GAME OVER",
                        color = if (cleared) GameBlue else GameRed,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Black,
                    )
                    Spacer(Modifier.height(6.dp))
                    Text(l("最終スコア"), color = GameGateText)
                    Spacer(Modifier.height(20.dp))
                    Text(score.coerceAtLeast(0).toString(), fontSize = 48.sp, fontWeight = FontWeight.Black)
                    Spacer(Modifier.height(26.dp))
                    Button(
                        onClick = ::resetAndStart,
                        shape = SmoothCornerShape(999.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = GameInk),
                        contentPadding = PaddingValues(horizontal = 30.dp, vertical = 14.dp),
                    ) {
                        Text(l("もう一度プレイ"), fontWeight = FontWeight.Black)
                    }
                }
            }
        }
    }
}

private fun spawnParticles(playerX: Float, correct: Boolean): List<GameParticle> {
    val count = if (correct) 7 else 5
    return List(count) { index ->
        val angle = -Math.PI * 0.88 + Math.PI * 0.76 * index / (count - 1).coerceAtLeast(1)
        val speed = 0.1f + Random.nextFloat() * 0.08f
        GameParticle(
            x = playerX,
            y = PlayerY,
            vx = (cos(angle) * speed).toFloat(),
            vy = (sin(angle) * speed - 0.05).toFloat(),
            life = 1f,
            color = if (correct) GameBlue else GameRed,
        )
    }
}

private fun DrawScope.drawWave(
    wave: RunnerWave,
    width: Float,
    height: Float,
    shakeX: Float,
    shakeY: Float,
) {
    val laneWidth = width / 3f
    val gateHeight = height * GateHeight
    val gateY = height * wave.y + shakeY
    val reactionProgress = 1f - wave.reaction
    wave.choices.forEachIndexed { lane, choice ->
        val selected = wave.selectedLane == lane
        val jump = if (selected && wave.result == GateResult.CORRECT) {
            -sin(reactionProgress * Math.PI).toFloat() * 30f
        } else 0f
        val recoil = if (selected && wave.result == GateResult.WRONG) {
            sin(reactionProgress * Math.PI).toFloat() * 24f
        } else 0f
        val wrongWobble = if (selected && wave.result == GateResult.WRONG) {
            sin(reactionProgress * Math.PI * 5).toFloat() * wave.reaction * 9f
        } else 0f
        val pull = if (selected && wave.result == GateResult.CORRECT) wave.reaction else wave.pull
        val indent = pull * 15f
        val left = lane * laneWidth + shakeX + wrongWobble
        val top = gateY + jump + recoil
        val color = when {
            selected && wave.result == GateResult.CORRECT -> blend(GameGate, GameBlue, 1f - wave.reaction)
            selected && wave.result == GateResult.WRONG -> blend(GameGate, GameRed, 1f - wave.reaction)
            else -> GameGate
        }
        val path = Path().apply {
            moveTo(left, top)
            lineTo(left + laneWidth, top)
            lineTo(left + laneWidth, top + gateHeight)
            lineTo(left + laneWidth * 0.64f, top + gateHeight)
            quadraticTo(
                left + laneWidth / 2f,
                top + gateHeight + indent,
                left + laneWidth * 0.36f,
                top + gateHeight,
            )
            lineTo(left, top + gateHeight)
            close()
        }
        drawPath(path, color)
        drawLine(
            Color.White.copy(alpha = 0.72f),
            Offset(left + laneWidth, top),
            Offset(left + laneWidth, top + gateHeight),
            1f,
        )
        drawCenteredText(
            choice,
            Offset(left + laneWidth / 2f, top + gateHeight / 2f + indent * 0.22f),
            if (selected && wave.result != GateResult.NORMAL) Color.White else GameGateText,
            min(39f, 24f + (8 - choice.length).coerceAtLeast(0) * 1.5f),
        )
    }
}

@Composable
private fun BoxScope.GameQuestionHud(
    questionNumber: Int,
    questionCount: Int,
    question: String,
) {
    val scale by animateFloatAsState(
        targetValue = 1f,
        animationSpec = tween(520, easing = CubicBezierEasing(0.175f, 0.885f, 0.32f, 1.275f)),
        label = "question-spring",
    )
    Card(
        modifier = Modifier
            .align(Alignment.TopCenter)
            .padding(start = 20.dp, top = 72.dp, end = 20.dp)
            .fillMaxWidth()
            .height(62.dp)
            .graphicsLayer { scaleX = scale; scaleY = scale },
        shape = SmoothCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, Color(0xFFEDF2F7)),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
    ) {
        Column {
            Row(
                Modifier.fillMaxWidth().weight(1f).padding(horizontal = 18.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
            ) {
                Text(
                    "Q$questionNumber / $questionCount",
                    color = Color(0xFFA0AEC0),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.background(Color(0xFFEDF2F7), SmoothCornerShape(999.dp))
                        .padding(horizontal = 10.dp, vertical = 4.dp),
                )
                Spacer(Modifier.width(14.dp))
                Text(question, fontSize = 23.sp, fontWeight = FontWeight.Black, maxLines = 1)
            }
            LinearProgressIndicator(
                progress = { questionNumber.toFloat() / questionCount.coerceAtLeast(1) },
                modifier = Modifier.fillMaxWidth().height(4.dp),
                color = GameBlue,
                trackColor = Color(0xFFEDF2F7),
            )
        }
    }
}

@Composable
private fun GameScoreHud(score: Int, modifier: Modifier = Modifier) {
    var bumped by remember { mutableStateOf(false) }
    LaunchedEffect(score) {
        bumped = true
        kotlinx.coroutines.delay(160)
        bumped = false
    }
    val scale by animateFloatAsState(
        if (bumped) 1.18f else 1f,
        spring(dampingRatio = 0.5f, stiffness = 620f),
        label = "score-bump",
    )
    Surface(
        modifier = modifier.graphicsLayer { scaleX = scale; scaleY = scale },
        shape = SmoothCornerShape(999.dp),
        color = Color.White.copy(alpha = 0.94f),
        border = BorderStroke(1.dp, Color(0xFFEDF2F7)),
        shadowElevation = 4.dp,
    ) {
        Row(
            Modifier.padding(horizontal = 15.dp, vertical = 7.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text("SCORE", color = GameInk, fontSize = 13.sp, fontWeight = FontWeight.Black)
            Spacer(Modifier.width(5.dp))
            AnimatedContent(
                score,
                transitionSpec = {
                    slideInVertically { it } + fadeIn() togetherWith
                        slideOutVertically { -it } + fadeOut()
                },
                label = "score-odometer",
            ) {
                Text(it.toString(), color = GameBlue, fontSize = 17.sp, fontWeight = FontWeight.Black)
            }
        }
    }
}

private fun DrawScope.drawCenteredText(
    text: String,
    center: Offset,
    color: Color,
    textSize: Float,
) {
    drawIntoCanvas { canvas ->
        val paint = android.graphics.Paint().apply {
            this.color = color.toArgb()
            this.textSize = textSize
            textAlign = android.graphics.Paint.Align.CENTER
            typeface = android.graphics.Typeface.DEFAULT_BOLD
            isAntiAlias = true
        }
        canvas.nativeCanvas.drawText(
            text,
            center.x,
            center.y - (paint.ascent() + paint.descent()) / 2f,
            paint,
        )
    }
}

private fun blend(from: Color, to: Color, amount: Float): Color {
    val t = amount.coerceIn(0f, 1f)
    return Color(
        red = from.red + (to.red - from.red) * t,
        green = from.green + (to.green - from.green) * t,
        blue = from.blue + (to.blue - from.blue) * t,
        alpha = 1f,
    )
}
