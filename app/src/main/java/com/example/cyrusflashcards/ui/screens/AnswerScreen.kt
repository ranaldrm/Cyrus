package com.example.cyrusflashcards.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
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
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.example.cyrusflashcards.CyrusHiltViewModel
import com.example.cyrusflashcards.CyrusViewModel
import com.example.cyrusflashcards.navigateAndAdvance
import kotlinx.coroutines.delay

//^^^^^^^^^^^^^^^^^^Uses 4 ViewModel methods: getCurrentCard, getDeckFinished, advanceCard, deleteCurrentCard^^^^^^^^^^^^^^^^^^^^
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AnswerScreen(
    navController: NavController,
    viewModel: CyrusHiltViewModel = hiltViewModel()
) {

    val currentCard by viewModel.getCurrentCard().collectAsState(initial = null)
    var shouldAdvanceCard by remember { mutableStateOf(false) }

    var difficultyLevel by remember { mutableStateOf(3) } // Track the slider value


    //this delays before advancing the card so that it does not show the answer before navigating
    LaunchedEffect(shouldAdvanceCard) {
        if (shouldAdvanceCard) {
            //make sure that difficulty is deleted from 6 to get the qfactor
            currentCard?.let { viewModel.updateCardeFactorAndInterval(it, 6- difficultyLevel) }
            delay(300)
            viewModel.advanceCard()
            shouldAdvanceCard = false
        }
    }


    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
//show imgURL or "no card Selected if something went wrong and there is no card
//        Text(currentCard?.imageURL ?: "No Card Selected")
        Spacer(modifier = Modifier.height(16.dp))
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
        Spacer(modifier = Modifier.height(16.dp))
        Text(currentCard?.name ?: "No Card Selected")
        Spacer(modifier = Modifier.height(16.dp))



        DifficultySlider(difficultyLevel) { newLevel ->
            difficultyLevel = newLevel
        }

        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                val canAdvance = viewModel.checkCanAdvance()
                if (canAdvance) {
                    navController.navigate("prompt")
                    shouldAdvanceCard = true






                } else {
                    navController.navigate("finished")
                }
            }
        ) {
            Text("Next")
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
            Text("Delete")
        }
    }

}

@Composable
fun DifficultySlider(difficultyLevel: Int, onDifficultyChange: (Int) -> Unit) {

    //remember that SM-2 q-factor uses higher numbers to represent higherquality response,
    //get q-factor by 5 - difficultyLevel
     // Using Int instead of Float
    Column (
        modifier = Modifier.padding(horizontal = 23.dp)
    ) {
        Slider(
            value = difficultyLevel.toFloat(),  // Convert from interger to Float for the Slider

            // Convert back to Int and take function to change main composable's difficulty value
            // as a function
            onValueChange = { newValue -> onDifficultyChange(newValue.toInt()) },
            steps = 6,
            valueRange = 0f..5f  // The value range still needs to be Float

        )

        Text(text = "Difficulty: $difficultyLevel")
    }
}