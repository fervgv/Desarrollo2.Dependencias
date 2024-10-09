
package com.example.desarrollo2.core.viewmodels
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch


class LoginViewModel : ViewModel() {
    // Propiedades para el correo y la contraseña
    private val _email = MutableLiveData<String>()
    val email: LiveData<String> get() = _email

    private val _password = MutableLiveData<String>()
    val password: LiveData<String> get() = _password

    // Propiedad para habilitar el botón de inicio de sesión
    private val _loginEnable = MutableLiveData<Boolean>()
    val loginEnable: LiveData<Boolean> get() = _loginEnable

    // Propiedad para mostrar el estado de carga
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    // Propiedad para manejar errores
    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    init {
        _loginEnable.value = false
        _isLoading.value = false
    }

    fun onEmailChanged(newEmail: String) {
        _email.value = newEmail
        validateLogin()
    }

    fun onPasswordChanged(newPassword: String) {
        _password.value = newPassword
        validateLogin()
    }

    // Método para validar si el inicio de sesión está habilitado
    private fun validateLogin() {
        _loginEnable.value = !(_email.value.isNullOrEmpty() || _password.value.isNullOrEmpty())
    }

    // Método para manejar el inicio de sesión
    fun onLoginSelected(email: String, password: String) {
        // Validaciones
        if (!isEmailValid(email)) {
            _errorMessage.value = "El correo electrónico debe ser de dominio @virginiogomez.cl."
            return
        }

        if (!isPasswordValid(password)) {
            _errorMessage.value = "La contraseña debe tener al menos 8 caracteres, incluir una letra, un número y un carácter especial."
            return
        }

        // Iniciar el proceso de inicio de sesión
        _isLoading.value = true
        _errorMessage.value = null // Limpiar el mensaje de error anterior

        viewModelScope.launch {
            // Simulación de un proceso de inicio de sesión
            try {
                // Aquí puedes agregar tu lógica de autenticación
                // Por ejemplo, hacer una llamada a la API para validar el usuario

                // Simulando un retraso para la autenticación
                kotlinx.coroutines.delay(2000)

                // Aquí deberías manejar el resultado de la autenticación
                // Por ejemplo, si el inicio de sesión es exitoso:
                // Redirect to another screen or update LiveData to reflect success
            } catch (e: Exception) {
                // Manejar cualquier error que ocurra durante el inicio de sesión
                _errorMessage.value = "Error en el inicio de sesión: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Método para validar el formato del correo electrónico
    private fun isEmailValid(email: String): Boolean {
        // Validar que el correo termine en @virginiogomez.cl
        return email.endsWith("@virginiogomez.cl")
    }

    // Método para validar la fortaleza de la contraseña
    private fun isPasswordValid(password: String): Boolean {
        // Validar que la contraseña tenga al menos 8 caracteres, incluya al menos una letra, un número y un carácter especial
        val lengthValid = password.length >= 8
        val hasLetter = password.any { it.isLetter() }
        val hasDigit = password.any { it.isDigit() }
        val hasSpecialChar = password.any { !it.isLetterOrDigit() }
        return lengthValid && hasLetter && hasDigit && hasSpecialChar
    }
}
