package com.example.desarrollo2.ui.screens

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.desarrollo2.core.viewmodels.AddContactViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddContactActivity : ComponentActivity() {

    private val addContactViewModel: AddContactViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppNavigation(addContactViewModel)
        }
    }

    @Composable
    fun AppNavigation(viewModel: AddContactViewModel) {
        val navController = rememberNavController()
        NavHost(navController = navController, startDestination = "addContact") {
            composable("addContact") {
                AddContactScreen(viewModel)
            }
            // Aquí puedes agregar más rutas a otras pantallas, como el registro
            // composable("registro") { RegistroScreen(viewModel) }
        }
    }

    @Composable
    fun AddContactScreen(viewModel: AddContactViewModel) {
        Box(
            androidx.compose.ui.Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            //aqui aun no le pongo nada
            }
    }

    @Composable
    fun AddContact(modifier: androidx.compose.ui.Modifier, viewModel: AddContactViewModel) {
        // Contenido principal de la pantalla de inicio de sesión
        val nickname: String by viewModel.contactName.observeAsState("")
        val email: String by viewModel.contactEmail.observeAsState("")
        val phoneNumber: String by viewModel.contactPhoneNumber.observeAsState("")
        val isContactValid: Boolean by viewModel.isContactValid.observeAsState(false)
        val context = LocalContext.current
        val isLoading: Boolean by viewModel.isLoading.observeAsState(initial = false)
        val errorMessage: String? by viewModel.errorMessage.observeAsState(initial = null)

        if (isLoading) {
            Box(Modifier.fillMaxSize()) {
                CircularProgressIndicator(Modifier.align(Alignment.Center))
            }
        } else {
            Column(modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
            ) {
                Text(text = "Agregar Contacto de Emergencia")
                Spacer(modifier = Modifier.height(16.dp))

                NicknameField(nickname = nickname) { viewModel.onNicknameChanged(it) }
                Spacer(modifier = Modifier.height(16.dp))

                EmailField(email = email) { viewModel.onEmailChanged(it) }
                Spacer(modifier = Modifier.height(16.dp))

                PhoneNumberField(phoneNumber = phoneNumber) { viewModel.onPhoneNumberChanged(it) }
                Spacer(modifier = Modifier.height(32.dp))

                // Mostrar el mensaje de error si existe
                errorMessage?.let {
                    Text(
                        text = it,
                        color = Color.Red,
                        modifier = Modifier.padding(16.dp)
                    )
                }

                RegisterButton(isContactValid){
                    // Al hacer clic en el botón, valida los campos
                    viewModel.validateContactFields(nickname, email, phoneNumber)
                    if (isContactValid) {
                        // Si está habilitado procede a el main activity
                        // (ESTO HAY QUE CAMBIARLO A LA PANTALLA PRINCIPAL)
                        val intent = Intent(context, MainActivity::class.java)
                        context.startActivity(intent)
                    }
                }

            }
        }

    }

    @Composable
    fun RegisterButton( registerEnabled: Boolean, onContactSelected: () -> Unit) {
        Button(onClick = { onContactSelected() },
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
            Text(text = "Guardar y continuar")
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun NicknameField(nickname: String, onTextFieldChanged: (String) -> Unit) {
        TextField(
            value = nickname,
            onValueChange = { onTextFieldChanged(it) },
            placeholder = { Text(text = "Nombre del contacto", color = Color.Gray) },
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
            placeholder = { Text(text = "Correo electrónico del contacto", color = Color.Gray) },
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
    fun PhoneNumberField(phoneNumber: String, onTextFieldChanged: (String) -> Unit) {
        TextField(
            value = phoneNumber,
            onValueChange = { onTextFieldChanged(it) },
            placeholder = { Text(text = "Número Telefónico del contacto", color = Color.Gray) },
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
}