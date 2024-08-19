package com.example.cyrusflashcards


import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cyrusflashcards.data.CyrusCard
import com.example.cyrusflashcards.data.CyrusDeck
import com.example.cyrusflashcards.data.CyrusCardDao
import com.example.cyrusflashcards.data.CyrusDeckDao
import io.github.jan.supabase.postgrest.Postgrest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SyncViewModel @Inject constructor(
    private val cyrusCardDao: CyrusCardDao,
    private val cyrusDeckDao: CyrusDeckDao,
    private val supabasePostgrest: Postgrest
) : ViewModel() {

    // Sync data from SQLite to Supabase
    fun syncLocalToSupabase() {
        viewModelScope.launch {
            try {


                // Sync Decks
                val localDecks = cyrusDeckDao.getAllDecks()
                Log.d("SyncViewModel", "Starting sync for Decks. Total decks: ${localDecks.size}")

                for (deck in localDecks) {
                    try {
                        val response = supabasePostgrest["cyrus_deck"].insert(deck)

                        // Log the raw response to understand the structure and content
                        Log.d("SyncViewModel", "Deck insert response: $response")
                    } catch (e: Exception) {
                        Log.e("SyncViewModel", "Exception occurred while inserting deck with ID:", e)
                    }
                }





                // Sync Cards
                val localCards = cyrusCardDao.getAllCards() // Assumed method to get all cards
                Log.d("SyncViewModel", "Starting sync for Cards. Total cards: ${localCards.size}")

                for (card in localCards) {
                    try {
                        val response = supabasePostgrest["cyrus_card"].insert(card)

                        // Log the raw response to understand the structure and content
                        Log.d("SyncViewModel", "Card insert response: $response")
                    } catch (e: Exception) {
                        Log.e("SyncViewModel", "Exception occurred while inserting card with ID: ", e)
                    }
                }


            } catch (e: Exception) {
                Log.e("SyncViewModel", "Exception occurred during sync operation", e)
            }
        }
    }


//    // Sync data from SQLite to Supabase
//    fun syncLocalToSupabase() {
//        viewModelScope.launch {
//            try {
//                // Sync Cards
//                val localCards = cyrusCardDao.getAllCards() // Assumed method to get all cards
//                for (card in localCards) {
//                    supabasePostgrest["cyrus_card"].insert(card) // Assuming table is "cyrus_card" on Supabase
//                }
//
//                // Sync Decks
//                val localDecks = cyrusDeckDao.getAllDecks()
//                for (deck in localDecks) {
//                    supabasePostgrest["cyrus_deck"].insert(deck) // Assuming table is "cyrus_deck" on Supabase
//                }
//            } catch (e: Exception) {
//                // Handle exceptions
//                e.printStackTrace()
//            }
//        }
//    }

    // Sync data from Supabase to SQLite
    fun syncSupabaseToLocal() {
        viewModelScope.launch {
            try {
                // Fetch and insert/update Cards
                val cardResponse = supabasePostgrest["cyrus_card"].select()
                val supabaseCards = cardResponse.decodeList<CyrusCard>() // Replace with appropriate decoding method
                supabaseCards.forEach { card ->
                    cyrusCardDao.addCard(card)
                }

                // Fetch and insert/update Decks
                val deckResponse = supabasePostgrest["cyrus_deck"].select()
                val supabaseDecks = deckResponse.decodeList<CyrusDeck>() // Replace with appropriate decoding method
                supabaseDecks.forEach { deck ->
                    cyrusDeckDao.createDeck(deck)
                }
            } catch (e: Exception) {
                // Handle exceptions
                e.printStackTrace()
            }
        }
    }

    // Perform bidirectional sync
    fun syncBidirectional() {
        viewModelScope.launch {
            syncLocalToSupabase()
            syncSupabaseToLocal()
        }
    }
}



//
//
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.example.cyrusflashcards.data.CyrusCard
//import com.example.cyrusflashcards.data.CyrusCardDao
//import com.example.cyrusflashcards.data.CyrusDeck
//import com.example.cyrusflashcards.data.CyrusDeckDao
//import io.github.jan.supabase.postgrest.Postgrest
//import io.github.jan.supabase.postgrest.bodyAs
//import dagger.hilt.android.lifecycle.HiltViewModel
//import kotlinx.coroutines.launch
//import javax.inject.Inject
//
//@HiltViewModel
//class SyncViewModel @Inject constructor(
//    private val cyrusCardDao: CyrusCardDao,
//    private val cyrusDeckDao: CyrusDeckDao,
//    private val supabasePostgrest: Postgrest
//) : ViewModel() {
//
//    // Sync data from SQLite to Supabase
//    fun syncLocalToSupabase() {
//        viewModelScope.launch {
//            try {
//                // Sync Cards
//                val localCards = cyrusCardDao.getAllCards() // Assumed method to get all cards
//                for (card in localCards) {
//                    supabasePostgrest["cyrus_card"].insert(card) // Assuming table is "cyrus_card" on Supabase
//                }
//
//                // Sync Decks
//                val localDecks = cyrusDeckDao.getAllDecks()
//                for (deck in localDecks) {
//                    supabasePostgrest["cyrus_deck"].insert(deck) // Assuming table is "cyrus_deck" on Supabase
//                }
//            } catch (e: Exception) {
//                // Handle exceptions
//                e.printStackTrace()
//            }
//        }
//    }
//
//    // Sync data from Supabase to SQLite
//    fun syncSupabaseToLocal() {
//        viewModelScope.launch {
//            try {
//                // Fetch and insert/update Cards
//                val supabaseResult = supabasePostgrest["cyrus_card"].select()
//                val supabaseCards: List<CyrusCard> = supabaseResult.bodyAs()
//
//                supabaseCards.forEach { card ->
//                    cyrusCardDao.addCard(card)
//                }
//
//                // Fetch and insert/update Decks
//                val supabaseDeckResult = supabasePostgrest["cyrus_deck"].select()
//                val supabaseDecks: List<CyrusDeck> = supabaseDeckResult.bodyAs()
//
//                supabaseDecks.forEach { deck ->
//                    cyrusDeckDao.createDeck(deck)
//                }
//            } catch (e: Exception) {
//                // Handle exceptions
//                e.printStackTrace()
//            }
//        }
//    }
//
//    // Perform bidirectional sync
//    fun syncBidirectional() {
//        viewModelScope.launch {
//            syncLocalToSupabase()
//            syncSupabaseToLocal()
//        }
//    }
//}
//
//
////import com.example.cyrusflashcards.data.CyrusCard
////import com.example.cyrusflashcards.data.CyrusDeck
////import io.github.jan.supabase.postgrest.result.PostgrestResult
////import kotlinx.coroutines.Dispatchers
////
//
////
////import androidx.lifecycle.ViewModel
////import androidx.lifecycle.viewModelScope
////import com.example.cyrusflashcards.data.CyrusCardDao
////import com.example.cyrusflashcards.data.CyrusDeckDao
////import io.github.jan.supabase.postgrest.Postgrest
////import dagger.hilt.android.lifecycle.HiltViewModel
////import kotlinx.coroutines.launch
////import javax.inject.Inject
////
////@HiltViewModel
////class SyncViewModel @Inject constructor(
////    private val cyrusCardDao: CyrusCardDao,
////    private val cyrusDeckDao: CyrusDeckDao,
////    private val supabasePostgrest: Postgrest
////) : ViewModel() {
////
////    // Sync data from SQLite to Supabase
////    fun syncLocalToSupabase() {
////        viewModelScope.launch {
////            try {
////                // Sync Cards
////                val localCards = cyrusCardDao.getAllCards() // Assumed method to get all cards
////                for (card in localCards) {
////                    supabasePostgrest["cyrus_card"].insert(card) // Assuming table is "cyrus_card" on Supabase
////                }
////
////                // Sync Decks
////                val localDecks = cyrusDeckDao.getAllDecks()
////                for (deck in localDecks) {
////                    supabasePostgrest["cyrus_deck"].insert(deck) // Assuming table is "cyrus_deck" on Supabase
////                }
////            } catch (e: Exception) {
////                // Handle exceptions
////            }
////        }
////    }
////
////    // Sync data from Supabase to SQLite
////    fun syncSupabaseToLocal() {
////        viewModelScope.launch {
////            try {
////                // Fetch and insert/update Cards
//                val supabaseCards = supabasePostgrest["cyrus_card"].select<CyrusCard>()
//                supabaseCards.forEach { card ->
//                    cyrusCardDao.addCard(card)
//                }
//
//                // Fetch and insert/update Decks
//                val supabaseDecks = supabasePostgrest["cyrus_deck"].select<CyrusDeck>()
//                supabaseDecks.forEach { deck ->
//                    cyrusDeckDao.createDeck(deck)
//                }
//            } catch (e: Exception) {
//                // Handle exceptions
//            }
//        }
//    }
//
//    // Perform bidirectional sync
//    fun syncBidirectional() {
//        viewModelScope.launch(Dispatchers.IO) {
//            try {
//                // Step 1: Sync Supabase to Local
//                val supabaseResult: PostgrestResult = supabasePostgrest["cyrus_card"].select()
//                val supabaseCards: List<CyrusCard> = supabaseResult.decodeList() // Convert the result to a list of CyrusCard
//                val localCards = cyrusCardDao.getAllCards()
//
//                supabaseCards.forEach { supabaseCard ->
//                    val localCard = localCards.find { it.cardId == supabaseCard.cardId }
//                    if (localCard == null) {
//                        // If the card doesn't exist locally, insert it
//                        cyrusCardDao.addCard(supabaseCard)
//                    } else {
//                        // Compare timestamps and update accordingly
//                        if (supabaseCard.lastUpdated > localCard.lastUpdated) {
//                            cyrusCardDao.updateCard(supabaseCard)
//                        } else if (supabaseCard.lastUpdated < localCard.lastUpdated) {
//                            supabasePostgrest["cyrus_card"].update(localCard)
//                        }
//                    }
//                }
//
//                // Handle cases where the local database has cards not in Supabase
//                localCards.forEach { localCard ->
//                    if (supabaseCards.none { it.cardId == localCard.cardId }) {
//                        supabasePostgrest["cyrus_card"].insert(localCard)
//                    }
//                }
//
//            } catch (e: Exception) {
//                // Handle exceptions, perhaps by logging or reverting changes
//            }
//        }
//    }
//}
