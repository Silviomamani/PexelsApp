package com.silviomamani.pexelsapp.ui.screens.register

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class RegisterScreenViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState = _uiState.asStateFlow()

    private val _uiEvent = Channel<String>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onEmailChange(email: String) {
        _uiState.value = _uiState.value.copy(email = email)
    }

    fun onPasswordChange(password: String) {
        _uiState.value = _uiState.value.copy(password = password)
    }

    fun onConfirmPasswordChange(confirmPassword: String) {
        _uiState.value = _uiState.value.copy(confirmPassword = confirmPassword)
    }

    fun onNameChange(name: String) {
        _uiState.value = _uiState.value.copy(name = name)
    }

    fun onRegisterClick() {
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

        if (currentState.password.length < 6) {
            viewModelScope.launch {
                _uiEvent.send("Error: La contraseña debe tener al menos 6 caracteres")
            }
            return
        }

        if (currentState.password != currentState.confirmPassword) {
            viewModelScope.launch {
                _uiEvent.send("Error: Las contraseñas no coinciden")
            }
            return
        }

        if (currentState.name.isBlank()) {
            viewModelScope.launch {
                _uiEvent.send("Error: El nombre es requerido")
            }
            return
        }

        // Proceder con el registro
        registerUser(currentState.email, currentState.password, currentState.name)
    }

    private fun registerUser(email: String, password: String, name: String) {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true)

                // Crear usuario con Firebase Auth
                val result = FirebaseAuth.getInstance()
                    .createUserWithEmailAndPassword(email, password)
                    .await()

                val user = result.user
                if (user != null) {
                    // Actualizar el perfil del usuario con el nombre
                    val profileUpdates = UserProfileChangeRequest.Builder()
                        .setDisplayName(name)
                        .build()

                    user.updateProfile(profileUpdates).await()

                    // Guardar información adicional en Firestore
                    saveUserToFirestore(user.uid, name, email)

                    _uiEvent.send("RegisterOK")
                } else {
                    _uiEvent.send("Error: No se pudo crear la cuenta")
                }
            } catch (e: FirebaseAuthWeakPasswordException) {
                _uiEvent.send("Error: La contraseña es muy débil")
            } catch (e: FirebaseAuthInvalidCredentialsException) {
                _uiEvent.send("Error: El email no es válido")
            } catch (e: FirebaseAuthUserCollisionException) {
                _uiEvent.send("Error: Ya existe una cuenta con este email")
            } catch (e: Exception) {
                _uiEvent.send("Error: ${e.message}")
            } finally {
                _uiState.value = _uiState.value.copy(isLoading = false)
            }
        }
    }

    private suspend fun saveUserToFirestore(uid: String, name: String, email: String) {
        try {
            val userDocument = hashMapOf(
                "name" to name,
                "email" to email,
                "createdAt" to System.currentTimeMillis()
            )

            FirebaseFirestore.getInstance()
                .collection("cuentas")
                .document(uid)
                .set(userDocument)
                .await()
        } catch (e: Exception) {
            // Log del error pero no interrumpe el flujo de registro
            Log.e("RegisterViewModel", "Error saving user to Firestore", e)
        }
    }
}

data class RegisterUiState(
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val name: String = "",
    val isLoading: Boolean = false
) {
}