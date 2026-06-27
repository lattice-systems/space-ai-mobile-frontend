package org.utl.idgs901.space_ai_mobile.data.navigation.algorithm

import org.junit.Assert.*
import org.junit.Test
import org.utl.idgs901.space_ai_mobile.domain.navigation.model.*

class AStarPathfinderTest {

    private val distanceCalculator = DistanceCalculator()
    private val pathfinder = AStarPathfinder(distanceCalculator)

    private val mockNodes = listOf(
        GraphNode("n1", 0.0, 0.0),
        GraphNode("n2", 1.0, 0.0),
        GraphNode("n3", 1.0, 1.0),
        GraphNode("n4", 0.0, 1.0)
    )

    private val mockEdges = listOf(
        GraphEdge("n1", "n2", 100.0, 71.0),
        GraphEdge("n2", "n3", 100.0, 71.0),
        GraphEdge("n1", "n4", 250.0, 180.0) // Slower path
    )

    private val mockGraph = CampusGraph(mockNodes, mockEdges)

    @Test
    fun `findPath returns shortest path from n1 to n3`() {
        val path = pathfinder.findPath(mockGraph, "n1", "n3")
        
        assertNotNull(path)
        assertEquals(3, path!!.size)
        assertEquals("n1", path[0].id)
        assertEquals("n2", path[1].id)
        assertEquals("n3", path[2].id)
    }

    @Test
    fun `findPath returns null if no route exists`() {
        // n5 doesn't exist
        val path = pathfinder.findPath(mockGraph, "n1", "n5")
        assertNull(path)
    }
}
