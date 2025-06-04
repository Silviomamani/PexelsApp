package com.silviomamani.pexelsapp.ui.screens.pexelslist

import com.silviomamani.pexelsapp.photos.Fotos

data class PexelsListScreenState (
    val username : String = "Username",
    val pexelsList : List<Fotos> = emptyList(),
    val searchQuery: String = ""
)