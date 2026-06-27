package org.utl.idgs901.space_ai_mobile.domain.navigation.repository

import org.utl.idgs901.space_ai_mobile.domain.navigation.model.*

interface NavigationRepository {
    suspend fun getGraph(): CampusGraph
    suspend fun findNearestNode(lat: Double, lng: Double): GraphNode?
    suspend fun calculateRoute(startNodeId: String, targetNodeId: String): NavigationResult
}
