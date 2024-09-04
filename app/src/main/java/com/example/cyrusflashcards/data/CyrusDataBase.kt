
package com.example.cyrusflashcards.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.util.Log

//need to update the database version each time change the dataclasses
@Database(entities = [CyrusCard::class, CyrusDeck::class], version = 6)
abstract class CyrusDatabase : RoomDatabase() {
    abstract fun cyrusCardDao(): CyrusCardDao
    abstract fun cyrusDeckDao(): CyrusDeckDao

    companion object {
        @Volatile
        private var INSTANCE: CyrusDatabase? = null

        fun getDatabase(context: Context): CyrusDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CyrusDatabase::class.java,
                    "cyrus_database"
                )
                    .fallbackToDestructiveMigration() // destructive migration so that
                    //when scheme is updated, previous content is wiped
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
