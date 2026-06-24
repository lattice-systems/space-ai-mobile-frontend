package org.utl.idgs901.space_ai_mobile.domain.model

import java.time.Instant

data class QrIdentity(
    val qrToken: String,
    val generatedAt: Instant,
    val expiresAt: Instant
)
