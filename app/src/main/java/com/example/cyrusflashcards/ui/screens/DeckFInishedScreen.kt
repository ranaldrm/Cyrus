package com.example.cyrusflashcards.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.cyrusflashcards.CyrusViewModel

@Composable
fun DeckFinishedScreen(
    navController: NavController,
    viewModel: CyrusViewModel
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Congratulations! \n Review Finished",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold
            )
            Button(
                onClick = { navController.navigate("deck_select") }

            ) {
                Text("Home")
            }
        }
    }
}





//{
//    Column {
//        Text (text = "Deck Finished")
//    }
//
//
//}