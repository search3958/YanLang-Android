package com.sentaro.yanlang

import com.sentaro.yanlang.data.AppState
import com.sentaro.yanlang.data.LearningDocument
import com.sentaro.yanlang.data.LearningStep
import com.sentaro.yanlang.data.normalized
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class AppStateNormalizationTest {
    @Test
    fun normalized_removesDuplicateDocumentIds() {
        val original = LearningDocument(id = "same", title = "original")
        val duplicate = LearningDocument(id = "same", title = "duplicate")

        val normalized = AppState(documents = listOf(original, duplicate)).normalized()

        assertEquals(listOf(original), normalized.documents)
    }

    @Test
    fun normalized_returnsToLibraryWhenActiveDocumentIsMissing() {
        val normalized = AppState(
            documents = emptyList(),
            activeDocumentId = "missing",
            currentStep = LearningStep.WORDBOOK,
        ).normalized()

        assertNull(normalized.activeDocumentId)
        assertEquals(LearningStep.LIBRARY, normalized.currentStep)
    }
}
