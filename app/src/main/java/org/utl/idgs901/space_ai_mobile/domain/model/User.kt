package org.utl.idgs901.space_ai_mobile.domain.model

data class User(
    val id: String,
    val name: String,
    val email: String,
    val folio: String?,
    val role: String
)
