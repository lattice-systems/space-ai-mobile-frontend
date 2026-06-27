package org.utl.idgs901.space_ai_mobile.presentation.map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.utl.idgs901.space_ai_mobile.domain.map.usecase.GetCampusLocationStateUseCase
import org.utl.idgs901.space_ai_mobile.domain.map.usecase.LoadCampusMapUseCase
import javax.inject.Inject

@HiltViewModel
class CampusMapViewModel @Inject constructor(
    private val loadCampusMapUseCase: LoadCampusMapUseCase,
    private val getCampusLocationStateUseCase: GetCampusLocationStateUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(CampusMapUiState())
    val uiState: StateFlow<CampusMapUiState> = _uiState.asStateFlow()

    private val _effect = MutableSharedFlow<CampusMapEffect>()
    val effect: SharedFlow<CampusMapEffect> = _effect.asSharedFlow()

    init {
        onEvent(CampusMapEvent.LoadData)
    }

    fun onEvent(event: CampusMapEvent) {
        when (event) {
            is CampusMapEvent.LoadData -> loadData()
            is CampusMapEvent.BuildingSelected -> {
                _uiState.update { it.copy(selectedBuilding = event.building) }
            }
            is CampusMapEvent.UserLocationUpdated -> checkLocation(event.lat, event.lng)
            is CampusMapEvent.ClearSelection -> {
                _uiState.update { it.copy(selectedBuilding = null) }
            }
        }
    }

    private fun loadData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val data = loadCampusMapUseCase()
                _uiState.update { 
                    it.copy(
                        isLoading = false,
                        buildings = data.buildings,
                        pois = data.pois,
                        buildingsGeoJson = data.buildingsGeoJson,
                        walkwaysGeoJson = data.walkwaysGeoJson
                    )
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, errorMessage = e.message) }
                _effect.emit(CampusMapEffect.ShowError(e.message ?: "Error al cargar datos"))
            }
        }
    }

    private fun checkLocation(lat: Double, lng: Double) {
        viewModelScope.launch {
            val state = getCampusLocationStateUseCase(lat, lng)
            _uiState.update { it.copy(locationState = state) }
        }
    }
}
