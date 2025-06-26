package com.silviomamani.pexelsapp.photos.local

import androidx.room.Database
import com.google.common.reflect.TypeToken
import com.google.gson.Gson
import com.silviomamani.pexelsapp.photos.Fotos
import com.silviomamani.pexelsapp.photos.ImagesJpg
import com.silviomamani.pexelsapp.photos.User
import com.silviomamani.pexelsapp.photos.VideoFile
import com.silviomamani.pexelsapp.photos.Videos



fun Fotos.toLocal() = PexelsLocal(
    id,
    photographer,
    alt,
    width,
    height,
    src.original,
    src.medium
)

fun List<Fotos>.toLocal() = map { it.toLocal() }

fun PexelsLocal.toExternal() = Fotos(
    id,
    photographer,
    alt,
    width,
    height,
    ImagesJpg(imageUrl, mediumUrl) // ← Usar ambas URLs
)

fun List<PexelsLocal>.localToExternal() = map(PexelsLocal::toExternal)




fun Videos.toLocal(): VideosLocal {
    // Convertir la lista de VideoFile a JSON string para guardar en Room
    val gson = Gson()
    val videoFilesJson = gson.toJson(videoFiles)

    return VideosLocal(
        id = id,
        width = width,
        height = height,
        duration = duration,
        userName = user.name,
        image = image,
        videoFilesJson = videoFilesJson
    )
}

fun List<Videos>.videosToLocal() = map { it.toLocal() }

fun VideosLocal.toExternal(): Videos {

    val gson = Gson()
    val listType = object : TypeToken<List<VideoFile>>() {}.type
    val videoFiles = gson.fromJson<List<VideoFile>>(videoFilesJson, listType) ?: emptyList()

    return Videos(
        id = id,
        width = width,
        height = height,
        duration = duration,
        user = User(userName),
        image = image,
        videoFiles = videoFiles
    )
}

fun List<VideosLocal>.videosLocalToExternal() = map(VideosLocal::toExternal)
