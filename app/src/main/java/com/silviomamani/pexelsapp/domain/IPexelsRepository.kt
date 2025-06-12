package com.silviomamani.pexelsapp.domain

import com.silviomamani.pexelsapp.photos.Fotos
import com.silviomamani.pexelsapp.photos.Videos

interface IPexelsRepository {
    suspend fun fetchPexels(search: String) : List<Fotos>
    suspend fun fetchFoto(pexelsId: Int) : Fotos
    suspend fun getPopularFotos(): List<Fotos>

    suspend fun fetchPexelsVideos(search: String): List<Videos>
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