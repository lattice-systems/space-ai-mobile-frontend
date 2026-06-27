package org.utl.idgs901.space_ai_mobile.core.util

import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DistanceFormatter @Inject constructor() {
    fun format(meters: Double): String {
        return if (meters >= 1000) {
            String.format(Locale.getDefault(), "%.1f km", meters / 1000.0)
        } else {
            "${meters.toInt()} m"
        }
    }
}

@Singleton
class TimeFormatter @Inject constructor() {
    fun format(seconds: Double): String {
        val minutes = (seconds / 60.0).toInt()
        return if (minutes < 1) {
            "Menos de 1 min"
        } else {
            "$minutes min"
        }
    }
}
