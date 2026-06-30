package org.utl.idgs901.space_ai_mobile.domain.usecase

import org.utl.idgs901.space_ai_mobile.domain.model.User
import javax.inject.Inject

class GetCurrentUserUseCase @Inject constructor() {
    /**
     * For now, this returns a mock user as we are in Sprint 1.
     * In the future, this could be fetched from a Local Database or Repository.
     */
    operator fun invoke(): User {
        return User(
            id = "alex_sterling_8892",
            name = "Juan Pablo Rea Cano",
            email = "juan@utleon.edu.mx",
            folio = "IDGS-801",
            role = "ESTUDIANTE DE INGENIERÍA"
        )
    }
}
