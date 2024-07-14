package com.example.cyrusflashcards.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.cyrusflashcards.CyrusViewModel
import com.example.cyrusflashcards.data.CyrusDeck

@Composable
fun DeckScreen(navController: NavController, viewModel: CyrusViewModel) {
//    val uiState by viewModel.uiState.collectAsState()
//    val deck: CyrusDeck = uiState.currentDeck
    val deck: CyrusDeck? = viewModel.currentDeck
    Column (
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Spacer (modifier = Modifier.height(16.dp))
        //have to use the extra stuff cos name is currenlt nullable
        if (deck != null) {
            Text(deck.name)
        }
        Spacer (modifier = Modifier.height(16.dp))
        Button(
            onClick ={ navController.navigate("prompt")}
        ) {
            Text("Review Cards")
        }
        Button(
            onClick ={ navController.navigate("create_card")}
        ) {
            Text("Add a Card")
        }


        Spacer (modifier = Modifier.height(16.dp))

    }

}