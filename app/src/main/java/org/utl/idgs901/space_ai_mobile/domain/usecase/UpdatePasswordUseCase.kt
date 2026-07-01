package org.utl.idgs901.space_ai_mobile.domain.usecase

import org.utl.idgs901.space_ai_mobile.domain.repository.UserRepository
import javax.inject.Inject

class UpdatePasswordUseCase @Inject constructor(
    private val repository: UserRepository
) {
    suspend operator fun invoke(current: String, new: String): Result<Unit> {
        return repository.updatePassword(current, new)
    }
}
