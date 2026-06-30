package org.utl.idgs901.space_ai_mobile.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.utl.idgs901.space_ai_mobile.domain.usecase.GetSettingsUseCase
import org.utl.idgs901.space_ai_mobile.domain.usecase.UpdateSettingsUseCase
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val getSettingsUseCase: GetSettingsUseCase,
    private val updateSettingsUseCase: UpdateSettingsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    init {
        observeSettings()
    }

    private fun observeSettings() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            getSettingsUseCase().collect { prefs ->
                _uiState.update { it.copy(preferences = prefs, isLoading = false) }
            }
        }
    }

    fun updateReduceAnimations(enabled: Boolean) {
        viewModelScope.launch { updateSettingsUseCase.updateReduceAnimations(enabled) }
    }

    fun updateHighContrast(enabled: Boolean) {
        viewModelScope.launch { updateSettingsUseCase.updateHighContrast(enabled) }
    }

    fun updateFontSize(size: String) {
        viewModelScope.launch { updateSettingsUseCase.updateFontSize(size) }
    }

    fun updateScreenReader(enabled: Boolean) {
        viewModelScope.launch { updateSettingsUseCase.updateScreenReader(enabled) }
    }

    fun updateHapticFeedback(enabled: Boolean) {
        viewModelScope.launch { updateSettingsUseCase.updateHapticFeedback(enabled) }
    }

    fun updateAdvancedAccessibility(enabled: Boolean) {
        viewModelScope.launch { updateSettingsUseCase.updateAdvancedAccessibility(enabled) }
    }
}
