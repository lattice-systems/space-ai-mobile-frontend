package org.utl.idgs901.space_ai_mobile.data.navigation.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.utl.idgs901.space_ai_mobile.data.navigation.algorithm.AStarPathfinder
import org.utl.idgs901.space_ai_mobile.data.navigation.algorithm.DistanceCalculator
import org.utl.idgs901.space_ai_mobile.domain.navigation.model.*
import org.utl.idgs901.space_ai_mobile.domain.navigation.repository.NavigationRepository
import javax.inject.Inject

class NavigationRepositoryImpl @Inject constructor(
    private val graphCache: GraphCache,
    private val pathfinder: AStarPathfinder,
    private val distanceCalculator: DistanceCalculator
) : NavigationRepository {

    override suspend fun getGraph(): CampusGraph = withContext(Dispatchers.Default) {
        graphCache.getGraph()
    }

    override suspend fun findNearestNode(lat: Double, lng: Double): GraphNode? = withContext(Dispatchers.Default) {
        val graph = graphCache.getGraph()
        graph.nodes.minByOrNull { node ->
            distanceCalculator.calculateDistance(lat, lng, node.latitude, node.longitude)
        }
    }

    override suspend fun calculateRoute(startNodeId: String, targetNodeId: String): NavigationResult = withContext(Dispatchers.Default) {
        val graph = graphCache.getGraph()
        val path = pathfinder.findPath(graph, startNodeId, targetNodeId)

        if (path == null) {
            NavigationResult.NoRouteFound
        } else {
            var totalDistance = 0.0
            for (i in 0 until path.size - 1) {
                totalDistance += distanceCalculator.calculateDistance(
                    path[i].latitude, path[i].longitude,
                    path[i + 1].latitude, path[i + 1].longitude
                )
            }
            
            NavigationResult.Success(
                NavigationRoute(
                    path = path,
                    distanceMeters = totalDistance,
                    estimatedWalkingTime = distanceCalculator.calculateWalkingTime(totalDistance)
                )
            )
        }
    }
}
