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
        return RetrofitInstance.pexelsApi.getFoto(pexelsId)
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

    override suspend fun getMostViewed(): List<Fotos> {
        Log.d("PexelsApp", "PexelsApiDataSource.getPopularFotos")
        return try {
            val result = RetrofitInstance.pexelsApi.getMostViewed()
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

    // ===============================
    // FAVORITOS PARA FOTOS
    // ===============================

    override suspend fun addFotoToFavorites(foto: Fotos) {
        val db = FirebaseFirestore.getInstance()
        val currentUser = FirebaseAuth.getInstance().currentUser
        val uid = currentUser?.uid ?: throw Exception("Usuario no logueado")

        val docRef = db.collection("usuarios")
            .document(uid)
            .collection("favoritos_fotos")
            .document(foto.id.toString())

        try {
            docRef.set(foto).await()
            Log.d("PexelsApp", "Foto agregada a favoritos: ${foto.id}")
        } catch (e: Exception) {
            Log.e("PexelsApp", "Error al agregar foto a favoritos: ${e.localizedMessage}")
            throw e
        }
    }

    override suspend fun removeFotoFromFavorites(fotoId: Int) {
        val db = FirebaseFirestore.getInstance()
        val currentUser = FirebaseAuth.getInstance().currentUser
        val uid = currentUser?.uid ?: throw Exception("Usuario no logueado")

        val docRef = db.collection("usuarios")
            .document(uid)
            .collection("favoritos_fotos")
            .document(fotoId.toString())

        try {
            docRef.delete().await()
            Log.d("PexelsApp", "Foto eliminada de favoritos: $fotoId")
        } catch (e: Exception) {
            Log.e("PexelsApp", "Error al eliminar foto de favoritos: ${e.localizedMessage}")
            throw e
        }
    }

    override suspend fun isFotoFavorite(fotoId: Int): Boolean {
        val db = FirebaseFirestore.getInstance()
        val currentUser = FirebaseAuth.getInstance().currentUser
        val uid = currentUser?.uid ?: return false

        val docRef = db.collection("usuarios")
            .document(uid)
            .collection("favoritos_fotos")
            .document(fotoId.toString())

        return try {
            val document = docRef.get().await()
            document.exists()
        } catch (e: Exception) {
            Log.e("PexelsApp", "Error al verificar foto favorita: ${e.localizedMessage}")
            false
        }
    }

    override suspend fun getFavoritesFotos(): List<Fotos> {
        val db = FirebaseFirestore.getInstance()
        val currentUser = FirebaseAuth.getInstance().currentUser
        val uid = currentUser?.uid ?: return emptyList()

        val collectionRef = db.collection("usuarios")
            .document(uid)
            .collection("favoritos_fotos")

        return try {
            val querySnapshot = collectionRef.get().await()
            querySnapshot.documents.mapNotNull { document ->
                document.toObject(Fotos::class.java)
            }
        } catch (e: Exception) {
            Log.e("PexelsApp", "Error al obtener fotos favoritas: ${e.localizedMessage}")
            emptyList()
        }
    }

    // ===============================
    // FAVORITOS PARA VIDEOS
    // ===============================

    override suspend fun addVideoToFavorites(video: Videos) {
        val db = FirebaseFirestore.getInstance()
        val currentUser = FirebaseAuth.getInstance().currentUser
        val uid = currentUser?.uid ?: throw Exception("Usuario no logueado")

        val docRef = db.collection("usuarios")
            .document(uid)
            .collection("favoritos_videos")
            .document(video.id.toString())

        try {
            docRef.set(video).await()
            Log.d("PexelsApp", "Video agregado a favoritos: ${video.id}")
        } catch (e: Exception) {
            Log.e("PexelsApp", "Error al agregar video a favoritos: ${e.localizedMessage}")
            throw e
        }
    }

    override suspend fun removeVideoFromFavorites(videoId: Int) {
        val db = FirebaseFirestore.getInstance()
        val currentUser = FirebaseAuth.getInstance().currentUser
        val uid = currentUser?.uid ?: throw Exception("Usuario no logueado")

        val docRef = db.collection("usuarios")
            .document(uid)
            .collection("favoritos_videos")
            .document(videoId.toString())

        try {
            docRef.delete().await()
            Log.d("PexelsApp", "Video eliminado de favoritos: $videoId")
        } catch (e: Exception) {
            Log.e("PexelsApp", "Error al eliminar video de favoritos: ${e.localizedMessage}")
            throw e
        }
    }

    override suspend fun isVideoFavorite(videoId: Int): Boolean {
        val db = FirebaseFirestore.getInstance()
        val currentUser = FirebaseAuth.getInstance().currentUser
        val uid = currentUser?.uid ?: return false

        val docRef = db.collection("usuarios")
            .document(uid)
            .collection("favoritos_videos")
            .document(videoId.toString())

        return try {
            val document = docRef.get().await()
            document.exists()
        } catch (e: Exception) {
            Log.e("PexelsApp", "Error al verificar video favorito: ${e.localizedMessage}")
            false
        }
    }

    override suspend fun getFavoritesVideos(): List<Videos> {
        val db = FirebaseFirestore.getInstance()
        val currentUser = FirebaseAuth.getInstance().currentUser
        val uid = currentUser?.uid ?: return emptyList()

        val collectionRef = db.collection("usuarios")
            .document(uid)
            .collection("favoritos_videos")

        return try {
            val querySnapshot = collectionRef.get().await()
            querySnapshot.documents.mapNotNull { document ->
                document.toObject(Videos::class.java)
            }
        } catch (e: Exception) {
            Log.e("PexelsApp", "Error al obtener videos favoritos: ${e.localizedMessage}")
            emptyList()
        }
    }
}
