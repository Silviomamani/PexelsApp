package com.silviomamani.pexelsapp.photos

interface IPexelsDataSource {

    suspend fun getPexelsList(search: String) : List <Fotos>
    suspend fun getPexelsById(pexelsId: Int) : Fotos
    suspend fun getPopularFotos(): List<Fotos>


    suspend fun getPexelsVideoList(search: String): List<Videos>
    suspend fun getVideoById(videoId: Int): Videos
    suspend fun getPopularVideos(): List<Videos>

    suspend fun getMostViewed(): List<Fotos>

    // Favoritos para fotos
    suspend fun addFotoToFavorites(foto: Fotos)
    suspend fun removeFotoFromFavorites(fotoId: Int)
    suspend fun isFotoFavorite(fotoId: Int): Boolean
    suspend fun getFavoritesFotos(): List<Fotos>


    // Favoritos para videos
    suspend fun addVideoToFavorites(video: Videos)
    suspend fun removeVideoFromFavorites(videoId: Int)
    suspend fun isVideoFavorite(videoId: Int): Boolean
    suspend fun getFavoritesVideos(): List<Videos>
}