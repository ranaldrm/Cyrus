package com.example.cyrusflashcards.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.cyrusflashcards.Authentification.AuthenticationViewModel
import com.example.cyrusflashcards.CyrusViewModel
import com.example.cyrusflashcards.R



@Composable
fun CreateAccountScreen(
    navController: NavController,
    viewModel: AuthenticationViewModel = hiltViewModel()
) {

    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        val email by viewModel.email.collectAsState(initial = "")
        val password by viewModel.password.collectAsState()
        val signUpSuccess by viewModel.signUpSuccess.collectAsState(initial = false)

        Spacer(modifier = Modifier.height(16.dp))
        Text(
            "Sign Up",
            fontSize = 40.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = email,
            onValueChange = { viewModel.changeEmail(it) },
            label = { Text("Enter email address:") }
        )
        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = password,
            onValueChange = { viewModel.changePassword(it) },
            label = { Text("Enter password:") }
        )
        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                viewModel.signUp()
            }
        ) {
            Text("Sign Up")
        }

        // Observe the sign-up success and navigate to the login screen if successful
        if (signUpSuccess) {
            LaunchedEffect(Unit) {
                navController.navigate("login")
            }
        }
    }
}




