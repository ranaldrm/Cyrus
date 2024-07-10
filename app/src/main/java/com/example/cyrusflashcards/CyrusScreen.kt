package com.example.cyrusflashcards

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.cyrusflashcards.ui.screens.CreateCardScreen
import com.example.cyrusflashcards.ui.screens.CreateDeckScreen
import com.example.cyrusflashcards.ui.screens.DeckScreen
import com.example.cyrusflashcards.ui.screens.HomeScreen
import com.example.cyrusflashcards.ui.screens.ReviewCardsScreen


@Composable
fun AppNavigation (){
    //create a navController to move round app
    val navController = rememberNavController()
    //Set up a NavHost to set out routes for each screen (represented as Strings)
    //pass the route for the HomeScreen as the parameter for startDestination
    NavHost(navController = navController, startDestination = "home"){
        composable("home"){
            HomeScreen(navController)
        }
        composable("create_card"){
            CreateCardScreen(navController)
        }
        composable("review"){
            ReviewCardsScreen(navController)
        }
//        composable("deck_select"){
//            SelectDeckScreen(navController)
//        }
        composable("deck") {
            DeckScreen(navController)
        }
        composable("create_deck") {
            CreateDeckScreen(navController)
        }
    }
}