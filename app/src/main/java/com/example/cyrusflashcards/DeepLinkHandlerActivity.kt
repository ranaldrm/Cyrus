package com.example.cyrusflashcards

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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

        // Handle the deep link intent
        supabaseClient.handleDeeplinks(intent = intent,
            onSessionSuccess = { userSession ->
                userSession.user?.let { user ->
                    Log.d("LOGIN", "Log in successfully with user info: $user")
                    navigateToMainApp()
                } ?: run {
                    Log.e("LOGIN", "User session is null or invalid")
                    // Handle the failure scenario here
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
        // Optionally, navigate to a login screen or show an error message
        Toast.makeText(this, "Failed to log in. Please try again.", Toast.LENGTH_LONG).show()
        finish()  // Finish the activity, or redirect to a different activity as needed
    }
}