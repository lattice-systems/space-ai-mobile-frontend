package org.utl.idgs901.space_ai_mobile.data.remote.api

import org.utl.idgs901.space_ai_mobile.data.remote.dto.LoginRequestDto
import org.utl.idgs901.space_ai_mobile.data.remote.dto.LoginResponseDto
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequestDto): LoginResponseDto
}
