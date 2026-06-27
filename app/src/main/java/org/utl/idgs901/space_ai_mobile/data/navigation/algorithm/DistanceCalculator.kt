package org.utl.idgs901.space_ai_mobile.data.navigation.algorithm

import javax.inject.Inject
import kotlin.math.*

class DistanceCalculator @Inject constructor() {

    private val EARTH_RADIUS = 6371000.0 // Meters
    private val WALKING_SPEED = 1.4 // Meters/second

    fun calculateDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)
        val a = sin(dLat / 2).pow(2) + cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) * sin(dLon / 2).pow(2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        return EARTH_RADIUS * c
    }

    fun calculateWalkingTime(distanceMeters: Double): Double {
        return distanceMeters / WALKING_SPEED
    }
}
