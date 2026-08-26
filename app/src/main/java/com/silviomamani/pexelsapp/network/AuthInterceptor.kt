package com.silviomamani.pexelsapp.network


import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .addHeader("Authorization", "xLXT6Z2TgweUUdaFx6fUdpaEoSR0mjfydXGqrqpOWkcvI7RzKdMCZK1r")
            .build()
        return chain.proceed(request)
    }
}