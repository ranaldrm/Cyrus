package com.example.cyrusflashcards

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController

import dagger.hilt.android.AndroidEntryPoint
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.gotrue.handleDeeplinks
import javax.inject.Inject

@AndroidEntryPoint
class DeepLinkHandlerActivity : ComponentActivity() {

    @Inject
    lateinit var supabaseClient: SupabaseClient



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Obtain the SyncViewModel using ViewModelProvider
        val syncViewModel: SyncViewModel by viewModels()

        // Handle the deep link intent
        supabaseClient.handleDeeplinks(intent = intent,
            onSessionSuccess = { userSession ->
                userSession.user?.let { user ->
                    Log.d("LOGIN", "Log in successfully with user info: $user")
                    syncViewModel.syncSupabaseToLocal()
                    navigateToMainApp()
                } ?: run {
                    Log.e("LOGIN", "Log in failure")
                    // Handle failure
                    handleFailure()
                }
            }
        )
    }

    private fun navigateToMainApp() {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        }
        startActivity(intent)
        finish()  // Finish the deep link handler activity to prevent returning to it
    }

    private fun handleFailure() {

        Toast.makeText(this, "Failed to log in..", Toast.LENGTH_LONG).show()
        finish()
    }
}