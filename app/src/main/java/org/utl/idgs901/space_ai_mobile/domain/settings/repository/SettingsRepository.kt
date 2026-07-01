package org.utl.idgs901.space_ai_mobile.domain.settings.repository

import kotlinx.coroutines.flow.Flow
import org.utl.idgs901.space_ai_mobile.domain.settings.model.SettingsPreferences

interface SettingsRepository {
    val settings: Flow<SettingsPreferences>
    suspend fun updateReduceAnimations(enabled: Boolean)
    suspend fun updateHighContrast(enabled: Boolean)
    suspend fun updateFontScale(scale: Float)
    suspend fun updateHapticsEnabled(enabled: Boolean)
    suspend fun updateAdvancedAccessibility(enabled: Boolean)
    
    // Non-persisted queries
    fun isLocationPermissionGranted(): Boolean
    fun isCameraPermissionGranted(): Boolean
    fun isNotificationsPermissionGranted(): Boolean
    fun getWifiUsage(): Long
    fun getMobileDataUsage(): Long
    fun getAppVersion(): String
}
