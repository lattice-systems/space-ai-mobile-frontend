package org.utl.idgs901.space_ai_mobile.domain.location.model

sealed class CampusLocationState {
    data object Inside : CampusLocationState()
    data object Outside : CampusLocationState()
    data object Unknown : CampusLocationState()
}
