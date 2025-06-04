package com.silviomamani.pexelsapp.ui.screens.homescreen

data class HomeScreenState(
    val selectedSection: Section = Section.PHOTOS,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

enum class Section {
    PHOTOS, VIDEOS, FAVORITES, UPLOAD
}