package com.example.desarrollo2.core.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.desarrollo2.core.repositories.IUsuarioRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import javax.inject.Inject


@HiltViewModel
class RegistroViewModel @Inject constructor(
    private val repository: IUsuarioRepository // Inyección de la dependencia del repositorio
) : ViewModel() {

    // LiveData para el correo electrónico
    private val _email = MutableLiveData<String>()
    val email: LiveData<String> = _email

    // LiveData para la contraseña
    private val _password = MutableLiveData<String>()
    val password: LiveData<String> = _password

    // LiveData para la confirmación de contraseña
    private val _confirmPassword = MutableLiveData<String>()
    val confirmPassword: LiveData<String> = _confirmPassword

    // LiveData para el nombre
    private val _name = MutableLiveData<String>()
    val name: LiveData<String> = _name

    // LiveData para el apodo
    private val _nickname = MutableLiveData<String>()
    val nickname: LiveData<String> = _nickname

    // LiveData para el número de teléfono
    private val _phoneNumber = MutableLiveData<String>()
    val phoneNumber: LiveData<String> = _phoneNumber

    // LiveData para habilitar el registro
    private val _registerEnable = MutableLiveData<Boolean>()
    val registerEnable: LiveData<Boolean> = _registerEnable

    // LiveData para el estado de carga
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    // Método para manejar el cambio de nombre
    fun onNameChanged(name: String) {
        _name.value = name
        updateRegisterEnable() // Actualiza la habilitación del registro
    }

    // Método para manejar el cambio de correo electrónico
    fun onEmailChanged(email: String) {
        _email.value = email
        updateRegisterEnable() // Actualiza la habilitación del registro
    }

    // Método para manejar el cambio de contraseña
    fun onPasswordChanged(password: String) {
        _password.value = password
        updateRegisterEnable() // Actualiza la habilitación del registro
    }

    // Método para manejar el cambio de confirmación de contraseña
    fun onConfirmPasswordChanged(confirmPassword: String) {
        _confirmPassword.value = confirmPassword
        updateRegisterEnable() // Actualiza la habilitación del registro
    }

    // Método para manejar el cambio de apodo
    fun onNicknameChanged(nickname: String) {
        _nickname.value = nickname
        updateRegisterEnable() // Actualiza la habilitación del registro
    }

    // Método para manejar el cambio de número de teléfono
    fun onPhoneNumberChanged(phoneNumber: String) {
        _phoneNumber.value = phoneNumber
        updateRegisterEnable() // Actualiza la habilitación del registro
    }

    // Actualiza el estado de habilitación del registro basado en la validez de los campos
    private fun updateRegisterEnable() {
        _registerEnable.value = isValidName(_name.value ?: "") &&
                isValidEmail(_email.value ?: "") &&
                isValidPassword(_password.value ?: "") &&
                passwordsMatch(_password.value ?: "", _confirmPassword.value ?: "") &&
                isValidPhoneNumber(_phoneNumber.value ?: "")
    }

    // Valida que el nombre no esté vacío
    private fun isValidName(name: String): Boolean = name.isNotBlank()

    // Valida que la contraseña tenga más de 6 caracteres
    private fun isValidPassword(password: String): Boolean = password.length > 6

    // Verifica que la contraseña y la confirmación coincidan
    private fun passwordsMatch(password: String, confirmPassword: String): Boolean = password == confirmPassword

    // Valida que el correo electrónico tenga el formato correcto
    private fun isValidEmail(email: String): Boolean {
        val pattern = Regex("^[A-Za-z0-9._%+-]+@virginiogomez\\.cl$")
        return pattern.matches(email)
    }

    // Valida que el número de teléfono tenga el formato correcto
    private fun isValidPhoneNumber(phoneNumber: String): Boolean {
        val pattern = Regex("^\\+569[0-9]{8}$")
        return pattern.matches(phoneNumber)
    }

    // Maneja la selección del registro
    fun onRegisterSelected(
        name: String,
        email: String,
        password: String,
        confirmPassword: String,
        nickname: String,
        phoneNumber: String
    ) {
        // Verifica si todos los campos son válidos antes de proceder
        if (isValidRegistration(name, email, password, confirmPassword, nickname, phoneNumber)) {
            // Procede con el registro
        } else {
            // Maneja el error, por ejemplo, mostrando un mensaje
        }
    }

    // Valida que todos los campos de registro sean correctos
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

    // Simula un proceso de inicio de sesión (carga)
    suspend fun onLoginSelected() {
        _isLoading.value = true
        delay(1000) // Simula un retraso de 1 segundo
        _isLoading.value = false
    }
}



