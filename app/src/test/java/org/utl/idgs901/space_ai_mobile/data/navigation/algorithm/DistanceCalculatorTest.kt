package org.utl.idgs901.space_ai_mobile.data.navigation.algorithm

import org.junit.Assert.assertEquals
import org.junit.Test

class DistanceCalculatorTest {

    private val calculator = DistanceCalculator()

    @Test
    fun `calculateDistance returns correct meters between two points`() {
        // UTL Entrance to Rectoría approx
        val lat1 = 21.0626202
        val lon1 = -101.5818894
        val lat2 = 21.0635383
        val lon2 = -101.5808716
        
        val result = calculator.calculateDistance(lat1, lon1, lat2, lon2)
        
        // Expected distance is ~147 meters based on Haversine
        assertEquals(147.0, result, 5.0) // 5m tolerance
    }

    @Test
    fun `calculateWalkingTime returns correct minutes`() {
        val distance = 240.0 // meters
        val resultSeconds = calculator.calculateWalkingTime(distance)
        val resultMinutes = resultSeconds / 60.0
        
        // 240 / 1.4 = 171.4 seconds = 2.85 minutes
        assertEquals(2.85, resultMinutes, 0.1)
    }
}
