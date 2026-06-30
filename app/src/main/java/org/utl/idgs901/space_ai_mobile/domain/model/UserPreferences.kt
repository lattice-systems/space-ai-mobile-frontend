package org.utl.idgs901.space_ai_mobile.domain.model

data class UserPreferences(
    val reduceAnimations: Boolean = false,
    val highContrast: Boolean = false,
    val fontSize: String = "Normal",
    val screenReaderEnabled: Boolean = false,
    val hapticFeedbackEnabled: Boolean = true,
    val advancedAccessibilityMode: Boolean = false
)
