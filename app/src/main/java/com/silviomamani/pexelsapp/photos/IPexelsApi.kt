package com.silviomamani.pexelsapp.photos

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface IPexelsApi {
    @GET("search")
    suspend fun getFotosSearch(
        @Query("query") search : String,
        @Query("per_page") perPage: Int = 12
    ) : PexelsResult

    @GET("photos/{Id}")
    suspend fun getFoto(
        @Path("Id") pexelsId: Int
    ): FotoDetailResult


    @GET("search")
    suspend fun getVideosSearch(
        @Query("query") search: String,
        @Query("per_page") perPage: Int = 15
    ): PexelsVideoResult


    @GET("videos/{id}")
    suspend fun getVideoById(
        @Path("id") videoId: Int
    ): VideoDetailResult

}
