package org.utl.idgs901.space_ai_mobile.data.navigation.tracker

import org.utl.idgs901.space_ai_mobile.data.navigation.algorithm.DistanceCalculator
import org.utl.idgs901.space_ai_mobile.domain.navigation.model.*
import javax.inject.Inject
import kotlin.math.*

class CurrentRouteTracker @Inject constructor(
    private val distanceCalculator: DistanceCalculator
) {

    fun getRemainingDistance(userLat: Double, userLng: Double, route: NavigationRoute, currentStep: Int): Double {
        val path = route.path
        if (currentStep >= path.size - 1) return 0.0
        
        // 1. Distance from user to next node
        var totalDist = distanceCalculator.calculateDistance(
            userLat, userLng,
            path[currentStep + 1].latitude, path[currentStep + 1].longitude
        )
        
        // 2. Sum all remaining segments
        for (i in currentStep + 1 until path.size - 1) {
            totalDist += distanceCalculator.calculateDistance(
                path[i].latitude, path[i].longitude,
                path[i + 1].latitude, path[i + 1].longitude
            )
        }
        
        return totalDist
    }

    fun isNearNode(userLat: Double, userLng: Double, node: GraphNode, radiusMeters: Double = 5.0): Boolean {
        val dist = distanceCalculator.calculateDistance(userLat, userLng, node.latitude, node.longitude)
        return dist <= radiusMeters
    }

    fun getDeviationDistance(userLat: Double, userLng: Double, route: NavigationRoute, currentStep: Int): Double {
        val path = route.path
        if (currentStep >= path.size - 1) return 100.0 // Far if no segments left
        
        val p1 = path[currentStep]
        val p2 = path[currentStep + 1]
        
        return distanceToSegment(userLat, userLng, p1.latitude, p1.longitude, p2.latitude, p2.longitude)
    }

    private fun distanceToSegment(px: Double, py: Double, x1: Double, y1: Double, x2: Double, y2: Double): Double {
        val l2 = (x1 - x2).pow(2) + (y1 - y2).pow(2)
        if (l2 == 0.0) return distanceCalculator.calculateDistance(px, py, x1, y1)
        
        var t = ((px - x1) * (x2 - x1) + (py - y1) * (y2 - y1)) / l2
        t = max(0.0, min(1.0, t))
        
        val projX = x1 + t * (x2 - x1)
        val projY = y1 + t * (y2 - y1)
        
        return distanceCalculator.calculateDistance(px, py, projX, projY)
    }
}
