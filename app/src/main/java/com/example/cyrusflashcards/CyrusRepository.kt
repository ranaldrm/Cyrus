package com.example.cyrusflashcards

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CyrusRepository @Inject constructor() {
    private val _uiState = MutableStateFlow(CyrusUiState())
    val uiState: StateFlow<CyrusUiState> get() = _uiState

    fun updateUiState(newUiState: CyrusUiState) {
        _uiState.value = newUiState
    }
}