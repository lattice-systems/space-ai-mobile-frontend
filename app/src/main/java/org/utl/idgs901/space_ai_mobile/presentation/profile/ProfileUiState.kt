package org.utl.idgs901.space_ai_mobile.presentation.profile

import org.utl.idgs901.space_ai_mobile.domain.model.UserProfile

data class ProfileUiState(
    val userProfile: UserProfile? = null,
    val isLoading: Boolean = false,
    val currentPassword: String = "",
    val newPassword: String = "",
    val confirmPassword: String = "",
    val isPasswordVisible: Boolean = false,
    val isUpdatingPassword: Boolean = false,
    val passwordError: String? = null,
    val updateSuccess: Boolean = false,
    val errorMessage: String? = null
)
