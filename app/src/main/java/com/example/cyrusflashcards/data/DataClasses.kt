package com.example.cyrusflashcards.data

class CyrusCard (
    val name: String?,
    val imageURL: String?
)

class CyrusDeck (
    val name: String?,
    val cards: MutableList<CyrusCard>? = mutableListOf()
) {
    val deckSize: Int?
        get() = cards?.size

}