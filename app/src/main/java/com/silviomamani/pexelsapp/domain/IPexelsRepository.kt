package com.silviomamani.pexelsapp.domain

import com.silviomamani.pexelsapp.photos.Fotos
import com.silviomamani.pexelsapp.photos.Videos

interface IPexelsRepository {
    suspend fun fetchPexels(search: String) : List<Fotos>

    suspend fun fetchFoto(pexelsId: Int) : Fotos

    suspend fun fetchPexelsVideos(search: String): List<Videos>

    suspend fun getVideoById(videoId: Int): Videos

    suspend fun getPopularFotos(): List<Fotos>
    suspend fun getPopularVideos(): List<Videos>
    suspend fun getMostViewed(): List<Fotos>

    suspend fun addToFavorites(foto: Fotos)
    suspend fun removeFromFavorites(fotoId: Int)
    suspend fun isFavorite(fotoId: Int): Boolean
    suspend fun getFavorites(): List<Fotos>
}