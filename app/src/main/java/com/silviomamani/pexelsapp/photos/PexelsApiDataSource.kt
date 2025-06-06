package com.silviomamani.pexelsapp.photos

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.silviomamani.pexelsapp.network.AuthInterceptor
import kotlinx.coroutines.tasks.await
import okhttp3.OkHttpClient
import retrofit2.HttpException
import java.io.IOException

class PexelsApiDataSource :IPexelsDataSource{

    override suspend fun getPexelsList(search: String): List<Fotos> {
        Log.d("PexelsApp", "PexelsApiDataSource.getPexelsList")
        return try{
            val pexelsResult = RetrofitInstance.pexelsApi.getFotosSearch(search)
            return pexelsResult.photos

        }catch (e: HttpException){
            Log.e("PexelsApp","Error desconocido: ${e.code()} ${e.localizedMessage}")
            emptyList()
        }
        catch (e:IOException){
            Log.e("PexelsApp","Error desconocido: ${e.localizedMessage}")
            emptyList()
        }
        catch (e:Exception){
            Log.e("PexelsApp","Error desconocido: ${e.localizedMessage}")
            emptyList()

        }
    }
    override suspend fun getPexelsVideoList(search: String): List<Videos> {
        Log.d("PexelsApp", "PexelsApiDataSource.getPexelsVideoList")
        return try {
            val pexelsResultVideos = RetrofitInstanceVideo.pexelsVideoApi.getVideosSearch(search)
            return pexelsResultVideos.videos // O como se llame el campo del JSON que contiene la lista
        } catch (e: HttpException) {
            Log.e("PexelsApp", "HTTP error: ${e.code()} ${e.localizedMessage}")
            emptyList()
        } catch (e: IOException) {
            Log.e("PexelsApp", "Network error: ${e.localizedMessage}")
            emptyList()
        } catch (e: Exception) {
            Log.e("PexelsApp", "Unexpected error: ${e.localizedMessage}")
            emptyList()
        }
    }

    override suspend fun getVideoById(videoId: Int): Videos {
        return try {
            RetrofitInstanceVideo.pexelsVideoApi.getVideoById(videoId)
        } catch (e: Exception) {
            Log.e("PexelsApp", "Error al obtener video por ID: ${e.localizedMessage}")
            throw e
        }
    }
    override suspend fun getPexelsById(pexelsId: Int): Fotos {
        val db = FirebaseFirestore.getInstance()


        val currentUser = FirebaseAuth.getInstance().currentUser
        val uid = currentUser?.uid ?: throw Exception("Usuario no logueado")


        val docRef = db.collection("usuarios")
            .document(uid)
            .collection("favoritos")
            .document(pexelsId.toString())

        val pexelsResult = docRef.get().await()
        var foto = pexelsResult.toObject(Fotos::class.java)

        return if (foto != null) {
            foto
        } else {
            foto = RetrofitInstance.pexelsApi.getFoto(pexelsId)
            docRef.set(foto).await()
            foto
        }
    }
    override suspend fun getPopularFotos(): List<Fotos> {
        Log.d("PexelsApp", "PexelsApiDataSource.getPopularFotos")
        return try {
            val result = RetrofitInstance.pexelsApi.getPopularFotos()
            result.photos
        } catch (e: HttpException) {
            Log.e("PexelsApp", "HTTP error en getPopularFotos: ${e.code()} ${e.localizedMessage}")
            emptyList()
        } catch (e: IOException) {
            Log.e("PexelsApp", "Network error en getPopularFotos: ${e.localizedMessage}")
            emptyList()
        } catch (e: Exception) {
            Log.e("PexelsApp", "Unexpected error en getPopularFotos: ${e.localizedMessage}")
            emptyList()
        }
    }

    override suspend fun getPopularVideos(): List<Videos> {
        Log.d("PexelsApp", "PexelsApiDataSource.getPopularVideos")
        return try {
            val result = RetrofitInstanceVideo.pexelsVideoApi.getPopularVideos()
            result.videos
        } catch (e: HttpException) {
            Log.e("PexelsApp", "HTTP error en getPopularVideos: ${e.code()} ${e.localizedMessage}")
            emptyList()
        } catch (e: IOException) {
            Log.e("PexelsApp", "Network error en getPopularVideos: ${e.localizedMessage}")
            emptyList()
        } catch (e: Exception) {
            Log.e("PexelsApp", "Unexpected error en getPopularVideos: ${e.localizedMessage}")
            emptyList()
        }
    }



}