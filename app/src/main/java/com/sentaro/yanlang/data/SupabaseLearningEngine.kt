package com.sentaro.yanlang.data

import org.json.JSONArray
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import java.util.UUID


class SupabaseLearningEngine(
    private val endpoint: String = DEFAULT_ENDPOINT,
) : LearningEngine {
    private val answerChecker = LocalKoreanLearningEngine()

    override fun analyze(
        source: String,
        targetLanguage: String,
        nativeLanguage: String,
    ): List<LearningToken> {
        val payload = JSONObject()
            .put("source", source)
            .put("targetLanguage", targetLanguage.ifBlank { "原文から判定" })
            .put("nativeLanguage", nativeLanguage)
        val firstContent = request("analyze", payload)
        return runCatching {
            parseAnalysis(firstContent, source)
        }.getOrElse { firstError ->
            parseAnalysis(
                request(
                    "repair_analysis",
                    payload
                        .put("validationError", firstError.message.orEmpty())
                        .put("previousResponse", firstContent),
                ),
                source,
            )
        }
    }

    private fun parseAnalysis(content: String, originalSource: String): List<LearningToken> {
        val root = JSONObject(extractJsonObject(content))
        val array = root.optJSONArray("tokens")
            ?: throw LearningApiException("AIの応答にtokensがありません")
        if (array.length() == 0) {
            throw LearningApiException("文章を単語に分割できませんでした")
        }

        val tokens = buildList {
            for (index in 0 until array.length()) {
                val item = array.optJSONObject(index)
                    ?: throw LearningApiException("tokens[$index]の形式が不正です")
                val sourcePart = AiTextCodec.unescape(item.optString("source")).trim()
                val translation = AiTextCodec.unescape(item.optString("translation")).trim()
                if (sourcePart.isBlank() || translation.isBlank()) {
                    throw LearningApiException("AIの単語データが不足しています")
                }
                val kind = if (item.optString("kind").lowercase() == "connector") {
                    TokenKind.CONNECTOR
                } else {
                    TokenKind.WORD
                }
                val choices = if (kind == TokenKind.CONNECTOR) {
                    normalizeChoices(
                        item.optJSONArray("choices").toStringList().map(AiTextCodec::unescape),
                        translation,
                    )
                } else {
                    emptyList()
                }
                add(
                    LearningToken(
                        id = "$index-${UUID.randomUUID()}",
                        source = sourcePart,
                        kind = kind,
                        translation = translation,
                        choices = choices,
                    ),
                )
            }
        }
        val expectedCoverage = normalizeForCoverage(originalSource)
        val actualCoverage = normalizeForCoverage(tokens.joinToString("") { it.source })
        if (actualCoverage != expectedCoverage) {
            throw LearningApiException(
                "分割結果が原文の順番または範囲と一致していません",
            )
        }
        return tokens
    }

    override fun checkAnswer(token: LearningToken, answer: String): Boolean {
        // AIが作った正解との照合は、入力ごとの待ち時間をなくすため端末内で行う。
        return answerChecker.checkAnswer(token, answer)
    }

    override fun evaluateTranslation(
        sourceText: String,
        translation: String,
        nativeLanguage: String,
    ): Pair<Int, List<TranslationMistake>> {
        val payload = JSONObject()
            .put("sourceText", sourceText)
            .put("translation", translation)
            .put("nativeLanguage", nativeLanguage)
        val firstContent = request("evaluate_translation", payload)
        val firstEvaluation = parseEvaluation(firstContent)
        if (firstEvaluation.meaningEquivalent) {
            return 100 to emptyList()
        }

        // 不正解判定だけは、同義表現を誤って弾いていないか別リクエストで再確認する。
        // 再確認にも原文と入力全文をそのまま渡し、単語帳データは渡さない。
        val reviewedEvaluation = runCatching {
            parseEvaluation(
                request(
                    "review_translation",
                    payload.put("previousResponse", firstContent),
                ),
            )
        }.getOrDefault(firstEvaluation)

        return if (reviewedEvaluation.meaningEquivalent) {
            100 to emptyList()
        } else {
            reviewedEvaluation.score to reviewedEvaluation.mistakes
        }
    }

    private fun parseEvaluation(content: String): AiEvaluation {
        val root = JSONObject(extractJsonObject(content))
        if (!root.has("score")) {
            throw LearningApiException("AIの応答にscoreがありません")
        }
        val score = root.optInt("score").coerceIn(0, 100)
        val mistakesJson = root.optJSONArray("mistakes") ?: JSONArray()
        val meaningEquivalent = root.optBoolean(
            "meaningEquivalent",
            score == 100 && mistakesJson.length() == 0,
        )
        if (meaningEquivalent) {
            return AiEvaluation(
                meaningEquivalent = true,
                score = 100,
                mistakes = emptyList(),
            )
        }
        val mistakes = buildList {
            for (index in 0 until mistakesJson.length()) {
                val item = mistakesJson.optJSONObject(index) ?: continue
                val source = AiTextCodec.unescape(item.optString("source")).trim()
                val correct = AiTextCodec.unescape(item.optString("correct")).trim()
                val submitted = AiTextCodec.unescape(item.optString("submitted"))
                    .trim()
                    .ifBlank { "未反映" }
                val isActuallyDifferent =
                    normalizeForComparison(submitted) != normalizeForComparison(correct)
                if (source.isNotBlank() && correct.isNotBlank() && isActuallyDifferent) {
                    add(
                        TranslationMistake(
                            source = source,
                            submitted = submitted,
                            correct = correct,
                        ),
                    )
                }
            }
        }
        return AiEvaluation(
            meaningEquivalent = false,
            score = score,
            mistakes = mistakes,
        )
    }

    override fun generateComprehensionQuestions(
        sourceText: String,
        nativeLanguage: String,
    ): List<ComprehensionQuestion> {
        val root = JSONObject(
            extractJsonObject(
                request(
                    "generate_comprehension",
                    JSONObject()
                        .put("sourceText", sourceText)
                        .put("nativeLanguage", nativeLanguage),
                ),
            ),
        )
        val array = root.optJSONArray("questions")
            ?: throw LearningApiException("AIの応答に理解チェックがありません")
        val questions = buildList {
            for (index in 0 until array.length()) {
                val item = array.optJSONObject(index) ?: continue
                val questionPrompt = AiTextCodec.unescape(item.optString("prompt")).trim()
                if (questionPrompt.isBlank()) continue
                val type = if (item.optString("type") == "multiple_choice") {
                    ComprehensionQuestionType.MULTIPLE_CHOICE
                } else {
                    ComprehensionQuestionType.FREE_TEXT
                }
                val choices = if (type == ComprehensionQuestionType.MULTIPLE_CHOICE) {
                    item.optJSONArray("choices").toStringList()
                        .map(AiTextCodec::unescape)
                        .distinct()
                        .take(3)
                } else {
                    emptyList()
                }
                if (type == ComprehensionQuestionType.MULTIPLE_CHOICE &&
                    choices.size != 3
                ) {
                    continue
                }
                add(
                    ComprehensionQuestion(
                        id = AiTextCodec.unescape(item.optString("id"))
                            .ifBlank { "q${index + 1}" } + "-${UUID.randomUUID()}",
                        prompt = questionPrompt,
                        type = type,
                        choices = choices,
                    ),
                )
            }
        }
        if (questions.size < 2 ||
            questions.none { it.type == ComprehensionQuestionType.MULTIPLE_CHOICE } ||
            questions.none { it.type == ComprehensionQuestionType.FREE_TEXT }
        ) {
            throw LearningApiException("理解チェックの問題を正しく作成できませんでした")
        }
        return questions
    }

    override fun evaluateComprehension(
        sourceText: String,
        questions: List<ComprehensionQuestion>,
        nativeLanguage: String,
    ): Pair<Int, List<ComprehensionQuestion>> {
        val submittedQuestions = JSONArray().apply {
            questions.forEach { question ->
                put(JSONObject().apply {
                    put("id", question.id)
                    put("question", question.prompt)
                    put("type", question.type.name.lowercase())
                    put("choices", JSONArray(question.choices))
                    put("userAnswer", question.answer)
                })
            }
        }
        val root = JSONObject(
            extractJsonObject(
                request(
                    "evaluate_comprehension",
                    JSONObject()
                        .put("sourceText", sourceText)
                        .put("nativeLanguage", nativeLanguage)
                        .put("questions", submittedQuestions),
                ),
            ),
        )
        val results = root.optJSONArray("results")
            ?: throw LearningApiException("AIの採点結果を読み取れませんでした")
        val resultById = buildMap {
            for (index in 0 until results.length()) {
                val item = results.optJSONObject(index) ?: continue
                put(AiTextCodec.unescape(item.optString("id")), item)
            }
        }
        val checked = questions.map { question ->
            val result = resultById[question.id]
                ?: throw LearningApiException("理解チェックの採点結果が不足しています")
            question.copy(
                isCorrect = result.optBoolean("isCorrect"),
                correctAnswer = AiTextCodec.unescape(result.optString("correctAnswer")).trim(),
                feedback = AiTextCodec.unescape(result.optString("feedback")).trim(),
            )
        }
        val calculatedScore = checked.count { it.isCorrect == true } * 100 / checked.size
        return calculatedScore to checked
    }

    private fun request(
        action: String,
        payload: JSONObject,
        retryAfterEmptyResponse: Boolean = true,
    ): String {
        val connection = (URL(endpoint).openConnection() as HttpURLConnection).apply {
            requestMethod = "POST"
            connectTimeout = 20_000
            readTimeout = 90_000
            doOutput = true
            setRequestProperty("Content-Type", "application/json; charset=utf-8")
            setRequestProperty("Accept", "application/json")
        }

        return try {
            val body = JSONObject()
                .put("action", action)
                .put("payload", payload)
                .toString()
            connection.outputStream.bufferedWriter(Charsets.UTF_8).use { it.write(body) }
            val status = connection.responseCode
            val responseText = (
                if (status in 200..299) connection.inputStream else connection.errorStream
                )?.bufferedReader(Charsets.UTF_8)?.use { it.readText() }.orEmpty()

            if (status !in 200..299) {
                val detail = runCatching {
                    JSONObject(responseText).optString("error")
                }.getOrNull().orEmpty()
                throw LearningApiException(
                    detail.ifBlank { "AIサーバーでエラーが発生しました（$status）" },
                    rawResponse = responseText,
                )
            }

            val content = JSONObject(responseText)
                .optJSONArray("choices")
                ?.optJSONObject(0)
                ?.optJSONObject("message")
                ?.optString("content")
            if (!content.isNullOrBlank()) return content
            if (retryAfterEmptyResponse) {
                return request(action, payload, retryAfterEmptyResponse = false)
            }
            throw LearningApiException(
                "AIが空の応答を返しました。再試行しても内容がありませんでした。",
                rawResponse = responseText,
            )
        } catch (error: LearningApiException) {
            throw error
        } catch (error: Exception) {
            throw LearningApiException(
                "AIに接続できませんでした。通信環境を確認して再試行してください。",
                error,
            )
        } finally {
            connection.disconnect()
        }
    }

    private fun extractJsonObject(content: String): String {
        return try {
            AiJsonResponse.extractObject(content)
        } catch (error: LearningApiException) {
            throw LearningApiException(error.message ?: "AIの応答を読み取れませんでした", error, content)
        }
    }

    private fun normalizeChoices(
        choices: List<String>,
        translation: String,
    ): List<String> {
        val unique = choices
            .map(String::trim)
            .filter(String::isNotBlank)
            .distinct()
            .toMutableList()
        if (translation !in unique) unique.add(translation)
        val fallbackChoices = listOf("〜に", "〜から", "〜だけ", "〜される")
        while (unique.size < 3) {
            unique.add(fallbackChoices.first { it !in unique })
        }
        // 正答が4番目以降になった場合にも必ず3択内へ残す。
        val result = unique.take(3).toMutableList()
        if (translation !in result) result[result.lastIndex] = translation
        return result.distinct().shuffled()
    }

    private fun normalizeForComparison(value: String): String {
        return value
            .lowercase()
            .replace(Regex("[\\s、。,.!！?？「」『』()（）]"), "")
    }

    private fun normalizeForCoverage(value: String): String {
        return value.replace(Regex("[\\s\\p{P}]"), "")
    }

    private fun JSONArray?.toStringList(): List<String> {
        if (this == null) return emptyList()
        return buildList {
            for (index in 0 until length()) {
                optString(index).takeIf(String::isNotBlank)?.let(::add)
            }
        }
    }

    private companion object {
        const val DEFAULT_ENDPOINT =
            "https://bhwxeffktrxzfdmpfhpd.supabase.co/functions/v1/noteapp"
    }

    private data class AiEvaluation(
        val meaningEquivalent: Boolean,
        val score: Int,
        val mistakes: List<TranslationMistake>,
    )
}

class LearningApiException(
    message: String,
    cause: Throwable? = null,
    val rawResponse: String? = null,
) : Exception(message, cause)
