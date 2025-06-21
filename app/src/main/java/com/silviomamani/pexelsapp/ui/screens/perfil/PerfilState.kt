package com.silviomamani.pexelsapp.ui.screens.perfil

data class PerfilState(
    val userPhotoUrl: String? = null,
    val userName: String = "",
    val userEmail: String = "",
    val displayName: String = "", // Nombre que se muestra en la app (editable)
    val isLoading: Boolean = false,
    val isEditingName: Boolean = false,
    val tempName: String = "" // Nombre temporal mientras se edita
)