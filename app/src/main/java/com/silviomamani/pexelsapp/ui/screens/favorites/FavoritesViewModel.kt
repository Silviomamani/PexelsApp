package com.silviomamani.pexelsapp.ui.screens.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.silviomamani.pexelsapp.photos.PexelsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FavoritesScreenViewModel : ViewModel() {

    private val repository = PexelsRepository() // Crea la instancia directamente aquí

    private val _state = MutableStateFlow(FavoritesScreenState())
    val state: StateFlow<FavoritesScreenState> = _state.asStateFlow()

    init {
        loadFavorites()
    }

    fun loadFavorites() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)

            try {
                val photos = repository.getFavoritesFotos()
                val videos = repository.getFavoritesVideos()

                _state.value = _state.value.copy(
                    favoritePhotos = photos,
                    favoriteVideos = videos,
                    isLoading = false
                )
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    error = "Error al cargar favoritos: ${e.localizedMessage}",
                    isLoading = false
                )
            }
        }
    }

    fun selectTab(tab: FavoriteTab) {
        _state.value = _state.value.copy(selectedTab = tab)
    }

    fun clearError() {
        _state.value = _state.value.copy(error = null)
    }
}