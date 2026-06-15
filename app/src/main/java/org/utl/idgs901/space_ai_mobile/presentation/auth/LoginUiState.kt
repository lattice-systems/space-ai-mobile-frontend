package org.utl.idgs901.space_ai_mobile.presentation.auth

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val isPasswordVisible: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val emailError: String? = null,
    val passwordError: String? = null
) {
    val isFormValid: Boolean = email.isNotBlank() && 
                              password.isNotBlank() && 
                              emailError == null && 
                              passwordError == null
}
