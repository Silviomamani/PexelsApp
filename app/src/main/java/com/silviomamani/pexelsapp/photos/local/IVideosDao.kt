package com.silviomamani.pexelsapp.photos.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface IVideosDao {

    @Query("SELECT * FROM videos")
    suspend fun getAllVideos(): List<VideosLocal>

    @Query("SELECT * FROM videos WHERE id = :id LIMIT 1")
    suspend fun findByIdVideo(id: Int): VideosLocal?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVideo(vararg video: VideosLocal)

    @Delete
    suspend fun deleteVideo(video: VideosLocal)

    @Query("DELETE FROM videos WHERE id = :videoId")
    suspend fun deleteVideoById(videoId: Int)
}