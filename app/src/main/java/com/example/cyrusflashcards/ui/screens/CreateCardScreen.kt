package com.example.cyrusflashcards.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
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
import com.example.cyrusflashcards.CyrusViewModel

@Composable
fun CreateCardScreen (navController: NavController, viewModel: CyrusViewModel){
    var nameText by remember { mutableStateOf("") }
    var imageText by remember { mutableStateOf("") }
    Column (
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center

    ) {
        //icon that can later be replaced by an image
        Icon(
            imageVector = Icons.Default.Person,
            contentDescription = "person",
            modifier = Modifier.size(100.dp)
        )
        Spacer (modifier = Modifier.height(16.dp))
        Text("Create a card")
        Spacer (modifier = Modifier.height(16.dp))

        TextField(
            value = nameText,
            onValueChange = {nameText = it},
            label = { Text("Enter a name") }
        )
        Spacer (modifier = Modifier.height(16.dp))
        TextField(
            value = imageText,
            onValueChange = {imageText = it},
            label = { Text("Enter a url for the image") }
        )
        //Button to create the card
        Button (
            onClick = {
                //find the deck in DataSource that corresponds to the
                //deck in currentDeck and add a card?
                viewModel.createCard(nameText, imageText)
                navController.popBackStack()

            }
        ) {
            Text("Create Card")
        }

    }
}