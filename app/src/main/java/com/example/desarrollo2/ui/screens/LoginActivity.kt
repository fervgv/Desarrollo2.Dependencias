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
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController


@AndroidEntryPoint
class LoginActivity : ComponentActivity() {
    private val loginViewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppNavigation(loginViewModel)
        }
    }

    @Composable
    fun AppNavigation(viewModel: LoginViewModel) {
        val navController = rememberNavController()
        NavHost(navController = navController, startDestination = "login") {
            composable("login") {
                LoginScreen(viewModel, navController)
            }
            // Aquí puedes agregar más rutas a otras pantallas, como el registro
            // composable("registro") { RegistroScreen(viewModel) }
        }
    }

    @Composable
    fun LoginScreen(viewModel: LoginViewModel, navController: NavController) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Login(Modifier.align(Alignment.Center), viewModel, navController)
        }
    }

    @Composable
    fun Login(modifier: Modifier, viewModel: LoginViewModel, navController: NavController) {
        val email: String by viewModel.email.observeAsState(initial = "")
        val password: String by viewModel.password.observeAsState(initial = "")
        val isLoginEnabled: Boolean by viewModel.loginEnable.observeAsState(initial = false)
        val isLoading: Boolean by viewModel.isLoading.observeAsState(initial = false)
        val errorMessage: String? by viewModel.errorMessage.observeAsState()

        val coroutineScope = rememberCoroutineScope()

        if (isLoading) {
            Box(Modifier.fillMaxSize()) {
                CircularProgressIndicator(Modifier.align(Alignment.Center))
            }
        } else {
            Column(modifier = modifier) {
                Spacer(modifier = Modifier.padding(16.dp))

                EmailLogin(email = email) { viewModel.onEmailChanged(it) }
                Spacer(modifier = Modifier.padding(16.dp))

                PasswordLogin(password = password) { viewModel.onPasswordChanged(it) }
                Spacer(modifier = Modifier.padding(16.dp))

                // Muestra el mensaje de error si existe
                errorMessage?.let {
                    Text(text = it, color = Color.Red)
                    Spacer(modifier = Modifier.height(8.dp))
                }

                LoginButton(
                    loginEnabled = isLoginEnabled,
                    onLoginSelected = { email, password ->
                        coroutineScope.launch {
                            viewModel.onLoginSelected(email, password)
                        }
                    },
                    email = email,
                    password = password
                )

                Spacer(modifier = Modifier.padding(16.dp))

                // Opción para ir a la pantalla de registro
                TextButton(onClick = { navController.navigate("registro") }) {
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
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                containerColor = Color(0xFFDEDDDD)
            )
        )
    }

    @Composable
    fun LoginButton(
        loginEnabled: Boolean,
        onLoginSelected: (String, String) -> Unit,
        email: String,
        password: String
    ) {
        Button(
            onClick = { onLoginSelected(email, password) },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF65C552),
                disabledContainerColor = Color(0xFF89AC82),
                contentColor = Color.White
            ),
            enabled = loginEnabled
        ) {
            Text(text = "Iniciar Sesión")
        }
    }
}

