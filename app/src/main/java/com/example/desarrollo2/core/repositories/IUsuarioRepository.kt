
package com.example.desarrollo2.core.repositories
import com.example.desarrollo2.core.models.Usuario

interface IUsuarioRepository {
    suspend fun agregarUsuario(usuario: Usuario): Boolean
    suspend fun obtenerUsuario(correo: String): Usuario?
    // Otras funciones que necesites
}