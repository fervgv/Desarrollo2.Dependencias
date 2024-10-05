package com.example.desarrollo2.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.desarrollo2.core.models.Usuario
import com.example.desarrollo2.core.viewmodels.UsuarioViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        // Inicializar LocationRequest
        locationRequest = LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY,
            5000 // Intervalo de 5 segundos entre actualizaciones
            // 💀💀💀💀💀💀💀💀
            //^^ esto no esta funcionando por alguna razon, los valores en pantalla no cambian
            //seguramente el texto es el que no esta cambiando
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
            //miPantalla(latitud = latitude, longitud = longitude)

            // Configuración de la navegación
            val navController = rememberNavController()
            NavHost(navController = navController, startDestination = "first_screen") {
                //composable("first_screen") { miPantalla(navController, latitude, longitude) }
                composable("first_screen") { RegisterScreen(usuarioViewModel) }
                //composable("second_screen") { secondScreen(navController) }

            }
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

    //////////////// aqui esta el formulario, primera pantalla //////////////////////////
    //esta pendiente que funcione correctamente, sofia te voy a suicidar
    //no lo arreglo ahora por que tengo sueño y quiero hablar con mi novia

    @Composable
    fun RegisterScreen(viewModel: UsuarioViewModel) {
        Box(
            Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Register(Modifier.align(Alignment.Center), viewModel)
        }
    }

    @Composable
    fun Register(modifier: Modifier, viewModel: UsuarioViewModel) {
        // Observa los estados de los campos desde el ViewModel
        val nickname: String by viewModel.nickname.observeAsState(initial = "")
        val name: String by viewModel.name.observeAsState(initial = "")
        val email: String by viewModel.email.observeAsState(initial = "")
        val password: String by viewModel.password.observeAsState(initial = "")
        val confirmPassword: String by viewModel.confirmPassword.observeAsState(initial = "")
        val phoneNumber: String by viewModel.phoneNumber.observeAsState(initial = "")
        val isRegisterEnabled: Boolean by viewModel.registerEnable.observeAsState(initial = false)
        val isLoading: Boolean by viewModel.isLoading.observeAsState(initial = false)

        val coroutineScope = rememberCoroutineScope()

        if (isLoading) {
            Box(Modifier.fillMaxSize()) {
                CircularProgressIndicator(Modifier.align(Alignment.Center))
            }
        } else {
            Column(modifier = modifier) {
                Spacer(modifier = Modifier.padding(16.dp))
                NameField(name = name) { viewModel.onNameChanged(it) }
                Spacer(modifier = Modifier.padding(16.dp))
                EmailField(email = email) { viewModel.onEmailChanged(it) }
                Spacer(modifier = Modifier.padding(16.dp))
                PasswordField(password = password) { viewModel.onPasswordChanged(it) }
                Spacer(modifier = Modifier.padding(16.dp))
                ConfirmPasswordField(confirmPassword = confirmPassword) { viewModel.onConfirmPasswordChanged(it) }
                Spacer(modifier = Modifier.padding(16.dp))
                PhoneNumberField(phoneNumber = phoneNumber) { viewModel.onPhoneNumberChanged(it) }
                Spacer(modifier = Modifier.padding(16.dp))

                // Botón de registro
                RegisterButton(isRegisterEnabled) {
                    if (isRegisterEnabled) {
                        coroutineScope.launch {
                            viewModel.onRegisterSelected(name, email, password, confirmPassword, phoneNumber)
                            // Manejar resultados de registro aquí (éxito/error)
                        }
                    }
                }
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun NameField(name: String, onTextFieldChanged: (String) -> Unit) {
        TextField(
            value = name,
            onValueChange = { onTextFieldChanged(it) },
            placeholder = { Text(text = "Nombre") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            singleLine = true,
            maxLines = 1,
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                containerColor = Color(0xFFDEDDDD)
            )
        )
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun EmailField(email: String, onTextFieldChanged: (String) -> Unit) {
        TextField(
            value = email,
            onValueChange = { onTextFieldChanged(it) },
            placeholder = { Text(text = "Correo electrónico") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            singleLine = true,
            maxLines = 1,
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                containerColor = Color(0xFFDEDDDD)
            )
        )
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun PasswordField(password: String, onTextFieldChanged: (String) -> Unit) {
        TextField(
            value = password,
            onValueChange = { onTextFieldChanged(it) },
            placeholder = { Text(text = "Contraseña") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            visualTransformation = PasswordVisualTransformation(),
            singleLine = true,
            maxLines = 1,
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                containerColor = Color(0xFFDEDDDD)
            )
        )
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun ConfirmPasswordField(confirmPassword: String, onTextFieldChanged: (String) -> Unit) {
        TextField(
            value = confirmPassword,
            onValueChange = { onTextFieldChanged(it) },
            placeholder = { Text(text = "Repetir Contraseña") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            visualTransformation = PasswordVisualTransformation(),
            singleLine = true,
            maxLines = 1,
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                containerColor = Color(0xFFDEDDDD)
            )
        )
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun PhoneNumberField(phoneNumber: String, onTextFieldChanged: (String) -> Unit) {
        TextField(
            value = phoneNumber,
            onValueChange = { onTextFieldChanged(it) },
            placeholder = { Text(text = "Número Telefónico", color = Color.Gray) },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            singleLine = true,
            maxLines = 1,
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                containerColor = Color(0xFFDEDDDD)
            )
        )
    }

    @Composable
    fun RegisterButton(registerEnabled: Boolean, onRegisterSelected: () -> Unit) {
        Button(
            onClick = { onRegisterSelected() },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFFF4303),
                disabledContainerColor = Color(0xFFF78058),
                contentColor = Color.White,
                disabledContentColor = Color.White
            ),
            enabled = registerEnabled
        ) {
            Text(text = "Registrarme")
        }
    }

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

    @Preview(showBackground = true)
    @Composable
    fun defaultPreview() {
        //imprime en pantalla lat y lon por defecto
        //miPantalla(latitud = "-31.098265", longitud = "-64.2947706")
    }
}
