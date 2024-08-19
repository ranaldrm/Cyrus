package com.example.cyrusflashcards

import android.content.Context
import com.example.cyrusflashcards.data.CyrusCardDao
import com.example.cyrusflashcards.data.CyrusDatabase
import com.example.cyrusflashcards.data.CyrusDeckDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object daoForHilt {


    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): CyrusDatabase {
        return CyrusDatabase.getDatabase(context)
    }

    @Provides
    fun provideCyrusCardDao(database: CyrusDatabase): CyrusCardDao {
        return database.cyrusCardDao()
    }

    @Provides
    fun provideCyrusDeckDao(database: CyrusDatabase): CyrusDeckDao {
        return database.cyrusDeckDao()
    }

}