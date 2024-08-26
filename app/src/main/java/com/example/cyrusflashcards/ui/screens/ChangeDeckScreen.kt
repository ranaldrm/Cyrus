package com.example.cyrusflashcards.ui.screens

import android.util.Log
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.cyrusflashcards.Authentification.AuthenticationViewModel
import com.example.cyrusflashcards.CyrusHiltViewModel
import com.example.cyrusflashcards.data.CyrusDeck
import kotlinx.coroutines.launch

@Composable
fun ChangeDeckScreen(
    navController: NavController,
    viewModel: CyrusHiltViewModel = hiltViewModel()
) {


    val decks by viewModel.getAllDecks().collectAsState(initial = emptyList())
    val uiState by viewModel.uiState.collectAsState()






    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {



        //way into the data
        ScrollChangeDecks(decks = decks, viewModel, navController)
        Spacer(modifier = Modifier.height(32.dp))

    }



}


@Composable
fun ScrollChangeDecks(decks: List<CyrusDeck>, viewModel: CyrusHiltViewModel, navController: NavController) {
    LazyColumn {
        items(decks) { deck ->

            ChangeDeckView(deck, viewModel, navController)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChangeDeckView(
    deck: CyrusDeck,
    viewModel: CyrusHiltViewModel,
    navController: NavController,
    deckId: Int = deck.deckId

) {
    val cardCount by viewModel.getCardCountForDeck(deck.deckId).collectAsState(initial = 0)
    val dueCardCount by viewModel.getDueCardCountForDeck(deck.deckId).collectAsState(initial = 0)
    val coroutineScope = rememberCoroutineScope() //this is used for the change deck function

    Card(
        //need to feed back event
        onClick = {
            Log.d("SelectDeckScreen", "Deck clicked")
            Log.d("SelectDeckScreen","Current deck id in list is $deckId ")
            coroutineScope.launch {
                viewModel.changeDeck(deckId)
                navController.navigate("select_deck")
            }

        },
        modifier = Modifier
            .padding(8.dp)
            .border(BorderStroke(2.dp, Color.Black)),
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            //required because name currently nullable, may change
            deck.name.let {
                Text(
                    text = "Class: $it",

                    textAlign = TextAlign.Center
                )
            }
            Text (
                text = "Total students: $cardCount  \n$dueCardCount to review today"
            )
        }
    }
}