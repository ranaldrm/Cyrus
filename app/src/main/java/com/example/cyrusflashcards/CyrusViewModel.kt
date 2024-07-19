package com.example.cyrusflashcards

//import androidx.compose.ui.window.application
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.cyrusflashcards.data.CyrusCard
import com.example.cyrusflashcards.data.CyrusDatabase
import com.example.cyrusflashcards.data.CyrusDeck
import com.example.cyrusflashcards.data.CyrusUiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

//on advice from chatGPT extending AndroidViewModel rather than ViewModel
//taking application as parameter
class CyrusViewModel(application: Application): AndroidViewModel(application) {

    private val _uiState = MutableStateFlow(CyrusUiState())
    val uiState: StateFlow<CyrusUiState> = _uiState

    //    private val application: CyrusApplication = CyrusApplication()
    private val cyrusDeckDao = CyrusDatabase.getDatabase(application).cyrusDeckDao()
    private val cyrusCardDao = CyrusDatabase.getDatabase(application).cyrusCardDao()

    //    val currentCard: CyrusCard? = _uiState.value.currentCard
//    val currentDeck: CyrusDeck? = _uiState.value.currentDeck
    var currentCardID: Int? = _uiState.value.currentCardId
    var currentDeckID: Int? = _uiState.value.currentDeckId

    //19/07/2024
    fun getCardCountForDeck(deckId: Int): Flow<Int> = flow {
        val count = cyrusDeckDao.getCardCountForDeck(deckId)
        emit(count)
    }


    fun deleteCard(id: Int) {
        //Concurrency: ViewModelScope is used so that any coroutines within will be automatically canceleld
        //if the ViewModel is cleared.
        viewModelScope.launch {
            val card = cyrusCardDao.getCardById(id)
            if (card != null) {
                cyrusCardDao.deleteCard(card)
            }
        }
    }

    fun createCard(deckId: Int?, name: String, url: String) {
        //Concurrency: ViewModelScope is used so that any coroutines within will be automatically canceleld
        //if the ViewModel is cleared.
        val card = deckId?.let { CyrusCard(deckId = it, name = name, imageURL = url) }
        viewModelScope.launch {
            if (card != null) {
                cyrusCardDao.addCard(card)
            }
        }
    }

    fun deleteDeck(deck: CyrusDeck) {
        //Concurrency: ViewModelScope is used so that any coroutines within will be automatically canceleld
        //if the ViewModel is cleared.
        viewModelScope.launch {

            cyrusDeckDao.deleteDeck(deck)
        }

    }

    fun deleteDeckByID(id: Int) {
        viewModelScope.launch {
            val deck = cyrusDeckDao.getDeckById(id)
            if (deck != null) {
                cyrusDeckDao.deleteDeck(deck)
            }
        }
    }

    fun deleteCurrentDeck (){
        currentDeckID?.let { deleteDeckByID(it) }
    }

    fun createDeck(name: String) {
        //Concurrency: ViewModelScope is used so that any coroutines within will be automatically canceleld
        //if the ViewModel is cleared.
        viewModelScope.launch {
            val deck = CyrusDeck(name = name)
            cyrusDeckDao.createDeck(deck)
        }

    }

    //   gets the ID of the current deck from the uiState and then uses it get the actual deck from the database

    fun getCurrentDeck(): Flow<CyrusDeck> = flow {
        val deck = currentDeckID?.let { cyrusDeckDao.getDeckById(it) }
        if (deck != null) {
            emit(deck)
        }
    }

    fun getCurrentCard():Flow<CyrusCard> = flow {
        val card = currentCardID?.let { cyrusCardDao.getCardById(it) }
        if (card != null) {
            emit(card)
        }
    }






//
//
//    }
    fun selectDeckbyID(id: Int) {
        _uiState.value = _uiState.value.copy(currentDeckId = id)
    }

    fun selectCard(card: CyrusCard) {
        _uiState.value = _uiState.value.copy(currentCardId = card.cardId)
    }


    //need to review 'value' and 'copy'
    fun selectDeck(deck: CyrusDeck) {
//        _uiState.value = _uiState.value.copy(currentDeck = deck)
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

    fun getDeckById(id: Int): Flow<CyrusDeck> = flow {
        val deck = cyrusDeckDao.getDeckById(id)
        if (deck != null) {
            emit(deck)
        }
    }

    fun getCardById(id: Int): Flow<CyrusCard> = flow {
        val card = cyrusCardDao.getCardById(id)
        if (card != null) {
            emit(card)
        }
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




