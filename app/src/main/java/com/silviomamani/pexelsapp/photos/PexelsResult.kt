package com.silviomamani.pexelsapp.photos

data class PexelsResult (
    val photos: List<Fotos>
)


data class Fotos(
    val id: Int = 0,
    val photographer: String = "",
    val alt: String = "",
    val width: Int = 0,
    val height: Int = 0,
    val src: ImagesJpg = ImagesJpg((""), (""))

)
data class ImagesJpg(
    val original: String = (""),
    val medium : String = ("")
)

typealias FotoDetailResult = Fotos


fun emptyFotos(): Fotos{
    return Fotos(0,"","",0,0,ImagesJpg("", ""))
}