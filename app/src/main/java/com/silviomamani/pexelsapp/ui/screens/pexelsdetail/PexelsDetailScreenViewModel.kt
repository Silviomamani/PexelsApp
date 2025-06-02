package com.silviomamani.pexelsapp.ui.screens.pexelsdetail

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

    private var fetcheJob: Job? = null

    fun fetchFoto(){
        fetcheJob?.cancel()
        fetcheJob = viewModelScope.launch {
            uiState = uiState.copy(pexelsId = uiState.pexelsId , pexelsDetail = pexelsRepository.fetchFoto(uiState.pexelsId))
        }

    }
    fun setPexelsId(pexelsId: Int): Unit{
        uiState= uiState.copy(pexelsId = pexelsId, pexelsDetail = uiState.pexelsDetail)
        fetchFoto()
    }
}