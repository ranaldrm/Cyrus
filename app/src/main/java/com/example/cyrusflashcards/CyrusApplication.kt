package com.example.cyrusflashcards

import android.app.Application
import android.util.Log
import com.example.cyrusflashcards.data.CyrusDatabase
import dagger.hilt.android.HiltAndroidApp

//boilerplate- this is basically used to connect the database to the viewmodel. The application initialises the database
//and then is based as a parameter to the ViewModel when creating it in the ViewModelFactory

//managed for use with hilt
@HiltAndroidApp
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