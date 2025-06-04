package com.silviomamani.pexelsapp.ui.screens.commons

import com.silviomamani.pexelsapp.photos.Fotos
import com.silviomamani.pexelsapp.photos.Videos

sealed class PexelsItem {
    data class PhotoItem(val foto: Fotos) : PexelsItem()
    data class VideoItem(val video: Videos) : PexelsItem()
}