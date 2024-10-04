package com.example.desarrollo2.domain.usescases
import com.example.desarrollo2.core.models.Usuario
import com.example.desarrollo2.core.repositories.IUsuarioRepository
import javax.inject.Inject


class RegistrarUsuarioUseCase @Inject constructor(
    private val usuarioRepository: IUsuarioRepository
) {
    suspend operator fun invoke(usuario: Usuario): Boolean {
        return if (usuario.correo.endsWith("@virginiogomez.cl")) {
            usuarioRepository.agregarUsuario(usuario)
        } else {
            false
        }
    }
}
