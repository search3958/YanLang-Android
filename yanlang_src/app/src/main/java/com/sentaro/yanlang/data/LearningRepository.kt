package com.sentaro.yanlang.data

import android.content.Context
import androidx.core.content.edit
import org.json.JSONArray
import org.json.JSONObject

class LearningRepository(context: Context) {
    private val preferences =
        context.getSharedPreferences("yanlang_learning_state", Context.MODE_PRIVATE)

    fun load(): AppState {
        val raw = preferences.getString(KEY_STATE, null) ?: return AppState()
        return runCatching { decode(JSONObject(raw)).normalized() }.getOrDefault(AppState())
    }

    fun save(state: AppState) {
        preferences.edit {
            putString(KEY_STATE, encode(state).toString())
        }
    }

    private fun encode(state: AppState): JSONObject = JSONObject().apply {
        val normalizedState = state.normalized()
        put("activeDocumentId", normalizedState.activeDocumentId)
        put("currentStep", normalizedState.currentStep.name)
        put("nativeLanguageCode", normalizedState.nativeLanguageCode)
        put("customNativeLanguage", normalizedState.customNativeLanguage)
        put("activityDates", JSONArray(normalizedState.activityDates.toList()))
        put("documents", JSONArray().apply {
            normalizedState.documents.forEach { document ->
                put(JSONObject().apply {
                    put("id", document.id)
                    put("targetLanguage", document.targetLanguage)
                    put("title", document.title)
                    put("sourceText", document.sourceText)
                    put("step", document.step.name)
                    put("wordbookIndex", document.wordbookIndex)
                    put("wordbookRevealed", document.wordbookRevealed)
                    put("revealedWordIds", JSONArray(document.revealedWordIds.toList()))
                    put("finalTranslation", document.finalTranslation)
                    put("finalScore", document.finalScore)
                    put("comprehensionScore", document.comprehensionScore)
                    put("updatedAt", document.updatedAt)
                    put("tokens", JSONArray().apply {
                        document.tokens.forEach { token ->
                            put(JSONObject().apply {
                                put("id", token.id)
                                put("source", token.source)
                                put("kind", token.kind.name)
                                put("translation", token.translation)
                                put("choices", JSONArray(token.choices))
                                put("answer", token.answer)
                                put("isCorrect", token.isCorrect)
                            })
                        }
                    })
                    put("finalMistakes", JSONArray().apply {
                        document.finalMistakes.forEach { mistake ->
                            put(JSONObject().apply {
                                put("source", mistake.source)
                                put("submitted", mistake.submitted)
                                put("correct", mistake.correct)
                            })
                        }
                    })
                    put("comprehensionQuestions", JSONArray().apply {
                        document.comprehensionQuestions.forEach { question ->
                            put(JSONObject().apply {
                                put("id", question.id)
                                put("prompt", question.prompt)
                                put("type", question.type.name)
                                put("choices", JSONArray(question.choices))
                                put("answer", question.answer)
                                put("isCorrect", question.isCorrect)
                                put("correctAnswer", question.correctAnswer)
                                put("feedback", question.feedback)
                            })
                        }
                    })
                })
            }
        })
    }

    private fun decode(json: JSONObject): AppState {
        val documentsJson = json.optJSONArray("documents") ?: JSONArray()
        val documents = buildList {
            for (index in 0 until documentsJson.length()) {
                val item = documentsJson.getJSONObject(index)
                val tokensJson = item.optJSONArray("tokens") ?: JSONArray()
                val tokens = buildList {
                    for (tokenIndex in 0 until tokensJson.length()) {
                        val token = tokensJson.getJSONObject(tokenIndex)
                        add(
                            LearningToken(
                                id = token.getString("id"),
                                source = token.getString("source"),
                                kind = enumValueOrDefault(
                                    token.optString("kind"),
                                    TokenKind.WORD,
                                ),
                                translation = token.optString("translation"),
                                choices = token.optJSONArray("choices").toStringList(),
                                answer = token.optString("answer"),
                                isCorrect = if (token.isNull("isCorrect")) {
                                    null
                                } else {
                                    token.optBoolean("isCorrect")
                                },
                            ),
                        )
                    }
                }
                val mistakesJson = item.optJSONArray("finalMistakes") ?: JSONArray()
                val mistakes = buildList {
                    for (mistakeIndex in 0 until mistakesJson.length()) {
                        val mistake = mistakesJson.getJSONObject(mistakeIndex)
                        add(
                            TranslationMistake(
                                source = mistake.optString("source"),
                                submitted = mistake.optString("submitted"),
                                correct = mistake.optString("correct"),
                            ),
                        )
                    }
                }
                val questionsJson =
                    item.optJSONArray("comprehensionQuestions") ?: JSONArray()
                val questions = buildList {
                    for (questionIndex in 0 until questionsJson.length()) {
                        val question = questionsJson.getJSONObject(questionIndex)
                        add(
                            ComprehensionQuestion(
                                id = question.optString("id"),
                                prompt = question.optString("prompt"),
                                type = enumValueOrDefault(
                                    question.optString("type"),
                                    ComprehensionQuestionType.FREE_TEXT,
                                ),
                                choices = question.optJSONArray("choices").toStringList(),
                                answer = question.optString("answer"),
                                isCorrect = if (question.isNull("isCorrect")) {
                                    null
                                } else {
                                    question.optBoolean("isCorrect")
                                },
                                correctAnswer = question.optString("correctAnswer"),
                                feedback = question.optString("feedback"),
                            ),
                        )
                    }
                }
                add(
                    LearningDocument(
                        id = item.getString("id"),
                        targetLanguage = item.optString("targetLanguage"),
                        title = item.optString("title"),
                        sourceText = item.optString("sourceText"),
                        tokens = tokens,
                        step = enumValueOrDefault(
                            item.optString("step"),
                            LearningStep.EDITOR,
                        ),
                        wordbookIndex = item.optInt("wordbookIndex"),
                        wordbookRevealed = item.optBoolean("wordbookRevealed"),
                        revealedWordIds = item.optJSONArray("revealedWordIds").toStringList().toSet(),
                        finalTranslation = item.optString("finalTranslation"),
                        finalScore = if (item.isNull("finalScore")) {
                            null
                        } else {
                            item.optInt("finalScore")
                        },
                        finalMistakes = mistakes,
                        comprehensionQuestions = questions,
                        comprehensionScore = if (item.isNull("comprehensionScore")) {
                            null
                        } else {
                            item.optInt("comprehensionScore")
                        },
                        updatedAt = item.optLong("updatedAt"),
                    ),
                )
            }
        }
        return AppState(
            documents = documents,
            activeDocumentId = json.optString("activeDocumentId").ifBlank { null },
            currentStep = enumValueOrDefault(
                json.optString("currentStep"),
                LearningStep.LIBRARY,
            ),
            nativeLanguageCode = when (val code = json.optString("nativeLanguageCode", "ja")) {
                "ko" -> "ko-kr"
                "es" -> "other"
                "zh" -> "zh-CN"
                else -> code
            },
            customNativeLanguage = when (json.optString("nativeLanguageCode", "ja")) {
                "es" -> json.optString("customNativeLanguage").ifBlank { "Español" }
                else -> json.optString("customNativeLanguage")
            },
            activityDates = json.optJSONArray("activityDates").toStringList().toSet(),
        )
    }

    private fun JSONArray?.toStringList(): List<String> {
        if (this == null) return emptyList()
        return buildList {
            for (index in 0 until length()) add(optString(index))
        }
    }

    private inline fun <reified T : Enum<T>> enumValueOrDefault(
        value: String,
        fallback: T,
    ): T = enumValues<T>().firstOrNull { it.name == value } ?: fallback

    private companion object {
        const val KEY_STATE = "state"
    }
}
