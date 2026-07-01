package org.utl.idgs901.space_ai_mobile.domain.settings.usecase

import kotlinx.coroutines.flow.Flow
import org.utl.idgs901.space_ai_mobile.domain.settings.model.SettingsPreferences
import org.utl.idgs901.space_ai_mobile.domain.settings.repository.SettingsRepository
import javax.inject.Inject

class GetSettingsUseCase @Inject constructor(
    private val repository: SettingsRepository
) {
    operator fun invoke(): Flow<SettingsPreferences> = repository.settings
}
