package com.example.desarrollo2.domain.usecases

import com.example.desarrollo2.core.models.ContactoEmergencia
import com.example.desarrollo2.core.repositories.ContactoEmergenciaRepository
import javax.inject.Inject

class AgregarContactoEmergenciaUseCase @Inject constructor(
    private val contactoEmergenciaRepository: ContactoEmergenciaRepository
) {
    suspend operator fun invoke(contacto: ContactoEmergencia): Boolean {
        return contactoEmergenciaRepository.agregarContacto(contacto)
    }
}
