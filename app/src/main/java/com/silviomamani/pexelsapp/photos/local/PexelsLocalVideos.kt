package com.silviomamani.pexelsapp.photos.local

import androidx.room.Entity

@Entity("videos")
data class PexelsLocalVideos(
    val id: Int = 0,
    val width: Int = 0,
    val height: Int = 0,
    val duration: Int = 0,
    val user : String = "",
    val image: String = "",
    val videoUrl: String = ""

)
