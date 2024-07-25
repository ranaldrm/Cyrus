package com.example.cyrusflashcards.ui.screens

import android.app.Application
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.cyrusflashcards.CyrusUiState
import com.example.cyrusflashcards.CyrusViewModel
import com.example.cyrusflashcards.CyrusViewModelFactory
import com.example.cyrusflashcards.data.CyrusDeck
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

//^^^^^^^^^^^^^^^^^^Uses 2 ViewModel methods: getDeckById,  deleteCurrentDeck^^^^^^^^^^^^^^^^^^^^

@Composable
fun DeckScreen(
    navController: NavController,
    viewModel: CyrusViewModel,
) {

    val uiState by viewModel.uiState.collectAsState()
    val currentDeckId: String? = uiState.currentDeckId?.toString()

    var currentDeckName by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(uiState.currentDeckId) {
        uiState.currentDeckId?.let { deckId ->
            scope.launch {
                viewModel.getDeckById(deckId).collect { deck ->
                    currentDeckName = deck.name
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        currentDeckName?.let {
            Text(it)
        } ?: run {
            Text("No Deck Selected")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { navController.navigate("prompt") }
        ) {
            Text("Review Cards")
        }
        Button(
            onClick = { navController.navigate("create_card") }
        ) {
            Text("Add a Card")
        }

        Button(
            onClick = {
                Log.d("DeckScreen", "Delete button clicked")
                viewModel.deleteCurrentDeck()
                navController.popBackStack()
            }
        ) {
            Text("Delete Deck")
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}
