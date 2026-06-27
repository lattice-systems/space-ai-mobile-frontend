package org.utl.idgs901.space_ai_mobile.presentation.navigation

import org.utl.idgs901.space_ai_mobile.domain.navigation.model.NavigationRoute

data class NavigationUiState(
    val graphLoaded: Boolean = false,
    val calculatingRoute: Boolean = false,
    val currentRoute: NavigationRoute? = null,
    val error: String? = null
)
