package com.example.desarrollo2.core.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.desarrollo2.core.models.Usuario
import com.example.desarrollo2.core.repositories.IUsuarioRepository
import kotlinx.coroutines.launch

class UsuarioViewModel(private val repository: IUsuarioRepository) : ViewModel() {

    fun registrarUsuario(usuario: Usuario) {
        viewModelScope.launch {
            val result = repository.agregarUsuario(usuario)
            // Manejar el resultado (por ejemplo, mostrar un mensaje)
        }
    }

    // Otros métodos para interactuar con el repositorio
}
