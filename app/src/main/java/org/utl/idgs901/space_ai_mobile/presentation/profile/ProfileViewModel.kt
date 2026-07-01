package org.utl.idgs901.space_ai_mobile.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.utl.idgs901.space_ai_mobile.domain.usecase.GetProfileUseCase
import org.utl.idgs901.space_ai_mobile.domain.usecase.UpdatePasswordUseCase
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getProfileUseCase: GetProfileUseCase,
    private val updatePasswordUseCase: UpdatePasswordUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        loadProfile()
    }

    fun loadProfile() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            // Using a mock ID for now
            getProfileUseCase("alex_sterling_8892").onSuccess { profile ->
                _uiState.update { it.copy(userProfile = profile, isLoading = false) }
            }.onFailure { e ->
                _uiState.update { it.copy(isLoading = false, errorMessage = e.message) }
            }
        }
    }

    fun onCurrentPasswordChange(value: String) {
        _uiState.update { it.copy(currentPassword = value, passwordError = null) }
    }

    fun onNewPasswordChange(value: String) {
        _uiState.update { it.copy(newPassword = value, passwordError = null) }
    }

    fun onConfirmPasswordChange(value: String) {
        _uiState.update { it.copy(confirmPassword = value, passwordError = null) }
    }

    fun togglePasswordVisibility() {
        _uiState.update { it.copy(isPasswordVisible = !it.isPasswordVisible) }
    }

    fun updatePassword() {
        val state = _uiState.value
        
        // Validations
        if (state.currentPassword.isBlank()) {
            _uiState.update { it.copy(passwordError = "La contraseña actual es obligatoria") }
            return
        }
        if (state.newPassword.length < 8) {
            _uiState.update { it.copy(passwordError = "Mínimo 8 caracteres") }
            return
        }
        if (!state.newPassword.any { it.isUpperCase() }) {
            _uiState.update { it.copy(passwordError = "Debe contener al menos una mayúscula") }
            return
        }
        if (!state.newPassword.any { it.isDigit() }) {
            _uiState.update { it.copy(passwordError = "Debe contener al menos un número") }
            return
        }
        if (!state.newPassword.any { !it.isLetterOrDigit() }) {
            _uiState.update { it.copy(passwordError = "Debe contener al menos un carácter especial") }
            return
        }
        if (state.newPassword != state.confirmPassword) {
            _uiState.update { it.copy(passwordError = "Las contraseñas no coinciden") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isUpdatingPassword = true, passwordError = null) }
            updatePasswordUseCase(state.currentPassword, state.newPassword).onSuccess {
                _uiState.update { 
                    it.copy(
                        isUpdatingPassword = false, 
                        updateSuccess = true,
                        currentPassword = "",
                        newPassword = "",
                        confirmPassword = ""
                    ) 
                }
            }.onFailure { e ->
                _uiState.update { it.copy(isUpdatingPassword = false, passwordError = e.message) }
            }
        }
    }
    
    fun resetUpdateSuccess() {
        _uiState.update { it.copy(updateSuccess = false) }
    }
}
