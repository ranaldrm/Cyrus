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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.cyrusflashcards.CyrusViewModel

@Composable
fun PromptScreen (
    navController: NavController,
    viewModel: CyrusViewModel
) {

    Column (
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Spacer (modifier = Modifier.height(16.dp))
        Text(
            text = "Review Card \n Remember the name",
            modifier = Modifier.padding(16.dp)
        )
        Spacer (modifier = Modifier.height(16.dp))
        Icon(
            imageVector = Icons.Default.Person,
            contentDescription = "person",
            modifier = Modifier.size(100.dp)
        )
        //wrapping for nullable - can I get rid of this?
        Text(
            text = viewModel.currentCard.imageURL
        )
        Button(
            onClick ={ navController.navigate("answer")}
        ) {
            Text("Show Answer")
        }



    }



}