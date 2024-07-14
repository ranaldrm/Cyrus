package com.example.cyrusflashcards

import androidx.compose.ui.window.application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.room.Room
import com.example.cyrusflashcards.data.CyrusCard
import com.example.cyrusflashcards.data.CyrusCardDao
import com.example.cyrusflashcards.data.CyrusDatabase
import com.example.cyrusflashcards.data.CyrusDeck
import com.example.cyrusflashcards.data.CyrusUiState
import com.example.cyrusflashcards.data.DataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class CyrusViewModel: ViewModel() {
    //this collects from the UIState and exposes an immutable version to the other
    //classes (I think?)
    private val _uiState = MutableStateFlow(CyrusUiState())
    private val application: CyrusApplication = CyrusApplication()
    private val cyrusDeckDao = CyrusDatabase.getDatabase(application).cyrusDeckDao()
    private val cyrusCardDao = CyrusDatabase.getDatabase(application).cyrusCardDao()
    val currentCard: CyrusCard? = _uiState.value.currentCard
    val currentDeck: CyrusDeck? = _uiState.value.currentDeck


    fun deleteCard(card: CyrusCard) {
        //Concurrency: ViewModelScope is used so that any coroutines within will be automatically canceleld
        //if the ViewModel is cleared.
        viewModelScope.launch {
            cyrusCardDao.deleteCard(card)
        }
    }

    fun createCard(card: CyrusCard) {
        //Concurrency: ViewModelScope is used so that any coroutines within will be automatically canceleld
        //if the ViewModel is cleared.
        viewModelScope.launch {
            cyrusCardDao.addCard(card)
        }
    }

    fun deleteDeck(deck: CyrusDeck) {
        //Concurrency: ViewModelScope is used so that any coroutines within will be automatically canceleld
        //if the ViewModel is cleared.
        viewModelScope.launch {
            cyrusDeckDao.deleteDeck(deck)
        }
    }

    fun createDeck(name: String) {
        //Concurrency: ViewModelScope is used so that any coroutines within will be automatically canceleld
        //if the ViewModel is cleared.
        viewModelScope.launch {
            val deck = CyrusDeck(name = name)
            cyrusDeckDao.createDeck(deck)
        }

    }


    fun selectCard(card: CyrusCard) {
        _uiState.value = _uiState.value.copy(currentCard = card)
        _uiState.value = _uiState.value.copy(currentCardId = card.cardId)
    }


    //need to review 'value' and 'copy'
    fun selectDeck(deck: CyrusDeck) {
        _uiState.value = _uiState.value.copy(currentDeck = deck)
        _uiState.value = _uiState.value.copy(currentDeckId = deck.deckId)
    }

    fun getCardsForDeck(deckID: Int): Flow<List<CyrusCard>> = flow {
        val cards = cyrusCardDao.getCardsForDeck(deckID)
        emit(cards)
    }

    fun getAllDecks(): Flow<List<CyrusDeck>> = flow {
        val decks = cyrusDeckDao.getAllDecks()
        emit(decks)
    }


}

    //also remember set current card to first card in deck
//        _uiState.value = _uiState.value.copy(currentCard = deck.cards.first())


//    fun advanceCard(): Boolean {
//        val index = _uiState.value.currentDeck.cards.indexOf(_uiState.value.currentCard)
//        if (index <= _uiState.value.currentDeck.cards.lastIndex) {
//            _uiState.value =
//                _uiState.value.copy(currentCard = _uiState.value.currentDeck.cards[index + 1])
//            return false
//        }
//        else {
//            return true
//        }
//    }




//
//    fun createDeck(name: String) {
//        DataSource.addDeck(name)
//    }
//    //maybe delete cos need to update deck in dataSource
//    fun createCard(name: String, url: String) {
//        val deck = _uiState.value.currentDeck
//        DataSource.addCard(deck, name, url)
//    }


//    companion object {
//        val factory : ViewModelProvider.Factory = viewModelFactory {
//            initializer {
//                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as CyrusApplication)
//                CyrusViewModel(application.database.busScheduleDao())
//            }
//        }
//    }




