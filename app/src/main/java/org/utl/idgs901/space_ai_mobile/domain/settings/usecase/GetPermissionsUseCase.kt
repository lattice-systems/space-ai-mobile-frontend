package org.utl.idgs901.space_ai_mobile.domain.settings.usecase

import org.utl.idgs901.space_ai_mobile.domain.settings.repository.SettingsRepository
import javax.inject.Inject

class GetPermissionsUseCase @Inject constructor(
    private val repository: SettingsRepository
) {
    fun isLocationGranted() = repository.isLocationPermissionGranted()
    fun isCameraGranted() = repository.isCameraPermissionGranted()
    fun isNotificationsGranted() = repository.isNotificationsPermissionGranted()
}
