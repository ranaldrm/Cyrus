package com.example.cyrusflashcards

import android.app.Application
import com.example.cyrusflashcards.data.CyrusDatabase

class CyrusApplication: Application () {
    val database: CyrusDatabase by lazy { CyrusDatabase.getDatabase(this) }


}