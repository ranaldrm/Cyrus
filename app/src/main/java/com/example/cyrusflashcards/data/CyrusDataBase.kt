package com.example.cyrusflashcards.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.util.Log

@Database(entities = [CyrusCard::class, CyrusDeck::class], version = 1)
abstract class CyrusDatabase : RoomDatabase() {
    abstract fun cyrusCardDao(): CyrusCardDao
    abstract fun cyrusDeckDao(): CyrusDeckDao

    companion object {
        @Volatile
        private var INSTANCE: CyrusDatabase? = null

        fun getDatabase(context: Context): CyrusDatabase {
            val appContext = context.applicationContext
                ?: throw IllegalStateException("Context should not be null")

            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    appContext,
                    CyrusDatabase::class.java,
                    "cyrus_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}







//            try {
//                Log.d("CyrusDatabase", "Context is null: ${context == null}")
//
//                if (context != null) {
//                    Log.d("CyrusDatabase", "Context is not null")
//                    Log.d(
//                        "CyrusDatabase",
//                        "ApplicationContext is null: ${context.applicationContext == null}"
//                    )
//                } else {
//                    Log.d("CyrusDatabase", "Context is null")
//                }
//
//                val appContext = context?.applicationContext
//                    ?: throw IllegalStateException("Context should not be null")
//                Log.d("CyrusDatabase", "AppContext obtained")
//
//                return INSTANCE ?: synchronized(this) {
//                    val instance = Room.databaseBuilder(
//                        appContext,
//                        CyrusDatabase::class.java,
//                        "cyrus_database"
//                    ).build()
//                    INSTANCE = instance
//                    instance
//                }
//            } catch (e: Exception) {
//                Log.e("CyrusDatabase", "Error getting database: ${e.message}", e)
//                throw e
//            }
//        }





//            return INSTANCE ?: synchronized(this) {
//                Room.databaseBuilder(context, CyrusDatabase::class.java, "cyrus_database")
//                    .build().also { INSTANCE = it }
//            }


