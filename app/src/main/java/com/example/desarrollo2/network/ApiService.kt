
package com.example.desarrollo2.network

import com.example.desarrollo2.core.models.ContactoEmergencia
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("contactos")
    suspend fun agregarContacto(@Body contacto: ContactoEmergencia): Response<Unit> // Ajusta según tu API
}