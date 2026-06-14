package org.utl.idgs901.space_ai_mobile.domain.usecase

import org.utl.idgs901.space_ai_mobile.domain.model.User
import org.utl.idgs901.space_ai_mobile.domain.repository.AuthRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String): Result<User> {
        return repository.login(email, password)
    }
}
