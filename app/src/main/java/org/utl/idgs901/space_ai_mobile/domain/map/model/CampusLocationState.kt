package org.utl.idgs901.space_ai_mobile.domain.map.model

sealed class CampusLocationState {
    data object Inside : CampusLocationState()
    data object Outside : CampusLocationState()
}
