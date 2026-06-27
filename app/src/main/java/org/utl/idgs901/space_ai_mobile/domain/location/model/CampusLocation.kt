package org.utl.idgs901.space_ai_mobile.domain.location.model

data class CampusLocation(
    val latitude: Double,
    val longitude: Double,
    val accuracy: Float,
    val timestamp: Long
)
