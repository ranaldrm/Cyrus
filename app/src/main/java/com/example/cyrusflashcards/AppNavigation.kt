package com.example.cyrusflashcards

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.cyrusflashcards.ui.screens.AnswerScreen
import com.example.cyrusflashcards.ui.screens.CreateCardScreen
import com.example.cyrusflashcards.ui.screens.CreateDeckScreen
import com.example.cyrusflashcards.ui.screens.DeckFinishedScreen
import com.example.cyrusflashcards.ui.screens.DeckScreen
import com.example.cyrusflashcards.ui.screens.HomeScreen
import com.example.cyrusflashcards.ui.screens.PromptScreen

import com.example.cyrusflashcards.ui.screens.SelectDeckScreen
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.cyrusflashcards.ui.screens.CreateAccountScreen
import com.example.cyrusflashcards.ui.screens.LoginScreen
import com.example.cyrusflashcards.ui.screens.SettingsScreen

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigation() {
    val context = LocalContext.current
    val application = context.applicationContext as Application

    // Use the ViewModelFactory to obtain an instance of the ViewModel
    val viewModel: CyrusViewModel = viewModel(factory = CyrusViewModelFactory(application))

    // Create a NavController to move around the app
    val navController = rememberNavController()

    Scaffold(
        topBar = {
            val currentBackStackEntry = navController.currentBackStackEntryAsState().value


            val currentRoute = currentBackStackEntry?.destination?.route
            val canGoToSettings = currentRoute != "login"
            val canNavigateBack = currentRoute != "login" && currentRoute != "home" && currentRoute != "prompt" && currentRoute != "answer"
            CyrusAppBar(
                canNavigateBack = canNavigateBack,
                navigateUp = { navController.navigateUp() },
                canGoToSettings = canGoToSettings,
                navigateToSettings = { navController.navigate("settings") }
            )

        },
        content = { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = "login",
                modifier = Modifier.padding(innerPadding)
            ) {
                composable("settings") {
                    SettingsScreen(navController, viewModel)
                }


                composable("login") {
                    LoginScreen(navController, viewModel)
                }

                composable("create") {
                    CreateAccountScreen(navController, viewModel)
                }
                composable("home") {
                    HomeScreen(navController, viewModel)
                }


                composable("create_card") {
                    CreateCardScreen(navController, viewModel)
                }
                composable("prompt") {
                    PromptScreen(navController, viewModel)
                }
                composable("deck_select") {
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
    )
}

//used in answer screen so that it can advance to the next card without revealing the next answer

fun NavController.navigateAndAdvance(route: String, viewModel: CyrusViewModel) {
    this.navigate(route)
    viewModel.advanceCard()
}






//
//@Composable
//fun AppNavigation (){
//    val context = LocalContext.current
//    val application = context.applicationContext as Application
//
//    // Use the ViewModelFactory to obtain an instance of the ViewModel
//    val viewModel: CyrusViewModel = viewModel(factory = CyrusViewModelFactory(application))
//
//
//
//
//    //create a navController to move round app
//    val navController = rememberNavController()
//
//
//
//    //Set up a NavHost to set out routes for each screen (represented as Strings)
//    //pass the route for the HomeScreen as the parameter for startDestination
//    NavHost(navController = navController, startDestination = "home"){
//        composable("home"){
//            HomeScreen(navController, viewModel)
//        }
//        composable("create_card"){
//            CreateCardScreen(navController, viewModel)
//        }
//        composable("prompt"){
//            PromptScreen(navController, viewModel)
//        }
//        composable("deck_select"){
//            SelectDeckScreen(navController, viewModel)
//        }
//        composable("deck") {
//            DeckScreen(navController, viewModel)
//        }
//        composable("create_deck") {
//            CreateDeckScreen(navController, viewModel)
//        }
//        composable("answer") {
//            AnswerScreen(navController, viewModel)
//        }
//        composable("finished") {
//            DeckFinishedScreen(navController, viewModel)
//        }
//    }
//}