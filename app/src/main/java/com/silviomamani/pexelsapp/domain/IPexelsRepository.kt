package com.silviomamani.pexelsapp.domain

import com.silviomamani.pexelsapp.photos.Fotos

interface IPexelsRepository {
    suspend fun fetchPexels(search: String) : List<Fotos>

    suspend fun fetchFoto(pexelsId: Int) : Fotos
}