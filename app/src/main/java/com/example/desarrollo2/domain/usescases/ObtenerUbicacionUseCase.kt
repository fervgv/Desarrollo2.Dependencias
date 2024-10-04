package com.example.desarrollo2.domain.usecases

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import com.example.desarrollo2.core.models.Ubicacion
import com.google.android.gms.location.FusedLocationProviderClient
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ObtenerUbicacionUseCase @Inject constructor(
    private val fusedLocationProviderClient: FusedLocationProviderClient,
    private val context: Context // Pasar el contexto
) {
    suspend fun obtenerUbicacionActual(): Ubicacion? {
        // Verificar si los permisos de ubicación están concedidos
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Permiso no concedido, retornar null
            return null
        }

        return try {
            // Obtener la última ubicación usando coroutines y 'await'
            val location = fusedLocationProviderClient.lastLocation.await()
            location?.let {
                Ubicacion(
                    latitud = it.latitude,
                    longitud = it.longitude,
                    timestamp = System.currentTimeMillis()
                )
            }
        } catch (e: SecurityException) {
            // Manejar la excepción en caso de que ocurra
            null
        }
    }
}
