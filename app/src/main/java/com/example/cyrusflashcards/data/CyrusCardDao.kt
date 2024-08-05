package com.example.cyrusflashcards.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface CyrusCardDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addCard(card: CyrusCard): Long

    //for bulk upload
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addCards(cards: List<CyrusCard>): List<Long>


    @Query("SELECT * FROM cyrus_card WHERE cardId = :id")
    suspend fun getCardById(id: Int): CyrusCard?

    //used when needing to get all cards when SM-2 algorithm not being used.
    @Query("SELECT * FROM cyrus_card WHERE deckId = :deckId")
    suspend fun getCardsForDeck(deckId: Int): List<CyrusCard>

    //used when SM-2 algorithm is turned on
    //    SQL query calculated whether date is due, concatenates strings to get date
    @Query("SELECT * FROM cyrus_card WHERE deckId = :deckId AND (lastReviewed IS NULL OR date(lastReviewed, '+' || interval || ' days') <= date('now'))")
    suspend fun getDueCardsForDeck(deckId: Int): List<CyrusCard>

    @Delete
    suspend fun deleteCard(card: CyrusCard)

    //used when a deck is deleted to delete all associated cards
    @Query("DELETE FROM cyrus_card WHERE deckId = :deckId")
    suspend fun deleteCardsByDeckId(deckId: Int)

    //used to update card with new e factor and interval
    @Update
    suspend fun updateCard(card: CyrusCard)




}