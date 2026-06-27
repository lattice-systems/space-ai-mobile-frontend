package org.utl.idgs901.space_ai_mobile.domain.navigation.usecase

import org.utl.idgs901.space_ai_mobile.domain.navigation.model.GraphNode
import org.utl.idgs901.space_ai_mobile.domain.navigation.repository.NavigationRepository
import javax.inject.Inject

class FindNearestNodeUseCase @Inject constructor(
    private val repository: NavigationRepository
) {
    suspend operator fun invoke(lat: Double, lng: Double): GraphNode? {
        return repository.findNearestNode(lat, lng)
    }
}
