package org.utl.idgs901.space_ai_mobile.domain.repository

import kotlinx.coroutines.flow.Flow
import org.utl.idgs901.space_ai_mobile.domain.model.UserPreferences

interface SettingsRepository {
    val userPreferences: Flow<UserPreferences>
    suspend fun updateReduceAnimations(enabled: Boolean)
    suspend fun updateHighContrast(enabled: Boolean)
    suspend fun updateFontSize(size: String)
    suspend fun updateScreenReader(enabled: Boolean)
    suspend fun updateHapticFeedback(enabled: Boolean)
    suspend fun updateAdvancedAccessibility(enabled: Boolean)
}
