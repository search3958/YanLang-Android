package com.sentaro.yanlang

import com.sentaro.yanlang.data.LocalKoreanLearningEngine
import com.sentaro.yanlang.data.ComprehensionQuestionType
import com.sentaro.yanlang.data.TokenKind
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class LocalKoreanLearningEngineTest {
    private val engine = LocalKoreanLearningEngine()

    @Test
    fun analyze_splitsWordsAndConnectorsInOnePass() {
        val tokens = engine.analyze("내 나라 재일로 좋아!")

        assertEquals(
            listOf("내", "나라", "재일", "로", "좋", "아"),
            tokens.map { it.source },
        )
        assertEquals(
            listOf(
                TokenKind.WORD,
                TokenKind.WORD,
                TokenKind.WORD,
                TokenKind.CONNECTOR,
                TokenKind.WORD,
                TokenKind.CONNECTOR,
            ),
            tokens.map { it.kind },
        )
        assertEquals("第一", tokens[2].translation)
        assertEquals("〜で／〜へ", tokens[3].translation)
    }

    @Test
    fun checkAnswer_acceptsOneOfSlashSeparatedMeanings() {
        val connector = engine.analyze("재일로").last()

        assertTrue(engine.checkAnswer(connector, "〜へ"))
        assertFalse(engine.checkAnswer(connector, "〜から"))
    }

    @Test
    fun evaluateTranslation_reportsMissingKnownWords() {
        val (score, mistakes) = engine.evaluateTranslation("내 나라", "私の")

        assertEquals(50, score)
        assertEquals(listOf("나라"), mistakes.map { it.source })
    }

    @Test
    fun comprehensionFlow_generatesBothTypesAndChecksAnswers() {
        val source = "내 나라"
        val questions = engine.generateComprehensionQuestions(source)

        assertTrue(questions.any {
            it.type == ComprehensionQuestionType.MULTIPLE_CHOICE
        })
        assertTrue(questions.any {
            it.type == ComprehensionQuestionType.FREE_TEXT
        })

        val answered = questions.mapIndexed { index, question ->
            question.copy(answer = if (index == 0) source else "文章の説明")
        }
        val (score, checked) = engine.evaluateComprehension(source, answered)

        assertEquals(100, score)
        assertTrue(checked.all { it.isCorrect == true })
    }
}
