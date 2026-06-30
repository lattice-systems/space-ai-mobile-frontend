package org.utl.idgs901.space_ai_mobile.domain.usecase

import org.utl.idgs901.space_ai_mobile.domain.repository.AuthRepository
import javax.inject.Inject

class LogoutUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke() {
        repository.logout()
    }
}
