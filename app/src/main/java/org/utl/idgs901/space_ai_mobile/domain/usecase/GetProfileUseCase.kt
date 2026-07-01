package org.utl.idgs901.space_ai_mobile.domain.usecase

import org.utl.idgs901.space_ai_mobile.domain.model.UserProfile
import org.utl.idgs901.space_ai_mobile.domain.repository.UserRepository
import javax.inject.Inject

class GetProfileUseCase @Inject constructor(
    private val repository: UserRepository
) {
    suspend operator fun invoke(userId: String): Result<UserProfile> {
        return repository.getUserProfile(userId)
    }
}
