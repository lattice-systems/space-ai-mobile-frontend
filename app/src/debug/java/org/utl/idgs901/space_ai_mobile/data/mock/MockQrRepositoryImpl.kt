package org.utl.idgs901.space_ai_mobile.data.mock

import org.utl.idgs901.space_ai_mobile.domain.model.QrIdentity
import org.utl.idgs901.space_ai_mobile.domain.repository.QrRepository
import java.time.Instant
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MockQrRepositoryImpl @Inject constructor() : QrRepository {
    
    override suspend fun getQr(userId: String): QrIdentity {
        val generatedAt = Instant.now()
        // Simulate a 15-minute expiration as per requirements
        val expiresAt = generatedAt.plusSeconds(15 * 60)
        
        return QrIdentity(
            qrToken = "SPACEIA_USER_${userId}_${UUID.randomUUID().toString().take(8)}",
            generatedAt = generatedAt,
            expiresAt = expiresAt
        )
    }
}
