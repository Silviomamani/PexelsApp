package com.silviomamani.pexelsapp.photos

import com.silviomamani.pexelsapp.domain.IPexelsRepository


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


    // Favoritos para fotos
    override suspend fun addFotoToFavorites(foto: Fotos) {
        pexelsDataSource.addFotoToFavorites(foto)
    }

    override suspend fun removeFotoFromFavorites(fotoId: Int) {
        pexelsDataSource.removeFotoFromFavorites(fotoId)
    }

    override suspend fun isFotoFavorite(fotoId: Int): Boolean {
        return pexelsDataSource.isFotoFavorite(fotoId)
    }

    override suspend fun getFavoritesFotos(): List<Fotos> {
        return pexelsDataSource.getFavoritesFotos()
    }


    // Favoritos para videos
    override suspend fun addVideoToFavorites(video: Videos) {
        pexelsDataSource.addVideoToFavorites(video)
    }
    override suspend fun removeVideoFromFavorites(videoId: Int) {
        pexelsDataSource.removeVideoFromFavorites(videoId)
    }
    override suspend fun isVideoFavorite(videoId: Int): Boolean {
        return pexelsDataSource.isVideoFavorite(videoId)
    }
    override suspend fun getFavoritesVideos(): List<Videos> {
        return pexelsDataSource.getFavoritesVideos()
    }
}
