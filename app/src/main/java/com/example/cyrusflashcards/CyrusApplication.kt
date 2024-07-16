package com.example.cyrusflashcards

import android.app.Application
import android.util.Log
import com.example.cyrusflashcards.data.CyrusDatabase

class CyrusApplication: Application () {
    val database: CyrusDatabase by lazy {
        Log.d("CyrusApplication", "Initializing database with application context: ${this != null}")
        CyrusDatabase.getDatabase(this)
    }
    override fun onCreate() {
        super.onCreate()
        Log.d("CyrusApplication", "Application created")
    }


}