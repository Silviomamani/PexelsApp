package com.silviomamani.pexelsapp.photos

import com.google.gson.annotations.SerializedName

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

//Video

data class PexelsVideoResult (
    val videos: List<Videos>
)
data class Videos(
    val id: Int = 0,
    val width: Int = 0,
    val height: Int = 0,
    val duration: Int = 0,
    val user: User = User(""),
    val image: String = "",
    @SerializedName("video_files")
    val videoFiles: List<VideoFile> = emptyList()

)
data class User(
    val name: String = "",
)
data class VideoFile(
    val id: Int = 0,
    val link: String = "",
)

typealias VideoDetailResult = Videos