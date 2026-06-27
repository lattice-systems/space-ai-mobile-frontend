package org.utl.idgs901.space_ai_mobile.presentation.map

import org.utl.idgs901.space_ai_mobile.domain.map.model.Building
import org.utl.idgs901.space_ai_mobile.domain.map.model.Poi
import org.utl.idgs901.space_ai_mobile.domain.map.model.CampusLocationState

data class CampusMapUiState(
    val isLoading: Boolean = false,
    val buildings: List<Building> = emptyList(),
    val pois: List<Poi> = emptyList(),
    val buildingsGeoJson: String? = null,
    val walkwaysGeoJson: String? = null,
    val selectedBuilding: Building? = null,
    val locationState: CampusLocationState = CampusLocationState.Outside,
    val errorMessage: String? = null
)
