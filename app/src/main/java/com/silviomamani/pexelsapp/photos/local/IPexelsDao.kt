package com.silviomamani.pexelsapp.photos.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query


@Dao
interface IPexelsDao {

    @Query("SELECT * FROM fotos")
    suspend fun getAllFotos(): List<PexelsLocal>

    @Query("SELECT * FROM fotos WHERE id = :id LIMIT 1")
    suspend fun findByIdFoto(id : Int): PexelsLocal

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFoto(vararg foto:PexelsLocal)

    @Delete
    suspend fun deleteFoto(foto:PexelsLocal)



    @Query("DELETE FROM fotos WHERE id = :fotoId")
    suspend fun deleteFotoById(fotoId: Int)


}