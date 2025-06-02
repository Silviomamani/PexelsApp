package com.silviomamani.pexelsapp.ui.screens.pexelsdetail

import com.silviomamani.pexelsapp.photos.Fotos
import com.silviomamani.pexelsapp.photos.emptyFotos

data class PexelsDetailScreenState (val pexelsId: Int = 0
                                    , val pexelsDetail: Fotos = emptyFotos()
){

}