package com.silviomamani.pexelsapp.ui.screens.perfil

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PerfilScreenViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(PerfilState())
    val uiState: StateFlow<PerfilState> = _uiState.asStateFlow()

    private val firebaseAuth = FirebaseAuth.getInstance()
    private var sharedPreferences: SharedPreferences? = null

    init {
        loadUserData()
    }

    fun initSharedPreferences(context: Context) {
        sharedPreferences = context.getSharedPreferences("perfil_prefs", Context.MODE_PRIVATE)
        loadDisplayName()
    }

    private fun loadUserData() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            val currentUser = firebaseAuth.currentUser
            if (currentUser != null) {
                _uiState.value = _uiState.value.copy(
                    userPhotoUrl = currentUser.photoUrl?.toString(),
                    userName = currentUser.displayName ?: "",
                    userEmail = currentUser.email ?: "",
                    isLoading = false
                )
            } else {
                _uiState.value = _uiState.value.copy(isLoading = false)
            }
        }
    }

    private fun loadDisplayName() {
        val savedDisplayName = sharedPreferences?.getString("display_name", "") ?: ""
        _uiState.value = _uiState.value.copy(
            displayName = if (savedDisplayName.isNotEmpty()) savedDisplayName else _uiState.value.userName
        )
    }

    fun startEditingName() {
        _uiState.value = _uiState.value.copy(
            isEditingName = true,
            tempName = _uiState.value.displayName
        )
    }

    fun updateTempName(newName: String) {
        _uiState.value = _uiState.value.copy(tempName = newName)
    }

    fun saveDisplayName() {
        val newDisplayName = _uiState.value.tempName.trim()
        if (newDisplayName.isNotEmpty()) {
            // Guardar en SharedPreferences
            sharedPreferences?.edit()?.putString("display_name", newDisplayName)?.apply()

            _uiState.value = _uiState.value.copy(
                displayName = newDisplayName,
                isEditingName = false,
                tempName = ""
            )
        }
    }

    fun cancelEditingName() {
        _uiState.value = _uiState.value.copy(
            isEditingName = false,
            tempName = ""
        )
    }

    fun logout(onLogoutComplete: () -> Unit) {
        viewModelScope.launch {
            try {
                firebaseAuth.signOut()
                // Limpiar datos guardados si es necesario
                sharedPreferences?.edit()?.clear()?.apply()
                onLogoutComplete()
            } catch (e: Exception) {
                // Manejar error si es necesario
                onLogoutComplete()
            }
        }
    }
}