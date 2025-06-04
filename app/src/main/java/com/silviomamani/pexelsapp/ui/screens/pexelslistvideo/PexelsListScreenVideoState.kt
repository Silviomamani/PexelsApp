package com.silviomamani.pexelsapp.ui.screens.pexelslistvideo

import com.silviomamani.pexelsapp.photos.Videos


data class PexelsListScreenVideoState (
    val username : String = "Username",
    val pexelsVideoList : List<Videos> = emptyList(),
    val searchQuery: String = ""
)

