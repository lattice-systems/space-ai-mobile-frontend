package org.utl.idgs901.space_ai_mobile.domain.model

data class UserProfile(
    val id: String,
    val name: String,
    val email: String,
    val matricula: String,
    val group: String,
    val career: String,
    val division: String,
    val campus: String,
    val phone: String,
    val isVerified: Boolean = true,
    val profilePictureUrl: String? = null
)
