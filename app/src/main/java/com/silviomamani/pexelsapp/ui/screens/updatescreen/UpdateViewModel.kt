package com.silviomamani.pexelsapp.ui.screens.updatescreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class UpdateViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(UpdateScreenState())
    val uiState: StateFlow<UpdateScreenState> = _uiState.asStateFlow()

    fun showContactDialog() {
        _uiState.value = _uiState.value.copy(showContactDialog = true)
    }

    fun hideContactDialog() {
        _uiState.value = _uiState.value.copy(showContactDialog = false)
    }

    fun openWhatsApp(context: android.content.Context) {
        viewModelScope.launch {
            try {
                val whatsappNumber = _uiState.value.whatsappNumber
                val message = "Hola! Me interesa obtener la versión premium de la aplicación."
                val url = "https://wa.me/$whatsappNumber?text=${java.net.URLEncoder.encode(message, "UTF-8")}"

                val intent = android.content.Intent(android.content.Intent.ACTION_VIEW)
                intent.data = android.net.Uri.parse(url)
                context.startActivity(intent)
            } catch (e: Exception) {
                // Manejar error si WhatsApp no está instalado
                showContactDialog()
            }
        }
    }

    fun setLoading(isLoading: Boolean) {
        _uiState.value = _uiState.value.copy(isLoading = isLoading)
    }
}