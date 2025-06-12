package com.silviomamani.pexelsapp.ui.screens.pexelsvideodetail

import com.silviomamani.pexelsapp.photos.Videos

data class PexelsVideoDetailScreenState(
    val video: Videos? = null,
    val isLoading: Boolean = true,
    val error: String? = null,
    val isFavorito: Boolean = false
) {
    val videoId: Int get() = video?.id ?: 0
    val videoDetail: Videos get() = video ?: Videos()
}