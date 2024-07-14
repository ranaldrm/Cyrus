package com.example.cyrusflashcards.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [CyrusCard::class, CyrusDeck::class], version = 1)
abstract class CyrusDatabase : RoomDatabase(){
    abstract fun cyrusCardDao(): CyrusCardDao
    abstract fun cyrusDeckDao(): CyrusDeckDao

    companion object {
        @Volatile
        private var INSTANCE: CyrusDatabase? = null

        fun getDatabase(context: Context): CyrusDatabase {
            //the instance variable is nullable,  this following means that if it has already been
            //created it will be returned, otherwise it will be created

            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(context, CyrusDatabase::class.java, "cyrus_database")
                    .build().also { INSTANCE = it }
            }
        }




    }

}