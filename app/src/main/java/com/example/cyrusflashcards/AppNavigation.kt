package com.example.cyrusflashcards

import android.app.Application
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.cyrusflashcards.ui.screens.AnswerScreen
import com.example.cyrusflashcards.ui.screens.CreateCardScreen
import com.example.cyrusflashcards.ui.screens.CreateDeckScreen
import com.example.cyrusflashcards.ui.screens.DeckFinishedScreen
import com.example.cyrusflashcards.ui.screens.DeckScreen
import com.example.cyrusflashcards.ui.screens.HomeScreen
import com.example.cyrusflashcards.ui.screens.PromptScreen

import com.example.cyrusflashcards.ui.screens.SelectDeckScreen


@Composable
fun AppNavigation (){
    val context = LocalContext.current
    val application = context.applicationContext as Application

    // Use the ViewModelFactory to obtain an instance of the ViewModel
    val viewModel: CyrusViewModel = viewModel(factory = CyrusViewModelFactory(application))


    //do I need the uiState below?
//    val uiState by viewModel.uiState.collectAsState()

    //create a navController to move round app
    val navController = rememberNavController()
    //Set up a NavHost to set out routes for each screen (represented as Strings)
    //pass the route for the HomeScreen as the parameter for startDestination
    NavHost(navController = navController, startDestination = "home"){
        composable("home"){
            HomeScreen(navController)
        }
        composable("create_card"){
            CreateCardScreen(navController, viewModel)
        }
        composable("prompt"){
            PromptScreen(navController, viewModel)
        }
        composable("deck_select"){
            SelectDeckScreen(navController, viewModel)
        }
        composable("deck") {
            DeckScreen(navController, viewModel)
        }
        composable("create_deck") {
            CreateDeckScreen(navController, viewModel)
        }
        composable("answer") {
            AnswerScreen(navController, viewModel)
        }
        composable("finished") {
            DeckFinishedScreen(navController, viewModel)
        }
    }
}