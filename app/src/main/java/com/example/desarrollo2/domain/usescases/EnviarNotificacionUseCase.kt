package com.example.desarrollo2.domain.usecases

import com.example.desarrollo2.core.models.Notificacion
import com.example.desarrollo2.network.ApiService
import javax.inject.Inject

class EnviarNotificacionUseCase @Inject constructor(
    private val apiService: ApiService
) {
    suspend operator fun invoke(notificacion: Notificacion): Boolean {
        return try {
            val response = apiService.agregarContacto(notificacion.destinatario)
            response.isSuccessful
        } catch (e: Exception) {
            false
        }
    }
}
