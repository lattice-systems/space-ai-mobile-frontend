package org.utl.idgs901.space_ai_mobile.domain.location.usecase

import org.junit.Assert.*
import org.junit.Test

class IsInsideCampusUseCaseTest {

    private val useCase = IsInsideCampusUseCase()

    // Simple square around 0,0
    private val squareBoundary = listOf(
        0.0 to 0.0,
        1.0 to 0.0,
        1.0 to 1.0,
        0.0 to 1.0
    )

    @Test
    fun `point exactly inside should return true`() {
        val result = useCase(0.5, 0.5, squareBoundary)
        assertTrue(result)
    }

    @Test
    fun `point exactly outside should return false`() {
        val result = useCase(2.0, 2.0, squareBoundary)
        assertFalse(result)
    }

    @Test
    fun `point very close to boundary should return true due to tolerance`() {
        // Point is ~11 meters north of the top boundary (1.0, 0.5)
        // At latitude 1.0, 0.0001 degrees latitude is ~11.1 meters
        val result = useCase(1.0001, 0.5, squareBoundary)
        assertTrue(result)
    }

    @Test
    fun `point far from boundary should return false`() {
        // Point is far beyond 20m tolerance
        val result = useCase(1.01, 0.5, squareBoundary)
        assertFalse(result)
    }
}
