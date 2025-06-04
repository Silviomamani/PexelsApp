package com.silviomamani.pexelsapp.ui.screens.pexelslistvideo

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.silviomamani.pexelsapp.domain.IPexelsRepository
import com.silviomamani.pexelsapp.photos.PexelsRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.io.IOException


class PexelsListScreenVideoViewModel (
    private val pexelsRepository: IPexelsRepository = PexelsRepository()
): ViewModel() {

    var uiState by mutableStateOf(PexelsListScreenVideoState())
        private set

    private var fetchJob: Job? = null

    init {
        getUserName()
    }

    fun fetchVideos() {
        fetchJob?.cancel()
        fetchJob = viewModelScope.launch {
            try {
                uiState = uiState.copy(
                    pexelsVideoList = pexelsRepository.fetchPexelsVideos(uiState.searchQuery),
                    searchQuery = uiState.searchQuery,
                    username = uiState.username
                )
            } catch (e: IOException) {
                Log.e("PexelsApp", "Error recuperando la lista de videos")
            }
        }
    }

    fun searchChange(search: String) {
        uiState = uiState.copy(
            searchQuery = search,
            pexelsVideoList = uiState.pexelsVideoList,
            username = uiState.username
        )
    }

    fun getUserName() {
        uiState = uiState.copy(
            searchQuery = uiState.searchQuery,
            pexelsVideoList = uiState.pexelsVideoList,
            username = FirebaseAuth.getInstance().currentUser?.displayName ?: "Usuario"
        )
    }
}
