package com.example.cyrusflashcards.ui.screens

import android.app.Application
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.cyrusflashcards.CyrusUiState
import com.example.cyrusflashcards.CyrusViewModel
import com.example.cyrusflashcards.CyrusViewModelFactory
import com.example.cyrusflashcards.data.CyrusCard
import com.example.cyrusflashcards.data.CyrusDeck
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.apache.poi.ss.usermodel.WorkbookFactory
import java.io.InputStream

//^^^^^^^^^^^^^^^^^^Uses 2 ViewModel methods: getDeckById,  deleteCurrentDeck^^^^^^^^^^^^^^^^^^^^

@Composable
fun DeckScreen(
    navController: NavController,
    viewModel: CyrusViewModel,
) {

    val uiState by viewModel.uiState.collectAsState()
    val currentDeckId: String? = uiState.currentDeckId?.toString()

    var currentDeckName by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()



    LaunchedEffect(uiState.currentDeckId) {
        uiState.currentDeckId?.let { deckId ->
            scope.launch {
                viewModel.getDeckById(deckId).collect { deck ->
                    currentDeckName = deck.name
                }
            }
        }
    }

//URI = Uniform Resource Identifier, a String that can identify a file
    var selectedFileUri by remember { mutableStateOf<Uri?>(null) }
    val context = LocalContext.current

    //file picker launcher will be used to select a file from the phone's storage
    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        selectedFileUri = uri
    }




    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        currentDeckName?.let {
            Text(it,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold

                )
        } ?: run {
            Text("No Deck Selected")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { navController.navigate("prompt") }
        ) {
            Text("Review Cards")
        }
        Button(
            onClick = { navController.navigate("create_card") }
        ) {
            Text("Add a Card")
        }



        Button(
            onClick = {
                Log.d("DeckScreen", "Delete button clicked")
                viewModel.deleteCurrentDeck()
                navController.popBackStack()
            }
        ) {
            Text("Delete Deck")
        }
//        Spacer(modifier = Modifier.height(16.dp))

        // Button to launch the file picker
        Button(onClick = { filePickerLauncher.launch("*/*") }) {
            Text("Upload File")
        }

        // If a file is selected, display its path and provide a button to process it
        selectedFileUri?.let { uri ->
            Text(text = "Selected file: ${uri.path}")

            Button(
                onClick = {
                    uri.let {
                        val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
                        // Handle the file input stream
                        inputStream?.let { stream ->
                            val fileType = context.contentResolver.getType(uri)
                            handleFileUpload(
                                stream,
                                fileType,
                                viewModel,
                                uiState.currentDeckId ?: 0
                            )
                        }
                    }
                },
//                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Process File")
            }
        }
        Button(
            onClick = { navController.navigate("deck_select") }
        ) {
            Text("Home")
        }
    }
}

    //function takes the input stream of the file and the file type, checks if it is an excel file
//and if so, uses the readExcelFile function below to change the file into cards.
    fun handleFileUpload(
        inputStream: InputStream,
        fileType: String?,
        viewModel: CyrusViewModel,
        deckId: Int
    ) {
        val cards: List<CyrusCard> =
            if (fileType == "application/vnd.ms-excel" || fileType == "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet") {
                readExcelFile(inputStream, deckId)
            } else {
                emptyList()
            }
        viewModel.bulkInsertCards(cards)
    }

    //Function turns input stream into a list of cards. by turning it into a workbook and cycling through
//the rows
    fun readExcelFile(inputStream: InputStream, deckId: Int): List<CyrusCard> {
        val workbook = WorkbookFactory.create(inputStream)
        val sheet = workbook.getSheetAt(0)
        val cards = mutableListOf<CyrusCard>()

        for (row in sheet) {
            val name = row.getCell(0).stringCellValue
            val imageURL = row.getCell(1).stringCellValue
            val card = CyrusCard(deckId = deckId, name = name, imageURL = imageURL)
            cards.add(card)
        }

        workbook.close()
        return cards
    }

