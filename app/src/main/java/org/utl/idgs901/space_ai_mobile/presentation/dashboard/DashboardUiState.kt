package org.utl.idgs901.space_ai_mobile.presentation.dashboard

import org.utl.idgs901.space_ai_mobile.domain.model.User

data class DashboardUiState(
    val user: User? = null,
    val isLoggedOut: Boolean = false
)
