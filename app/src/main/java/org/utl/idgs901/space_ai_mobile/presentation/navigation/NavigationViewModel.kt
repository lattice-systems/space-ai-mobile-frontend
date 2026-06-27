package org.utl.idgs901.space_ai_mobile.presentation.navigation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.utl.idgs901.space_ai_mobile.domain.map.model.Building
import org.utl.idgs901.space_ai_mobile.domain.navigation.model.NavigationResult
import org.utl.idgs901.space_ai_mobile.domain.navigation.repository.NavigationRepository
import org.utl.idgs901.space_ai_mobile.domain.navigation.usecase.CalculateRouteUseCase
import javax.inject.Inject

@HiltViewModel
class NavigationViewModel @Inject constructor(
    private val calculateRouteUseCase: CalculateRouteUseCase,
    private val repository: NavigationRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(NavigationUiState())
    val uiState: StateFlow<NavigationUiState> = _uiState.asStateFlow()

    init {
        checkGraphLoaded()
    }

    private fun checkGraphLoaded() {
        viewModelScope.launch {
            try {
                repository.getGraph()
                _uiState.update { it.copy(graphLoaded = true) }
            } catch (e: Exception) {
                _uiState.update { it.copy(error = "Error al cargar el grafo: ${e.message}") }
            }
        }
    }

    fun calculateRoute(userLat: Double, userLng: Double, destination: Building) {
        viewModelScope.launch {
            _uiState.update { it.copy(calculatingRoute = true, error = null) }
            
            when (val result = calculateRouteUseCase(userLat, userLng, destination)) {
                is NavigationResult.Success -> {
                    _uiState.update { 
                        it.copy(
                            calculatingRoute = false,
                            currentRoute = result.route
                        )
                    }
                }
                is NavigationResult.NoRouteFound -> {
                    _uiState.update { 
                        it.copy(
                            calculatingRoute = false,
                            error = "No se encontró una ruta disponible."
                        )
                    }
                }
                is NavigationResult.InvalidDestination -> {
                    _uiState.update { 
                        it.copy(
                            calculatingRoute = false,
                            error = "El destino no es válido."
                        )
                    }
                }
            }
        }
    }
    
    fun clearRoute() {
        _uiState.update { it.copy(currentRoute = null, error = null) }
    }
}
