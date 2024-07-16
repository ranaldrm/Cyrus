package com.example.cyrusflashcards

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class CyrusViewModelFactory(
    private val application: Application
) : ViewModelProvider.AndroidViewModelFactory(application) {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CyrusViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CyrusViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}