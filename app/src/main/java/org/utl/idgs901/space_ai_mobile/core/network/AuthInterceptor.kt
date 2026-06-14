package org.utl.idgs901.space_ai_mobile.core.network

import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import org.utl.idgs901.space_ai_mobile.core.datastore.TokenManager
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthInterceptor @Inject constructor(
    private val tokenManager: TokenManager
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        
        // Skip adding token for public endpoints if needed
        if (request.url.encodedPath.contains("/auth/login") || 
            request.url.encodedPath.contains("/auth/register")) {
            return chain.proceed(request)
        }

        val token = runBlocking {
            tokenManager.getAccessToken()
        }

        val authenticatedRequest = if (token != null) {
            request.newBuilder()
                .header("Authorization", "Bearer $token")
                .build()
        } else {
            request
        }

        return chain.proceed(authenticatedRequest)
    }
}
