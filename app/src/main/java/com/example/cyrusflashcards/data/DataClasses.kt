package com.example.cyrusflashcards.data

class CyrusCard (
    val name: String,
    val imageURL: String = "no URL entered"
)

class CyrusDeck (
    val name: String,
    val cards: MutableList<CyrusCard> = mutableListOf()
) {
    val deckSize: Int
        get() = cards.size

    fun addCard(card: CyrusCard) {
        cards.add(card)
    }


}