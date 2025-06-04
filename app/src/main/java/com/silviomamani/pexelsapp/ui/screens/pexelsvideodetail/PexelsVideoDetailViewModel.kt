package com.silviomamani.pexelsapp.ui.screens.pexelsvideodetail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.silviomamani.pexelsapp.domain.IPexelsRepository
import com.silviomamani.pexelsapp.photos.PexelsRepository
import kotlinx.coroutines.launch
class PexelsVideoDetailViewModel(
    private val pexelsRepository: IPexelsRepository = PexelsRepository()
) : ViewModel() {

    var uiState by mutableStateOf(PexelsVideoDetailScreenState())
        private set

    fun fetchVideoById(videoId: Int) {
        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true, error = null)
            try {
                val video = pexelsRepository.getVideoById(videoId)
                uiState = uiState.copy(video = video, isLoading = false)
            } catch (e: Exception) {
                uiState = uiState.copy(
                    error = e.localizedMessage ?: "Error desconocido al cargar el video",
                    isLoading = false
                )
            }
        }
    }
}
