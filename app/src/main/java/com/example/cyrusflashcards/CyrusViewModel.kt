package com.example.cyrusflashcards

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.cyrusflashcards.data.CyrusCard
import com.example.cyrusflashcards.data.CyrusDatabase
import com.example.cyrusflashcards.data.CyrusDeck
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class CyrusViewModel(application: Application): AndroidViewModel(application) {

    private val _uiState = MutableStateFlow(CyrusUiState())
    val uiState: StateFlow<CyrusUiState> get() = _uiState

    private val cyrusDeckDao = CyrusDatabase.getDatabase(application).cyrusDeckDao()
    private val cyrusCardDao = CyrusDatabase.getDatabase(application).cyrusCardDao()

    val currentDeckID: Int?
        get() = _uiState.value.currentDeckId

    val currentCardID: Int?
        get() = _uiState.value.currentCardId






    fun getCardCountForDeck(deckId: Int): Flow<Int> = flow {
        emit(cyrusDeckDao.getCardCountForDeck(deckId))
    }

    fun selectCurrentDeckByID(id: Int) {
        Log.d("ViewModel", "Method called in ViewModel passing $id")
        _uiState.value = _uiState.value.copy(currentDeckId = id)
        Log.d("ViewModel", "Current deck id in ViewModel is ${_uiState.value.currentDeckId}")
    }

    fun getCurrentCard(): Flow<CyrusCard> = flow {
        Log.d("ViewModel", "getCurrentCard called in ViewModel")
        currentCardID?.let { cardId ->
            val card = cyrusCardDao.getCardById(cardId)
            if (card != null) {
                emit(card)
            }
        }
    }

    fun deleteCard(id: Int) {
        viewModelScope.launch {
            cyrusCardDao.getCardById(id)?.let { cyrusCardDao.deleteCard(it) }
        }
    }

    fun createCard(deckId: Int?, name: String, url: String) {
        deckId?.let {
            val card = CyrusCard(deckId = it, name = name, imageURL = url)
            viewModelScope.launch {
                cyrusCardDao.addCard(card)
            }
        }
    }

    fun deleteDeck(deck: CyrusDeck) {
        viewModelScope.launch {
            cyrusDeckDao.deleteDeck(deck)
        }
    }

    fun deleteDeckByID(id: Int) {
        viewModelScope.launch {
            cyrusDeckDao.getDeckById(id)?.let { cyrusDeckDao.deleteDeck(it) }
        }
    }

    fun deleteCurrentDeck() {
        currentDeckID?.let { deleteDeckByID(it) }
    }

    fun createDeck(name: String) {
        viewModelScope.launch {
            val deck = CyrusDeck(name = name)
            cyrusDeckDao.createDeck(deck)
        }
    }

    fun getCurrentDeck(): Flow<CyrusDeck> = flow {
        currentDeckID?.let { deckId ->
            cyrusDeckDao.getDeckById(deckId)?.let { emit(it) }
        }
    }

    fun selectCard(card: CyrusCard) {
        _uiState.value = _uiState.value.copy(currentCardId = card.cardId)
    }

    fun selectDeck(deck: CyrusDeck) {
        _uiState.value = _uiState.value.copy(currentDeckId = deck.deckId)
    }

    fun getCardsForDeck(deckID: Int): Flow<List<CyrusCard>> = flow {
        emit(cyrusCardDao.getCardsForDeck(deckID))
    }

    fun getAllDecks(): Flow<List<CyrusDeck>> = flow {
        emit(cyrusDeckDao.getAllDecks())
    }

    fun getDeckById(id: Int): Flow<CyrusDeck> = flow {
        cyrusDeckDao.getDeckById(id)?.let { emit(it) }
    }

    fun getCardById(id: Int): Flow<CyrusCard> = flow {
        cyrusCardDao.getCardById(id)?.let { emit(it) }
    }
}
































////Old version
////on advice from chatGPT extending AndroidViewModel rather than ViewModel
////taking application as parameter
//class CyrusViewModel(application: Application): AndroidViewModel(application) {
//
//    private val _uiState = MutableStateFlow(CyrusUiState())
//    val uiState: StateFlow<CyrusUiState> get()= _uiState
//
//    //    private val application: CyrusApplication = CyrusApplication()
//    private val cyrusDeckDao = CyrusDatabase.getDatabase(application).cyrusDeckDao()
//    private val cyrusCardDao = CyrusDatabase.getDatabase(application).cyrusCardDao()
//
//    //    val currentCard: CyrusCard? = _uiState.value.currentCard
////    val currentDeck: CyrusDeck? = _uiState.value.currentDeck
//    var currentCardID: Int? = _uiState.value.currentCardId
//
//    val currentDeckID: Int?
//        get() = _uiState.value.currentDeckId
//
////used in the SelectDeckScreen to show how many cards are in each deck
//    fun getCardCountForDeck(deckId: Int): Flow<Int> = flow {
//        val count = cyrusDeckDao.getCardCountForDeck(deckId)
//        emit(count)
//    }
//
//    //for testing purposes
//
//
//    fun selectCurrentDeckbyID(id: Int) {
//        Log.d("ViewModel", "Current deck id in viewModel is $currentDeckID")
//        Log.d("ViewModel", "Method called in Viewmodel passing $id")
//        _uiState.value = _uiState.value.copy(currentDeckId = id)
//
//        Log.d("ViewModel", "Current deck id in ViewModel is $currentDeckID")
//    }
//
//
//
//    fun getCurrentCard():Flow<CyrusCard> = flow {
//        Log.d("ViewModel", "getcurrentCard called in ViewModel")
//        Log.d("ViewModel", "Current deck id in viewModel is $currentDeckID")
//        val card = currentCardID?.let { cyrusCardDao.getCardById(it) }
//        if (card != null) {
//            emit(card)
//        }
//    }
//
//    fun deleteCard(id: Int) {
//        //Concurrency: ViewModelScope is used so that any coroutines within will be automatically canceleld
//        //if the ViewModel is cleared.
//        viewModelScope.launch {
//            val card = cyrusCardDao.getCardById(id)
//            if (card != null) {
//                cyrusCardDao.deleteCard(card)
//            }
//        }
//    }
//
//    fun createCard(deckId: Int?, name: String, url: String) {
//        //Concurrency: ViewModelScope is used so that any coroutines within will be automatically canceleld
//        //if the ViewModel is cleared.
//        val card = deckId?.let { CyrusCard(deckId = it, name = name, imageURL = url) }
//        viewModelScope.launch {
//            if (card != null) {
//                cyrusCardDao.addCard(card)
//            }
//        }
//    }
//
//    fun deleteDeck(deck: CyrusDeck) {
//        //Concurrency: ViewModelScope is used so that any coroutines within will be automatically canceleld
//        //if the ViewModel is cleared.
//        viewModelScope.launch {
//
//            cyrusDeckDao.deleteDeck(deck)
//        }
//
//    }
//
//    fun deleteDeckByID(id: Int) {
//        viewModelScope.launch {
//            val deck = cyrusDeckDao.getDeckById(id)
//            if (deck != null) {
//                cyrusDeckDao.deleteDeck(deck)
//            }
//        }
//    }
//
//    fun deleteCurrentDeck (){
//        currentDeckID?.let { deleteDeckByID(it) }
//    }
//
//    fun createDeck(name: String) {
//        //Concurrency: ViewModelScope is used so that any coroutines within will be automatically canceleld
//        //if the ViewModel is cleared.
//        viewModelScope.launch {
//            val deck = CyrusDeck(name = name)
//            cyrusDeckDao.createDeck(deck)
//        }
//
//    }
//
//    //   gets the ID of the current deck from the uiState and then uses it get the actual deck from the database
//
//    fun getCurrentDeck(): Flow<CyrusDeck> = flow {
//        val deck = currentDeckID?.let { cyrusDeckDao.getDeckById(currentDeckID!!) }
//        if (deck != null) {
//            emit(deck)
//        }
//    }
//
//
//
//
//
//
//
//
//
//
//
//    fun selectCard(card: CyrusCard) {
//        _uiState.value = _uiState.value.copy(currentCardId = card.cardId)
//    }
//
//
//    //need to review 'value' and 'copy'
//    fun selectDeck(deck: CyrusDeck) {
////        _uiState.value = _uiState.value.copy(currentDeck = deck)
//        _uiState.value = _uiState.value.copy(currentDeckId = deck.deckId)
//
//    }
//
//    fun getCardsForDeck(deckID: Int): Flow<List<CyrusCard>> = flow {
//        val cards = cyrusCardDao.getCardsForDeck(deckID)
//        emit(cards)
//    }
//
//    fun getAllDecks(): Flow<List<CyrusDeck>> = flow {
//        val decks = cyrusDeckDao.getAllDecks()
//        emit(decks)
//    }
//
//    fun getDeckById(id: Int): Flow<CyrusDeck> = flow {
//        val deck = cyrusDeckDao.getDeckById(id)
//        if (deck != null) {
//            emit(deck)
//        }
//    }
//
//    fun getCardById(id: Int): Flow<CyrusCard> = flow {
//        val card = cyrusCardDao.getCardById(id)
//        if (card != null) {
//            emit(card)
//        }
//    }
//
//
//}
//


//This was greyed out some time ago
//    companion object {
//        val factory : ViewModelProvider.Factory = viewModelFactory {
//            initializer {
//                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as CyrusApplication)
//                CyrusViewModel(application.database.busScheduleDao())
//            }
//        }
//    }




