package com.example.cyrusflashcards.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.cyrusflashcards.CyrusViewModel

@Composable
fun DeckFinishedScreen(
    navController: NavController,
    viewModel: CyrusViewModel
){
    Column {
        Text (text = "Deck Finished")
    }


}