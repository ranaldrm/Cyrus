package com.example.cyrusflashcards

import com.example.cyrusflashcards.data.CyrusCard
import com.example.cyrusflashcards.data.CyrusDeck

data class CyrusUiState(

    //tracks the currently selected card (within a deck)
    val currentCardId: Int? = null,
    //tracks the currently selected deck
    val currentDeckId: Int? = null,

    val currentCard: CyrusCard? = null,

    val currentDeck: CyrusDeck? = null,

    val cardIndex: Int = 0,

    //A list of cards which can be taken from a deck in the database, these can then be cycled through by
    //index
    val cards: List<CyrusCard> = emptyList(),

    val deckFinished: Boolean = false

)




