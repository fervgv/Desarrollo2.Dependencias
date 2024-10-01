package com.example.desarrollo2.core.repositories

import com.example.desarrollo2.core.models.ContactoEmergencia
import com.example.desarrollo2.network.ApiService // Importar tu interfaz de Retrofit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ContactoEmergenciaRepository(private val apiService: ApiService) {

    suspend fun agregarContacto(contacto: ContactoEmergencia): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                // Realizar una llamada a la API para agregar el contacto
                apiService.agregarContacto(contacto) // Supongamos que tienes este método en tu ApiService
                true
            } catch (e: Exception) {
                false // Manejar error según sea necesario
            }
        }
    }

    // Otros métodos para obtener, actualizar o eliminar contactos
}
