package com.example.desarrollo2.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import com.example.desarrollo2.core.models.Usuario
import com.example.desarrollo2.core.viewmodels.UsuarioViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val usuarioViewModel: UsuarioViewModel by viewModels()
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback
    // Variables para almacenar latitud y longitud
    private var latitude: String = "0.0" // Valores predeterminados
    private var longitude: String = "0.0" // Valores predeterminados

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        // Inicializar LocationRequest
        locationRequest = LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY,
            5000 // Intervalo de 5 segundos entre actualizaciones
        ).build()

        // Inicializar LocationCallback
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                for (location in locationResult.locations) {
                    latitude = location.latitude.toString()
                    longitude = location.longitude.toString()
                    // Actualiza la UI con la nueva ubicación
                    // Cambiar el estado si es necesario
                }
            }
        }

        // Establecer el contenido de la interfaz
        setContent {
            var showDialog by remember { mutableStateOf(true) }

            if (showDialog) {
                AlertDialog(
                    onDismissRequest = { showDialog = false },
                    confirmButton = {
                        TextButton(onClick = {
                            requestLocationPermission()
                            showDialog = false
                        }) {
                            Text("Permitir")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = {
                            // Maneja el caso de denegación
                            showDialog = false
                        }) {
                            Text("Denegar")
                        }
                    },
                    title = { Text("Permisos de Ubicación") },
                    text = { Text("La aplicación 'Helper' necesita acceder a tu ubicación para funcionar correctamente. ¿Deseas permitir el acceso a la ubicación?") }
                )
            }

            // Llama a la función que muestra la pantalla
            miPantalla(latitud = latitude, longitud = longitude)
        }

        // Solicitar permiso de ubicación si no está otorgado
        requestLocationPermission()

        // Ejemplo de uso de UsuarioViewModel
        val nuevoUsuario = Usuario("nickname", "Nombre Completo", "correo@virginiogomez.cl", "contraseña", emptyList())
        usuarioViewModel.registrarUsuario(nuevoUsuario)
    }

    private fun requestLocationPermission() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissionsLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        } else {
            obtenerUltimaUbicacion()
        }
    }

    private val requestPermissionsLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            obtenerUltimaUbicacion()
        }
    }

    private fun obtenerUltimaUbicacion() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            // Solicitar actualizaciones de ubicación
            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null)
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun miPantalla(latitud: String, longitud: String) {
        Scaffold(
            topBar = {
                TopAppBar(title = { Text("Registro de Usuario") })
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier.padding(innerPadding)
            ) {
                Text(text = "Bienvenido a la actividad de registro.")
                Text(text = "Latitud: $latitud")
                Text(text = "Longitud: $longitud")
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // Detener las actualizaciones de ubicación cuando se destruya la actividad
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    @Preview(showBackground = true)
    @Composable
    fun defaultPreview() {
        miPantalla(latitud = "-31.098265", longitud = "-64.2947706")
    }
}
