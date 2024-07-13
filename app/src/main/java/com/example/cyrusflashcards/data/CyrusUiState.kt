package com.example.cyrusflashcards.data
data class CyrusUiState(
    //the vales of name and imageURL depend on the currentCard. The default of 0, means
    //that no card is currently selected
    val currentCard: CyrusCard = CyrusCard("", ""),
    val currentDeck: CyrusDeck = CyrusDeck("", mutableListOf(CyrusCard("", ""))),
   //Do I need this here or should it be in ViewModel?

)