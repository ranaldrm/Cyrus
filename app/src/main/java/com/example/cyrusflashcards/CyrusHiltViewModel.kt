package com.example.cyrusflashcards

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cyrusflashcards.data.CyrusCard
import com.example.cyrusflashcards.data.CyrusCardDao
import com.example.cyrusflashcards.data.CyrusDeck
import com.example.cyrusflashcards.data.CyrusDeckDao
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject
import io.github.jan.supabase.gotrue.Auth
import android.net.Uri
import dagger.hilt.android.internal.Contexts.getApplication
import android.app.Application
import org.jetbrains.annotations.VisibleForTesting
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.util.UUID


@HiltViewModel
class CyrusHiltViewModel @Inject constructor(
    private val application: Application,
    private val supabaseClient: SupabaseClient,
    private val cyrusCardDao: CyrusCardDao,
    private val cyrusDeckDao: CyrusDeckDao,
    private val supabasePostgrest: Postgrest,
    private val cyrusRepository: CyrusRepository,
    private val auth: Auth

) : ViewModel() {

    val uiState: StateFlow<CyrusUiState> get() = cyrusRepository.uiState



//^^^^^^^^^^^^^^^^^^^^^^SELECTDECKSCREEN^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
// ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

    fun getCardCountForDeck(deckId: Int): Flow<Int> = flow {
        emit(cyrusDeckDao.getCardCountForDeck(deckId))
    }



    fun getDueCardCountForDeck(deckId: Int): Flow<Int> = flow {
        emit(cyrusCardDao.getDueCardCountForDeck(deckId))
    }

    fun selectCurrentDeckByID(id: Int) {
        Log.d("ViewModel", "Select Deck Method called in ViewModel passing $id")
        viewModelScope.launch {
            // Get a list of cards in the deck from the database, either all cards
            // or just due cards depending on whether SM-2 algorithm is turned on

            val currentUiState = cyrusRepository.uiState.value

            val cards: List<CyrusCard> = if (currentUiState.usingSM2) {
                Log.d("ViewModel", "getting SM-2 cards")
                cyrusCardDao.getDueCardsForDeck(deckId = id)
            } else {
                Log.d("ViewModel", "getting all cards")
                cyrusCardDao.getCardsForDeck(deckId = id)
            }

            // Shuffle the cards to review if shuffle has been toggled on
            val cardsForReview = if (currentUiState.shuffleCards) {
                Log.d("ViewModel", "Shuffling cards")
                cards.shuffled()
            } else {
                cards
            }

            if (cardsForReview.isNotEmpty()) {
                // Make sure cardIndex is within bounds
                val cardIndex = 0

                // The current card is the first card in the list
                val card: CyrusCard = cardsForReview[cardIndex]

                // Update the UI state in the repository with the current deck ID, the list of cards, and the current card ID
                cyrusRepository.updateUiState(
                    currentUiState.copy(
                        currentDeckId = id,
                        cards = cardsForReview,
                        currentCardId = card.cardId,
                        cardIndex = cardIndex,
                        deckFinished = false
                    )
                )

                Log.d("ViewModel", "Card index is ${cyrusRepository.uiState.value.cardIndex}")
                Log.d("ViewModel", "Current deck id in ViewModel is ${cyrusRepository.uiState.value.currentDeckId}")
            } else {
                Log.d("ViewModel", "No cards found in the deck with ID $id")
                // Handle the case where no cards are found
                cyrusRepository.updateUiState(
                    currentUiState.copy(
                        currentDeckId = id,
                        cards = emptyList(),
                        currentCardId = null
                    )
                )
            }
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

    fun bulkInsertCards(cards: List<CyrusCard>) {
        viewModelScope.launch {

            cyrusCardDao.addCards(cards)

        }
    }

    //^^^^^^^^^^^^^^^^^^^^^^PROMPTSCREEN^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
// ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

    //also used by answerScreen
    //also going to use in updateCard method here in ViewModel
    fun getCurrentCard(): Flow<CyrusCard?> = flow {
        Log.d("ViewModel", "getCurrentCard called in ViewModel")

        // Access the currentCardId from the repository's uiState
        val currentCardId = cyrusRepository.uiState.value.currentCardId

        currentCardId?.let { cardId ->
            val card = cyrusCardDao.getCardById(cardId)
            if (card != null) {
                emit(card)
            } else {
                emit(null)  // Emit null if no card is found
            }
        } ?: run {
            emit(null)  // Emit null if currentCardId is null
        }
    }

    //^^^^^^^^^^^^^^^^^^^^^^^ANSWERSCREEN^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
//^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

//Also uses getCurrentCard (See PromptScreen),

    fun advanceCard() {
        val currentState = cyrusRepository.uiState.value

        if (checkCanAdvance()) {
            val nextIndex = currentState.cardIndex + 1
            val nextCard = currentState.cards[nextIndex]

            // Update the state to advance to the next card
            cyrusRepository.updateUiState(
                currentState.copy(
                    cardIndex = nextIndex,
                    currentCardId = nextCard.cardId
                )
            )
        } else {
            // Update the state to mark the deck as finished
            cyrusRepository.updateUiState(
                currentState.copy(
                    deckFinished = true,
                    cardIndex = 0
                )
            )
        }
    }

    fun checkCanAdvance(): Boolean {
        val currentState = cyrusRepository.uiState.value
        return currentState.cardIndex < currentState.cards.size - 1
    }

    fun getDeckFinished(): Boolean {
        return cyrusRepository.uiState.value.deckFinished
    }



    //needs an API of >=26 to get current date
    @VisibleForTesting
    @RequiresApi(Build.VERSION_CODES.O)
    fun updateCardeFactorAndInterval (card: CyrusCard, qFactor: Int) {
        val newEFactor = calculateNewEFactor(card.eFactor, qFactor)
        val newInterval = calculateNewInterval(card.interval, card.reviewCount, newEFactor)
        val updatedCard = card.copy(
            eFactor = newEFactor,
            interval = newInterval,
            lastReviewed = LocalDate.now().toString(),
            reviewCount = card.reviewCount + 1
        )
        viewModelScope.launch {
            cyrusCardDao.updateCard(updatedCard)
        }
    }

    //used in updateCardeFactorAndInterval
    @VisibleForTesting
    internal fun calculateNewInterval (interval: Int, reviewCount: Int, eFactor: Double): Int {
        var newInterval = interval
        if (reviewCount == 1 ) {
            newInterval = 1
        } else if (reviewCount == 2) {
            newInterval = 6
        } else if (reviewCount >= 3) {
            newInterval = interval * eFactor.toInt()
        }
        return newInterval
    }

    //used in updateCardeFactorAndInterval
    @VisibleForTesting
    internal fun calculateNewEFactor(eFactor: Double, qFactor: Int): Double {
        var newEfactor = eFactor + (0.1 - (5 - qFactor) * (0.08 + (5 - qFactor) * 0.02))

//minium Efactor of 1.3
        if (newEfactor < 1.3) {
            newEfactor = 1.3
        }
        return newEfactor
    }

    fun deleteCurrentCard() {
        val currentState = cyrusRepository.uiState.value

        currentState.currentCardId?.let { cardId ->
            viewModelScope.launch {
                val card = cyrusCardDao.getCardById(cardId)
                card?.let {
                    cyrusCardDao.deleteCard(it)
                    val updatedCards = currentState.cards.filter { it.cardId != cardId }

                    if (updatedCards.isNotEmpty()) {
                        val nextIndex = if (currentState.cardIndex >= updatedCards.size) 0 else currentState.cardIndex
                        val nextCard = updatedCards[nextIndex]

                        // Update the state to reflect the changes
                        cyrusRepository.updateUiState(
                            currentState.copy(
                                cards = updatedCards,
                                cardIndex = nextIndex,
                                currentCardId = nextCard.cardId,
                                currentCard = nextCard,
                                deckFinished = false // Reset deckFinished if there are still cards left
                            )
                        )
                    } else {
                        // Update the state to reflect that the deck is finished
                        cyrusRepository.updateUiState(
                            currentState.copy(
                                cards = updatedCards,
                                cardIndex = 0,
                                currentCardId = null,
                                currentCard = null,
                                deckFinished = true // Set deckFinished if no cards are left
                            )
                        )
                    }
                }
            }
        }
    }
    //^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^SETTINGSCREEN^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    //^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

    fun toggleAlgorithm(on: Boolean) {
        val currentState = cyrusRepository.uiState.value
        cyrusRepository.updateUiState(currentState.copy(usingSM2 = on))
    }

    fun toggleShuffle(on: Boolean) {
        val currentState = cyrusRepository.uiState.value
        cyrusRepository.updateUiState(currentState.copy(shuffleCards = on))
    }
    fun checkAlgorithm(): Boolean {
        return cyrusRepository.uiState.value.usingSM2
    }
    fun checkShuffle(): Boolean {
        return cyrusRepository.uiState.value.shuffleCards
    }


////////////////////CREATECARD SCREEN////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////

    //used in createCard to get ID for card constructor
    fun getCurrentDeckId(): Int? {    //Used in CreateCardScreen
        return cyrusRepository.uiState.value.currentDeckId
    }


    fun createCard(deckId: Int?, name: String, uri: Uri?) {
        deckId?.let { id ->
            val userId = auth.currentUserOrNull()?.id

            // Save the image from URI to internal storage and get the resulting path
            val imagePath: String? = uri?.let { saveImageToInternalStorage(it) }

            // Create the CyrusCard object, ensuring imagePath is used correctly
            val card = CyrusCard(
                deckId = id,
                name = name,
                imageURI = imagePath ?: "no URL entered",
                userID = userId
            )

            // Save the card to the database
            viewModelScope.launch {
                cyrusCardDao.addCard(card)
            }
        }
    }

//    fun createCard(deckId: Int?, name: String, uri: Uri?) {
//        deckId?.let {
//            val deckId = getCurrentDeckId()
//            val userId = auth.currentUserOrNull()?.id
//
//            val imagePath = uri?.let { uri -> saveImageToInternalStorage(uri) }
//            val card =
//                imagePath?.let { it1 ->
//                    if (deckId != null) {
//                        CyrusCard(deckId = deckId, name = name, imageURL = it1, userID = userId)
//                    }
//                }
//            viewModelScope.launch {
//                if (card != null) {
//                    cyrusCardDao.addCard(card)
//                }
//            }
//        }
//    }

    fun saveImageToInternalStorage(uri: Uri): String? {
        val context = application.applicationContext
        return try {
            // Create a unique file name
            val fileName = "${UUID.randomUUID()}.jpg"
            // Create a file in the internal storage
            val file = File(context.filesDir, fileName)

            // Open the input stream from the URI and write it to the file
            val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
            val outputStream = FileOutputStream(file)

            inputStream?.use { input ->
                outputStream.use { output ->
                    input.copyTo(output)
                }
            }

            // Return the file path
            file.absolutePath
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }




    /////MISC/////////////////////////


    fun getCurrentCardId(): Int? {    //Used in CreateCardScreen
        return cyrusRepository.uiState.value.currentCardId
    }




    fun deleteCurrentDeck() {
        val currentDeckId = cyrusRepository.uiState.value.currentDeckId
        currentDeckId?.let { deleteDeckByID(it) }
    }


    fun createDeck(name: String) {
        viewModelScope.launch {
            val userId = auth.currentUserOrNull()?.id
            val deck = CyrusDeck(name = name, userID = userId)
            cyrusDeckDao.createDeck(deck)
        }
    }

    fun deleteAllDescks() {
        viewModelScope.launch {
            cyrusDeckDao.deleteAllDecks()
        }
    }

    fun deleteAllCards() {
        viewModelScope.launch {
            cyrusCardDao.deleteAllCards()
        }
    }


    fun getDeckById(id: Int): Flow<CyrusDeck> = flow {
        cyrusDeckDao.getDeckById(id)?.let { emit(it) }
    }

    fun getCurrentUserID (): String? {
        val userId = auth.currentUserOrNull()?.id
        return userId
    }


    suspend fun changeDeck (newDeckId: Int) {
        viewModelScope.launch {
            val currentCardId = cyrusRepository.uiState.value.currentCardId
            val card = currentCardId?.let { cyrusCardDao.getCardById(it) }
            if (card != null) {
                card.deckId = newDeckId
                cyrusCardDao.updateCard(card)

            }

        }
    }




}