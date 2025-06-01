package com.silviomamani.pexelsapp.photos

import com.silviomamani.pexelsapp.domain.IPexelsRepository

class PexelsRepository (
            val pexelsDataSource: IPexelsDataSource = PexelsApiDataSource()
            ): IPexelsRepository{
    override suspend fun fetchPexels(search: String) : List<Fotos>{
        return pexelsDataSource.getPexelsList(search)
    }
}