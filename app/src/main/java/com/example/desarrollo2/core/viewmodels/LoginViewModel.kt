package com.example.desarrollo2.core.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.desarrollo2.core.repositories.IUsuarioRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: IUsuarioRepository
) : ViewModel() {

    ////// esto es para el login
    private val _email = MutableLiveData<String>()
    val email : LiveData<String> = _email

    private val _password = MutableLiveData<String>()
    val password : LiveData<String> = _password

    //aqui habilita y deshabilita el boton de registro
    private val _loginEnable = MutableLiveData<Boolean>()
    val loginEnable : LiveData<Boolean> = _loginEnable

    //pantalla de carga
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading : LiveData<Boolean> = _isLoading

    init {
        email.observeForever { validateLoginFields() }
        password.observeForever { validateLoginFields() }
    }

    private fun validateLoginFields() {
        val isValid = !email.value.isNullOrEmpty() && !password.value.isNullOrEmpty()
        _loginEnable.value = isValid
    }

    fun onLoginSelected(email: String, password: String) {
        // Lógica para manejar el inicio de sesión
        //isLoading.value = true
        // Aquí puedes agregar la lógica para llamar a una API o verificar credenciales
        // Cuando el proceso de login termina:
        //isLoading.value = false
    }

    //reacciona a los cambios en el formulario
    fun onEmailChanged(email: String) {
        _email.value = email
        updateLoginEnable()
    }

    fun onPasswordChanged(password: String) {
        _password.value = password
        updateLoginEnable()
    }

    private fun isValidPassword(password: String): Boolean = password.length > 6
    private fun isValidEmail(email: String): Boolean = email.length > 6 //{
    //val pattern = Regex("^[A-Za-z0-9._%+-]+@virginiogomez\\.cl$") // Define la extension de correo valido
    //return pattern.matches(email)
    //}

    //esto es para actualizar el boton de registro
    private fun updateLoginEnable() {
        _loginEnable.value =
                isValidEmail(_email.value ?: "") &&
                isValidPassword(_password.value ?: "")
    }

    //muestra un circulito de carga por 1 segundo, es chamullo para el usuario
    suspend fun onLoginSelected() {
        _isLoading.value = true
        delay(1000)
        _isLoading.value = false
    }
}