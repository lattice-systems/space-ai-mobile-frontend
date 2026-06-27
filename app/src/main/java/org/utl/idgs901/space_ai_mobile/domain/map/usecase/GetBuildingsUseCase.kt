package org.utl.idgs901.space_ai_mobile.domain.map.usecase

import org.utl.idgs901.space_ai_mobile.domain.map.model.Building
import org.utl.idgs901.space_ai_mobile.domain.map.repository.CampusRepository
import javax.inject.Inject

class GetBuildingsUseCase @Inject constructor(
    private val repository: CampusRepository
) {
    suspend operator fun invoke(): List<Building> = repository.getBuildings()
}
