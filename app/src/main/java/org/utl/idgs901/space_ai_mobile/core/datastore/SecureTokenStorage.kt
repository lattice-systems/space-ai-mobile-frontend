package org.utl.idgs901.space_ai_mobile.core.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.utl.idgs901.space_ai_mobile.core.security.CryptoManager
import javax.inject.Inject
import javax.inject.Singleton

private val Context.tokenDataStore: DataStore<AuthTokens> by dataStore(
    fileName = "tokens.pb",
    serializer = TokenSerializer
)

@Singleton
class SecureTokenStorage @Inject constructor(
    @ApplicationContext private val context: Context,
    private val cryptoManager: CryptoManager
) {
    val tokens: Flow<AuthTokens> = context.tokenDataStore.data

    suspend fun updateAccessToken(token: String) {
        val encryptedToken = cryptoManager.encrypt(token)
        context.tokenDataStore.updateData { currentTokens ->
            currentTokens.toBuilder()
                .setAccessToken(encryptedToken)
                .build()
        }
    }

    suspend fun updateRefreshToken(token: String) {
        val encryptedToken = cryptoManager.encrypt(token)
        context.tokenDataStore.updateData { currentTokens ->
            currentTokens.toBuilder()
                .setRefreshToken(encryptedToken)
                .build()
        }
    }

    suspend fun updateExpiresAt(expiresAt: Long) {
        context.tokenDataStore.updateData { currentTokens ->
            currentTokens.toBuilder()
                .setExpiresAt(expiresAt)
                .build()
        }
    }

    suspend fun clearTokens() {
        context.tokenDataStore.updateData {
            AuthTokens.getDefaultInstance()
        }
    }

    fun getDecryptedAccessToken(encryptedToken: String): String {
        return cryptoManager.decrypt(encryptedToken)
    }

    fun getDecryptedRefreshToken(encryptedToken: String): String {
        return cryptoManager.decrypt(encryptedToken)
    }
}
