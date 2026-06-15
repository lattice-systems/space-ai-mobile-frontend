package org.utl.idgs901.space_ai_mobile.data.remote.dto

import com.google.gson.annotations.SerializedName

data class QrResponseDto(
    @SerializedName("qrToken") val qrToken: String,
    @SerializedName("generatedAt") val generatedAt: String,
    @SerializedName("expiresAt") val expiresAt: String
)
