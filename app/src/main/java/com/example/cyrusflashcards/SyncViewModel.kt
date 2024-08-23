package com.example.cyrusflashcards


import android.app.Application
import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cyrusflashcards.data.CyrusCard
import com.example.cyrusflashcards.data.CyrusDeck
import com.example.cyrusflashcards.data.CyrusCardDao
import com.example.cyrusflashcards.data.CyrusDeckDao
import io.github.jan.supabase.postgrest.Postgrest
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.storage.Storage
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

@HiltViewModel
class SyncViewModel @Inject constructor(
    private val cyrusCardDao: CyrusCardDao,
    private val cyrusDeckDao: CyrusDeckDao,
    private val supabasePostgrest: Postgrest,
    private val supabaseStorage: Storage,
    private val application: Application,
    private val auth: Auth

    ) : ViewModel() {

    private val context: Context = application.applicationContext

    fun syncLocalToSupabaseWithBucketManagement() {

        val userId = auth.currentUserOrNull()?.id
        val bucketName = "cyruspictures"

        viewModelScope.launch {
            try {
                // Step 1: Sync Decks (
                val localDecks = cyrusDeckDao.getAllDecks()
                Log.d("SyncViewModel", "Starting sync for Decks. Total decks: ${localDecks.size}")

                for (deck in localDecks) {
                    try {
                        val response = supabasePostgrest["cyrus_deck"].insert(deck)
                        Log.d("SyncViewModel", "Deck insert response: $response")
                    } catch (e: Exception) {
                        Log.e("SyncViewModel", "Exception occurred while inserting deck with ID: ${deck.deckId}", e)
                    }
                }

                // Step 2: Sync Cards and upload images to storage bucket
                val localCards = cyrusCardDao.getAllCards()
                Log.d("SyncViewModel", "Starting sync for Cards. Total cards: ${localCards.size}")

                for (card in localCards) {
                    try {
                        //my version val bucket

                        // Upload the image directly to the bucket
                        if (card.imageURI != null) {
                            val file = File(card.imageURI!!)
                            val filePath = "images/${file.name}"

                            // Convert the File to ByteArray
                            val fileBytes = file.readBytes()
                            val uploadResponse = supabaseStorage.from(bucketName).upload(filePath, fileBytes, upsert = true)

                            // Construct the public URL for the uploaded image
                            val publicURL = "https://kosrnnsxhlmqjtiiywmr.supabase.co/storage/v1/object/public/$bucketName/$filePath"
                            card.imageURL = publicURL
                        }

                        // Insert the card into Supabase
                        val response = supabasePostgrest["cyrus_card"].insert(card)
                        Log.d("SyncViewModel", "Card insert response: $response")
                    } catch (e: Exception) {
                        Log.e("SyncViewModel", "Exception occurred while inserting card with ID: ${card.cardId}", e)
                    }
                }

            } catch (e: Exception) {
                Log.e("SyncViewModel", "Exception occurred during sync operation", e)
            }
        }
    }


//    fun syncLocalToSupabaseWithBucketManagement() {
//
//
//        val userId = auth.currentUserOrNull()?.id
//        viewModelScope.launch {
//            try {
//                // Step 1: Check if the user's bucket exists, if not, create it
//
//                val bucketName = "cyruspictures"
//                val userFolderName = "$userId"
//                Log.d("SyncViewModel", "Checking for user's folder in the $bucketName bucket")
//
//                // List the files/folders in the user's folder to check if it exists
//                val userFolderExists = try {
//                    val items = supabaseStorage.from(bucketName).list(userFolderName)
//                    items.isNotEmpty()
//                } catch (e: Exception) {
//                    // If the folder doesn't exist or another error occurs, we consider it as not existing
//                    false
//                }
//                Log.d("SyncViewModel", "User folder exists $userFolderExists")
//                if (!userFolderExists) {
//                    // Creating a folder by uploading a placeholder file (since folders are typically implicit)
//                    val placeholderFilePath = "$userFolderName/.placeholder"
//                    val placeholderContent = "placeholder".toByteArray() // Empty file content
//                    Log.d("SyncViewModel", "Trying to create new user folder")
//                    supabaseStorage.from(bucketName).upload(placeholderFilePath, placeholderContent)
//                    Log.d("SyncViewModel", "Created a new folder: $userFolderName in bucket: $bucketName")
//                }
////                val bucketName = "user_$userId"
////                Log.d("SyncViewModel", "Checking for user's bucket")
////                val buckets =supabaseStorage.retrieveBuckets()
////                Log.d("SyncViewModel", "Checked for buckets")
////
////                val userBucketExists = buckets.any { it.id == bucketName }
////                if (!userBucketExists) {
////                    supabaseStorage.createBucket(id = bucketName) {
////                        public = false
////                        fileSizeLimit = 20.megabytes // Adjust as needed
////                    }
////                    Log.d("SyncViewModel", "Created a new storage bucket: $bucketName")
////                }
//
//                // Step 2: Wipe all existing data in Supabase for the user
//                try {
//                    // Delete all decks associated with the user
//                    supabasePostgrest["cyrus_card"].delete()
//                    supabasePostgrest["cyrus_deck"].delete()
////                supabasePostgrest["cyrus_deck"].delete()
////                supabasePostgrest["cyrus_deck"].delete()
//
//                } catch (e: Exception) {
//                    Log.e("SyncViewModel", "Exception occurred while deleting data in Supabase", e)
//                }
//
//                // Step 3: Sync Decks
//                val localDecks = cyrusDeckDao.getAllDecks()
//                Log.d("SyncViewModel", "Starting sync for Decks. Total decks: ${localDecks.size}")
//
//                for (deck in localDecks) {
//                    try {
//                        val response = supabasePostgrest["cyrus_deck"].insert(deck)
//                        Log.d("SyncViewModel", "Deck insert response: $response")
//                    } catch (e: Exception) {
//                        Log.e("SyncViewModel", "Exception occurred while inserting deck with ID: ${deck.deckId}", e)
//                    }
//                }
//
//                // Step 4: Sync Cards and upload images to storage bucket
//                val localCards = cyrusCardDao.getAllCards()
//                Log.d("SyncViewModel", "Starting sync for Cards. Total cards: ${localCards.size}")
//
//                for (card in localCards) {
//                    try {
//                        // Check if the card has an image URI and upload it to the bucket
//                        if (card.imageURI != null) {
//                            val file = File(card.imageURI!!)
//                            val filePath = "images/${file.name}"
//
//                            // Convert the File to ByteArray
//                            val fileBytes = file.readBytes()
//                            val uploadResponse = supabaseStorage.from(bucketName).upload(filePath, fileBytes)
//
//                            // Manually construct the public URL
//                            val publicURL = "https://kosrnnsxhlmqjtiiywmr.supabase.co/storage/v1/object/public/$bucketName/$filePath"
//                            card.imageURL = publicURL
//
////                            // If the upload is successful, update the card's imageURL with the new URL
////                            val publicURL = supabaseStorage.from(bucketName).getPublicUrl(filePath)
////                            card.imageURL = publicURL
//                        }
//
//                        // Insert the card into Supabase
//                        val response = supabasePostgrest["cyrus_card"].insert(card)
//                        Log.d("SyncViewModel", "Card insert response: $response")
//                    } catch (e: Exception) {
//                        Log.e("SyncViewModel", "Exception occurred while inserting card with ID: ${card.cardId}", e)
//                    }
//                }

//                // Step 4: Sync Cards and upload images to storage bucket
//                val localCards = cyrusCardDao.getAllCards()
//                Log.d("SyncViewModel", "Starting sync for Cards. Total cards: ${localCards.size}")
//
//                for (card in localCards) {
//                    try {
//                        // Check if the card has an image URI and upload it to the bucket
//                        if (card.imageURI != null) {
//                            val file = File(card.imageURI!!)
//                            val filePath = "images/${file.name}"
//                            val uploadResponse = supabaseStorage.from(bucketName).upload(filePath, file)
//
//                            // If the upload is successful, update the card's imageURL with the new URL
//                            val publicURL = supabaseStorage.from(bucketName).getPublicUrl(filePath)
//                            card.imageURL = publicURL
//                        }
//
//                        // Insert the card into Supabase
//                        val response = supabasePostgrest["cyrus_card"].insert(card)
//                        Log.d("SyncViewModel", "Card insert response: $response")
//                    } catch (e: Exception) {
//                        Log.e("SyncViewModel", "Exception occurred while inserting card with ID: ${card.cardId}", e)
//                    }
//                }

//            } catch (e: Exception) {
//                Log.e("SyncViewModel", "Exception occurred during sync operation", e)
//            }
//        }
//    }




//
//    // Sync data from Supabase to SQLite
//    fun syncSupabaseToLocalwithImages() {
//        viewModelScope.launch {
//            try {
//                // Fetch decks from Supabase
//                val deckResponse = supabasePostgrest["cyrus_deck"].select()
//                if (deckResponse != null) {
//                    try {
//                        val supabaseDecks = deckResponse.decodeList<CyrusDeck>()
//                        cyrusDeckDao.deleteAllDecks() // Clear existing decks in the local DB
//                        supabaseDecks.forEach { deck ->
//                            cyrusDeckDao.createDeck(deck)
//                        }
//                    } catch (e: Exception) {
//                        Log.e("SyncViewModel", "Failed to decode decks from Supabase response.", e)
//                    }
//                } else {
//                    Log.e("SyncViewModel", "Failed to fetch decks from Supabase.")
//                }
//
//                // Fetch cards from Supabase
//                val cardResponse = supabasePostgrest["cyrus_card"].select()
//                if (cardResponse != null) {
//                    try {
//                        val supabaseCards = cardResponse.decodeList<CyrusCard>()
//                        cyrusCardDao.deleteAllCards() // Clear existing cards in the local DB
//
//                        supabaseCards.forEach { card ->
//                            var updatedCard = card
//
//                            // If the card has a public image URL, download the image
//                            card.imageURL?.let { url ->
//                                val imageUri =
//                                    downloadImageFromSupabase(url, card.cardId.toString())
//                                updatedCard = updatedCard.copy(imageURI = imageUri, imageURL = null)
//                            }
//
//                            // Insert or update the card in the local database
//                            cyrusCardDao.addCard(updatedCard)
//                        }
//                    } catch (e: Exception) {
//                        Log.e("SyncViewModel", "Failed to decode cards from Supabase response.", e)
//                    }
//                } else {
//                    Log.e("SyncViewModel", "Failed to fetch cards from Supabase.")
//                }
//            } catch (e: Exception) {
//                Log.e("SyncViewModel", "Exception occurred during sync from Supabase to local", e)
//            }
//        }
//    }

//    // Helper function to download image from Supabase and save locally
//    private fun downloadImageFromSupabase(url: String, cardId: String): String {
//        return try {
//            // Assuming `retrieve` is the correct method; replace it with the correct one if available.
//            val response = supabaseStorage.from("cyruspictures").retrieve(url)
//            if (response != null) {
//                val file = File(context.filesDir, "$cardId.jpg") // Save image as a file with cardId as the name
//                file.outputStream().use { output ->
//                    output.write(response.bytes) // Assuming `bytes` is the correct property
//                }
//                file.toURI().toString() // Return the URI as a string
//            } else {
//                Log.e("SyncViewModel", "Failed to download image from Supabase.")
//                ""
//            }
//        } catch (e: Exception) {
//            Log.e("SyncViewModel", "Exception occurred while downloading image: ", e)
//            ""
//        }
//    }
//


//
//
//
//    // Sync data from SQLite to Supabase
//    fun syncLocalToSupabaseWithImages() {
//        viewModelScope.launch {
//            try {
//                // Delete all records in Supabase tables before syncing
//                supabasePostgrest["cyrus_card"].delete()
//                supabasePostgrest["cyrus_deck"].delete()
//
//
//
//                // Sync Decks
//                val localDecks = cyrusDeckDao.getAllDecks()
//                Log.d("SyncViewModel", "Starting sync for Decks. Total decks: ${localDecks.size}")
//
//                for (deck in localDecks) {
//                    try {
//                        supabasePostgrest["cyrus_deck"].insert(deck)
//
//                    } catch (e: Exception) {
//                        Log.e("SyncViewModel", "Exception occurred while inserting deck with ID:", e)
//                    }
//                }
//
//                // Sync Cards with image upload
//                val localCards = cyrusCardDao.getAllCards()
//                Log.d("SyncViewModel", "Starting sync for Cards. Total cards: ${localCards.size}")
//
//                for (card in localCards) {
//                    try {
//                        var updatedCard = card
//
//                        // Upload image to Supabase if URI is present
//                        card.imageURI?.let { uri ->
//                            val imageFile = getFileFromUri(uri)
//                            val byteArray = imageFile.readBytes() // Convert to ByteArray
//                            val path = "cyruspictures/${imageFile.name}"
//                            supabaseStorage.from("cyruspictures").upload(path, byteArray)
//
//
//                            // Manually construct the public URL
//                            val baseUrl = "https://kosrnnsxhlmqjtiiywmr.supabase.co/storage/v1/object/public"
//                            val imageUrl = "$baseUrl/cyruspictures/$path"
//
//                            updatedCard = updatedCard.copy(imageURL = imageUrl, imageURI = null) // Clear the URI after uploading
//
//                        }
//
//                        supabasePostgrest["cyrus_card"].insert(updatedCard)
//
//                    } catch (e: Exception) {
//                        Log.e("SyncViewModel", "Exception occurred while inserting card with ID: ", e)
//                    }
//                }
//            } catch (e: Exception) {
//                Log.e("SyncViewModel", "Exception occurred during sync operation", e)
//            }
//        }
//    }
//
//    // Convert URI to File
//    private fun getFileFromUri(uri: String): File {
//        val uri = Uri.parse(uri)
//        val inputStream = context.contentResolver.openInputStream(uri)
//        val tempFile = File(context.cacheDir, "temp_image")
//        inputStream?.use { input ->
//            FileOutputStream(tempFile).use { output ->
//                input.copyTo(output)
//            }
//        }
//        return tempFile
//    }
//
//





//
//    // Sync data from SQLite to Supabase
//    fun syncLocalToSupabaseWithImages() {
//        viewModelScope.launch {
//            try {
//                // Delete all records in Supabase tables before syncing
//                supabasePostgrest["cyrus_card"].delete()
//                supabasePostgrest["cyrus_deck"].delete()
//
//
//
//                // Sync Decks
//                val localDecks = cyrusDeckDao.getAllDecks()
//                Log.d("SyncViewModel", "Starting sync for Decks. Total decks: ${localDecks.size}")
//
//                for (deck in localDecks) {
//                    try {
//                        val response = supabasePostgrest["cyrus_deck"].insert(deck)
//                        Log.d("SyncViewModel", "Deck insert response: $response")
//                    } catch (e: Exception) {
//                        Log.e("SyncViewModel", "Exception occurred while inserting deck with ID:", e)
//                    }
//                }
//
//                // Sync Cards with image upload
//                val localCards = cyrusCardDao.getAllCards()
//                Log.d("SyncViewModel", "Starting sync for Cards. Total cards: ${localCards.size}")
//
//                for (card in localCards) {
//                    try {
//                        var updatedCard = card
//
//                        // Upload image to Supabase if URI is present
//                        card.imageURI?.let { uri ->
//                            val imageFile = getFileFromUri(uri)
//                            val path = "bucket-name/${imageFile.name}"
//                            val uploadResponse = supabaseStorage.from("bucket-name").upload(path, imageFile.inputStream())
//
//                            if (uploadResponse.error == null) {
//                                // Assuming you get the URL after uploading the image
//                                val imageUrl = supabaseStorage.from("bucket-name").getPublicUrl(path)
//
//                                updatedCard = updatedCard.copy(imageURL = imageUrl, imageURI = null) // Clear the URI after uploading
//                            } else {
//                                Log.e("SyncViewModel", "Image upload failed: ${uploadResponse.error?.message}")
//                            }
//                        }
//
//                        val response = supabasePostgrest["cyrus_card"].insert(updatedCard)
//                        Log.d("SyncViewModel", "Card insert response: $response")
//                    } catch (e: Exception) {
//                        Log.e("SyncViewModel", "Exception occurred while inserting card with ID: ", e)
//                    }
//                }
//            } catch (e: Exception) {
//                Log.e("SyncViewModel", "Exception occurred during sync operation", e)
//            }
//        }
//    }
//
//    // Convert URI to File
//    private fun getFileFromUri(uri: String): File {
//        val uri = Uri.parse(uri)
//        val inputStream = context.contentResolver.openInputStream(uri)
//        val tempFile = File(context.cacheDir, "temp_image")
//        inputStream?.use { input ->
//            FileOutputStream(tempFile).use { output ->
//                input.copyTo(output)
//            }
//        }
//        return tempFile
//    }



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


}


