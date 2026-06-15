package org.utl.idgs901.space_ai_mobile.data.remote.api

import org.utl.idgs901.space_ai_mobile.data.remote.dto.QrResponseDto
import retrofit2.http.GET
import retrofit2.http.Path

interface QrApi {
    @GET("auth/qr/{userId}")
    suspend fun getQr(
        @Path("userId") userId: String
    ): QrResponseDto
}
