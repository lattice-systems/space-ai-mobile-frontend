package org.utl.idgs901.space_ai_mobile.presentation.map

sealed class CampusMapEffect {
    data class ShowError(val message: String) : CampusMapEffect()
}
