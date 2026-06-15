package org.utl.idgs901.space_ai_mobile.data.repository

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import org.utl.idgs901.space_ai_mobile.core.datastore.TokenManager
import org.utl.idgs901.space_ai_mobile.domain.model.User
import org.utl.idgs901.space_ai_mobile.domain.repository.AuthRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MockAuthRepositoryImpl @Inject constructor(
    private val tokenManager: TokenManager
) : AuthRepository {

    private val _isUserLoggedIn = MutableStateFlow(false)

    override suspend fun login(email: String, password: String): Result<User> {
        // Simulate network delay
        delay(1500)

        return if (email == "ejemplo@gmail.com" && password == "admin12345") {
            val mockUser = User(
                id = "mock-uuid-1234",
                name = "Juan Pablo Rea Cano",
                email = email,
                folio = "23001501",
                role = "student"
            )

            // Persist mock tokens
            tokenManager.saveAccessToken("mock_access_token_jwt")
            tokenManager.saveRefreshToken("mock_refresh_token")
            tokenManager.saveExpiresAt(System.currentTimeMillis() + 3600000)
            
            _isUserLoggedIn.value = true
            Result.success(mockUser)
        } else {
            Result.failure(Exception("Invalid credentials (Mock Mode)"))
        }
    }

    override suspend fun logout() {
        tokenManager.clearTokens()
        _isUserLoggedIn.value = false
    }

    override val isUserLoggedIn: Flow<Boolean> = tokenManager.isUserLoggedIn
}
