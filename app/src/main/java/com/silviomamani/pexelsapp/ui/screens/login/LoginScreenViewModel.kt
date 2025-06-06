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
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class LoginScreenViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState = _uiState.asStateFlow()

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

    fun onEmailChange(email: String) {
        _uiState.value = _uiState.value.copy(email = email)
    }

    fun onPasswordChange(password: String) {
        _uiState.value = _uiState.value.copy(password = password)
    }

    fun onLoginClick() {
        val currentState = _uiState.value

        // Validaciones básicas
        if (currentState.email.isBlank()) {
            viewModelScope.launch {
                _uiEvent.send("Error: El email es requerido")
            }
            return
        }

        if (currentState.password.isBlank()) {
            viewModelScope.launch {
                _uiEvent.send("Error: La contraseña es requerida")
            }
            return
        }

        loginUser(currentState.email, currentState.password)
    }

    private fun loginUser(email: String, password: String) {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true)

                val result = FirebaseAuth.getInstance()
                    .signInWithEmailAndPassword(email, password)
                    .await()

                val user = result.user
                if (user != null) {
                    _uiEvent.send("LoginOK")
                } else {
                    _uiEvent.send("Error: Email o contraseña incorrecta")
                }
            } catch (e: FirebaseAuthInvalidUserException) {
                _uiEvent.send("Error: Email o contraseña incorrecta")
            } catch (e: FirebaseAuthInvalidCredentialsException) {
                _uiEvent.send("Error: Email o contraseña incorrecta")
            } catch (e: Exception) {
                _uiEvent.send("Error: Email o contraseña incorrecta")
            } finally {
                _uiState.value = _uiState.value.copy(isLoading = false)
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
                    _uiEvent.send("Error: No se pudo iniciar sesión con Google")
                }
            } catch (e: ApiException) {
                _uiEvent.send("Error: ${e.message}")
            } catch (e: Exception) {
                _uiEvent.send("Error: ${e.message}")
            }
        }
    }
}

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false
)