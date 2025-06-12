package com.silviomamani.pexelsapp.ui.screens.pexelsdetail

import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.silviomamani.pexelsapp.domain.IPexelsRepository
import com.silviomamani.pexelsapp.photos.PexelsRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class PexelsDetailScreenViewModel(
    private val pexelsRepository: IPexelsRepository = PexelsRepository()
) : ViewModel() {

    var uiState by mutableStateOf(PexelsDetailScreenState())
        private set

    private var fetchJob: Job? = null
    private var downloadManager: ImageDownloadManager? = null

    fun initializeDownloadManager(context: Context) {
        if (downloadManager == null) {
            downloadManager = ImageDownloadManager(context)
        }
    }

    fun setPexelsId(pexelsId: Int) {
        uiState = uiState.copy(pexelsId = pexelsId)
        fetchFoto()
        checkIfFavorite()
    }

    private fun fetchFoto() {
        fetchJob?.cancel()
        fetchJob = viewModelScope.launch {
            try {
                val foto = pexelsRepository.fetchFoto(uiState.pexelsId)
                uiState = uiState.copy(pexelsDetail = foto)
            } catch (e: Exception) {
                Log.e("PexelsDetail", "Error al cargar foto: ${e.message}")
            }
        }
    }

    private fun checkIfFavorite() {
        viewModelScope.launch {
            try {
                val isFav = pexelsRepository.isFotoFavorite(uiState.pexelsId)
                uiState = uiState.copy(isFavorito = isFav)
            } catch (e: Exception) {
                Log.e("PexelsDetail", "Error al verificar favorito: ${e.message}")
            }
        }
    }

    fun toggleFavorito() {
        viewModelScope.launch {
            try {
                if (uiState.isFavorito) {
                    // Quitar de favoritos
                    pexelsRepository.removeFotoFromFavorites(uiState.pexelsId)
                    uiState = uiState.copy(isFavorito = false)
                    Log.d("PexelsDetail", "Foto eliminada de favoritos")
                } else {
                    // Agregar a favoritos
                    pexelsRepository.addFotoToFavorites(uiState.pexelsDetail)
                    uiState = uiState.copy(isFavorito = true)
                    Log.d("PexelsDetail", "Foto agregada a favoritos")
                }
            } catch (e: Exception) {
                Log.e("PexelsDetail", "Error al cambiar favorito: ${e.message}")
            }
        }
    }

    fun downloadImage() {
        viewModelScope.launch {
            uiState = uiState.copy(isDownloading = true)

            try {
                val success = downloadManager?.downloadImage(
                    imageUrl = uiState.pexelsDetail.src.original,
                    fileName = "pexels_${uiState.pexelsDetail.id}_${uiState.pexelsDetail.photographer.replace(" ", "_")}.jpg"
                ) ?: false

                Log.d("PexelsDetail", if (success) "Descarga exitosa" else "Error en descarga")
            } catch (e: Exception) {
                Log.e("PexelsDetail", "Error al descargar imagen: ${e.message}")
            } finally {
                uiState = uiState.copy(isDownloading = false)
            }
        }
    }
}