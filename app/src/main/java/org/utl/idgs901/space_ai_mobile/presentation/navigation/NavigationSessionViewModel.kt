package org.utl.idgs901.space_ai_mobile.presentation.navigation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.utl.idgs901.space_ai_mobile.core.util.DistanceFormatter
import org.utl.idgs901.space_ai_mobile.core.util.TimeFormatter
import org.utl.idgs901.space_ai_mobile.data.navigation.navigation.CampusNavigationEngine
import org.utl.idgs901.space_ai_mobile.domain.navigation.model.InstructionType
import org.utl.idgs901.space_ai_mobile.domain.navigation.model.NavigationRoute
import javax.inject.Inject

@HiltViewModel
class NavigationSessionViewModel @Inject constructor(
    private val navigationEngine: CampusNavigationEngine,
    private val distanceFormatter: DistanceFormatter,
    private val timeFormatter: TimeFormatter
) : ViewModel() {

    private val _uiState = MutableStateFlow(NavigationSessionUiState())
    val uiState: StateFlow<NavigationSessionUiState> = _uiState.asStateFlow()

    init {
        observeNavigationEngine()
    }

    private fun observeNavigationEngine() {
        viewModelScope.launch {
            navigationEngine.activeNavigation.collect { active ->
                if (active == null) {
                    _uiState.update { 
                        it.copy(
                            activeNavigation = null,
                            isNavigating = false,
                            hasArrived = false
                        )
                    }
                    return@collect
                }

                val hasArrived = active.currentInstruction.type == InstructionType.ARRIVED && active.remainingDistance < 5.0
                
                _uiState.update { 
                    it.copy(
                        activeNavigation = active,
                        currentInstruction = active.currentInstruction,
                        remainingDistance = distanceFormatter.format(active.remainingDistance),
                        remainingTime = timeFormatter.format(active.remainingTime),
                        isNavigating = true,
                        hasArrived = hasArrived
                    )
                }
            }
        }
    }

    fun startNavigation(route: NavigationRoute, destinationName: String) {
        navigationEngine.startNavigation(route, destinationName)
    }

    fun stopNavigation() {
        navigationEngine.stopNavigation()
    }

    fun updateLocation(lat: Double, lng: Double) {
        navigationEngine.updateProgress(lat, lng)
    }
}
