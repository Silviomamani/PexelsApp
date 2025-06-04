package com.silviomamani.pexelsapp.photos

import com.silviomamani.pexelsapp.network.AuthInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private const val BASE_URL = "https://api.pexels.com/v1/"

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(
                OkHttpClient.Builder()
                    .addInterceptor(AuthInterceptor())
                    .build()
            )
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val pexelsApi: IPexelsApi by lazy {
        retrofit.create(IPexelsApi::class.java)
    }
}

object RetrofitInstanceVideo {
    private const val BASE_URL = "https://api.pexels.com/videos/"

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(
                OkHttpClient.Builder()
                    .addInterceptor(AuthInterceptor())
                    .build()
            )
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val pexelsVideoApi: IPexelsApi by lazy {
        retrofit.create(IPexelsApi::class.java)
    }
}
