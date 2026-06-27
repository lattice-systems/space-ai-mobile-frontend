package org.utl.idgs901.space_ai_mobile.presentation.location

import org.utl.idgs901.space_ai_mobile.domain.location.model.CampusLocation
import org.utl.idgs901.space_ai_mobile.domain.location.model.CampusLocationState

data class CampusLocationUiState(
    val location: CampusLocation? = null,
    val campusState: CampusLocationState = CampusLocationState.Unknown,
    val isLoading: Boolean = false,
    val permissionGranted: Boolean = false,
    val accuracy: Float? = null
)
