package com.sentaro.yanlang.data

import org.json.JSONArray
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import java.util.UUID
import io.github.jan.supabase.auth.auth


class SupabaseLearningEngine(
    private val endpoint: String = DEFAULT_ENDPOINT,
    private val authRepository: AuthRepository? = null,
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
        val firstContent: String = request("analyze", payload)
        return try {
            parseAnalysis(firstContent, source)
        } catch (firstError: Throwable) {
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
        val rawLines: List<String> = content.trim().lines()
        val lines: List<String> = rawLines.map { line ->
            line.trim()
        }.filter { line ->
            line.isNotBlank() && !line.startsWith("```")
        }

        if (lines.isEmpty()) {
            throw LearningApiException("文章を単語に分割できませんでした")
        }

        val tokens: MutableList<LearningToken> = mutableListOf()
        for ((index, line) in lines.withIndex()) {
                val parts = line.split("|", limit = 4)
                if (parts.size < 3) {
                    throw LearningApiException("分割結果の形式が不正です（行 ${index + 1}）")
                }
                val sourcePart = AiTextCodec.unescape(parts[0]).trim()
                val kindValue = AiTextCodec.unescape(parts[1]).trim().lowercase()
                val translation = AiTextCodec.unescape(parts[2]).trim()
                if (sourcePart.isBlank() || translation.isBlank()) {
                    throw LearningApiException("AIの単語データが不足しています")
                }
                val kind = if (kindValue == "connector" || kindValue == "cnct") {
                    TokenKind.CONNECTOR
                } else {
                    TokenKind.WORD
                }
                val choices: List<String> = if (kind == TokenKind.CONNECTOR && parts.size == 4) {
                    normalizeChoices(
                        parseBracketedChoices(parts[3]),
                        translation,
                    )
                } else {
                    emptyList()
                }
            tokens.add(
                LearningToken(
                    id = "$index-${UUID.randomUUID()}",
                    source = sourcePart,
                    kind = kind,
                    translation = translation,
                    choices = choices,
                ),
            )
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

    private fun parseBracketedChoices(field: String): List<String> {
        val trimmed = field.trim()
        if (!trimmed.startsWith("[") || !trimmed.endsWith("]")) return emptyList()
        val inner = trimmed.substring(1, trimmed.length - 1).trim()
        if (inner.isBlank()) return emptyList()
        val result: MutableList<String> = mutableListOf()
        val rawChoices: List<String> = inner.split("|")
        for (rawChoice in rawChoices) {
            val cleanChoice: String = AiTextCodec.unescape(rawChoice).trim()
            if (cleanChoice.isNotBlank()) {
                result.add(cleanChoice)
            }
        }
        return result
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
        val reviewedEvaluation: AiEvaluation = try {
            parseEvaluation(
                request(
                    "review_translation",
                    payload.put("previousResponse", firstContent),
                ),
            )
        } catch (_: Throwable) {
            firstEvaluation
        }

        return if (reviewedEvaluation.meaningEquivalent) {
            100 to emptyList()
        } else {
            reviewedEvaluation.score to reviewedEvaluation.mistakes
        }
    }

    private fun parseEvaluation(content: String): AiEvaluation {
        val root = JSONObject(AiJsonResponse.extractObject(content))
        if (!root.has("score")) {
            throw LearningApiException("AIの応答にscoreがありません")
        }
        val score = root.optInt("score").coerceIn(0, 100)
        val mistakesJson = root.optJSONArray("mistakes") ?: root.optJSONArray("miss") ?: JSONArray()
        val meaningEquivalent = (root.optBoolean("meaningEquivalent", false) || root.optBoolean("equivalent", false)) ||
            (score == 100 && mistakesJson.length() == 0)
        if (meaningEquivalent) {
            return AiEvaluation(
                meaningEquivalent = true,
                score = 100,
                mistakes = emptyList(),
            )
        }
        val mistakes: MutableList<TranslationMistake> = mutableListOf()
        for (index in 0 until mistakesJson.length()) {
            val item = mistakesJson.optJSONObject(index)
            if (item == null) {
                continue
            }
            val source = AiTextCodec.unescape(item.optString("source")).trim()
            val correct = AiTextCodec.unescape(item.optString("correct")).trim()
            val submitted = AiTextCodec.unescape(
                if (item.has("submitted")) item.optString("submitted") else item.optString("sent"),
            ).trim().ifBlank { "未反映" }
            val isActuallyDifferent =
                normalizeForComparison(submitted) != normalizeForComparison(correct)
            if (source.isNotBlank() && correct.isNotBlank() && isActuallyDifferent) {
                mistakes.add(
                    TranslationMistake(
                        source = source,
                        submitted = submitted,
                        correct = correct,
                    ),
                )
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
        val content = request(
            "generate_comprehension",
            JSONObject()
                .put("sourceText", sourceText)
                .put("nativeLanguage", nativeLanguage),
        )
        val rawLines: List<String> = content.trim().lines()
        val lines: List<String> = rawLines.map { line ->
            line.trim()
        }.filter { line ->
            line.isNotBlank() && !line.startsWith("```")
        }

        if (lines.isEmpty()) {
            throw LearningApiException("AIの応答に理解チェックがありません")
        }
        val questions: MutableList<ComprehensionQuestion> = mutableListOf()
        for ((index, line) in lines.withIndex()) {
            val parts = line.split("|", limit = 4)
            if (parts.size < 3) {
                continue
            }
            val questionId = AiTextCodec.unescape(parts[0]).trim()
                .ifBlank { "q${index + 1}" }
            val typeValue = AiTextCodec.unescape(parts[1]).trim().lowercase()
            val questionPrompt = AiTextCodec.unescape(parts[2]).trim()
            if (questionPrompt.isBlank()) {
                continue
            }
            val type: ComprehensionQuestionType = if (typeValue == "multiple_choice" || typeValue == "mlt_choice") {
                ComprehensionQuestionType.MULTIPLE_CHOICE
            } else {
                ComprehensionQuestionType.FREE_TEXT
            }
            val choices: List<String> = if (type == ComprehensionQuestionType.MULTIPLE_CHOICE && parts.size == 4) {
                parseBracketedChoices(parts[3]).distinct().take(3)
            } else {
                emptyList()
            }
            if (type == ComprehensionQuestionType.MULTIPLE_CHOICE && choices.size != 3) {
                continue
            }
            questions.add(
                ComprehensionQuestion(
                    id = "${questionId}-${UUID.randomUUID()}",
                    prompt = questionPrompt,
                    type = type,
                    choices = choices,
                ),
            )
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
        val content = request(
            "evaluate_comprehension",
            JSONObject()
                .put("sourceText", sourceText)
                .put("nativeLanguage", nativeLanguage)
                .put("questions", submittedQuestions),
        )
        val rawLines: List<String> = content.trim().lines()
        val lines: List<String> = rawLines.map { line ->
            line.trim()
        }.filter { line ->
            line.isNotBlank() && !line.startsWith("```")
        }

        if (lines.isEmpty()) {
            throw LearningApiException("AIの採点結果を読み取れませんでした")
        }
        val resultById: MutableMap<String, List<String>> = mutableMapOf()
        for (line in lines) {
            val parts = line.split("|", limit = 4)
            if (parts.size < 4) {
                continue
            }
            val id = AiTextCodec.unescape(parts[0]).trim()
            if (id.isNotBlank()) {
                resultById[id] = parts
            }
        }
        val checked: MutableList<ComprehensionQuestion> = mutableListOf()
        for (question in questions) {
            val result = resultById[question.id]
                ?: throw LearningApiException("理解チェックの採点結果が不足しています")
            val isOK = AiTextCodec.unescape(result[1]).trim().lowercase() == "true"
            val correctAnswer = AiTextCodec.unescape(result[2]).trim()
            val feedback = AiTextCodec.unescape(result[3]).trim()
            checked.add(
                question.copy(
                    isCorrect = isOK,
                    correctAnswer = correctAnswer,
                    feedback = feedback,
                ),
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
            if (authRepository != null && !authRepository.currentUserId.isNullOrBlank()) {
                val token: String? = SupabaseAuthClient.client.auth.currentSessionOrNull()?.accessToken
                if (token != null) {
                    setRequestProperty("Authorization", "Bearer $token")
                }
            }
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
                val errorJson: JSONObject? = try {
                    JSONObject(responseText)
                } catch (_: Exception) {
                    null
                }
                val detailCandidates = arrayOf("error", "message", "details")
                var detail = ""
                for (key in detailCandidates) {
                    val candidate = errorJson?.optString(key).orEmpty().trim()
                    if (candidate.isNotBlank()) {
                        detail = candidate
                        break
                    }
                }
                if (detail.isBlank()) {
                    detail = responseText.trim()
                }
                val errorCode = errorJson?.optString("code").orEmpty()
                val creditExhausted = indicatesCreditExhaustion(responseText)
                println("[SupabaseLearningEngine] AI request failed: action=$action status=$status creditExhausted=$creditExhausted code=$errorCode")
                throw LearningApiException(
                    if (creditExhausted) {
                        "AIクレジットが不足しています"
                    } else {
                        detail.ifBlank { "AIサーバーでエラーが発生しました（$status）" }
                    },
                    rawResponse = responseText,
                    isCreditExhausted = creditExhausted,
                    statusCode = status,
                )
            }

            if (indicatesCreditExhaustion(responseText)) {
                throw LearningApiException(
                    "AIクレジットが不足しています",
                    rawResponse = responseText,
                    isCreditExhausted = true,
                    statusCode = status,
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

    private fun indicatesCreditExhaustion(responseText: String?): Boolean {
        if (responseText.isNullOrBlank()) {
            println("[SupabaseLearningEngine] Credit exhaustion check skipped: empty response")
            return false
        }

        val marker: String = CREDIT_EXHAUSTION_MARKER
        val errorValue: String = try {
            JSONObject(responseText).optString("error", "")
        } catch (jsonError: Exception) {
            println("[SupabaseLearningEngine] Credit exhaustion JSON parse failed: ${jsonError.message}")
            ""
        }

        val matched: Boolean = errorValue.contains(marker) || responseText.contains(marker)
        println("[SupabaseLearningEngine] Credit exhaustion check: matched=$matched marker=$marker")
        return matched
    }

    private companion object {
        const val DEFAULT_ENDPOINT =
            "https://bhwxeffktrxzfdmpfhpd.supabase.co/functions/v1/yanlang-2"
        const val CREDIT_EXHAUSTION_MARKER = "1日のトークン上限"
    }

    private data class AiEvaluation(
        val meaningEquivalent: Boolean,
        val score: Int,
        val mistakes: List<TranslationMistake>,
    )
}


private fun normalizeForCoverage(value: String): String {
    if (value.isEmpty()) return ""
    val result = StringBuilder(value.length)
    for (ch in value) {
        if (!ch.isWhitespace() && !ch.isPunctuationLikeForCoverage()) {
            result.append(ch)
        }
    }
    return result.toString()
}

private fun Char.isPunctuationLikeForCoverage(): Boolean = when (this) {
    in '\u0021'..'\u002f',
    in '\u003a'..'\u0040',
    in '\u005b'..'\u0060',
    in '\u007b'..'\u007e',
    '、', '。', '，', '．', '！', '？', '：', '；', '「', '」', '『', '』', '（', '）',
    '［', '］', '【', '】', '〈', '〉', '《', '》', '・', '…', '—', '–', '―', '〜', '～',
    '「', '」', '“', '”', '‘', '’', '，', '．', '：', '；' -> true
    else -> when (Character.getType(this)) {
        Character.CONNECTOR_PUNCTUATION.toInt(),
        Character.DASH_PUNCTUATION.toInt(),
        Character.START_PUNCTUATION.toInt(),
        Character.END_PUNCTUATION.toInt(),
        Character.INITIAL_QUOTE_PUNCTUATION.toInt(),
        Character.FINAL_QUOTE_PUNCTUATION.toInt(),
        Character.OTHER_PUNCTUATION.toInt(),
        Character.MATH_SYMBOL.toInt(),
        Character.CURRENCY_SYMBOL.toInt(),
        Character.MODIFIER_SYMBOL.toInt(),
        Character.OTHER_SYMBOL.toInt() -> true
        else -> false
    }
}

private fun normalizeForComparison(value: String): String {
    val result = StringBuilder(value.length)
    for (ch in value.lowercase()) {
        if (!ch.isWhitespace() && !ch.isPunctuationLikeForCoverage()) {
            result.append(ch)
        }
    }
    return result.toString()
}

private fun normalizeChoices(raw: List<String>, translation: String): List<String> {
    val normalizedTranslation = normalizeForComparison(translation)
    val unique = LinkedHashMap<String, String>()

    for (choice in raw) {
        val clean = choice.trim()
        if (clean.isBlank()) continue
        val key = normalizeForComparison(clean)
        if (!unique.containsKey(key)) {
            unique[key] = clean
        }
    }

    if (normalizedTranslation.isNotBlank()) {
        unique[normalizedTranslation] = translation.trim()
    }

    val result = unique.values.toMutableList()
    if (result.size > 3) {
        val correctIndex = result.indexOfFirst { normalizeForComparison(it) == normalizedTranslation }
        if (correctIndex > 2) {
            val correct = result.removeAt(correctIndex)
            result[2] = correct
        }
        while (result.size > 3) result.removeAt(result.lastIndex)
    }

    return result
}

class LearningApiException(
    message: String,
    cause: Throwable? = null,
    val rawResponse: String? = null,
    val isCreditExhausted: Boolean = false,
    val statusCode: Int? = null,
) : Exception(message, cause)
