package com.example.cyrusflashcards.data
data class CyrusUiState(

    val currentCardId: Int? = null,
    val currentDeckId: Int? = null,






//    val currentCard: CyrusCard? = null,
//    val currentDeck: CyrusDeck? = null




    //The following was for when data was being held in an ordinary  class


    //the vales of name and imageURL depend on the currentCard. The default of 0, means
    //that no card is currently selected
//    val currentCard: CyrusCard = CyrusCard("", ""),
//    val currentDeck: CyrusDeck = CyrusDeck("", mutableListOf(CyrusCard("", ""))),
   //Do I need this here or should it be in ViewModel?

)