package com.silviomamani.pexelsapp.ui.screens.pexelslist

import com.silviomamani.pexelsapp.photos.Fotos
import com.silviomamani.pexelsapp.photos.Videos
import com.silviomamani.pexelsapp.ui.screens.commons.PexelsItem
import com.silviomamani.pexelsapp.ui.screens.commons.SearchType

data class PexelsListScreenState(
    val username: String = "Username",
    val pexelsList: List<Fotos> = emptyList(),
    val pexelsVideosList: List<Videos> = emptyList(),
    val searchQuery: String = "",
    val searchType: SearchType = SearchType.PHOTOS,
    val hasSearched: Boolean = false
)