package org.utl.idgs901.space_ai_mobile.domain.settings.usecase

import org.utl.idgs901.space_ai_mobile.domain.settings.repository.SettingsRepository
import javax.inject.Inject

class UpdateAccessibilityUseCase @Inject constructor(
    private val repository: SettingsRepository
) {
    suspend fun updateReduceAnimations(enabled: Boolean) = repository.updateReduceAnimations(enabled)
    suspend fun updateHighContrast(enabled: Boolean) = repository.updateHighContrast(enabled)
    suspend fun updateHapticsEnabled(enabled: Boolean) = repository.updateHapticsEnabled(enabled)
    suspend fun updateAdvancedAccessibility(enabled: Boolean) = repository.updateAdvancedAccessibility(enabled)
}
