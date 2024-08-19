package com.example.cyrusflashcards.ui.screens



import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
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
import androidx.compose.ui.semantics.Role.Companion.Image
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.cyrusflashcards.CyrusViewModel
import com.example.cyrusflashcards.R
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.cyrusflashcards.Authentification.AuthenticationViewModel
import com.example.cyrusflashcards.SyncViewModel


@Composable
fun LoginScreen(
    navController: NavController,
    authenticationViewModel: AuthenticationViewModel = hiltViewModel(),
    synchViewModel: SyncViewModel = hiltViewModel()
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        val email by authenticationViewModel.email.collectAsState()
        val password by authenticationViewModel.password.collectAsState()
        val isAuthenticated by authenticationViewModel.isAuthenticated.collectAsState()

        Image(
            painter =painterResource(id = R.drawable.achaemenid_falcon_svg),
            contentDescription = "persian eagle"
        )
        Spacer (modifier = Modifier.height(16.dp))
        Text("Welcome",
            fontSize = 40.sp,
            fontWeight = FontWeight.Bold ,


            )
        Spacer (modifier = Modifier.height(16.dp))

        TextField(
            value = email,
            onValueChange = { authenticationViewModel.changeEmail(it) },
            label = { Text("Enter email address:") }
        )
        Spacer (modifier = Modifier.height(8.dp))

        TextField(
            value = password,
            onValueChange = { authenticationViewModel.changePassword(it) },
            label = { Text("Enter password:") }
        )

        Spacer (modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                authenticationViewModel.signIn()
            }
        ) {
            Text("Log In")
        }

        // Only navigate if authentication is successful- seperate from button which logs in
        if (isAuthenticated) {
            LaunchedEffect(isAuthenticated) {
//                synchViewModel.syncSupabaseToLocal()
                navController.navigate("select_deck")
            }
        }

        Button(
            onClick = {
                navController.navigate("signup")
            }
        ) {
            Text("Create Account")
        }







    }
}




//
//@Composable
//fun LoginScreen(
//    navController: NavController,
//    viewModel: AuthenticationViewModel = hiltViewModel()
//) {
//    Column(
//        modifier = Modifier
//            .fillMaxSize(),
//        horizontalAlignment = Alignment.CenterHorizontally,
//        verticalArrangement = Arrangement.Center
//    ) {
//
//        // Collect the email and password states
//        val email by viewModel.email.collectAsState(initial = "")
//        val password by viewModel.password.collectAsState()
//
//        // Debugging logs to ensure state is being captured
//        androidx.compose.runtime.LaunchedEffect(email) {
//            android.util.Log.d("LoginScreen", "Current email in UI: $email")
//        }
//
//        // Icon that can later be replaced by an image
//        Image(
//            painter = painterResource(id = R.drawable.achaemenid_falcon_svg),
//            contentDescription = "persian eagle"
//        )
//        Spacer(modifier = Modifier.height(16.dp))
//
//        Text(
//            "Welcome",
//            fontSize = 40.sp,
//            fontWeight = FontWeight.Bold
//        )
//        Spacer(modifier = Modifier.height(16.dp))
//
//        // Email TextField
//        TextField(
//            value = email,
//            onValueChange = { viewModel.changeEmail(it) },
//            label = { Text("Enter email address:") }
//        )
//        Spacer(modifier = Modifier.height(16.dp))
//
//        // Password TextField
//        TextField(
//            value = password,
//            onValueChange = { viewModel.changePassword(it) },
//            label = { Text("Enter password") }
//        )
//        Spacer(modifier = Modifier.height(16.dp))
//
//        // Login Button
//        Button(
//            onClick = {
//                viewModel.signIn()
//            }
//        ) {
//            Text("Log In")
//        }
//
//        // Sign-Up Button
//        Button(
//            onClick = {
//                navController.navigate("signup")
//            }
//        ) {
//            Text("Create Account")
//        }
//
//        // Observe the email to navigate to the home screen after successful login
//        androidx.compose.runtime.LaunchedEffect(viewModel.email) {
//            viewModel.email.collect { currentEmail ->
//                if (currentEmail.isNotEmpty()) {
//                    navController.navigate("home")
//                }
//            }
//        }
//    }
//}

//
//@Composable
//fun LoginScreen (
//    navController: NavController,
//    viewModel: AuthenticationViewModel = hiltViewModel()
//
//){
//
//    Column (
//        modifier = Modifier
//            .fillMaxSize(),
//        horizontalAlignment = Alignment.CenterHorizontally,
//        verticalArrangement = Arrangement.Center
//
//    ) {
//
//        val email = viewModel.email.collectAsState(initial = "")
//        val password = viewModel.password.collectAsState()
//
//        //icon that can later be replaced by an image
//        Image(
//            painter =painterResource(id = R.drawable.achaemenid_falcon_svg),
//            contentDescription = "persian eagle"
//        )
//        Spacer (modifier = Modifier.height(16.dp))
//        Text("Welcome",
//            fontSize = 40.sp,
//            fontWeight = FontWeight.Bold ,
//
//
//            )
//        Spacer (modifier = Modifier.height(16.dp))
//
//        TextField(
//            value = email.value,
//            onValueChange = {viewModel.changeEmail(it)},
//            label = { Text("Enter email address:") }
//        )
//        Spacer (modifier = Modifier.height(16.dp))
//        TextField(
//            value = password.value,
//            onValueChange = {viewModel.changePassword(it)},
//            label = { Text("Enter password") }
//        )
//        Spacer (modifier = Modifier.height(16.dp))
//        //Button to create the card
//        Button (
//            onClick = {
//                viewModel.signIn()
//                navController.navigate("home")
//
//
//            }
//        ) {
//            Text("Log In")
//        }
//        Button (
//            onClick = {
//                navController.navigate("signup")
//
//
//            }
//        ) {
//            Text("Create Account")
//        }
//
//    }
