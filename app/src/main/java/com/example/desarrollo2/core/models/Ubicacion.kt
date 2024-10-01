package com.example.desarrollo2.core.models

data class Ubicacion(
    val latitud: Double,
    val longitud: Double,
    val timestamp: Long // Registra cuándo se obtuvo la ubicación
)
