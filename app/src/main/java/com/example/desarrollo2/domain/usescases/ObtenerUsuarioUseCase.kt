package com.example.desarrollo2.domain.usecases

import com.example.desarrollo2.core.models.Usuario
import com.example.desarrollo2.core.repositories.IUsuarioRepository
import javax.inject.Inject

class ObtenerUsuarioUseCase @Inject constructor(
    private val usuarioRepository: IUsuarioRepository
) {
    suspend operator fun invoke(correo: String): Usuario? {
        return usuarioRepository.obtenerUsuario(correo)
    }
}
