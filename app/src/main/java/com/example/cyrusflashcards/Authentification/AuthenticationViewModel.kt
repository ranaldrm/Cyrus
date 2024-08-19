package com.example.cyrusflashcards.Authentification

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthenticationViewModel @Inject constructor(
    private val authenticationRepository: AuthenticationRepository
) : ViewModel() {

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password

    private val _isAuthenticated = MutableStateFlow(false)
    val isAuthenticated: StateFlow<Boolean> = _isAuthenticated

    private val _signUpSuccess = MutableStateFlow(false)
    val signUpSuccess: StateFlow<Boolean> = _signUpSuccess

    fun changeEmail(email: String) {
        Log.d("AuthenticationViewModel", "Email changed: $email")
        _email.value = email
    }

    fun changePassword(password: String) {
        Log.d("AuthenticationViewModel", "Password changed")
        _password.value = password
    }

    fun signIn() {
        viewModelScope.launch {
            val signInSuccessful = authenticationRepository.signIn(
                email = _email.value,
                password = _password.value
            )
            if (signInSuccessful) {
                _isAuthenticated.value = true
                // Explicitly set the email in the state flow
                _email.value = _email.value // Ensure _email is reassigned
                Log.d("AuthenticationViewModel", "User email set to: ${_email.value}")
            } else {
                _isAuthenticated.value = false
                Log.e("AuthenticationViewModel", "Sign in failed")
            }
        }
    }


//    fun signIn() {
//        viewModelScope.launch {
//            val signInSuccessful = authenticationRepository.signIn(
//                email = _email.value,
//                password = _password.value
//            )
//            if (signInSuccessful) {
//                _isAuthenticated.value = true
//                Log.d("AuthenticationViewModel", "User email set to: $email")
//            } else {
//                _isAuthenticated.value = false
//                Log.e("AuthenticationViewModel", "Sign in failed")
//            }
//        }
//    }

    fun signUp() {
        viewModelScope.launch {
            val signUpSuccessful = authenticationRepository.signUp(
                email = _email.value,
                password = _password.value
            )
            if (signUpSuccessful) {
                _isAuthenticated.value = true
            } else {
                _isAuthenticated.value = false
                Log.e("AuthenticationViewModel", "Sign up failed")
            }
        }
    }

    private fun fetchCurrentUserEmail() {
        viewModelScope.launch {
            val currentEmail = authenticationRepository.getCurrentUserEmail()
            if (currentEmail != null) {
                _email.value = currentEmail
                Log.d("AuthenticationViewModel", "User email set to: $currentEmail")
            } else {
                Log.e("AuthenticationViewModel", "Failed to fetch current user email")
            }
        }
    }

    // Call this method whenever you need the email, like when initializing the ViewModel
    fun initialize() {
        fetchCurrentUserEmail()
    }


}

//    fun signUp() {
//        Log.d("AuthenticationViewModel", "Attempting sign up with email: ${_email.value}")
//        viewModelScope.launch {
//            val signUpSuccessful = authenticationRepository.signUp(
//                email = _email.value,
//                password = _password.value
//            )
//            if (signUpSuccessful) {
//                Log.d("AuthenticationViewModel", "Sign up successful")
//            } else {
//                Log.e("AuthenticationViewModel", "Sign up failed")
//            }
//        }
//    }
//
//    fun signIn() {
//        viewModelScope.launch {
//            Log.d("AuthenticationViewModel", "Attempting sign in with email: ${_email.value}")
//            val signInSuccessful =  authenticationRepository.signIn(
//                email = _email.value,
//                password = _password.value
//            )
//            if (signInSuccessful) {
//                Log.d("AuthenticationViewModel", "Sign in successful")
//            } else {
//                Log.e("AuthenticationViewModel", "Sign in failed")
//            }
//        }
//    }


