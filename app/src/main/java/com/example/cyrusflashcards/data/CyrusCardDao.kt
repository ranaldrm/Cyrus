package com.example.cyrusflashcards.data

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

interface CyrusCardDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCard(card: CyrusCard): Long

    @Query("SELECT * FROM cyrus_card WHERE id = :id")
    suspend fun getCardById(id: Int): CyrusCard?

    @Query("SELECT * FROM cyrus_card WHERE deck_id = :deckId")
    suspend fun getCardsForDeck(deckId: Int): List<CyrusCard>

    @Delete
    suspend fun deleteCard(card: CyrusCard)


}