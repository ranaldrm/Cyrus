package com.example.cyrusflashcards.data

class CyrusUiState {
    data class CyrusUiState(
        //the vales of name and imageURL depend on the currentCard. The default of 0, means
        //that no card is currently selected
        val currentCard: CyrusCard = CyrusCard(null, null),
        val currentDeck: CyrusDeck = CyrusDeck(null, null)
    )

}