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

class PexelsListScreenViewModel (
    private val pexelsRepository: IPexelsRepository = PexelsRepository()
): ViewModel()
{
    var uiState by mutableStateOf(PexelsListScreenState())
    private set
init{
    getUserName()
}
    private var fetchJob: Job? = null
    fun fetchFotos (){
        fetchJob?.cancel()
        fetchJob = viewModelScope.launch {
            try{
                uiState = uiState.copy(pexelsList = pexelsRepository.fetchPexels(uiState.searchQuery), searchQuery = uiState.searchQuery, username = uiState.username)
            }
            catch(e: IOException){
                Log.e("PexelsApp","Error se esta Recuperando la Lista")

            }
        }

    }
    fun searchChange(search:String){
        uiState = uiState.copy(searchQuery = search, pexelsList = uiState.pexelsList, username = uiState.username)

    }
    fun getUserName(){
        uiState =uiState.copy(searchQuery = uiState.searchQuery, pexelsList = uiState.pexelsList, username = FirebaseAuth.getInstance().currentUser?.displayName?: "Usuario")
    }
}

