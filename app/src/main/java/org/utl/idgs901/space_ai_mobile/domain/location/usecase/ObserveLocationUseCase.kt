package org.utl.idgs901.space_ai_mobile.domain.location.usecase

import kotlinx.coroutines.flow.Flow
import org.utl.idgs901.space_ai_mobile.domain.location.model.CampusLocation
import org.utl.idgs901.space_ai_mobile.domain.location.repository.LocationRepository
import javax.inject.Inject

class ObserveLocationUseCase @Inject constructor(
    private val repository: LocationRepository
) {
    operator fun invoke(): Flow<CampusLocation> = repository.observeLocation()
}
