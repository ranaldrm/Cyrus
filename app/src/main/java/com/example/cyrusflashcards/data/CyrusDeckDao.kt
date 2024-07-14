package com.example.cyrusflashcards.data

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

interface CyrusDeckDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDeck(deck: CyrusDeck): Long

    @Query("SELECT * FROM cyrus_deck")
    suspend fun getAllDecks(): List<CyrusDeck>

    @Delete
    suspend fun deleteDeck(deck: CyrusDeck)
}