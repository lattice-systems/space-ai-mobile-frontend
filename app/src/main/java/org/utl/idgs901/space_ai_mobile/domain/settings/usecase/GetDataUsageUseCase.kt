package org.utl.idgs901.space_ai_mobile.domain.settings.usecase

import org.utl.idgs901.space_ai_mobile.domain.settings.repository.SettingsRepository
import javax.inject.Inject

class GetDataUsageUseCase @Inject constructor(
    private val repository: SettingsRepository
) {
    operator fun invoke(): Pair<Long, Long> {
        return Pair(repository.getWifiUsage(), repository.getMobileDataUsage())
    }
}
