package com.example.cyrusflashcards.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


//add serializable to also make compatible with supabase
@Serializable
@Entity(tableName= "cyrus_card")
data class CyrusCard (
    @PrimaryKey(autoGenerate = true)
    @SerialName("cardId")
    val cardId: Int = 0,
    @SerialName("deckId")
    val deckId: Int,     //foreign key for deck
    @SerialName("name")
    val name: String,
    @SerialName("imageURL")
    val imageURL: String = "no URL entered",
    @SerialName("eFactor")
    var eFactor: Double = 2.5, //how easy the card is- starts at 2.5
    @SerialName("interval")
    var interval: Int = 1,  //how often the card should be reviewed
    @SerialName("lastReviewed")
    var lastReviewed: String? = null, // currently using a String// whether to review on a particular day
    //is handled by the DeckDao SQL quiry
    @SerialName("reviewCount")
    var reviewCount: Int = 0 // number of times the card has been reviewed,
    //this is used for the SM-2 algorithm and never goes above 3, so 3 means >=3
)

//add serializable to also make compatible with supabase
@Serializable
@Entity(tableName= "cyrus_deck")
data class CyrusDeck (
    @PrimaryKey(autoGenerate = true)
    @SerialName("deckId")
    val deckId: Int = 0,
    @SerialName("name")
    val name: String,
)


//to be used within system
data class DomainCyrusCard (
    val cardId: Int = 0,
    val deckId: Int,     //foreign key for deck
    val name: String,
    val imageURL: String = "no URL entered",
    var eFactor: Double = 2.5, //how easy the card is- starts at 2.5
    var interval: Int = 1,  //how often the card should be reviewed
    var lastReviewed: String? = null, // currently using a String// whether to review on a particular day
    //is handled by the DeckDao SQL quiry
    var reviewCount: Int = 0 // number of times the card has been reviewed,
    //this is used for the SM-2 algorithm and never goes above 3, so 3 means >=3
)

data class DomainCyrusDeck (
    val deckId: Int = 0,
    val name: String,
)

