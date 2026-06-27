package org.utl.idgs901.space_ai_mobile.domain.location.usecase

import javax.inject.Inject
import kotlin.math.*

class IsInsideCampusUseCase @Inject constructor() {

    /**
     * Determines if a point is inside the campus boundary using the Ray-Casting algorithm.
     * Includes a 20m tolerance margin.
     */
    operator fun invoke(
        lat: Double,
        lng: Double,
        boundary: List<Pair<Double, Double>>
    ): Boolean {
        if (boundary.isEmpty()) return false

        // 1. Direct check: Point In Polygon
        if (isPointInPolygon(lat to lng, boundary)) return true

        // 2. Tolerance check: 20 meters
        return isPointNearBoundary(lat, lng, boundary, 20.0)
    }

    private fun isPointInPolygon(point: Pair<Double, Double>, polygon: List<Pair<Double, Double>>): Boolean {
        var intersectCount = 0
        for (i in 0 until polygon.size) {
            val next = (i + 1) % polygon.size
            if (rayCastIntersect(point, polygon[i], polygon[next])) {
                intersectCount++
            }
        }
        return intersectCount % 2 == 1
    }

    private fun rayCastIntersect(point: Pair<Double, Double>, vertA: Pair<Double, Double>, vertB: Pair<Double, Double>): Boolean {
        val (py, px) = point
        val (ay, ax) = vertA
        val (by, bx) = vertB
        if (ay > py && by > py) return false
        if (ay < py && by < py) return false
        if (ax < px && bx < px) return false
        
        // Avoid division by zero
        if (abs(by - ay) < 1e-10) return false
        
        val intersectX = ax + (py - ay) * (bx - ax) / (by - ay)
        return intersectX > px
    }

    private fun isPointNearBoundary(lat: Double, lng: Double, boundary: List<Pair<Double, Double>>, toleranceMeters: Double): Boolean {
        for (i in 0 until boundary.size) {
            val next = (i + 1) % boundary.size
            val dist = distanceToSegment(lat, lng, boundary[i], boundary[next])
            if (dist <= toleranceMeters) return true
        }
        return false
    }

    private fun distanceToSegment(lat: Double, lng: Double, a: Pair<Double, Double>, b: Pair<Double, Double>): Double {
        val r = 6371000.0 // Earth radius in meters
        
        // Simple approximation for short distances
        val x = lng * cos(Math.toRadians(lat))
        val y = lat
        
        val ax = a.second * cos(Math.toRadians(a.first))
        val ay = a.first
        
        val bx = b.second * cos(Math.toRadians(b.first))
        val by = b.first
        
        val l2 = (ax - bx).pow(2) + (ay - by).pow(2)
        if (l2 == 0.0) return haversine(lat, lng, a.first, a.second)
        
        var t = ((x - ax) * (bx - ax) + (y - ay) * (by - ay)) / l2
        t = max(0.0, min(1.0, t))
        
        val projLat = ay + t * (by - ay)
        val projLng = ax + t * (bx - ax) / cos(Math.toRadians(projLat))
        
        return haversine(lat, lng, projLat, projLng)
    }

    private fun haversine(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val r = 6371000.0
        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)
        val a = sin(dLat / 2).pow(2) + cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) * sin(dLon / 2).pow(2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        return r * c
    }
}
