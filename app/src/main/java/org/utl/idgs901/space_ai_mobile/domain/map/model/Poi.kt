package org.utl.idgs901.space_ai_mobile.domain.map.model

data class Poi(
    val id: String,
    val name: String,
    val category: String,
    val icon: String,
    val latitude: Double = 0.0,
    val longitude: Double = 0.0
)
