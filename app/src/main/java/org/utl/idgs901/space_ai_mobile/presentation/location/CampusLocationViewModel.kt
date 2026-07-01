package org.utl.idgs901.space_ai_mobile.presentation.location

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
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

    private val campusBoundary by lazy { 
        val b = boundaryProvider.getCampusBoundary()
        Log.d("CampusLocation", "Boundary loaded with ${b.size} nodes")
        b
    }

    init {
        checkPermissions()
    }

    fun checkPermissions() {
        val granted = repository.hasLocationPermission()
        _uiState.update { it.copy(permissionGranted = granted) }
        if (granted) {
            startObservingLocation()
        }
    }

    private fun startObservingLocation() {
        if (!repository.hasLocationPermission()) {
            Log.w("CampusLocation", "Permission not granted, cannot start observing")
            return
        }

        _uiState.update { it.copy(isLoading = true) }
        Log.d("CampusLocation", "Starting location observation...")

        // Handle case where GPS might take too long to respond
        viewModelScope.launch {
            delay(5000) // If after 5s we still don't have location, show Inactivo as default
            if (_uiState.value.location == null) {
                Log.w("CampusLocation", "Initial location timeout, defaulting to Outside/Inactivo")
                _uiState.update { 
                    it.copy(
                        isLoading = false,
                        campusState = CampusLocationState.Outside 
                    )
                }
            }
        }

        viewModelScope.launch {
            observeLocationUseCase()
                .onStart { Log.d("CampusLocation", "Flow started") }
                .catch { e ->
                    Log.e("CampusLocation", "Error in location flow", e)
                    _uiState.update { it.copy(isLoading = false, campusState = CampusLocationState.Outside) }
                }
                .collect { location ->
                    Log.d("CampusLocation", "New location: ${location.latitude}, ${location.longitude} (Acc: ${location.accuracy})")
                    
                    val isInside = isInsideCampusUseCase(
                        location.latitude,
                        location.longitude,
                        campusBoundary
                    )
                    
                    val newState = if (isInside) CampusLocationState.Inside else CampusLocationState.Outside
                    Log.d("CampusLocation", "Calculated state: $newState (IsInside: $isInside)")
                    
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
