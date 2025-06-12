package com.silviomamani.pexelsapp.ui.screens.pexelsvideodetail

import android.util.Log
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
                // Verificar si es favorito después de cargar el video
                checkIfVideoFavorite()
            } catch (e: Exception) {
                uiState = uiState.copy(
                    error = e.localizedMessage ?: "Error desconocido al cargar el video",
                    isLoading = false
                )
            }
        }
    }

    private fun checkIfVideoFavorite() {
        viewModelScope.launch {
            try {
                // Solo verificar si tenemos un video cargado
                uiState.video?.let { video ->
                    val isFav = pexelsRepository.isVideoFavorite(video.id)
                    uiState = uiState.copy(isFavorito = isFav)
                }
            } catch (e: Exception) {
                Log.e("VideoDetail", "Error al verificar video favorito: ${e.message}")
            }
        }
    }

    fun toggleVideoFavorito() {
        viewModelScope.launch {
            try {
                // Solo proceder si tenemos un video cargado
                uiState.video?.let { video ->
                    if (uiState.isFavorito) {
                        // Quitar video de favoritos
                        pexelsRepository.removeVideoFromFavorites(video.id)
                        uiState = uiState.copy(isFavorito = false)
                        Log.d("VideoDetail", "Video eliminado de favoritos")
                    } else {
                        // Agregar video a favoritos
                        pexelsRepository.addVideoToFavorites(video)
                        uiState = uiState.copy(isFavorito = true)
                        Log.d("VideoDetail", "Video agregado a favoritos")
                    }
                }
            } catch (e: Exception) {
                Log.e("VideoDetail", "Error al cambiar video favorito: ${e.message}")
            }
        }
    }
}