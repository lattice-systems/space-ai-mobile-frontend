package org.utl.idgs901.space_ai_mobile.domain.settings.usecase

import org.utl.idgs901.space_ai_mobile.domain.settings.repository.SettingsRepository
import javax.inject.Inject

class UpdateAppearanceUseCase @Inject constructor(
    private val repository: SettingsRepository
) {
    suspend fun updateFontScale(scale: Float) = repository.updateFontScale(scale)
}
