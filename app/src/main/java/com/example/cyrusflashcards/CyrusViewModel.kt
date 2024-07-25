package com.example.cyrusflashcards

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.cyrusflashcards.data.CyrusCard
import com.example.cyrusflashcards.data.CyrusDatabase
import com.example.cyrusflashcards.data.CyrusDeck
import com.example.cyrusflashcards.data.CyrusDeckDao
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






//^^^^^^^^^^^^^^^^^^^^^^SELECTDECKSCREEN^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
// ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

    fun getCardCountForDeck(deckId: Int): Flow<Int> = flow {
        emit(cyrusDeckDao.getCardCountForDeck(deckId))
    }

//also used by DeckScreen
    fun selectCurrentDeckByID(id: Int) {
        Log.d("ViewModel", "Method called in ViewModel passing $id")
          //ViewModelScope starts a new coroutine in the ViewModel's scope so that the suspend function can be called from
        // cyrusDeckDao
        viewModelScope.launch {
            //get a list of cards in the deck from the database
            val cards: List<CyrusCard> = cyrusCardDao.getCardsForDeck(deckId = id)

            //the current card is the first card in the list
            val card: CyrusCard = cards[_uiState.value.cardIndex]

            // Update the UI state with the current deck ID, the list of cards and the current cardID
            _uiState.value = _uiState.value.copy(
                currentDeckId = id,
                cards = cards,
                currentCardId = card.cardId
            )

            Log.d("ViewModel", "Current deck id in ViewModel is ${_uiState.value.currentDeckId}")

        }
    }

  //get all decks so these can be scrolled through in a list and a deck selected
    fun getAllDecks(): Flow<List<CyrusDeck>> = flow {
        emit(cyrusDeckDao.getAllDecks())
    }
//^^^^^^^^^^^^^^^^^^^^^^DECKSCREEN^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
// ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

//also uses selectCurrentDeckByID see SELECTDECKSCREEN


    //Deletes deck and deletes all cards in deck
    fun deleteDeckByID(id: Int) {
        viewModelScope.launch {
            cyrusDeckDao.getDeckById(id)?.let { cyrusDeckDao.deleteDeck(it); cyrusCardDao.deleteCardsByDeckId(id) }
        }
    }


    //^^^^^^^^^^^^^^^^^^^^^^PROMPTSCREEN^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
// ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

      //also used by answerScreen
    fun getCurrentCard(): Flow<CyrusCard> = flow {
        Log.d("ViewModel", "getCurrentCard called in ViewModel")
        currentCardID?.let { cardId ->
            val card = cyrusCardDao.getCardById(cardId)
            if (card != null) {
                emit(card)
            }
        }
    }

    //^^^^^^^^^^^^^^^^^^^^^^^ANSWERSCREEN^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
//^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

//Also uses getCurrentCard (See PromptScreen)

    fun advanceCard() {
        if (_uiState.value.cardIndex < _uiState.value.cards.size - 1) {
            val nextIndex = _uiState.value.cardIndex + 1
            val nextCard = _uiState.value.cards[nextIndex]
            _uiState.value = _uiState.value.copy(cardIndex = nextIndex)
            _uiState.value = _uiState.value.copy(currentCardId = nextCard.cardId)
        } else {
            _uiState.value = _uiState.value.copy(deckFinished = true)
        }
    }

     fun getDeckFinished(): Boolean {
         return _uiState.value.deckFinished
     }


    fun deleteCurrentCard() {
        _uiState.value.currentCardId?.let { cardId ->
            viewModelScope.launch {
                val card = cyrusCardDao.getCardById(cardId)
                card?.let {
                    cyrusCardDao.deleteCard(it)
                    val updatedCards = _uiState.value.cards.filter { it.cardId != cardId }

                    if (updatedCards.isNotEmpty()) {
                        val nextIndex = if (_uiState.value.cardIndex >= updatedCards.size) 0 else _uiState.value.cardIndex
                        val nextCard = updatedCards[nextIndex]

                        _uiState.value = _uiState.value.copy(
                            cards = updatedCards,
                            cardIndex = nextIndex,
                            currentCardId = nextCard.cardId,
                            currentCard = nextCard,
                            deckFinished = false // Reset deckFinished if there are still cards left
                        )
                    } else {
                        _uiState.value = _uiState.value.copy(
                            cards = updatedCards,
                            cardIndex = 0,
                            currentCardId = null,
                            currentCard = null,
                            deckFinished = true // Set deckFinished if no cards are left
                        )
                    }
                }
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




