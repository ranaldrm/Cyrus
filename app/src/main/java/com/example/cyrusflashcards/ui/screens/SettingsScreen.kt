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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.cyrusflashcards.CyrusHiltViewModel
import com.example.cyrusflashcards.CyrusViewModel
import com.example.cyrusflashcards.SyncViewModel

@Composable
fun SettingsScreen(
    navController: NavController,
    viewModel: CyrusHiltViewModel = hiltViewModel(),
    syncViewModel: SyncViewModel = hiltViewModel()

) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Settings",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { navController.navigate("deck_select") }
            ) {
                Text("Home")
            }

            Button(
                    onClick = { viewModel.deleteAllDescks()
                    viewModel.deleteAllCards()}
                    ) {
                Text("Delete Local")
            }
            Button(
                onClick = { syncViewModel.syncLocalToSupabaseWithBucketManagement() }
            ) {
                Text("Sync Up")
            }
            Button(
                onClick = { syncViewModel.syncSupabaseToLocal() }
            ) {
                Text("Sync Down")
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "Use SM-2 algorithm")

            SwitchForAlg(viewModel)
            Text(text = "Shuffle on review")
            SwitchForShuffle(viewModel)

        }
    }

}



@Composable
fun SwitchForAlg(viewModel: CyrusHiltViewModel = viewModel()) {
    // Get the initial checked state from the ViewModel
    var checked by remember { mutableStateOf(viewModel.checkAlgorithm()) }

    Switch(
        checked = checked,
        onCheckedChange = { isChecked ->
            checked = isChecked
            viewModel.toggleAlgorithm(isChecked)
        }
    )
}

@Composable
fun SwitchForShuffle(viewModel: CyrusHiltViewModel = viewModel()) {
    // Get the initial checked state from the ViewModel
    var checked by remember { mutableStateOf(viewModel.checkShuffle()) }

    Switch(
        checked = checked,
        onCheckedChange = { isChecked ->
            checked = isChecked
            viewModel.toggleShuffle(isChecked)
        }
    )
}


//@Composable
//fun SwitchForAlg(viewModel: CyrusViewModel) {
//    // Check the current state from the ViewModel when the composable is first composed
//    val checked = remember { viewModel.checkAlgorithm() }
//
//    Switch(
//        checked = checked,
//        onCheckedChange = { viewModel.toggleAlgorithm(it) }
//    )
//}
//
//@Composable
//fun SwitchForShuffle(viewModel: CyrusViewModel) {
//    val checked = remember { viewModel.checkShuffle() }
//
//    Switch(
//        checked = checked,
//        onCheckedChange = {
//            viewModel.toggleShuffle(it)
//        }
//    )
//}






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