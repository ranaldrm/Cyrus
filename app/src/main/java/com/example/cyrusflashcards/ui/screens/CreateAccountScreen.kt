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
import androidx.navigation.NavController
import com.example.cyrusflashcards.CyrusViewModel
import com.example.cyrusflashcards.R

@Composable
fun CreateAccountScreen (navController: NavController, viewModel: CyrusViewModel){
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    Column (
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center

    ) {


        Spacer (modifier = Modifier.height(16.dp))
        Text("Create an Account",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer (modifier = Modifier.height(16.dp))

        TextField(
            value = username,
            onValueChange = {password = it},
            label = { Text("Enter a name") }
        )
        Spacer (modifier = Modifier.height(16.dp))
        TextField(
            value = username,
            onValueChange = {password = it},
            label = { Text("Enter a password") }
        )

        Spacer (modifier = Modifier.height(16.dp))
        Button (
            onClick = {


            }
        ) {
            Text("Create Account")
        }

    }
}