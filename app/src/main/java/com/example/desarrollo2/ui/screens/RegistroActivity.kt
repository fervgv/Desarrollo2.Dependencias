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
import com.example.desarrollo2.core.viewmodels.RegistroViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RegistroActivity : ComponentActivity()  {
    // Usar el ViewModel de la pantalla de inicio de sesión
    private val registroViewModel: RegistroViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Llamar a la función que contiene la navegación de la app
            AppNavigation(registroViewModel)
        }
    }

    @Composable
    fun AppNavigation(viewModel: RegistroViewModel) {
        val navController = rememberNavController()

        // Configurar la navegación
        NavHost(navController = navController, startDestination = "register") {
            composable("register") {
                // Pasar el viewModel a LoginScreen
                registerScreen(viewModel)
            }
            // Aquí puedes agregar más rutas a otras pantallas
            // Ejemplo:
            // composable("registro") { RegistroScreen(viewModel) }
        }
    }

    @Composable
    fun registerScreen(viewModel: RegistroViewModel) {
        Box(
            Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Register(Modifier.align(Alignment.Center), viewModel)
        }
    }

    @Composable
    fun Register(modifier: Modifier, viewModel: RegistroViewModel) {
        // Observa los estados de los campos desde el ViewModel
        val name: String by viewModel.name.observeAsState(initial = "")
        val email: String by viewModel.email.observeAsState(initial = "")
        val password: String by viewModel.password.observeAsState(initial = "")
        val confirmPassword: String by viewModel.confirmPassword.observeAsState(initial = "")
        val nickname: String by viewModel.nickname.observeAsState(initial = "")
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
                NicknameField(nickname = nickname) { viewModel.onNicknameChanged(it) }
                Spacer(modifier = Modifier.padding(16.dp))
                PhoneNumberField(phoneNumber = phoneNumber) { viewModel.onPhoneNumberChanged(it) }
                Spacer(modifier = Modifier.padding(16.dp))

                // Botón de registro
                RegisterButton(isRegisterEnabled) {
                    if (isRegisterEnabled) {
                        coroutineScope.launch {
                            viewModel.onRegisterSelected(name, email, password, confirmPassword, nickname, phoneNumber)
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
            placeholder = { Text(text = "Nombre", color = Color.Gray) },
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
    fun PasswordField(password: String, onTextFieldChanged: (String) -> Unit) {
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

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun ConfirmPasswordField(confirmPassword: String, onTextFieldChanged: (String) -> Unit) {
        TextField(
            value = confirmPassword,
            onValueChange = { onTextFieldChanged(it) },
            placeholder = { Text(text = "Repetir Contraseña", color = Color.Gray) },
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
    fun NicknameField(nickname: String, onTextFieldChanged: (String) -> Unit) {
        TextField(
            value = nickname,
            onValueChange = { onTextFieldChanged(it) },
            placeholder = { Text(text = "Nickname", color = Color.Gray) },
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
                containerColor = Color(0xFF65C552),
                disabledContainerColor = Color(0xFF89AC82),
                contentColor = Color.White,
                disabledContentColor = Color.White
            ),
            enabled = registerEnabled
        ) {
            Text(text = "Registrarme")
        }
    }
}