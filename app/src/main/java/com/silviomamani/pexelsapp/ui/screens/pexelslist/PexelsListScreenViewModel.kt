package com.silviomamani.pexelsapp.ui.screens.pexelslist

import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.silviomamani.pexelsapp.domain.IPexelsRepository
import com.silviomamani.pexelsapp.photos.Fotos
import com.silviomamani.pexelsapp.photos.PexelsRepository
import com.silviomamani.pexelsapp.photos.PexelsResult
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.io.IOException
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.silviomamani.pexelsapp.photos.Videos
import com.silviomamani.pexelsapp.ui.screens.commons.PexelsItem
import com.silviomamani.pexelsapp.ui.screens.commons.SearchType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class PexelsListScreenViewModel (
    private val pexelsRepository: IPexelsRepository = PexelsRepository()
): ViewModel()
{
    var uiState by mutableStateOf(PexelsListScreenState())
    private set
init{
    getUserName()
}
    val combinedList: List<PexelsItem>
        get() = when (uiState.searchType) {
            SearchType.PHOTOS -> uiState.pexelsList.map { PexelsItem.PhotoItem(it) }
            SearchType.VIDEOS -> uiState.pexelsVideosList.map { PexelsItem.VideoItem(it) }
        }
    private var fetchJob: Job? = null
    fun fetchResults() {
        when (uiState.searchType) {
            SearchType.PHOTOS -> fetchFotos()
            SearchType.VIDEOS -> fetchVideos()
        }
    }

    fun fetchFotos() {
        fetchJob?.cancel()
        fetchJob = viewModelScope.launch {
            try {
                val fotos = pexelsRepository.fetchPexels(uiState.searchQuery)
                uiState = uiState.copy(pexelsList = fotos)
            } catch (e: IOException) {
                Log.e("PexelsApp", "Error al recuperar fotos: ${e.message}")
            }
        }
    }

    fun fetchVideos() {
        fetchJob?.cancel()
        fetchJob = viewModelScope.launch {
            try {
                val videos = pexelsRepository.fetchPexelsVideos(uiState.searchQuery)
                Log.d("PexelsApp", "Videos obtenidos: ${videos.size}")
                uiState = uiState.copy(pexelsVideosList = videos)
            } catch (e: IOException) {
                Log.e("PexelsApp", "Error al recuperar videos: ${e.message}")
            }
        }
    }

    fun onSearchTypeChange(type: SearchType) {
        uiState = uiState.copy(searchType = type)
    }
    fun searchChange(search: String) {
        uiState = uiState.copy(
            searchQuery = search,
            pexelsList = uiState.pexelsList,
            pexelsVideosList = uiState.pexelsVideosList,
            username = uiState.username
        )
    }


    fun getUserName(){
        uiState =uiState.copy(searchQuery = uiState.searchQuery, pexelsList = uiState.pexelsList, username = FirebaseAuth.getInstance().currentUser?.displayName?: "Usuario")
    }

    private val _recommendedPhotos = MutableStateFlow<List<Fotos>>(emptyList())
    val recommendedPhotos: StateFlow<List<Fotos>> = _recommendedPhotos

    private val _recommendedVideos = MutableStateFlow<List<Videos>>(emptyList())
    val recommendedVideos: StateFlow<List<Videos>> = _recommendedVideos


    fun fetchRecommendedPhotos() {
        viewModelScope.launch {
            try {
                val photos = pexelsRepository.fetchPexels("nature") // o cualquier término genérico
                _recommendedPhotos.value = photos
            } catch (e: IOException) {
                Log.e("PexelsApp", "Error al recuperar fotos recomendadas: ${e.message}")
            }
        }
    }

    fun fetchRecommendedVideos() {
        viewModelScope.launch {
            try {
                val videos = pexelsRepository.fetchPexelsVideos("popular") // puede ser "popular" o algo general
                _recommendedVideos.value = videos
            } catch (e: IOException) {
                Log.e("PexelsApp", "Error al recuperar videos recomendados: ${e.message}")
            }
        }
    }
}

