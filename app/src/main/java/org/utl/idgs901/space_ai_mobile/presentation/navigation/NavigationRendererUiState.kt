package org.utl.idgs901.space_ai_mobile.presentation.navigation

import org.utl.idgs901.space_ai_mobile.domain.navigation.model.NavigationRoute

data class NavigationRendererUiState(
    val route: NavigationRoute? = null,
    val distance: String = "",
    val estimatedTime: String = "",
    val destinationName: String = "",
    val animationProgress: Float = 0f,
    val isRouteVisible: Boolean = false,
    val isAnimating: Boolean = false
)
