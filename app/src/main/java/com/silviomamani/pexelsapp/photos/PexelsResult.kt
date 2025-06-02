package com.silviomamani.pexelsapp.photos

data class PexelsResult (
    val photos: List<Fotos>
)

data class FotoDetailResult(
    val photos: Fotos
)

data class Fotos(
    val id: Int,
    val photographer: String,
    val alt: String,
    val width: Int,
    val height: Int,
    val src: ImagesJpg

)
data class ImagesJpg(
    val original: String
)

fun emptyFotos(): Fotos{
    return Fotos(0,"","",0,0,ImagesJpg(""))
}