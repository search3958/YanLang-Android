package com.sentaro.yanlang.data

import java.util.Locale
import java.util.UUID

/**
 * UIからAI実装を分離するための境界。
 * 本番ではこのinterfaceをサーバーAPI実装に差し替える。
 */
interface LearningEngine {
    fun analyze(
        source: String,
        targetLanguage: String = "",
        nativeLanguage: String = "日本語",
    ): List<LearningToken>
    fun checkAnswer(token: LearningToken, answer: String): Boolean
    fun evaluateTranslation(
        sourceText: String,
        translation: String,
        nativeLanguage: String = "日本語",
    ): Pair<Int, List<TranslationMistake>>
    fun generateComprehensionQuestions(
        sourceText: String,
        nativeLanguage: String = "日本語",
    ): List<ComprehensionQuestion>
    fun evaluateComprehension(
        sourceText: String,
        questions: List<ComprehensionQuestion>,
        nativeLanguage: String = "日本語",
    ): Pair<Int, List<ComprehensionQuestion>>
}

/**
 * オフラインでも一連の学習体験を試せる韓国語向けの軽量実装。
 * 語幹をWORD、助詞・接続語尾をCONNECTORとして一括で分割する。
 */
class LocalKoreanLearningEngine : LearningEngine {
    private val wordTranslations = mapOf(
        "내" to "私の",
        "나" to "私",
        "나라" to "国",
        "재일" to "第一",
        "제일" to "一番",
        "좋" to "良い",
        "경애" to "敬愛",
        "사랑" to "愛",
        "사람" to "人",
        "친구" to "友達",
        "학교" to "学校",
        "한국" to "韓国",
        "일본" to "日本",
        "언어" to "言語",
        "공부" to "勉強",
        "오늘" to "今日",
        "내일" to "明日",
        "어제" to "昨日",
        "먹" to "食べる",
        "가" to "行く",
        "보" to "見る",
        "하" to "する",
        "배우" to "学ぶ",
    )

    private data class ConnectorInfo(
        val translation: String,
        val distractors: List<String>,
    )

    private val connectors = linkedMapOf(
        "으로" to ConnectorInfo("〜で／〜へ", listOf("〜から", "〜だけ")),
        "에서" to ConnectorInfo("〜で／〜から", listOf("〜へ", "〜より")),
        "에게" to ConnectorInfo("〜に", listOf("〜を", "〜と")),
        "하는" to ConnectorInfo("〜する", listOf("〜される", "〜な")),
        "지만" to ConnectorInfo("〜だが", listOf("〜なので", "〜なら")),
        "으면" to ConnectorInfo("〜なら", listOf("〜して", "〜だが")),
        "면서" to ConnectorInfo("〜しながら", listOf("〜した後", "〜するため")),
        "하고" to ConnectorInfo("〜と／〜して", listOf("〜から", "〜だけ")),
        "은" to ConnectorInfo("〜は／〜な", listOf("〜を", "〜へ")),
        "는" to ConnectorInfo("〜は／〜する", listOf("〜を", "〜から")),
        "이" to ConnectorInfo("〜が", listOf("〜を", "〜に")),
        "가" to ConnectorInfo("〜が", listOf("〜は", "〜で")),
        "을" to ConnectorInfo("〜を／〜する", listOf("〜が", "〜へ")),
        "를" to ConnectorInfo("〜を", listOf("〜は", "〜に")),
        "와" to ConnectorInfo("〜と", listOf("〜へ", "〜から")),
        "과" to ConnectorInfo("〜と", listOf("〜が", "〜を")),
        "로" to ConnectorInfo("〜で／〜へ", listOf("〜から", "〜だけ")),
        "에" to ConnectorInfo("〜に／〜へ", listOf("〜を", "〜より")),
        "고" to ConnectorInfo("〜して／〜と", listOf("〜なら", "〜まで")),
        "면" to ConnectorInfo("〜なら", listOf("〜ので", "〜だが")),
        "아" to ConnectorInfo("〜だ／〜ね", listOf("〜から", "〜だけ")),
        "어" to ConnectorInfo("〜だ／〜して", listOf("〜なら", "〜まで")),
    )

    override fun analyze(
        source: String,
        targetLanguage: String,
        nativeLanguage: String,
    ): List<LearningToken> {
        val chunks = Regex("[가-힣]+|[A-Za-z0-9]+")
            .findAll(source)
            .map { it.value }
            .toList()

        return chunks.flatMapIndexed { chunkIndex, chunk ->
            splitChunk(chunk).mapIndexed { partIndex, part ->
                val connector = connectors[part]
                if (connector == null) {
                    LearningToken(
                        id = "$chunkIndex-$partIndex-${UUID.randomUUID()}",
                        source = part,
                        kind = TokenKind.WORD,
                        translation = wordTranslations[part] ?: part,
                    )
                } else {
                    val choices = (connector.distractors + connector.translation)
                        .shuffled()
                    LearningToken(
                        id = "$chunkIndex-$partIndex-${UUID.randomUUID()}",
                        source = part,
                        kind = TokenKind.CONNECTOR,
                        translation = connector.translation,
                        choices = choices,
                    )
                }
            }
        }
    }

    private fun splitChunk(chunk: String): List<String> {
        // 単独語として意味を持つものを、同形の助詞より優先する。
        if (wordTranslations.containsKey(chunk)) return listOf(chunk)

        val suffix = connectors.keys.firstOrNull {
            chunk.endsWith(it) && chunk.length > it.length
        } ?: return listOf(chunk)

        val stem = chunk.dropLast(suffix.length)
        return listOf(stem, suffix).filter { it.isNotBlank() }
    }

    override fun checkAnswer(token: LearningToken, answer: String): Boolean {
        val expected = normalize(token.translation)
            .split("／", "/", "・")
            .filter { it.isNotBlank() }
        val actual = normalize(answer)
        return expected.any { candidate ->
            actual == candidate || actual.contains(candidate) || candidate.contains(actual)
        } && actual.isNotBlank()
    }

    override fun evaluateTranslation(
        sourceText: String,
        translation: String,
        nativeLanguage: String,
    ): Pair<Int, List<TranslationMistake>> {
        val tokens = analyze(sourceText)
        val normalized = normalize(translation)
        val meaningfulTokens = tokens.filter {
            it.kind == TokenKind.WORD || it.translation.length >= 2
        }
        if (meaningfulTokens.isEmpty()) {
            return (if (translation.isBlank()) 0 else 100) to emptyList()
        }

        val mistakes = meaningfulTokens.mapNotNull { token ->
            val candidates = normalize(token.translation)
                .split("／", "/", "・")
                .filter { it.isNotBlank() && !it.startsWith("〜") }
            val found = candidates.any(normalized::contains)
            if (found || candidates.isEmpty()) {
                null
            } else {
                TranslationMistake(
                    source = token.source,
                    submitted = token.answer.ifBlank { "未反映" },
                    correct = token.translation.replace("〜", ""),
                )
            }
        }
        val score = (((meaningfulTokens.size - mistakes.size) * 100f) /
            meaningfulTokens.size).toInt()
        return score to mistakes
    }

    override fun generateComprehensionQuestions(
        sourceText: String,
        nativeLanguage: String,
    ): List<ComprehensionQuestion> = listOf(
        ComprehensionQuestion(
            id = UUID.randomUUID().toString(),
            prompt = "この文章で述べられている内容を選んでください",
            type = ComprehensionQuestionType.MULTIPLE_CHOICE,
            choices = listOf(sourceText, "文章とは異なる内容", "書かれていない内容"),
        ),
        ComprehensionQuestion(
            id = UUID.randomUUID().toString(),
            prompt = "文章の内容を短く説明してください",
            type = ComprehensionQuestionType.FREE_TEXT,
        ),
    )

    override fun evaluateComprehension(
        sourceText: String,
        questions: List<ComprehensionQuestion>,
        nativeLanguage: String,
    ): Pair<Int, List<ComprehensionQuestion>> {
        val checked = questions.mapIndexed { index, question ->
            val correct = if (index == 0) {
                question.answer == sourceText
            } else {
                question.answer.isNotBlank()
            }
            question.copy(
                isCorrect = correct,
                correctAnswer = if (index == 0) sourceText else "文章に沿った説明",
                feedback = if (correct) "正しく理解できています" else "原文を確認しましょう",
            )
        }
        val score = if (checked.isEmpty()) {
            0
        } else {
            checked.count { it.isCorrect == true } * 100 / checked.size
        }
        return score to checked
    }

    private fun normalize(value: String): String = value
        .lowercase(Locale.getDefault())
        .replace(Regex("[\\s〜~、。,.!！?？()（）]"), "")
}
