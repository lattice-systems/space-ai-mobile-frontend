package org.utl.idgs901.space_ai_mobile.domain.settings.usecase

import org.utl.idgs901.space_ai_mobile.domain.settings.repository.SettingsRepository
import javax.inject.Inject

class GetVersionUseCase @Inject constructor(
    private val repository: SettingsRepository
) {
    operator fun invoke() = repository.getAppVersion()
}
