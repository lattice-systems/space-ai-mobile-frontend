package org.utl.idgs901.space_ai_mobile.domain.map.repository

import org.utl.idgs901.space_ai_mobile.domain.map.model.Building
import org.utl.idgs901.space_ai_mobile.domain.map.model.Poi

interface MapRepository {
    suspend fun getBuildings(): List<Building>
    suspend fun getPois(): List<Poi>
    suspend fun getBuildingsGeoJson(): String
    suspend fun getWalkwaysGeoJson(): String
    suspend fun getCampusBoundary(): List<Pair<Double, Double>>
}
