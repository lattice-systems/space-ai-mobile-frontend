package org.utl.idgs901.space_ai_mobile.presentation.location

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.utl.idgs901.space_ai_mobile.data.location.geofence.CampusBoundaryProvider
import org.utl.idgs901.space_ai_mobile.domain.location.model.CampusLocationState
import org.utl.idgs901.space_ai_mobile.domain.location.repository.LocationRepository
import org.utl.idgs901.space_ai_mobile.domain.location.usecase.IsInsideCampusUseCase
import org.utl.idgs901.space_ai_mobile.domain.location.usecase.ObserveLocationUseCase
import javax.inject.Inject

@HiltViewModel
class CampusLocationViewModel @Inject constructor(
    private val observeLocationUseCase: ObserveLocationUseCase,
    private val isInsideCampusUseCase: IsInsideCampusUseCase,
    private val boundaryProvider: CampusBoundaryProvider,
    private val repository: LocationRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CampusLocationUiState())
    val uiState: StateFlow<CampusLocationUiState> = _uiState.asStateFlow()

    private val campusBoundary by lazy { boundaryProvider.getCampusBoundary() }

    init {
        checkPermissions()
        startObservingLocation()
    }

    fun checkPermissions() {
        val granted = repository.hasLocationPermission()
        _uiState.update { it.copy(permissionGranted = granted) }
        if (granted) {
            startObservingLocation()
        }
    }

    private fun startObservingLocation() {
        if (!repository.hasLocationPermission()) return

        _uiState.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            observeLocationUseCase()
                .catch { e ->
                    _uiState.update { it.copy(isLoading = false) }
                }
                .collect { location ->
                    val isInside = isInsideCampusUseCase(
                        location.latitude,
                        location.longitude,
                        campusBoundary
                    )
                    
                    val newState = if (isInside) CampusLocationState.Inside else CampusLocationState.Outside
                    
                    _uiState.update { 
                        it.copy(
                            location = location,
                            campusState = newState,
                            isLoading = false,
                            accuracy = location.accuracy
                        )
                    }
                }
        }
    }
}
