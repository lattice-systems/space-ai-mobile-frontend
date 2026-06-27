package org.utl.idgs901.space_ai_mobile.presentation.navigation

import org.utl.idgs901.space_ai_mobile.domain.navigation.model.ActiveNavigation
import org.utl.idgs901.space_ai_mobile.domain.navigation.model.NavigationInstruction

data class NavigationSessionUiState(
    val activeNavigation: ActiveNavigation? = null,
    val currentInstruction: NavigationInstruction? = null,
    val remainingDistance: String = "",
    val remainingTime: String = "",
    val isNavigating: Boolean = false,
    val hasArrived: Boolean = false
)
