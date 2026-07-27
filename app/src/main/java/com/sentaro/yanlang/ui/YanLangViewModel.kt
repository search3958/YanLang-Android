package com.sentaro.yanlang.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.sentaro.yanlang.data.AppState
import com.sentaro.yanlang.data.LearningDocument
import com.sentaro.yanlang.data.LearningRepository
import com.sentaro.yanlang.data.normalized

/**
 * Holds the live learning state across Compose recomposition and configuration changes.
 * Persistence is a backup; the ViewModel is the source of truth while the task is open.
 */
class YanLangViewModel(
    private val repository: LearningRepository,
) : ViewModel() {
    var appState by mutableStateOf(repository.load())
        private set

    fun commit(newState: AppState) {
        val normalizedState = newState.normalized()
        appState = normalizedState
        repository.save(normalizedState)
    }

    fun updateDocument(id: String, transform: (LearningDocument) -> LearningDocument) {
        commit(
            appState.copy(
                documents = appState.documents.map { document ->
                    if (document.id == id) {
                        transform(document).copy(updatedAt = System.currentTimeMillis())
                    } else {
                        document
                    }
                },
            ),
        )
    }
}
