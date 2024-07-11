package com.example.cyrusflashcards.data



object DataSource {
    val card1 = CyrusCard("Card1", "www.card1.com")
    val card2 = CyrusCard("Card2", "www.card2.com")
    val card3 = CyrusCard("Card3", "www.card3.com")
    val card4 = CyrusCard("Card4", "www.card4.com")
    val card5 = CyrusCard("Card5", "www.card5.com")
    val card6 = CyrusCard("Card6", "www.card6.com")
    val card7 = CyrusCard("Card7", "www.card7.com")
    val card8 = CyrusCard("Card8", "www.card8.com")
    val card9 = CyrusCard("Card9", "www.card9.com")
    val card10 = CyrusCard("Card10", "www.card10.com")
    val card11 = CyrusCard("Card11", "www.card11.com")
    val card12 = CyrusCard("Card12", "www.card12.com")
    val card13 = CyrusCard("Card13", "www.card13.com")
    val card14 = CyrusCard("Card14", "www.card14.com")
    val card15 = CyrusCard("Card15", "www.card15.com")
    val card16 = CyrusCard("Card16", "www.card16.com")
    val card17 = CyrusCard("Card17", "www.card17.com")
    val card18 = CyrusCard("Card18", "www.card18.com")
    val card19 = CyrusCard("Card19", "www.card19.com")
    val card20 = CyrusCard("Card20", "www.card20.com")
    val card21 = CyrusCard("Card21", "www.card21.com")
    val card22 = CyrusCard("Card22", "www.card22.com")
    val card23 = CyrusCard("Card23", "www.card23.com")
    val card24 = CyrusCard("Card24", "www.card24.com")
    val card25 = CyrusCard("Card25", "www.card25.com")
    val card26 = CyrusCard("Card26", "www.card26.com")
    val card27 = CyrusCard("Card27", "www.card27.com")
    val card28 = CyrusCard("Card28", "www.card28.com")
    val card29 = CyrusCard("Card29", "www.card29.com")
    val card30 = CyrusCard("Card30", "www.card30.com")


    val deck1 = CyrusDeck("Deck1", mutableListOf(card1, card2, card3))
    val deck2 = CyrusDeck("Deck2", mutableListOf(card4, card5, card6))
    val deck3 = CyrusDeck("Deck3", mutableListOf(card7, card8, card9))
    val deck4 = CyrusDeck("Deck4", mutableListOf(card10, card11, card12))
    val deck5 = CyrusDeck("Deck5", mutableListOf(card13, card14, card15))
    val deck6 = CyrusDeck("Deck6", mutableListOf(card16, card17, card18))
    val deck7 = CyrusDeck("Deck7", mutableListOf(card19, card20, card21))
    val deck8 = CyrusDeck("Deck8", mutableListOf(card22, card23, card24))
    val deck9 = CyrusDeck("Deck9", mutableListOf(card25, card26, card27))
    val deck10 = CyrusDeck("Deck10", mutableListOf(card28, card29, card30))

    val decks = mutableListOf(deck1, deck2, deck3, deck4, deck5, deck6, deck7, deck8, deck9, deck10)

    fun addDeck(name: String) {
        val newDeck = CyrusDeck(name)
        decks.add(newDeck)
    }
}
