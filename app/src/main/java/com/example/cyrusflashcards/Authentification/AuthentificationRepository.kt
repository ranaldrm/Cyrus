package com.example.cyrusflashcards.Authentification

import android.util.Log
import io.github.jan.supabase.gotrue.Auth
import javax.inject.Inject

import io.github.jan.supabase.gotrue.providers.Google
import io.github.jan.supabase.gotrue.providers.builtin.Email

class AuthenticationRepository @Inject constructor(
    private val auth: Auth
)  {
    suspend fun signIn(email: String, password: String): Boolean {
        return try {
            Log.d("AuthenticationRepository", "Attempting to sign in with email: $email")
            auth.signInWith(Email) {
                this.email = email
                this.password = password
            }
            Log.d("AuthenticationRepository", "Sign in successful for email: $email")
            true
        } catch (e: Exception) {
            Log.e("AuthenticationRepository", "Sign in failed for email: $email", e)
            false
        }
    }

    suspend fun signUp(email: String, password: String): Boolean {
        return try {
            Log.d("AuthenticationRepository", "Attempting to sign up with email: $email")
            auth.signUpWith(Email) {
                this.email = email
                this.password = password
            }
            Log.d("AuthenticationRepository", "Sign up successful for email: $email")
            true
        } catch (e: Exception) {
            Log.e("AuthenticationRepository", "Sign up failed for email: $email", e)
            false
        }
    }

    suspend fun getCurrentUserEmail(): String? {
        return try {
            val session = auth.currentSessionOrNull()
            session?.user?.email
        } catch (e: Exception) {
            Log.e("AuthenticationRepository", "Failed to fetch current user email", e)
            null
        }
    }


}