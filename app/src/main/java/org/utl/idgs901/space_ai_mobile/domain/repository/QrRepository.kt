package org.utl.idgs901.space_ai_mobile.domain.repository

import org.utl.idgs901.space_ai_mobile.domain.model.QrIdentity

interface QrRepository {
    suspend fun getQr(userId: String): QrIdentity
}
