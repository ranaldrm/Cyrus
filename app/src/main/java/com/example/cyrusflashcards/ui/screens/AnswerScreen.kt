package com.example.cyrusflashcards.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.cyrusflashcards.CyrusViewModel

//^^^^^^^^^^^^^^^^^^Uses 4 ViewModel methods: getCurrentCard, getDeckFinished, advanceCard, deleteCurrentCard^^^^^^^^^^^^^^^^^^^^
@Composable
fun AnswerScreen(
    navController: NavController,
    viewModel: CyrusViewModel
) {

    val currentCard by viewModel.getCurrentCard().collectAsState(initial = null)
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
//show imgURL or "no card Selected if something went wrong and there is no card
        Text(currentCard?.imageURL ?: "No Card Selected")
        Spacer(modifier = Modifier.height(16.dp))
        Icon(
            imageVector = Icons.Default.Person,
            contentDescription = "person",
            modifier = Modifier.size(100.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(currentCard?.name ?: "No Card Selected")
        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                viewModel.advanceCard()
                if (viewModel.getDeckFinished()) {
                    navController.navigate("finished")
                } else {
                    navController.navigate("prompt")
                }
            }
        ) {
            Text("Next Card")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button (
            onClick = {
                viewModel.deleteCurrentCard()
                if (viewModel.getDeckFinished()) {
                    navController.navigate("finished")
                } else {
                    navController.navigate("prompt")
                }
            }

        ) {
            Text("Delete Card")
        }
    }
}