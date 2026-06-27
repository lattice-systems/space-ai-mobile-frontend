package org.utl.idgs901.space_ai_mobile.data.navigation.instruction

import org.junit.Assert.*
import org.junit.Test
import org.utl.idgs901.space_ai_mobile.domain.navigation.model.*

class InstructionGeneratorTest {

    private val generator = InstructionGenerator()

    @Test
    fun `generateInstructions detects a right turn correctly`() {
        val path = listOf(
            GraphNode("1", 0.0, 0.0),      // Origin
            GraphNode("2", 1.0, 0.0),      // Moving North
            GraphNode("3", 1.0, 1.0)       // Turning Right (East)
        )
        val route = NavigationRoute(path, 100.0, 70.0)
        
        val instructions = generator.generateInstructions(route, "Destino")
        
        // Expected: START, RIGHT, ARRIVED
        assertEquals(3, instructions.size)
        assertEquals(InstructionType.START, instructions[0].type)
        assertEquals(InstructionType.RIGHT, instructions[1].type)
        assertEquals(InstructionType.ARRIVED, instructions[2].type)
    }

    @Test
    fun `generateInstructions detects a left turn correctly`() {
        val path = listOf(
            GraphNode("1", 0.0, 0.0),
            GraphNode("2", 1.0, 0.0),
            GraphNode("3", 1.0, -1.0)      // Turning Left (West)
        )
        val route = NavigationRoute(path, 100.0, 70.0)
        
        val instructions = generator.generateInstructions(route, "Destino")
        
        assertEquals(InstructionType.LEFT, instructions[1].type)
    }
}
