package org.utl.idgs901.space_ai_mobile.domain.repository

import kotlinx.coroutines.flow.Flow
import org.utl.idgs901.space_ai_mobile.domain.model.User

interface AuthRepository {
    suspend fun login(email: String, password: String): Result<User>
    suspend fun logout()
    val isUserLoggedIn: Flow<Boolean>
}
