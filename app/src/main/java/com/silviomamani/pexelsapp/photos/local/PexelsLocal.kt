package com.silviomamani.pexelsapp.photos.local

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity("fotos")
data class PexelsLocal(
    @PrimaryKey val id: Int = 0,
    val photographer: String = "",
    val alt: String = "",
    val width: Int = 0,
    val height: Int = 0,
    val imageUrl : String = ""
    )

