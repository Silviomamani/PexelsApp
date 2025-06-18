package com.silviomamani.pexelsapp.ui.screens.favorites

import com.silviomamani.pexelsapp.photos.Fotos
import com.silviomamani.pexelsapp.photos.Videos

data class FavoritesScreenState(
    val favoritePhotos: List<Fotos> = emptyList(),
    val favoriteVideos: List<Videos> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val selectedTab: FavoriteTab = FavoriteTab.PHOTOS
)

enum class FavoriteTab(val title: String) {
    PHOTOS("Fotos"),
    VIDEOS("Videos")
}