package com.example.cyrusflashcards.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
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

//    val deck: CyrusDeck = uiState.currentDeck
//    val deck: CyrusDeck? = viewModel.currentDeck
//
//    val observedDeck: CyrusDeck = viewModel.currentDeck

    val currentDeck by viewModel.getCurrentDeck().collectAsState(initial = null)

    Column (
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Spacer (modifier = Modifier.height(16.dp))
        //have to use the extra stuff cos name is currenlt nullable
        if (currentDeck != null) {
            //Asserting not null- probably a better way to handle this
            Text(currentDeck!!.name)
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
//        Button(
//            onClick = {
//                if (currentDeck != null) {
//                    val nonNullDeck: CyrusDeck = currentDeck as CyrusDeck
//                    navController.popBackStack()
//                    viewModel.deleteDeck(nonNullDeck)
//
//                }
//            }
//        ){
//
//            Text("Delete Deck")
//        }
        Button(
            onClick = {
                Log.d("DeckScreen", "Delete button clicked")
                viewModel.deleteCurrentDeck()
//                currentDeck?.let { nonNullDeck ->
//                    Log.d("DeckScreen", "Non-null deck: ${nonNullDeck.name}")
//                    viewModel.deleteDeck(nonNullDeck)
//                    Log.d("DeckScreen", "Deck deleted")
//
//                    navController.popBackStack()
//                    Log.d("DeckScreen", "Navigated back")
//
//                }
            }
        ) {
            Text("Delete Deck")
        }


        Spacer (modifier = Modifier.height(16.dp))

    }

}