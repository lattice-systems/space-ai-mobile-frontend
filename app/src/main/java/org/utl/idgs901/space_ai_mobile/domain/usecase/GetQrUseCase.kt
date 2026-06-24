package org.utl.idgs901.space_ai_mobile.domain.usecase

import org.utl.idgs901.space_ai_mobile.domain.model.QrIdentity
import org.utl.idgs901.space_ai_mobile.domain.repository.QrRepository
import javax.inject.Inject

class GetQrUseCase @Inject constructor(
    private val repository: QrRepository
) {
    suspend operator fun invoke(userId: String): QrIdentity {
        return repository.getQr(userId)
    }
}
