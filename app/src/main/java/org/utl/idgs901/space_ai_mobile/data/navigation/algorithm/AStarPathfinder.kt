package org.utl.idgs901.space_ai_mobile.data.navigation.algorithm

import org.utl.idgs901.space_ai_mobile.domain.navigation.model.*
import java.util.*
import javax.inject.Inject

class AStarPathfinder @Inject constructor(
    private val distanceCalculator: DistanceCalculator
) {

    fun findPath(graph: CampusGraph, startId: String, targetId: String): List<GraphNode>? {
        val nodesMap = graph.nodes.associateBy { it.id }
        val startNode = nodesMap[startId] ?: return null
        val targetNode = nodesMap[targetId] ?: return null

        val adjacencyList = mutableMapOf<String, MutableList<GraphEdge>>()
        graph.edges.forEach { edge ->
            adjacencyList.getOrPut(edge.from) { mutableListOf() }.add(edge)
            // Assuming bidirectional for campus walkways unless specified otherwise
            adjacencyList.getOrPut(edge.to) { mutableListOf() }.add(GraphEdge(edge.to, edge.from, edge.distance, edge.walkingTime))
        }

        val openSet = PriorityQueue<NodeScore>(compareBy { it.fScore })
        val closedSet = mutableSetOf<String>()
        
        val cameFrom = mutableMapOf<String, String>()
        val gScore = mutableMapOf<String, Double>().withDefault { Double.MAX_VALUE }
        
        gScore[startId] = 0.0
        openSet.add(NodeScore(startId, heuristic(startNode, targetNode)))

        while (openSet.isNotEmpty()) {
            val current = openSet.poll() ?: break
            
            if (current.id == targetId) {
                return reconstructPath(cameFrom, nodesMap, targetId)
            }

            closedSet.add(current.id)

            adjacencyList[current.id]?.forEach { edge ->
                if (edge.to in closedSet) return@forEach

                val tentativeGScore = (gScore[current.id] ?: Double.MAX_VALUE) + edge.distance
                
                if (tentativeGScore < (gScore[edge.to] ?: Double.MAX_VALUE)) {
                    cameFrom[edge.to] = current.id
                    gScore[edge.to] = tentativeGScore
                    
                    val fScore = tentativeGScore + heuristic(nodesMap[edge.to]!!, targetNode)
                    if (openSet.none { it.id == edge.to }) {
                        openSet.add(NodeScore(edge.to, fScore))
                    }
                }
            }
        }

        return null
    }

    private fun heuristic(node: GraphNode, target: GraphNode): Double {
        return distanceCalculator.calculateDistance(
            node.latitude, node.longitude,
            target.latitude, target.longitude
        )
    }

    private fun reconstructPath(
        cameFrom: Map<String, String>,
        nodesMap: Map<String, GraphNode>,
        targetId: String
    ): List<GraphNode> {
        val path = mutableListOf<GraphNode>()
        var current: String? = targetId
        while (current != null) {
            nodesMap[current]?.let { path.add(0, it) }
            current = cameFrom[current]
        }
        return path
    }

    private data class NodeScore(val id: String, val fScore: Double)
}
