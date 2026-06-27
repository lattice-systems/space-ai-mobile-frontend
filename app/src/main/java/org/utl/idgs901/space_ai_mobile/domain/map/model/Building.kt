package org.utl.idgs901.space_ai_mobile.domain.map.model

data class Building(
    val id: String,
    val name: String,
    val category: String,
    val levels: Int,
    val height: Double,
    val color: String,
    val icon: String = "school",
    val status: String = "Disponible",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0
)
