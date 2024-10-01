package com.example.desarrollo2.core.models

data class Usuario(
    val nickname: String,
    val nombreCompleto: String,
    val correo: String, // Debe ser del dominio @virginiogomez.cl
    val contraseña: String,
    val contactosDeEmergencia: List<ContactoEmergencia>
)

