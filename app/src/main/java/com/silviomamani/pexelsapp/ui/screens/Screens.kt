package com.silviomamani.pexelsapp.ui.screens

sealed class Screens(val route: String) {
    object Splash : Screens("Splash")
    object PexelsList : Screens("pexels_list_screen")
    object PexelsDetail : Screens("pexels_detail_screen")
    object Login : Screens("login_screen")
    object PexelsVideosDetail : Screens("pexels_videos_detail_screen")
    object Home : Screens("home")
    object Register : Screens("register")
    object Update : Screens("update_screen")
    object Favorites : Screens("favorites_screen")

}