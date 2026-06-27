package org.utl.idgs901.space_ai_mobile.presentation.map

import org.utl.idgs901.space_ai_mobile.domain.map.model.Building

sealed class CampusMapEvent {
    data object LoadData : CampusMapEvent()
    data class BuildingSelected(val building: Building) : CampusMapEvent()
    data class UserLocationUpdated(val lat: Double, val lng: Double) : CampusMapEvent()
    data object ClearSelection : CampusMapEvent()
}
