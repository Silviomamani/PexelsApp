package com.silviomamani.pexelsapp.photos.local

import android.content.Context

object PexelsDatabaseProvider {
    lateinit var dbLocal: PexelsLocalDatabase
        private set

    fun createDatabase(context:Context){
        dbLocal=PexelsLocalDatabase.getInstance(context)
    }
}