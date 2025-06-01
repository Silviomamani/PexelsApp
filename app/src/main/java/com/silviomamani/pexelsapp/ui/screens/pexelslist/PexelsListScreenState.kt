package com.silviomamani.pexelsapp.ui.screens.pexelslist

import com.silviomamani.pexelsapp.photos.Fotos

data class PexelsListScreenState (
    val pexelsList : List<Fotos> = emptyList(),
    val searchQuery: String = ""
)