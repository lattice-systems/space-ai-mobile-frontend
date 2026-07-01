package org.utl.idgs901.space_ai_mobile.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import org.utl.idgs901.space_ai_mobile.domain.settings.model.SettingsPreferences
import org.utl.idgs901.space_ai_mobile.domain.settings.usecase.GetSettingsUseCase
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    getSettingsUseCase: GetSettingsUseCase
) : ViewModel() {

    val settings: StateFlow<SettingsPreferences> = getSettingsUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = SettingsPreferences()
        )
}
