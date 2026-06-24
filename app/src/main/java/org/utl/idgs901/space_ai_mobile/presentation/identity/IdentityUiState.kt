package org.utl.idgs901.space_ai_mobile.presentation.identity

import org.utl.idgs901.space_ai_mobile.domain.model.QrIdentity

data class IdentityUiState(
    val qrIdentity: QrIdentity? = null,
    val remainingTimeText: String = "00:00",
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
