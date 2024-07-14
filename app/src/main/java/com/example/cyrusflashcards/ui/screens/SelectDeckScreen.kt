package com.example.cyrusflashcards.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.cyrusflashcards.CyrusViewModel
import com.example.cyrusflashcards.data.CyrusDeck
import com.example.cyrusflashcards.data.CyrusUiState
import com.example.cyrusflashcards.data.DataSource

@Composable
fun SelectDeckScreen(
    navController: NavController,
    viewModel: CyrusViewModel

) {
    val decks by viewModel.getAllDecks().collectAsState(initial = emptyList())


    //maybe delete? do I need a uiState seperate from ViewModel?
//    val uiState by viewModel.uiState.collectAsState()
    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick ={ navController.navigate("create_deck")}
        ) {
            Text("Create a new Deck")
        }
        //way into the data
        ScrollDecks(decks = decks, viewModel, navController)
        Spacer(modifier = Modifier.height(32.dp))

    }
}

@Composable
fun ScrollDecks(decks: List<CyrusDeck>, viewModel: CyrusViewModel, navController: NavController) {
    LazyColumn {
        items(decks) { deck ->
            DeckView(deck, viewModel, navController)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeckView(deck: CyrusDeck, viewModel: CyrusViewModel, navController: NavController) {
    Card(

        //need to feed back event
        onClick = {
            viewModel.selectDeck(deck)
            navController.navigate("deck")

                  },
        modifier = Modifier
            .padding(8.dp)
            .border(BorderStroke(2.dp, Color.Black)),
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            //required because name currently nullable, may change
            deck.name.let {
                Text(
                    text = it
                )
            }
            Text (
                text = "Deck Size: To do....."
            )
        }
    }
}

//@Preview
//@Composable
//fun SelectDeckScreenPreview() {
//    SelectDeckScreen(navController = rememberNavController())
//}

