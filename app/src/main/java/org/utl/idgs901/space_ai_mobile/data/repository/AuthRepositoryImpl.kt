package org.utl.idgs901.space_ai_mobile.data.repository

import kotlinx.coroutines.flow.Flow
import org.utl.idgs901.space_ai_mobile.core.datastore.TokenManager
import org.utl.idgs901.space_ai_mobile.data.remote.api.AuthApi
import org.utl.idgs901.space_ai_mobile.data.remote.dto.LoginRequestDto
import org.utl.idgs901.space_ai_mobile.data.remote.mapper.toDomain
import org.utl.idgs901.space_ai_mobile.domain.model.User
import org.utl.idgs901.space_ai_mobile.domain.repository.AuthRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val authApi: AuthApi,
    private val tokenManager: TokenManager,
) : AuthRepository {

    override suspend fun login(email: String, password: String): Result<User> {
        return try {
            val response = authApi.login(LoginRequestDto(email, password))
            tokenManager.saveAccessToken(response.accessToken)
            tokenManager.saveRefreshToken(response.refreshToken)
            tokenManager.saveExpiresAt(System.currentTimeMillis() + (response.expiresIn * 1000))
            Result.success(response.user.toDomain())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun logout() {
        tokenManager.clearTokens()
    }

    override val isUserLoggedIn: Flow<Boolean> = tokenManager.isUserLoggedIn
}
