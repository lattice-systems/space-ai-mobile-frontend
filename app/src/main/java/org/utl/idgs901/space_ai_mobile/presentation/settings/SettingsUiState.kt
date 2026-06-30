package org.utl.idgs901.space_ai_mobile.presentation.settings

import org.utl.idgs901.space_ai_mobile.domain.model.UserPreferences

data class SettingsUiState(
    val preferences: UserPreferences = UserPreferences(),
    val isLoading: Boolean = false,
    val privacyPolicyUrl: String = "https://spaceia.mx/privacidad",
    val termsUrl: String = "https://spaceia.mx/terminos",
    val supportEmail: String = "soporte@spaceia.mx",
    val version: String = "1.0.0",
    val buildDate: String = "2026.07"
)
