package com.silviomamani.pexelsapp.ui.screens.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.google.android.gms.auth.api.signin.GoogleSignInAccount

import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

import kotlinx.coroutines.channels.Channel

import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

import com.google.android.gms.tasks.Task


class LoginScreenViewModel : ViewModel() {

    private val _uiEvent = Channel<String>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        checkAuthStatus()
    }

    private fun checkAuthStatus() {
        if (FirebaseAuth.getInstance().currentUser != null) {
            viewModelScope.launch {
                _uiEvent.send("LoginOK")
            }
        }
    }

    fun onGoogleLoginClick() {
        viewModelScope.launch {
            _uiEvent.send("StartGoogleSignIn")
        }
    }

    fun handleGoogleSignInResult(task: Task<GoogleSignInAccount>) {
        viewModelScope.launch {
            try {
                val account = task.getResult(ApiException::class.java)
                val credential = GoogleAuthProvider.getCredential(account.idToken, null)
                val result = FirebaseAuth.getInstance().signInWithCredential(credential).await()

                if (result.user != null) {
                    _uiEvent.send("LoginOK")
                } else {
                    _uiEvent.send("Error: No se pudo iniciar sesión")
                }
            } catch (e: ApiException) {
                _uiEvent.send("Error: ${e.message}")
            } catch (e: Exception) {
                _uiEvent.send("Error: ${e.message}")
            }
        }
    }
}