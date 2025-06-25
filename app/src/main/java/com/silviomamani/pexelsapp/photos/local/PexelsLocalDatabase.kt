package com.silviomamani.pexelsapp.photos.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch


@Database(
    entities = [PexelsLocal::class],
    version = 2,
    exportSchema = false
)
abstract class PexelsLocalDatabase: RoomDatabase() {

    abstract fun pexelsDao(): IPexelsDao

    companion object {
        @Volatile
        private var _instace: PexelsLocalDatabase? = null

        fun getInstance(context: Context): PexelsLocalDatabase = _instace ?: synchronized(this) {
            _instace ?: buildDatabase(context).also { _instace = it }
        }

        private fun buildDatabase(context: Context): PexelsLocalDatabase = Room.databaseBuilder(
            context,
            PexelsLocalDatabase::class.java,
            "pexels_database"
        )
            .fallbackToDestructiveMigration()
            .build()

        suspend fun clean(context: Context) = coroutineScope {
            launch(Dispatchers.IO) {
                getInstance(context).clearAllTables()
            }
        }
    }
}