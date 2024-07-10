package com.example.cyrusflashcards.data

class CyrusCard (
    val name: String,
    val imageURL: String
)

class CyrusDeck (
    val name: String,
    val cards: List<CyrusCard>
) {
    val deckSize: Int = cards.size

}