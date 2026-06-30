package org.utl.idgs901.space_ai_mobile.domain.usecase

import kotlinx.coroutines.flow.Flow
import org.utl.idgs901.space_ai_mobile.domain.model.UserPreferences
import org.utl.idgs901.space_ai_mobile.domain.repository.SettingsRepository
import javax.inject.Inject

class GetSettingsUseCase @Inject constructor(
    private val repository: SettingsRepository
) {
    operator fun invoke(): Flow<UserPreferences> = repository.userPreferences
}
