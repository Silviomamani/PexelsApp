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
    override suspend fun fetchPexelsVideos(search: String): List<Videos>{
        return pexelsDataSource.getPexelsVideoList(search)
    }
    override suspend fun getVideoById(videoId: Int): Videos {
        return pexelsDataSource.getVideoById(videoId)
    }
}
