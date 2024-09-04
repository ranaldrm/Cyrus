package com.example.cyrusflashcards

import android.app.Application
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.cyrusflashcards.Authentification.AuthenticationViewModel
import com.example.cyrusflashcards.ui.screens.CreateAccountScreen
import com.example.cyrusflashcards.ui.screens.HomeScreen
import com.example.cyrusflashcards.ui.screens.LoginScreen
import com.example.cyrusflashcards.ui.screens.SettingsScreen
import androidx.compose.runtime.LaunchedEffect
import com.example.cyrusflashcards.ui.screens.AnswerScreen
import com.example.cyrusflashcards.ui.screens.ChangeDeckScreen
import com.example.cyrusflashcards.ui.screens.CreateCardScreen
import com.example.cyrusflashcards.ui.screens.CreateDeckScreen
import com.example.cyrusflashcards.ui.screens.DeckFinishedScreen
import com.example.cyrusflashcards.ui.screens.DeckScreen
import com.example.cyrusflashcards.ui.screens.PromptScreen
import com.example.cyrusflashcards.ui.screens.SelectDeckScreen

//new version of navigation with hilt
//
@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CyrusAppNavigation(
    viewModel: AuthenticationViewModel = hiltViewModel()
) {
    val navController = rememberNavController()
    val userEmail by viewModel.email.collectAsState(initial = "")

    Log.d("CyrusAppNavigation", "Current email: $userEmail"





    )

    Scaffold(
        topBar = {
            val currentBackStackEntry = navController.currentBackStackEntryAsState().value
            val currentRoute = currentBackStackEntry?.destination?.route
            val canGoToSettings = currentRoute != "login"
            val canNavigateBack = currentRoute != "login" && currentRoute != "prompt" && currentRoute != "answer"

            if (userEmail.isNotEmpty()) {
                CyrusAppBar(
                    canNavigateBack = canNavigateBack,
                    navigateUp = { navController.navigateUp() },
                    canGoToSettings = canGoToSettings,
                    navigateToSettings = { navController.navigate("settings") },
                    userEmail = ""
                )
            } else {
                CyrusAppBar(
                    canNavigateBack = canNavigateBack,
                    navigateUp = { navController.navigateUp() },
                    canGoToSettings = canGoToSettings,
                    navigateToSettings = { navController.navigate("settings") },
                    userEmail = "Cannot get user email"
                )

            }

//            CyrusAppBar(
//                canNavigateBack = canNavigateBack,
//                navigateUp = { navController.navigateUp() },
//                canGoToSettings = canGoToSettings,
//                navigateToSettings = { navController.navigate("settings") },
//                userEmail = userEmail
//            )
        },
        content = { innerPadding ->
            NavHost(
                navController,
                startDestination = "login",
                modifier = Modifier.padding(innerPadding)
            ) {
                composable("login") {
                    LoginScreen(navController)
                }
                composable("signup") {
                    CreateAccountScreen(navController)
                }
                composable("home") {
                    HomeScreen(navController)
                }
                composable("select_deck") {
                    SelectDeckScreen(navController)
                }

                composable("deck_select") {
                    SelectDeckScreen(navController)
                }

                composable("settings") {
                    SettingsScreen(navController)
                }

                composable("change_deck") {
                    ChangeDeckScreen(navController)
                }




                composable("create") {
                    CreateAccountScreen(navController)
                }
                composable("home") {
                    HomeScreen(navController)
                }


                composable("create_card") {
                    CreateCardScreen(navController)
                }
                composable("prompt") {
                    PromptScreen(navController)
                }

                composable("deck") {
                    DeckScreen(navController)
                }
                composable("create_deck") {
                    CreateDeckScreen(navController)
                }
                composable("answer") {
                    AnswerScreen(navController)
                }
                composable("finished") {
                    DeckFinishedScreen(navController)
                }



//                composable("settings") {
//                    SettingsScreen(navController)
//                }
            }
        }
    )
}




//used in answer screen so that it can advance to the next card without revealing the next answer

    fun NavController.navigateAndAdvance(route: String, viewModel: CyrusViewModel) {
        this.navigate(route)
        viewModel.advanceCard()
    }