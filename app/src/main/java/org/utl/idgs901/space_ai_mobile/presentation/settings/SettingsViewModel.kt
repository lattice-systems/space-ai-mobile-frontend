package org.utl.idgs901.space_ai_mobile.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.utl.idgs901.space_ai_mobile.core.permission.PermissionManager
import org.utl.idgs901.space_ai_mobile.domain.settings.usecase.*
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val getSettingsUseCase: GetSettingsUseCase,
    private val updateAccessibilityUseCase: UpdateAccessibilityUseCase,
    private val updateAppearanceUseCase: UpdateAppearanceUseCase,
    private val getPermissionsUseCase: GetPermissionsUseCase,
    private val getDataUsageUseCase: GetDataUsageUseCase,
    private val getVersionUseCase: GetVersionUseCase,
    private val permissionManager: PermissionManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    init {
        loadData()
        observeSettings()
    }

    private fun loadData() {
        val (wifi, mobile) = getDataUsageUseCase()
        _uiState.update { 
            it.copy(
                appVersion = getVersionUseCase(),
                wifiUsage = formatBytes(wifi),
                mobileUsage = formatBytes(mobile),
                locationPermission = getPermissionsUseCase.isLocationGranted(),
                cameraPermission = getPermissionsUseCase.isCameraGranted(),
                notificationsPermission = getPermissionsUseCase.isNotificationsGranted()
            )
        }
    }

    private fun observeSettings() {
        viewModelScope.launch {
            getSettingsUseCase().collect { prefs ->
                _uiState.update { it.copy(preferences = prefs) }
            }
        }
    }

    fun updateReduceAnimations(enabled: Boolean) {
        viewModelScope.launch { updateAccessibilityUseCase.updateReduceAnimations(enabled) }
    }

    fun updateHighContrast(enabled: Boolean) {
        viewModelScope.launch { updateAccessibilityUseCase.updateHighContrast(enabled) }
    }

    fun updateFontScale(scale: Float) {
        viewModelScope.launch { updateAppearanceUseCase.updateFontScale(scale) }
    }

    fun updateHapticsEnabled(enabled: Boolean) {
        viewModelScope.launch { updateAccessibilityUseCase.updateHapticsEnabled(enabled) }
    }

    fun updateAdvancedAccessibility(enabled: Boolean) {
        viewModelScope.launch { updateAccessibilityUseCase.updateAdvancedAccessibility(enabled) }
    }

    fun openSystemSettings() {
        permissionManager.openAppSettings()
    }

    fun refreshPermissions() {
        _uiState.update { 
            it.copy(
                locationPermission = getPermissionsUseCase.isLocationGranted(),
                cameraPermission = getPermissionsUseCase.isCameraGranted(),
                notificationsPermission = getPermissionsUseCase.isNotificationsGranted()
            )
        }
    }

    private fun formatBytes(bytes: Long): String {
        return when {
            bytes >= 1024 * 1024 * 1024 -> String.format("%.2f GB", bytes.toDouble() / (1024 * 1024 * 1024))
            bytes >= 1024 * 1024 -> String.format("%.2f MB", bytes.toDouble() / (1024 * 1024))
            bytes >= 1024 -> String.format("%.2f KB", bytes.toDouble() / 1024)
            else -> "$bytes Bytes"
        }
    }
}
