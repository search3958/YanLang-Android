package com.sentaro.yanlang.data

enum class LearningStep {
    LIBRARY,
    EDITOR,
    WORDBOOK,
    WORD_CHECK,
    CONNECTOR_CHECK,
    FINAL_TRANSLATION,
    COMPREHENSION_CHECK,
}

enum class TokenKind {
    WORD,
    CONNECTOR,
}

data class LearningToken(
    val id: String,
    val source: String,
    val kind: TokenKind,
    val translation: String,
    val choices: List<String> = emptyList(),
    val answer: String = "",
    val isCorrect: Boolean? = null,
)

data class TranslationMistake(
    val source: String,
    val submitted: String,
    val correct: String,
)

enum class ComprehensionQuestionType {
    MULTIPLE_CHOICE,
    FREE_TEXT,
}

data class ComprehensionQuestion(
    val id: String,
    val prompt: String,
    val type: ComprehensionQuestionType,
    val choices: List<String> = emptyList(),
    val answer: String = "",
    val isCorrect: Boolean? = null,
    val correctAnswer: String = "",
    val feedback: String = "",
)

data class LearningDocument(
    val id: String,
    val targetLanguage: String = "",
    val title: String = "",
    val sourceText: String = "",
    val tokens: List<LearningToken> = emptyList(),
    val step: LearningStep = LearningStep.EDITOR,
    val wordbookIndex: Int = 0,
    val wordbookRevealed: Boolean = false,
    val revealedWordIds: Set<String> = emptySet(),
    val finalTranslation: String = "",
    val finalScore: Int? = null,
    val finalMistakes: List<TranslationMistake> = emptyList(),
    val comprehensionQuestions: List<ComprehensionQuestion> = emptyList(),
    val comprehensionScore: Int? = null,
    val updatedAt: Long = System.currentTimeMillis(),
) {
    val wordTokens: List<LearningToken>
        get() = tokens.filter { it.kind == TokenKind.WORD }

    val connectorTokens: List<LearningToken>
        get() = tokens.filter { it.kind == TokenKind.CONNECTOR }

    val completedItems: Int
        get() = tokens.count { it.isCorrect != null }

    val progress: Float
        get() {
            if (step == LearningStep.COMPREHENSION_CHECK &&
                comprehensionScore != null
            ) {
                return 1f
            }
            if (step == LearningStep.FINAL_TRANSLATION && finalScore != null) return 0.9f
            if (tokens.isEmpty()) return 0f
            return (completedItems.toFloat() / tokens.size).coerceIn(0f, 1f)
        }
}

data class AppState(
    val documents: List<LearningDocument> = emptyList(),
    val activeDocumentId: String? = null,
    val currentStep: LearningStep = LearningStep.LIBRARY,
    val nativeLanguageCode: String = "ja",
    val customNativeLanguage: String = "",
    val activityDates: Set<String> = emptySet(),
)

fun AppState.normalized(): AppState {
    val uniqueDocuments = documents.distinctBy { it.id }
    val validActiveId = activeDocumentId?.takeIf { id ->
        uniqueDocuments.any { it.id == id }
    }
    return copy(
        documents = uniqueDocuments,
        activeDocumentId = validActiveId,
        currentStep = if (validActiveId == null) LearningStep.LIBRARY else currentStep,
    )
}
