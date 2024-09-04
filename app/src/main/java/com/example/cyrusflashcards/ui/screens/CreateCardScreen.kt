package com.example.cyrusflashcards.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import com.example.cyrusflashcards.CyrusHiltViewModel
import com.example.cyrusflashcards.CyrusViewModel

@Composable
fun CreateCardScreen(
    navController: NavController,
    viewModel: CyrusHiltViewModel = hiltViewModel()
) {
    var nameText by remember { mutableStateOf("") }
    var imageURL by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }

    // Image picker launcher
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? ->
            imageUri = uri

            imageURL = ""
        }
    )

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Display selected image or fallback to icon
        if (imageUri != null) {
            Image(
                painter = rememberAsyncImagePainter(model = imageUri),
                contentDescription = "Selected Image",
                modifier = Modifier.size(100.dp)
            )
        } else {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "person",
                modifier = Modifier.size(100.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            "Create a student",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = nameText,
            onValueChange = { nameText = it },
            label = { Text("Enter a name") }
        )

        Spacer(modifier = Modifier.height(16.dp))

//        TextField(
//            value = imageURL,
//            onValueChange = { imageURL = it },
//            label = { Text("Enter a URL for the image") }
//        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                imagePickerLauncher.launch("image/*")
            }
        ) {
            Text("Upload Image")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                // Call createCard with both URL and URI
                val currentDeckID = viewModel.getCurrentDeckId()
                viewModel.createCard(currentDeckID, nameText, imageUri)
                navController.popBackStack()
            }
        ) {
            Text("Create Student")
        }
    }
}

