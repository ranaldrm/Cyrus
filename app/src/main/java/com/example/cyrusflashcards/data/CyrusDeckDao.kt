package com.example.cyrusflashcards.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CyrusDeckDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun createDeck(deck: CyrusDeck): Long

    @Query("SELECT * FROM cyrus_deck")
    suspend fun getAllDecks(): List<CyrusDeck>

    @Delete
    suspend fun deleteDeck(deck: CyrusDeck)


    @Query("SELECT * FROM cyrus_deck WHERE DeckId = :id")
    suspend fun getDeckById(id: Int): CyrusDeck?
}