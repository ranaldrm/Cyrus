package com.example.cyrusflashcards.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CyrusCardDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addCard(card: CyrusCard): Long

    //maybe delete as redundant
    @Query("SELECT * FROM cyrus_card WHERE cardId = :id")
    suspend fun getCardById(id: Int): CyrusCard?

    @Query("SELECT * FROM cyrus_card WHERE deckId = :deckId")
    suspend fun getCardsForDeck(deckId: Int): List<CyrusCard>

    @Delete
    suspend fun deleteCard(card: CyrusCard)

    //used when a deck is deleted to delete all associated cards
    @Query("DELETE FROM cyrus_card WHERE deckId = :deckId")
    suspend fun deleteCardsByDeckId(deckId: Int)




}