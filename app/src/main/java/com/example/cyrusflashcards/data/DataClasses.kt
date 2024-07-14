package com.example.cyrusflashcards.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey



@Entity(tableName= "cyrus_card")
data class CyrusCard (
    @PrimaryKey(autoGenerate = true)
    val cardId: Int = 0,
    val deckId: Int,     //foreign key for deck
    val name: String,
    val imageURL: String = "no URL entered"
)

@Entity(tableName= "cyrus_deck")
data class CyrusDeck (
    @PrimaryKey(autoGenerate = true)
    val deckId: Int = 0,
    val name: String,
    val cards: MutableList<CyrusCard> = mutableListOf()
)
//) {
//    val deckSize: Int
//        get() = cards.size
//
//    fun addCard(card: CyrusCard) {
//        cards.add(card)
//    }
//

//}