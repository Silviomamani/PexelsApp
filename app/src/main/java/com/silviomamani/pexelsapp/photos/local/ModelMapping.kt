package com.silviomamani.pexelsapp.photos.local

import com.silviomamani.pexelsapp.photos.Fotos
import com.silviomamani.pexelsapp.photos.ImagesJpg

fun Fotos.toLocal() = PexelsLocal(
    id,
    photographer,
    alt,
    width,
    height,
    src.original,
)

fun List<Fotos>.toLocal() = map {  }

fun PexelsLocal.toExternal() = Fotos(
    id,
    photographer,
    alt,
    width,
    height,
    ImagesJpg(imageUrl)
)

fun List<PexelsLocal>.localToExternal() = map(PexelsLocal::toExternal)