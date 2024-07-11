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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.cyrusflashcards.data.CyrusDeck
import com.example.cyrusflashcards.data.DataSource

@Composable
fun CreateDeckScreen (navController: NavController) {
    var nameText by remember { mutableStateOf("") }
    Column (
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    )
    {
        Spacer (modifier = Modifier.height(16.dp))
        Text("Create a dard")
        Spacer (modifier = Modifier.height(16.dp))

        TextField(
            value = nameText,
            onValueChange = {nameText = it},
            label = { Text("Enter a name") }
        )
        Spacer (modifier = Modifier.height(16.dp))
        //Button to create the card
        Button (
            onClick = {
                val deck = CyrusDeck(nameText)
                DataSource.decks.add(deck)
                //Use popBackStack to go back to previous screen
//
                navController.popBackStack()
            }
        ) {
            Text("Create Deck")
        }


    }


}