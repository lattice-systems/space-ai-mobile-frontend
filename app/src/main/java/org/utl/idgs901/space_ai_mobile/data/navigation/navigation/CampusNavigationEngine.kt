package org.utl.idgs901.space_ai_mobile.data.navigation.navigation

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.utl.idgs901.space_ai_mobile.data.navigation.algorithm.DistanceCalculator
import org.utl.idgs901.space_ai_mobile.data.navigation.instruction.InstructionGenerator
import org.utl.idgs901.space_ai_mobile.data.navigation.tracker.CurrentRouteTracker
import org.utl.idgs901.space_ai_mobile.domain.navigation.model.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CampusNavigationEngine @Inject constructor(
    private val instructionGenerator: InstructionGenerator,
    private val routeTracker: CurrentRouteTracker,
    private val distanceCalculator: DistanceCalculator
) {
    private val _activeNavigation = MutableStateFlow<ActiveNavigation?>(null)
    val activeNavigation: StateFlow<ActiveNavigation?> = _activeNavigation.asStateFlow()

    private var fullInstructions = listOf<NavigationInstruction>()

    fun startNavigation(route: NavigationRoute, destinationName: String) {
        fullInstructions = instructionGenerator.generateInstructions(route, destinationName)
        
        _activeNavigation.value = ActiveNavigation(
            route = route,
            currentStep = 0,
            currentInstruction = fullInstructions.firstOrNull() ?: generateDefaultStart(route),
            remainingDistance = route.distanceMeters,
            remainingTime = route.estimatedWalkingTime
        )
    }

    fun updateProgress(userLat: Double, userLng: Double) {
        val current = _activeNavigation.value ?: return
        
        // 1. Check if near target node of current segment to advance step
        val nextNodeIndex = current.currentStep + 1
        if (nextNodeIndex < current.route.path.size) {
            if (routeTracker.isNearNode(userLat, userLng, current.route.path[nextNodeIndex])) {
                advanceStep(current)
            }
        }

        // 2. Update metrics
        val remainingDist = routeTracker.getRemainingDistance(userLat, userLng, current.route.path, current.currentStep)
        val remainingTime = distanceCalculator.calculateWalkingTime(remainingDist)
        
        // 3. Check for instruction update
        val instruction = findRelevantInstruction(current.currentStep, current.route.path)

        _activeNavigation.update { 
            it?.copy(
                remainingDistance = remainingDist,
                remainingTime = remainingTime,
                currentInstruction = instruction
            )
        }
    }

    private fun advanceStep(current: ActiveNavigation) {
        if (current.currentStep < current.route.path.size - 1) {
            _activeNavigation.update { it?.copy(currentStep = current.currentStep + 1) }
        }
    }

    private fun findRelevantInstruction(step: Int, path: List<GraphNode>): NavigationInstruction {
        // Find next instruction in the list that hasn't been passed
        val currNode = path[step]
        return fullInstructions.find { it.node.id == currNode.id } 
            ?: fullInstructions.firstOrNull { path.indexOf(it.node) > step }
            ?: fullInstructions.last()
    }

    private fun generateDefaultStart(route: NavigationRoute) = NavigationInstruction(
        type = InstructionType.START,
        text = "Sigue la ruta",
        distance = 0.0,
        node = route.path.first()
    )

    fun stopNavigation() {
        _activeNavigation.value = null
    }

    // Proxy helper for distance tracker
    private fun CurrentRouteTracker.getRemainingDistance(lat: Double, lng: Double, path: List<GraphNode>, step: Int): Double {
        return this.getRemainingDistance(lat, lng, NavigationRoute(path, 0.0, 0.0), step)
    }
}
