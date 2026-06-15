package org.utl.idgs901.space_ai_mobile.presentation.auth

sealed class LoginEvent {
    data class EmailChanged(val email: String) : LoginEvent()
    data class PasswordChanged(val password: String) : LoginEvent()
    object TogglePasswordVisibility : LoginEvent()
    object SubmitLogin : LoginEvent()
    object DismissError : LoginEvent()
}
