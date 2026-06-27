package org.utl.idgs901.space_ai_mobile.domain.navigation.usecase

import org.utl.idgs901.space_ai_mobile.domain.map.model.Building
import org.utl.idgs901.space_ai_mobile.domain.navigation.model.GraphNode
import org.utl.idgs901.space_ai_mobile.domain.navigation.repository.NavigationRepository
import javax.inject.Inject

class FindDestinationNodeUseCase @Inject constructor(
    private val repository: NavigationRepository
) {
    suspend operator fun invoke(building: Building): GraphNode? {
        // In this implementation, we search for the node nearest to the building center
        return repository.findNearestNode(building.latitude, building.longitude)
    }
}
