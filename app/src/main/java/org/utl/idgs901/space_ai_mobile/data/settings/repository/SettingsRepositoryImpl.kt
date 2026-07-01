package org.utl.idgs901.space_ai_mobile.data.settings.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.utl.idgs901.space_ai_mobile.BuildConfig
import org.utl.idgs901.space_ai_mobile.core.datastore.PreferencesSerializer
import org.utl.idgs901.space_ai_mobile.core.datastore.UserPreferencesProto
import org.utl.idgs901.space_ai_mobile.core.network.DataUsageManager
import org.utl.idgs901.space_ai_mobile.core.permission.PermissionManager
import org.utl.idgs901.space_ai_mobile.domain.settings.model.SettingsPreferences
import org.utl.idgs901.space_ai_mobile.domain.settings.repository.SettingsRepository
import javax.inject.Inject
import javax.inject.Singleton

private val Context.settingsDataStore: DataStore<UserPreferencesProto> by dataStore(
    fileName = "user_settings.pb",
    serializer = PreferencesSerializer
)

@Singleton
class SettingsRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val permissionManager: PermissionManager,
    private val dataUsageManager: DataUsageManager
) : SettingsRepository {

    override val settings: Flow<SettingsPreferences> = context.settingsDataStore.data
        .map { proto ->
            SettingsPreferences(
                reduceAnimations = proto.reduceAnimations,
                highContrast = proto.highContrast,
                fontScale = if (proto.fontScale == 0f) 1.0f else proto.fontScale,
                hapticsEnabled = proto.hapticsEnabled,
                advancedAccessibility = proto.advancedAccessibility
            )
        }

    override suspend fun updateReduceAnimations(enabled: Boolean) {
        context.settingsDataStore.updateData { it.toBuilder().setReduceAnimations(enabled).build() }
    }

    override suspend fun updateHighContrast(enabled: Boolean) {
        context.settingsDataStore.updateData { it.toBuilder().setHighContrast(enabled).build() }
    }

    override suspend fun updateFontScale(scale: Float) {
        context.settingsDataStore.updateData { it.toBuilder().setFontScale(scale).build() }
    }

    override suspend fun updateHapticsEnabled(enabled: Boolean) {
        context.settingsDataStore.updateData { it.toBuilder().setHapticsEnabled(enabled).build() }
    }

    override suspend fun updateAdvancedAccessibility(enabled: Boolean) {
        context.settingsDataStore.updateData { it.toBuilder().setAdvancedAccessibility(enabled).build() }
    }

    override fun isLocationPermissionGranted() = permissionManager.isLocationPermissionGranted()
    override fun isCameraPermissionGranted() = permissionManager.isCameraPermissionGranted()
    override fun isNotificationsPermissionGranted() = permissionManager.isNotificationsPermissionGranted()

    override fun getWifiUsage() = dataUsageManager.getUsageStats().wifiBytes
    override fun getMobileDataUsage() = dataUsageManager.getUsageStats().mobileBytes

    override fun getAppVersion(): String = "${BuildConfig.VERSION_NAME} (${BuildConfig.VERSION_CODE})"
}
