package org.utl.idgs901.space_ai_mobile.domain.navigation.model

data class GraphNode(
    val id: String,
    val latitude: Double,
    val longitude: Double
)

data class GraphEdge(
    val from: String,
    val to: String,
    val distance: Double,
    val walkingTime: Double
)

data class CampusGraph(
    val nodes: List<GraphNode>,
    val edges: List<GraphEdge>
)

data class NavigationRoute(
    val path: List<GraphNode>,
    val distanceMeters: Double,
    val estimatedWalkingTime: Double
)

sealed class NavigationResult {
    data class Success(val route: NavigationRoute) : NavigationResult()
    data object NoRouteFound : NavigationResult()
    data object InvalidDestination : NavigationResult()
}
