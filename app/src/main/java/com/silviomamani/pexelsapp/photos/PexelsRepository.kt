package com.silviomamani.pexelsapp.photos

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.silviomamani.pexelsapp.domain.IPexelsRepository
import kotlinx.coroutines.tasks.await

class PexelsRepository(
    val pexelsDataSource: IPexelsDataSource = PexelsApiDataSource()
) : IPexelsRepository {

    override suspend fun fetchPexels(search: String): List<Fotos> {
        return pexelsDataSource.getPexelsList(search)
    }

    override suspend fun fetchFoto(pexelsId: Int): Fotos {
        return pexelsDataSource.getPexelsById(pexelsId)
    }

    override suspend fun getPopularFotos(): List<Fotos> {
        return pexelsDataSource.getPopularFotos()
    }

    override suspend fun getMostViewed(): List<Fotos> {
        return pexelsDataSource.getMostViewed()
    }




    override suspend fun fetchPexelsVideos(search: String): List<Videos>{
        return pexelsDataSource.getPexelsVideoList(search)
    }
    override suspend fun getVideoById(videoId: Int): Videos {
        return pexelsDataSource.getVideoById(videoId)
    }
    override suspend fun getPopularVideos(): List<Videos> {
        return pexelsDataSource.getPopularVideos()
    }
    override suspend fun addToFavorites(foto: Fotos) {
        pexelsDataSource.addToFavorites(foto)
    }

    override suspend fun removeFromFavorites(fotoId: Int) {
        pexelsDataSource.removeFromFavorites(fotoId)
    }

    override suspend fun isFavorite(fotoId: Int): Boolean {
        return pexelsDataSource.isFavorite(fotoId)
    }

    override suspend fun getFavorites(): List<Fotos> {
        return pexelsDataSource.getFavorites()
    }
}

