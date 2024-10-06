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
    private val repository: IUsuarioRepository
) : ViewModel(){

    //guion bajo por delante por que es privado
    //esto es para el formulario, email, contraseña, nombre, nickname, numero de telefono
    private val _email = MutableLiveData<String>()
    val email : LiveData<String> = _email

    private val _password = MutableLiveData<String>()
    val password : LiveData<String> = _password

    private val _confirmPassword = MutableLiveData<String>()
    val confirmPassword : LiveData<String> = _confirmPassword

    private val _name = MutableLiveData<String>()
    val name : LiveData<String> = _name

    private val _nickname = MutableLiveData<String>()
    val nickname : LiveData<String> = _nickname

    private val _phoneNumber = MutableLiveData<String>()
    val phoneNumber: LiveData<String> = _phoneNumber

    //aqui habilita y deshabilita el boton de registro
    private val _registerEnable = MutableLiveData<Boolean>()
    val registerEnable : LiveData<Boolean> = _registerEnable

    //pantalla de carga
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading : LiveData<Boolean> = _isLoading

    //aqui reacciona a los cambios en el formulario
    fun onNameChanged(name: String) {
        _name.value = name
        updateRegisterEnable()
    }

    fun onEmailChanged(email: String) {
        _email.value = email
        updateRegisterEnable()
    }

    fun onPasswordChanged(password: String) {
        _password.value = password
        updateRegisterEnable()
    }

    fun onConfirmPasswordChanged(confirmPassword: String) {
        _confirmPassword.value = confirmPassword
        updateRegisterEnable()
    }

    fun onNicknameChanged(nickname: String) {
        _nickname.value = nickname
        updateRegisterEnable()
    }

    fun onPhoneNumberChanged(phoneNumber: String) {
        _phoneNumber.value = phoneNumber
        updateRegisterEnable()
    }

    //esto es para actualizar el boton de registro
    private fun updateRegisterEnable() {
        _registerEnable.value = isValidName(_name.value ?: "") &&
                //isValidEmail(_email.value ?: "") &&
                isValidPassword(_password.value ?: "") &&
                passwordsMatch(_password.value ?: "", _confirmPassword.value ?: "") //&&
        //isValidPhoneNumber(_phoneNumber.value ?: "")
    }

    // valida que el nombre no este vacio
    private fun isValidName(name: String): Boolean = name.isNotBlank()

    //aqui valida la contraseña, pero solo si es mas de 6 caracteres, hay que hacer que valide contraseñas reales
    //PENDIENTE 💀💀💀💀💀💀💀💀💀💀💀💀💀💀💀💀💀💀💀💀💀💀
    private fun isValidPassword(password: String): Boolean = password.length > 6

    //valida que las contraseñas sean iguales
    private fun passwordsMatch(password: String, confirmPassword: String): Boolean = password == confirmPassword

    //aqui valida que sea un correo con @virginiogomez.cl
    //por ahora en comentario para que no sea tan molesto con el testing
    /////////////////////////////////////////////////////////
    /*private fun isValidEmail(email: String): Boolean {
        val pattern = Regex("^[A-Za-z0-9._%+-]+@virginiogomez\\.cl$") // Define la extension de correo valido
        return pattern.matches(email)
    }

    //valida que el numero de telefono sea valido
    private fun isValidPhoneNumber(phoneNumber: String): Boolean {
        val pattern = Regex("^\\+569[0-9]{8}$") // +56 9 seguido por 8 digitos
        return pattern.matches(phoneNumber)
    }*/

    fun onRegisterSelected(
        name: String,
        email: String,
        password: String,
        confirmPassword: String,
        nickname: String,
        phoneNumber: String
    ) {
        // Aquí iría la lógica para registrar al usuario
        // Por ejemplo, podrías hacer una llamada a un repositorio o API
        if (isValidRegistration(name, email, password, confirmPassword, nickname, phoneNumber)) {
            // Procede con el registro
        } else {
            // Maneja el error, por ejemplo, mostrando un mensaje
        }
    }

    // este es el metodo para validar el registro
    private fun isValidRegistration(
        name: String,
        email: String,
        password: String,
        confirmPassword: String,
        nickname: String,
        phoneNumber: String
    ): Boolean {
        return isValidName(name) &&
                //isValidEmail(email) &&
                isValidPassword(password) &&
                passwordsMatch(password, confirmPassword) //&&
        //isValidPhoneNumber(phoneNumber)
    }

    //muestra un circulito de carga por 1 segundo, es chamullo para el usuario
    suspend fun onLoginSelected() {
        _isLoading.value = true
        delay(1000)
        _isLoading.value = false
    }
}