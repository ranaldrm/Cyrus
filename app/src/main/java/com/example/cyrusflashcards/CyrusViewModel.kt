package com.example.cyrusflashcards

import androidx.lifecycle.ViewModel
import com.example.cyrusflashcards.data.CyrusDeck
import com.example.cyrusflashcards.data.CyrusUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class CyrusViewModel: ViewModel() {
    //this collects from the UIState and exposes an immutable version to the other
    //classes (I think?)
    private val _uiState = MutableStateFlow(CyrusUiState())
    val uiState: StateFlow<CyrusUiState> get() = _uiState

    fun selectDeck(deck: CyrusDeck) {
        _uiState.value.
    }
}
