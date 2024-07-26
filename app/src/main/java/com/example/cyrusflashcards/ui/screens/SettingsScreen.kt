package com.example.cyrusflashcards.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.cyrusflashcards.CyrusViewModel

@Composable
fun SettingsScreen(
    navController: NavController,
    viewModel: CyrusViewModel
){
    Column {
        Text (text = "Settings")
        Button(
            onClick ={ viewModel.deleteAllDescks()}
        ) {
            Text("Delete All Decks")
        }
    }


}