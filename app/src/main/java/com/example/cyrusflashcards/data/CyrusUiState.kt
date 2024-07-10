package com.example.cyrusflashcards.data

class CyrusUiState {
    data class CyrusUiState(
        //the vales of name and imageURL depend on the currentCard. The default of 0, means
        //that no card is currently selected
        val currentCard: Int = 0,
        val name: String = "",
        val imageURL: String = ""
    )

}