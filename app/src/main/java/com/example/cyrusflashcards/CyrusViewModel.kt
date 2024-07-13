package com.example.cyrusflashcards

import androidx.lifecycle.ViewModel
import com.example.cyrusflashcards.data.CyrusCard
import com.example.cyrusflashcards.data.CyrusDeck
import com.example.cyrusflashcards.data.CyrusUiState
import com.example.cyrusflashcards.data.DataSource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class CyrusViewModel: ViewModel() {
    //this collects from the UIState and exposes an immutable version to the other
    //classes (I think?)
    private val _uiState = MutableStateFlow(CyrusUiState())
    val uiState: StateFlow<CyrusUiState> get() = _uiState


//need to review 'value' and 'copy'
    fun selectDeck(deck: CyrusDeck) {
        _uiState.value = _uiState.value.copy(currentDeck = deck)
    //also set current card to first card in deck
        _uiState.value = _uiState.value.copy(currentCard = deck.cards.first())
    }

    fun advanceCard(): Boolean {
        val index = _uiState.value.currentDeck.cards.indexOf(_uiState.value.currentCard)
        if (index <= _uiState.value.currentDeck.cards.lastIndex) {
            _uiState.value =
                _uiState.value.copy(currentCard = _uiState.value.currentDeck.cards[index + 1])
            return false
        }
        else {
            return true
        }
    }

    val currentCard: CyrusCard = _uiState.value.currentCard
    val currentDeck: CyrusDeck = _uiState.value.currentDeck

    fun selectCard(card: CyrusCard) {
        _uiState.value = _uiState.value.copy(currentCard = card)
    }

    fun createDeck(name: String) {
        DataSource.addDeck(name)
    }
    //maybe delete cos need to update deck in dataSource
    fun createCard(name: String, url: String) {
        val deck = _uiState.value.currentDeck
        DataSource.addCard(deck, name, url)
    }
    //maybe delete cos need to update deck in datasource
    fun deleteCard(card: CyrusCard) {
        _uiState.value.currentDeck.cards.remove(card)
    }



}
