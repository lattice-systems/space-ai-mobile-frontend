package org.utl.idgs901.space_ai_mobile.presentation.navigation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.utl.idgs901.space_ai_mobile.core.util.DistanceFormatter
import org.utl.idgs901.space_ai_mobile.core.util.TimeFormatter
import org.utl.idgs901.space_ai_mobile.domain.navigation.model.NavigationRoute
import javax.inject.Inject

@HiltViewModel
class NavigationRendererViewModel @Inject constructor(
    private val distanceFormatter: DistanceFormatter,
    private val timeFormatter: TimeFormatter
) : ViewModel() {

    private val _uiState = MutableStateFlow(NavigationRendererUiState())
    val uiState: StateFlow<NavigationRendererUiState> = _uiState.asStateFlow()

    private var animationJob: Job? = null

    fun setRoute(route: NavigationRoute, destinationName: String) {
        animationJob?.cancel()
        _uiState.update { 
            it.copy(
                route = route,
                distance = distanceFormatter.format(route.distanceMeters),
                estimatedTime = timeFormatter.format(route.estimatedWalkingTime),
                destinationName = destinationName,
                animationProgress = 0f,
                isRouteVisible = true,
                isAnimating = true
            )
        }
        
        startAnimation()
    }

    private fun startAnimation() {
        animationJob = viewModelScope.launch {
            val duration = 800L
            val steps = 30
            val delayPerStep = duration / steps
            
            for (i in 1..steps) {
                delay(delayPerStep)
                val progress = i / steps.toFloat()
                _uiState.update { it.copy(animationProgress = progress) }
            }
            _uiState.update { it.copy(isAnimating = false) }
        }
    }

    fun clearRoute() {
        animationJob?.cancel()
        _uiState.update { 
            it.copy(
                route = null,
                isRouteVisible = false,
                animationProgress = 0f
            )
        }
    }
}
