package org.utl.idgs901.space_ai_mobile.presentation.navigation

import org.maplibre.android.camera.CameraPosition
import org.maplibre.android.camera.CameraUpdateFactory
import org.maplibre.android.geometry.LatLng
import org.maplibre.android.maps.MapLibreMap
import org.utl.idgs901.space_ai_mobile.domain.navigation.model.ActiveNavigation
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.*

@Singleton
class NavigationCameraController @Inject constructor() {

    fun updateCamera(map: MapLibreMap, userLocation: LatLng, active: ActiveNavigation?) {
        if (active == null) return

        val path = active.route.path
        val step = active.currentStep
        
        // Calculate bearing of current segment
        var targetBearing = 0.0
        if (step < path.size - 1) {
            val curr = path[step]
            val next = path[step + 1]
            targetBearing = calculateBearing(curr.latitude, curr.longitude, next.latitude, next.longitude)
        }

        val cameraPosition = CameraPosition.Builder()
            .target(userLocation)
            .zoom(18.5)
            .tilt(60.0)
            .bearing(targetBearing)
            .build()

        map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), 1000)
    }

    private fun calculateBearing(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val phi1 = Math.toRadians(lat1)
        val phi2 = Math.toRadians(lat2)
        val deltaLambda = Math.toRadians(lon2 - lon1)
        val y = sin(deltaLambda) * cos(phi2)
        val x = cos(phi1) * sin(phi2) - sin(phi1) * cos(phi2) * cos(deltaLambda)
        return (Math.toDegrees(atan2(y, x)) + 360) % 360
    }
}
