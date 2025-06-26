package com.silviomamani.pexelsapp.photos.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("videos")
data class VideosLocal(
    @PrimaryKey val id: Int = 0,
    val width: Int = 0,
    val height: Int = 0,
    val duration: Int = 0,
    val userName: String = "",
    val image: String = "",
    val videoFilesJson: String = ""
)