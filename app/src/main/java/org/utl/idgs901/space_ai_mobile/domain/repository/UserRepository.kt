package org.utl.idgs901.space_ai_mobile.domain.repository

import org.utl.idgs901.space_ai_mobile.domain.model.UserProfile

interface UserRepository {
    suspend fun getUserProfile(userId: String): Result<UserProfile>
    suspend fun updatePassword(currentPassword: String, newPassword: String): Result<Unit>
}
