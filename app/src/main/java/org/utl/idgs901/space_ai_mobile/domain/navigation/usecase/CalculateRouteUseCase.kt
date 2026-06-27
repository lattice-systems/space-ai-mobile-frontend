package org.utl.idgs901.space_ai_mobile.domain.navigation.usecase

import org.utl.idgs901.space_ai_mobile.domain.map.model.Building
import org.utl.idgs901.space_ai_mobile.domain.navigation.model.NavigationResult
import org.utl.idgs901.space_ai_mobile.domain.navigation.repository.NavigationRepository
import javax.inject.Inject

class CalculateRouteUseCase @Inject constructor(
    private val repository: NavigationRepository,
    private val findNearestNodeUseCase: FindNearestNodeUseCase,
    private val findDestinationNodeUseCase: FindDestinationNodeUseCase
) {
    suspend operator fun invoke(userLat: Double, userLng: Double, destination: Building): NavigationResult {
        val startNode = findNearestNodeUseCase(userLat, userLng) ?: return NavigationResult.NoRouteFound
        val endNode = findDestinationNodeUseCase(destination) ?: return NavigationResult.InvalidDestination
        
        return repository.calculateRoute(startNode.id, endNode.id)
    }
}
