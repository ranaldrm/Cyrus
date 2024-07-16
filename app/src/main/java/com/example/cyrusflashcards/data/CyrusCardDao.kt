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

    @Query("SELECT * FROM cyrus_card WHERE cardId = :id")
    suspend fun getCardById(id: Int): CyrusCard?

    @Query("SELECT * FROM cyrus_card WHERE deckId = :deckId")
    suspend fun getCardsForDeck(deckId: Int): List<CyrusCard>

    @Delete
    suspend fun deleteCard(card: CyrusCard)


}