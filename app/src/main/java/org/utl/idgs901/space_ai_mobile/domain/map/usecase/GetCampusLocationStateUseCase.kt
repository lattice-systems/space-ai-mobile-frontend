package org.utl.idgs901.space_ai_mobile.domain.map.usecase

import org.utl.idgs901.space_ai_mobile.domain.map.model.CampusLocationState
import org.utl.idgs901.space_ai_mobile.domain.map.repository.MapRepository
import javax.inject.Inject

class GetCampusLocationStateUseCase @Inject constructor(
    private val repository: MapRepository
) {
    suspend operator fun invoke(lat: Double, lng: Double): CampusLocationState {
        val boundary = repository.getCampusBoundary()
        return if (isPointInPolygon(lat to lng, boundary)) {
            CampusLocationState.Inside
        } else {
            CampusLocationState.Outside
        }
    }

    private fun isPointInPolygon(point: Pair<Double, Double>, polygon: List<Pair<Double, Double>>): Boolean {
        if (polygon.isEmpty()) return false
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
        val intersectX = ax + (py - ay) * (bx - ax) / (by - ay)
        return intersectX > px
    }
}
