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

class PexelsListScreenViewModel (
    private val pexelsRepository: IPexelsRepository = PexelsRepository()
): ViewModel()
{
    var uiState by mutableStateOf(PexelsListScreenState())
    private set
init{
    //fetchFotos()
}
    private var fetchJob: Job? = null
    fun fetchFotos (){
        fetchJob?.cancel()
        fetchJob = viewModelScope.launch {
            try{
                uiState = uiState.copy(pexelsList = pexelsRepository.fetchPexels(uiState.searchQuery))
            }
            catch(e: IOException){
                Log.e("PexelsApp","Error se esta Recuperando la Lista")

            }
        }

    }
    fun searchChange(search:String){
        uiState = uiState.copy(searchQuery = search, pexelsList = uiState.pexelsList)

    }
}

