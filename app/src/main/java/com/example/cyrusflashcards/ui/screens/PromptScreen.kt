package com.example.cyrusflashcards.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import com.example.cyrusflashcards.CyrusHiltViewModel
import com.example.cyrusflashcards.CyrusViewModel

//^^^^^^^^^^^^^^^^^^Uses 1 ViewModel methods: getCurrentCard^^^^^^^^^^^^^^^^^^^^
@Composable
fun PromptScreen (
    navController: NavController,
    viewModel: CyrusHiltViewModel = hiltViewModel()

) {
//    val currentDeck by viewModel.getCurrentDeck().collectAsState(initial = null)
    val currentCard by viewModel.getCurrentCard().collectAsState(initial = null)




    Column (
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Spacer (modifier = Modifier.height(16.dp))
//        Text(
//            text = "Review Card",
//
//            modifier = Modifier.padding(16.dp)
//        )
        Spacer (modifier = Modifier.height(16.dp))
//        Icon(
//            imageVector = Icons.Default.Person,
//            contentDescription = "person",
//            modifier = Modifier.size(100.dp)
//        )
        Image(
            painter = rememberAsyncImagePainter(model = currentCard?.imageURL),
            contentDescription = null,
            modifier = Modifier
                .height(200.dp),
            contentScale = ContentScale.Crop
        )
        Spacer (modifier = Modifier.height(16.dp))
        //show imgURL or "no card Selected if something went wrong and there is no card
//        Text(currentCard?.imageURL ?: "No Card Selected")


        //wrapping for nullable - can I get rid of this?
//        currentCard?.let { Text(it.imageURL) }

        Button(
            onClick ={ navController.navigate("answer")}
        ) {
            Text("Answer")
        }



    }



}