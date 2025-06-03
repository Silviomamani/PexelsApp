package com.silviomamani.pexelsapp.photos

import android.util.Log
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.silviomamani.pexelsapp.network.AuthInterceptor
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

    override suspend fun getPexelsById(pexelsId: Int): Fotos {
        return RetrofitInstance.pexelsApi.getFoto(pexelsId)
    }

}