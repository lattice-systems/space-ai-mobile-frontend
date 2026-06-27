package org.utl.idgs901.space_ai_mobile.domain.location.repository

import kotlinx.coroutines.flow.Flow
import org.utl.idgs901.space_ai_mobile.domain.location.model.CampusLocation

interface LocationRepository {
    fun observeLocation(): Flow<CampusLocation>
    suspend fun getCurrentLocation(): CampusLocation?
    fun hasLocationPermission(): Boolean
}
