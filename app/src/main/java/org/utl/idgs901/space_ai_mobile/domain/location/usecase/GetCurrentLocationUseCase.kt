package org.utl.idgs901.space_ai_mobile.domain.location.usecase

import org.utl.idgs901.space_ai_mobile.domain.location.model.CampusLocation
import org.utl.idgs901.space_ai_mobile.domain.location.repository.LocationRepository
import javax.inject.Inject

class GetCurrentLocationUseCase @Inject constructor(
    private val repository: LocationRepository
) {
    suspend operator fun invoke(): CampusLocation? = repository.getCurrentLocation()
}
