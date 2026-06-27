package org.utl.idgs901.space_ai_mobile.domain.navigation.model

data class ActiveNavigation(
    val route: NavigationRoute,
    val currentStep: Int,
    val currentInstruction: NavigationInstruction,
    val remainingDistance: Double,
    val remainingTime: Double
)
