package com.silviomamani.pexelsapp.ui.screens.updatescreen

data class UpdateScreenState(
    val isLoading: Boolean = false,
    val showContactDialog: Boolean = false,
    val whatsappNumber: String = "+1234567890", // Número de WhatsApp de silvio.mm
    val premiumFeatures: List<String> = listOf(
        "Funciones avanzadas",
        "Sin publicidad",
        "Soporte prioritario",
        "Actualizaciones tempranas"
    )
)
