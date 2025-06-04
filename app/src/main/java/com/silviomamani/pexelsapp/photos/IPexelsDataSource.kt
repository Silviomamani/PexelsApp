package com.silviomamani.pexelsapp.photos

interface IPexelsDataSource {
    suspend fun getPexelsList(search: String) : List <Fotos>
    suspend fun getPexelsById(pexelsId: Int) : Fotos
    suspend fun getPexelsVideoList(search: String): List<Videos>
    suspend fun getVideoById(videoId: Int): Videos

    suspend fun getPopularFotos(): List<Fotos>
    suspend fun getPopularVideos(): List<Videos>

}