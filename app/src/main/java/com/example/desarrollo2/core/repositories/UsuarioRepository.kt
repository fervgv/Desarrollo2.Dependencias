package com.example.desarrollo2.core.repositories

import com.example.desarrollo2.core.models.Usuario
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await
import javax.inject.Inject // Importa la anotación

class UsuarioRepository @Inject constructor() : IUsuarioRepository {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance().reference

    override suspend fun agregarUsuario(usuario: Usuario): Boolean {
        return try {
            val result = auth.createUserWithEmailAndPassword(usuario.correo, usuario.contraseña).await()
            val uid = result.user?.uid ?: return false
            database.child("usuarios").child(uid).setValue(usuario).await()
            true
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun obtenerUsuario(correo: String): Usuario? {
        return try {
            val snapshot = database.child("usuarios").orderByChild("correo").equalTo(correo).get().await()
            if (snapshot.exists()) {
                snapshot.children.first().getValue(Usuario::class.java)
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }
}
