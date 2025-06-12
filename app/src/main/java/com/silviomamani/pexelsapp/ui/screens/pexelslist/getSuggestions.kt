package com.silviomamani.pexelsapp.ui.screens.pexelslist

import com.silviomamani.pexelsapp.ui.screens.commons.SearchType

fun getSuggestions(searchType: SearchType): List<String> {
    return when (searchType) {
        SearchType.PHOTOS -> listOf(
            "Naturaleza", "Paisajes", "Personas",
            "Animales", "Ciudades", "Comida",
            "Tecnología", "Arte", "Deportes",
            "Viajes", "Arquitectura", "Flores"
        )
        SearchType.VIDEOS -> listOf(
            "Naturaleza", "Timelapse", "Personas",
            "Animales", "Ciudades", "Océano",
            "Montañas", "Tecnología", "Arte",
            "Deportes", "Viajes", "Cocina"
        )
    }
}