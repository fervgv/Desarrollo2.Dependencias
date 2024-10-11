package com.example.desarrollo2.core.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.desarrollo2.core.repositories.IUsuarioRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class RegistroViewModel @Inject constructor(
    private val repository: IUsuarioRepository // Inyección de la dependencia del repositorio
) : ViewModel() {

    // LiveData para los campos del formulario
    private val _email = MutableLiveData<String>()
    val email: LiveData<String> = _email

    private val _password = MutableLiveData<String>()
    val password: LiveData<String> = _password

    private val _confirmPassword = MutableLiveData<String>()
    val confirmPassword: LiveData<String> = _confirmPassword

    private val _name = MutableLiveData<String>()
    val name: LiveData<String> = _name

    private val _nickname = MutableLiveData<String>()
    val nickname: LiveData<String> = _nickname

    private val _phoneNumber = MutableLiveData<String>()
    val phoneNumber: LiveData<String> = _phoneNumber


    // LiveData para habilitar el boton de registro
    private val _registerEnable = MutableLiveData<Boolean>()
    val registerEnable: LiveData<Boolean> = _registerEnable

    // LiveData para mostrar el estado de carga
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    // LiveData para mensajes de error específicos por campo
    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    // Método para actualizar el mensaje de error
    fun validateFields(name: String, email: String, password: String, confirmPassword: String, nickname: String, phoneNumber: String) {
        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || nickname.isEmpty() || phoneNumber.isEmpty()) {
            _errorMessage.value = "Todos los campos son obligatorios"
        } else if (password != confirmPassword) {
            _errorMessage.value = "Las contraseñas no coinciden"
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _errorMessage.value = "Correo electrónico no válido"
        } else {
            _errorMessage.value = null  // No hay errores
            _registerEnable.value = true  // Habilita el botón si no hay errores
        }
    }

    init {
        _registerEnable.value = false
        _isLoading.value = false
    }

    // Validación y actualización de cada campo
    fun onNameChanged(name: String) {
        _name.value = name
        updateRegisterEnable() // Actualiza la habilitación del registro
    }

    fun onEmailChanged(email: String) {
        _email.value = email
        updateRegisterEnable() // Actualiza la habilitación del registro
    }

    fun onPasswordChanged(password: String) {
        _password.value = password
        updateRegisterEnable() // Actualiza la habilitación del registro
    }

    fun onConfirmPasswordChanged(confirmPassword: String) {
        _confirmPassword.value = confirmPassword
        updateRegisterEnable() // Actualiza la habilitación del registro
    }

    fun onNicknameChanged(nickname: String) {
        _nickname.value = nickname
        updateRegisterEnable() // Actualiza la habilitación del registro
    }

    fun onPhoneNumberChanged(phoneNumber: String) {
        _phoneNumber.value = phoneNumber
        updateRegisterEnable() // Actualiza la habilitación del registro
    }

    //  Actualiza el estado de habilitación del botón de registro basado en las validaciones
    private fun updateRegisterEnable() {
        _registerEnable.value = isValidName(_name.value ?: "") &&
                isValidEmail(_email.value ?: "") &&
                isValidPassword(_password.value ?: "") &&
                passwordsMatch(_password.value ?: "", _confirmPassword.value ?: "") &&
                isValidPhoneNumber(_phoneNumber.value ?: "")
    }

    // Validaciones individuales
    private fun isValidName(name: String): Boolean {
        return if (name.isBlank()) {
            _errorMessage.value = "El nombre no puede estar vacío."
            false
        } else {
            _errorMessage.value = null
            true
        }
    }

    private fun isValidEmail(email: String): Boolean {
        val pattern = Regex("^[A-Za-z0-9._%+-]+@virginiogomez\\.cl$")
        return if (!pattern.matches(email)) {
            _errorMessage.value = "El correo electrónico debe ser de dominio @virginiogomez.cl."
            false
        } else {
            _errorMessage.value = null
            true
        }
    }

    private fun isValidPassword(password: String): Boolean {
        val lengthValid = password.length >= 8
        val hasLetter = password.any { it.isLetter() }
        val hasDigit = password.any { it.isDigit() }
        val hasSpecialChar = password.any { !it.isLetterOrDigit() }

        return if (!lengthValid || !hasLetter || !hasDigit || !hasSpecialChar) {
            _errorMessage.value = "La contraseña debe tener al menos 8 caracteres, incluir una letra, un número y un carácter especial."
            false
        } else {
            _errorMessage.value = null
            true
        }
    }

    private fun passwordsMatch(password: String, confirmPassword: String): Boolean {
        return if (password != confirmPassword) {
            _errorMessage.value = "Las contraseñas no coinciden."
            false
        } else {
            _errorMessage.value = null
            true
        }
    }

    private fun isValidPhoneNumber(phoneNumber: String): Boolean {
        val pattern = Regex("^\\+569[0-9]{8}$")
        return if (!pattern.matches(phoneNumber)) {
            _errorMessage.value = "El número de teléfono debe ser válido y comenzar con +569."
            false
        } else {
            _errorMessage.value = null
            true
        }
    }

    // Método para manejar el registro
    fun onRegisterSelected(
        name: String,
        email: String,
        password: String,
        confirmPassword: String,
        nickname: String,
        phoneNumber: String
    ) {
        if (!isValidRegistration(name, email, password, confirmPassword, nickname, phoneNumber)) {
            return // No continuar si hay errores
        }

        _isLoading.value = true
        _errorMessage.value = null // Limpiar errores anteriores

        viewModelScope.launch {
            try {
                // Simular proceso de registro o llamar a la API
                kotlinx.coroutines.delay(2000)

                // Aquí manejarías el resultado de un registro exitoso
            } catch (e: Exception) {
                _errorMessage.value = "Error durante el registro: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Validación completa del formulario
    private fun isValidRegistration(
        name: String,
        email: String,
        password: String,
        confirmPassword: String,
        nickname: String,
        phoneNumber: String
    ): Boolean {
        return isValidName(name) &&
                isValidEmail(email) &&
                isValidPassword(password) &&
                passwordsMatch(password, confirmPassword) &&
                isValidPhoneNumber(phoneNumber)
    }

}