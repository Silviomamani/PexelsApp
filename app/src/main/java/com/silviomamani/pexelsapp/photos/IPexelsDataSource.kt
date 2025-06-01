package com.silviomamani.pexelsapp.photos

interface IPexelsDataSource {
    suspend fun getPexelsList(search: String) : List <Fotos>
}