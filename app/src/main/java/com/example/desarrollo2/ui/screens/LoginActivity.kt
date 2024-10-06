package com.example.desarrollo2.ui.screens

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.desarrollo2.core.viewmodels.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginActivity : ComponentActivity() {
    // Usar el ViewModel de la pantalla de inicio de sesión
    private val loginViewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Llamar a la función que contiene la navegación de la app
            AppNavigation(loginViewModel)
        }
    }

    @Composable
    fun AppNavigation(viewModel: LoginViewModel) {
        val navController = rememberNavController()

        // Configurar la navegación
        NavHost(navController = navController, startDestination = "login") {
            composable("login") {
                // Pasar el viewModel a LoginScreen
                LoginScreen(viewModel)
            }
            // Aquí puedes agregar más rutas a otras pantallas
            // Ejemplo:
            // composable("registro") { RegistroScreen(viewModel) }
        }
    }

    @Composable
    fun LoginScreen(viewModel: LoginViewModel) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Login(Modifier.align(Alignment.Center), viewModel)
        }
    }

    @Composable
    fun Login(modifier: Modifier, viewModel: LoginViewModel) {
        val email: String by viewModel.email.observeAsState(initial = "")
        val password: String by viewModel.password.observeAsState(initial = "")
        val isLoginEnabled: Boolean by viewModel.loginEnable.observeAsState(initial = false)
        val isLoading: Boolean by viewModel.isLoading.observeAsState(initial = false)

        val coroutineScope = rememberCoroutineScope()

        if (isLoading) {
            Box(Modifier.fillMaxSize()) {
                CircularProgressIndicator(Modifier.align(Alignment.Center))
            }
        } else {
            Column(modifier = modifier) {
                Spacer(modifier = Modifier.padding(16.dp))

                // Campo para el correo
                EmailLogin(email = email) { viewModel.onEmailChanged(it) }
                Spacer(modifier = Modifier.padding(16.dp))

                // Campo para la contraseña
                PasswordLogin(password = password) { viewModel.onPasswordChanged(it) }
                Spacer(modifier = Modifier.padding(16.dp))

                // Botón de inicio de sesión
                LoginButton(isLoginEnabled) {
                    if (isLoginEnabled) {
                        coroutineScope.launch {
                            // Llama a la función en el ViewModel para manejar el inicio de sesión
                            viewModel.onLoginSelected(email, password)
                        }
                    }
                }

                Spacer(modifier = Modifier.padding(16.dp))

                Spacer(modifier = Modifier.padding(16.dp))

                // Opción para ir a la pantalla de registro
                TextButton(onClick = { /* Navegar a la pantalla de registro */ }) {
                    Text(text = "¿No tienes una cuenta? Regístrate")
                }
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun EmailLogin(email: String, onTextFieldChanged: (String) -> Unit) {
        TextField(
            value = email,
            onValueChange = { onTextFieldChanged(it) },
            placeholder = { Text(text = "Correo electrónico", color = Color.Gray) },
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
    fun PasswordLogin(password: String, onTextFieldChanged: (String) -> Unit) {
        TextField(
            value = password,
            onValueChange = { onTextFieldChanged(it) },
            placeholder = { Text(text = "Contraseña", color = Color.Gray) },
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

    @Composable
    fun LoginButton(loginEnabled: Boolean, onLoginSelected: () -> Unit) {
        Button(
            onClick = { onLoginSelected() },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF65C552),
                disabledContainerColor = Color(0xFF89AC82),
                contentColor = Color.White,
                disabledContentColor = Color.White
            ),
            enabled = loginEnabled
        ) {
            Text(text = "Iniciar Sesión")
        }
    }
}