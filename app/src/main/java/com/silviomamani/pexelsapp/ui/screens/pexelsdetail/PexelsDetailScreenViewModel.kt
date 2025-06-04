package com.silviomamani.pexelsapp.ui.screens.pexelsdetail

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.silviomamani.pexelsapp.domain.IPexelsRepository
import com.silviomamani.pexelsapp.photos.Fotos
import com.silviomamani.pexelsapp.photos.PexelsRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class PexelsDetailScreenViewModel(
    private val pexelsRepository: IPexelsRepository = PexelsRepository()
) : ViewModel() {
    var uiState by mutableStateOf(PexelsDetailScreenState())
    private set

    private var fetcheJob: Job? = null

    fun fetchFoto() {
        fetcheJob?.cancel()
        fetcheJob = viewModelScope.launch {
            val foto = pexelsRepository.fetchFoto(uiState.pexelsId)

            val db = FirebaseFirestore.getInstance()
            val currentUser = FirebaseAuth.getInstance().currentUser
            val email = currentUser?.email ?: throw Exception("Usuario no logueado")

            val docRef = db.collection("usuarios")
                .document(email)
                .collection("favoritos")
                .document(foto.id.toString())

            val snapshot = docRef.get().await()
            val isFavorito = snapshot.exists()

            uiState = uiState.copy(
                pexelsDetail = foto,
                isFavorito = isFavorito
            )
        }
    }
    fun setPexelsId(pexelsId: Int): Unit{
        uiState= uiState.copy(pexelsId = pexelsId, pexelsDetail = uiState.pexelsDetail)
        fetchFoto()
    }

    fun toggleFavorito() {
        viewModelScope.launch {
            try {
                val foto = uiState.pexelsDetail
                val db = FirebaseFirestore.getInstance()
                val currentUser = FirebaseAuth.getInstance().currentUser
                val email = currentUser?.email ?: throw Exception("Usuario no logueado")

                val docRef = db.collection("usuarios")
                    .document(email)
                    .collection("favoritos")
                    .document(foto.id.toString())

                val snapshot = docRef.get().await()

                if (snapshot.exists()) {
                    docRef.delete().await()
                    uiState = uiState.copy(isFavorito = false)
                } else {
                    docRef.set(foto).await()
                    uiState = uiState.copy(isFavorito = true)
                }
            } catch (e: Exception) {
                Log.e("Favoritos", "Error al guardar/eliminar favorito", e)
            }
        }
    }
    }