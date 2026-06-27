package org.utl.idgs901.space_ai_mobile.domain.map.usecase

import org.utl.idgs901.space_ai_mobile.domain.map.model.Poi
import org.utl.idgs901.space_ai_mobile.domain.map.repository.CampusRepository
import javax.inject.Inject

class GetPoisUseCase @Inject constructor(
    private val repository: CampusRepository
) {
    suspend operator fun invoke(): List<Poi> = repository.getPois()
}
