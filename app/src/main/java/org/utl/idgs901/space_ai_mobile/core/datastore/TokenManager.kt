package org.utl.idgs901.space_ai_mobile.core.datastore

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenManager @Inject constructor(
    private val secureTokenStorage: SecureTokenStorage
) {
    val accessToken: Flow<String?> = secureTokenStorage.tokens.map { 
        val token = it.accessToken
        if (token.isEmpty()) null else secureTokenStorage.getDecryptedAccessToken(token)
    }

    val refreshToken: Flow<String?> = secureTokenStorage.tokens.map { 
        val token = it.refreshToken
        if (token.isEmpty()) null else secureTokenStorage.getDecryptedRefreshToken(token)
    }

    val isUserLoggedIn: Flow<Boolean> = accessToken.map { it != null }

    suspend fun saveAccessToken(token: String) {
        secureTokenStorage.updateAccessToken(token)
    }

    suspend fun saveRefreshToken(token: String) {
        secureTokenStorage.updateRefreshToken(token)
    }

    suspend fun saveExpiresAt(expiresAt: Long) {
        secureTokenStorage.updateExpiresAt(expiresAt)
    }

    suspend fun getAccessToken(): String? = accessToken.first()

    suspend fun getRefreshToken(): String? = refreshToken.first()

    suspend fun clearTokens() {
        secureTokenStorage.clearTokens()
    }
}
