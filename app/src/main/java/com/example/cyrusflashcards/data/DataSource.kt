package com.example.cyrusflashcards.data

object DataSource {

    object DataSource {
        val card1: CyrusCard = CyrusCard("Card1", "www.card1.com")
        val card2: CyrusCard = CyrusCard("Card2", "www.card2.com")
        val card3: CyrusCard = CyrusCard("Card3", "www.card3.com")
        val card4: CyrusCard = CyrusCard("Card4", "www.card4.com")

        val deck1: CyrusDeck = CyrusDeck("Deck1", listOf(card1, card2))
        val deck2: CyrusDeck = CyrusDeck("Deck2", listOf(card3, card4))

        val decks: List<CyrusDeck> = listOf(deck1, deck2)
    }
}