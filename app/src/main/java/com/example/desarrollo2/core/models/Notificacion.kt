package com.example.desarrollo2.core.models

data class Notificacion(
    val mensaje: String,
    val destinatario: ContactoEmergencia,
    val ubicacion: Ubicacion,
    val timestamp: Long //
)
