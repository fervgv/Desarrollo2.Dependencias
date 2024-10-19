package com.example.desarrollo2.core.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AddContactViewModel @Inject constructor() : ViewModel() {

    private val _contactName = MutableLiveData("")
    val contactName: LiveData<String> = _contactName

    private val _contactEmail = MutableLiveData("")
    val contactEmail: LiveData<String> = _contactEmail

    private val _contactPhoneNumber = MutableLiveData("")
    val contactPhoneNumber: LiveData<String> = _contactPhoneNumber

    private val _isContactValid = MutableLiveData(false)
    val isContactValid: LiveData<Boolean> = _isContactValid

    // LiveData para habilitar el boton de registro
    private val _contactoEnable = MutableLiveData<Boolean>()
    val contactoEnable: LiveData<Boolean> = _contactoEnable

    // LiveData para mostrar el estado de carga
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    // LiveData para mensajes de error específicos por campo
    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    // Método para actualizar el mensaje de error en ContactViewModel
    fun validateContactFields(nickname: String, email: String, phoneNumber: String) {
        if (nickname.isEmpty() || email.isEmpty() || phoneNumber.isEmpty()) {
            _errorMessage.value = "Todos los campos son obligatorios"
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _errorMessage.value = "Correo electrónico no válido"
        } else if (!phoneNumber.matches(Regex("^[+]?[0-9]{10,13}\$"))) {
            _errorMessage.value = "Número de teléfono no válido"
        } else {
            _errorMessage.value = null  // No hay errores
            _isContactValid.value = true  // Habilita el botón si no hay errores
        }
    }


    init {
        _isLoading.value = false
    }

    fun onNicknameChanged(newNickname: String) {
        _contactName.value = newNickname
        validateContact()
    }

    fun onEmailChanged(newEmail: String) {
        _contactEmail.value = newEmail
        validateContact()
    }

    fun onPhoneNumberChanged(newPhoneNumber: String) {
        _contactPhoneNumber.value = newPhoneNumber
        validateContact()
    }

    //  Actualiza el estado de habilitación del botón de registro basado en las validaciones
    private fun updateContactEnable() {
        _contactoEnable.value = isValidContactName(_contactName.value ?: "") &&
                isValidContactEmail(_contactEmail.value ?: "") &&
                isValidContactPhoneNumber(_contactPhoneNumber.value ?: "")
    }

    //validaciones individuales
    private fun isValidContactName(contactName: String): Boolean {
        return if (contactName.isBlank()) {
            _errorMessage.value = "El nombre del contacto no puede estar vacío."
            false
        } else {
            _errorMessage.value = null
            true
        }
    }

    private fun isValidContactEmail(contactEmail: String): Boolean {
        val pattern = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$")
        return if (!pattern.matches(contactEmail)) {
            _errorMessage.value = "El correo electrónico no puede estar vacio."
            false
        } else {
            _errorMessage.value = null
            true
        }
    }

    private fun isValidContactPhoneNumber(contactPhoneNumber: String): Boolean {
        val pattern = Regex("^\\+569[0-9]{8}$")
        return if (!pattern.matches(contactPhoneNumber)) {
            _errorMessage.value = "El número de teléfono debe ser válido y comenzar con +569."
            false
        } else {
            _errorMessage.value = null
            true
        }
    }

    private fun validateContact() {
        _isContactValid.value = !(_contactName.value.isNullOrEmpty() || _contactEmail.value.isNullOrEmpty() || _contactPhoneNumber.value.isNullOrEmpty())
    }

    fun addContact(nickname: String, email: String, phoneNumber: String) {
        // Aquí puedes añadir la lógica para agregar el contacto a la base de datos
    }

}