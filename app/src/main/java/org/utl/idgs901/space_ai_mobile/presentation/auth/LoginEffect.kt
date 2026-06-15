package org.utl.idgs901.space_ai_mobile.presentation.auth

sealed class LoginEffect {
    object NavigateToDashboard : LoginEffect()
    data class ShowSnackbar(val message: String) : LoginEffect()
}
