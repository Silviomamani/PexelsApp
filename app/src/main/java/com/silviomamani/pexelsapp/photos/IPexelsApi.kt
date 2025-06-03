package com.silviomamani.pexelsapp.photos

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface IPexelsApi {
    @GET("search")
    suspend fun getFotosSearch(
        @Query("query") search : String
    ) : PexelsResult
    @GET("photos/{Id}")
    suspend fun getFoto(
        @Path("Id") pexelsId: Int
    ): FotoDetailResult
}