package org.utl.idgs901.space_ai_mobile.domain.usecase

import org.utl.idgs901.space_ai_mobile.domain.repository.SettingsRepository
import javax.inject.Inject

class UpdateSettingsUseCase @Inject constructor(
    private val repository: SettingsRepository
) {
    suspend fun updateReduceAnimations(enabled: Boolean) = repository.updateReduceAnimations(enabled)
    suspend fun updateHighContrast(enabled: Boolean) = repository.updateHighContrast(enabled)
    suspend fun updateFontSize(size: String) = repository.updateFontSize(size)
    suspend fun updateScreenReader(enabled: Boolean) = repository.updateScreenReader(enabled)
    suspend fun updateHapticFeedback(enabled: Boolean) = repository.updateHapticFeedback(enabled)
    suspend fun updateAdvancedAccessibility(enabled: Boolean) = repository.updateAdvancedAccessibility(enabled)
}
