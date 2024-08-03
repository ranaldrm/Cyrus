package com.example.cyrusflashcards.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.cyrusflashcards.CyrusViewModel

@Composable
fun SettingsScreen(
    navController: NavController,
    viewModel: CyrusViewModel
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Settings")
            Button(
                onClick = { viewModel.deleteAllDescks() }
            ) {
                Text("Delete All Decks")
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "Use SM-2 algorithm for repetition")

            SwitchForAlg(viewModel)
        }
    }

}



@Composable
fun SwitchForAlg(viewModel: CyrusViewModel) {
    var checked by remember { mutableStateOf(true) }

    Switch(
        checked = checked,
        onCheckedChange = {
            checked = it
            viewModel.toggleAlgorithm(it)
        }
    )
}






//{
//    Column {
//        Text (text = "Settings")
//        Button(
//            onClick ={ viewModel.deleteAllDescks()}
//        ) {
//            Text("Delete All Decks")
//        }
//    }
//
//
//}