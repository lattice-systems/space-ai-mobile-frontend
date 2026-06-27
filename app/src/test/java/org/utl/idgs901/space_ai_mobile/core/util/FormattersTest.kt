package org.utl.idgs901.space_ai_mobile.core.util

import org.junit.Assert.assertEquals
import org.junit.Test

class FormattersTest {

    private val distanceFormatter = DistanceFormatter()
    private val timeFormatter = TimeFormatter()

    @Test
    fun `distance format meters correctly`() {
        assertEquals("240 m", distanceFormatter.format(240.0))
        assertEquals("999 m", distanceFormatter.format(999.0))
    }

    @Test
    fun `distance format kilometers correctly`() {
        assertEquals("1.0 km", distanceFormatter.format(1000.0))
        assertEquals("1.2 km", distanceFormatter.format(1240.0))
    }

    @Test
    fun `time format minutes correctly`() {
        assertEquals("3 min", timeFormatter.format(180.0))
        assertEquals("1 min", timeFormatter.format(60.0))
    }

    @Test
    fun `time format less than minute correctly`() {
        assertEquals("Menos de 1 min", timeFormatter.format(30.0))
    }
}
