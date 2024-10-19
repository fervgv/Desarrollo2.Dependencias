package com.example.desarrollo2.ui.screens

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.material.icons.Icons
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.desarrollo2.core.models.Usuario
import com.example.desarrollo2.core.viewmodels.UsuarioViewModel
import com.example.desarrollo2.ui.NavigationWrapper
import com.example.desarrollo2.ui.theme.Desarrollo2Theme
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val usuarioViewModel: UsuarioViewModel by viewModels()
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback
    // Variables para almacenar latitud y longitud
    private var latitude: String = "0.0" // Valores predeterminados
    private var longitude: String = "0.0" // Valores predeterminados

    private lateinit var navHostController: NavHostController
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        enableEdgeToEdge()
        setContent {
            navHostController = rememberNavController()

            Desarrollo2Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavigationWrapper(navHostController, auth)
                }
            }

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

            // Solicitar permiso de ubicación si no está otorgado
            requestLocationPermission()
        }

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

//        // Establecer el contenido de la interfaz
//        setContent {
//
//            // Llama a la función que muestra la pantalla
//            //miPantalla(latitud = latitude, longitud = longitude)
//
//            // Configuración de la navegación
//            val navController = rememberNavController()
//            NavHost(navController = navController, startDestination = "intro") {
//                composable("intro") { IntroduccionScreen(navController) }
//                //navhost es para manejar navegacion dentro de pantallas compose
//                //mientras que intent es para manejar navegacion entre activities
//                //composable("first_screen") { miPantalla(navController, latitude, longitude) }
//
//            }
//        }
//
//
//
//        // Ejemplo de uso de UsuarioViewModel
//        val nuevoUsuario = Usuario("nickname", "Nombre Completo", "correo@virginiogomez.cl", "contraseña", emptyList())
//        usuarioViewModel.registrarUsuario(nuevoUsuario)
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

    ////////////////////////////////////////////////////////////////////////////////

    //esta es la pantalla de introduccion con los botones de login y registro
//    @OptIn(ExperimentalMaterial3Api::class)
//    @Composable
//    fun IntroduccionScreen(navController: NavController) {
//        Scaffold(
//            topBar = {
//                TopAppBar(title = { Text("Bienvenido a Helper") })
//            }
//        ) { innerPadding ->
//            Column(
//                modifier = Modifier
//                    .padding(innerPadding)
//                    .padding(16.dp)
//                    .fillMaxSize(),
//                horizontalAlignment = Alignment.CenterHorizontally,
//                verticalArrangement = Arrangement.Center
//            ) {
//                // Botón de Iniciar Sesión
//                Button(
//                    onClick = {
//                        val context = navController.context
//                        val intent = Intent(context, LoginActivity::class.java)
//                        context.startActivity(intent)
//                    },
//                    modifier = Modifier.fillMaxWidth().padding(16.dp)
//                ) {
//                    Text("Iniciar Sesión")
//                }
//
//                // Botón de Registrarse
//                Button(
//                    onClick = {
//                        val context = navController.context
//                        val intent = Intent(context, RegistroActivity::class.java)
//                        context.startActivity(intent)
//                    },
//                    modifier = Modifier.fillMaxWidth().padding(16.dp)
//                ) {
//                    Text("Registrarse")
//                }
//            }
//        }
//    }
////////////////////////////////////////////////////////////////////////////////////////////////
    //esto es la pantalla con la latitud y longitud del usuario
    /*@OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun miPantalla(latitud: String, longitud: String) {
        var email by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }

        Scaffold(
            topBar = {
                TopAppBar(title = { Text("Registro de Usuario") })
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TopAppBar(title = { Text("Registro de Usuario") })
                Text(text = "Bienvenido a la actividad de registro.")
                Text(text = "Latitud: $latitud")
                Text(text = "Longitud: $longitud")

            }
        }
    }*/

    override fun onDestroy() {
        super.onDestroy()
        // Detener las actualizaciones de ubicación cuando se destruya la actividad
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

//    @Preview(showBackground = true)
//    @Composable
//    fun defaultPreview() {
//        //imprime en pantalla lat y lon por defecto
//        //miPantalla(latitud = "-31.098265", longitud = "-64.2947706")
//    }
}
