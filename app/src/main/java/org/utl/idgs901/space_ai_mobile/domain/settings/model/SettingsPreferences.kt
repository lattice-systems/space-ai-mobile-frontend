package org.utl.idgs901.space_ai_mobile.domain.settings.model

data class SettingsPreferences(
    val reduceAnimations: Boolean = false,
    val highContrast: Boolean = false,
    val fontScale: Float = 1.0f,
    val hapticsEnabled: Boolean = true,
    val advancedAccessibility: Boolean = false
)
