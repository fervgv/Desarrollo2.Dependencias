package com.example.desarrollo2.ui.screens
import androidx.compose.ui.Modifier
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.padding
import dagger.hilt.android.AndroidEntryPoint
import com.example.desarrollo2.core.models.Usuario
import com.example.desarrollo2.core.viewmodels.UsuarioViewModel
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview


@AndroidEntryPoint
class MiActividad : ComponentActivity() {

    // Aquí se obtiene la instancia de UsuarioViewModel
    private val usuarioViewModel: UsuarioViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Contenido de la interfaz de usuario
            MiPantalla()
        }

        // Ahora puedes usar usuarioViewModel para interactuar con los datos
        val nuevoUsuario = Usuario("nickname", "Nombre Completo", "correo@virginiogomez.cl", "contraseña", emptyList())
        usuarioViewModel.registrarUsuario(nuevoUsuario)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MiPantalla() {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Registro de Usuario") })
        }
    ) { innerPadding ->
        // Contenido de tu pantalla aquí
        // Por ejemplo, un texto de bienvenida
        Text(
            text = "Bienvenido a la actividad de registro.",
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MiPantalla()
}

