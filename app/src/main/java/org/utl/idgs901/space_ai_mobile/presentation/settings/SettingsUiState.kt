package org.utl.idgs901.space_ai_mobile.presentation.settings

import org.utl.idgs901.space_ai_mobile.domain.settings.model.SettingsPreferences

data class SettingsUiState(
    val preferences: SettingsPreferences = SettingsPreferences(),
    val isLoading: Boolean = false,
    
    // Permissions Status
    val locationPermission: Boolean = false,
    val notificationsPermission: Boolean = false,
    val cameraPermission: Boolean = false,
    
    // Data Usage
    val wifiUsage: String = "0 MB",
    val mobileUsage: String = "0 MB",
    
    // About
    val appVersion: String = "",
    val privacyPolicyUrl: String = "https://spaceia.mx/privacidad",
    val termsUrl: String = "https://spaceia.mx/terminos",
    val supportEmail: String = "soporte@spaceia.mx"
)
