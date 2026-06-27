package org.utl.idgs901.space_ai_mobile.domain.map.usecase

import org.utl.idgs901.space_ai_mobile.domain.map.model.Building
import org.utl.idgs901.space_ai_mobile.domain.map.model.Poi
import org.utl.idgs901.space_ai_mobile.domain.map.repository.MapRepository
import javax.inject.Inject

data class CampusMapData(
    val buildings: List<Building>,
    val pois: List<Poi>,
    val buildingsGeoJson: String,
    val walkwaysGeoJson: String
)

class LoadCampusMapUseCase @Inject constructor(
    private val repository: MapRepository
) {
    suspend operator fun invoke(): CampusMapData {
        return CampusMapData(
            buildings = repository.getBuildings(),
            pois = repository.getPois(),
            buildingsGeoJson = repository.getBuildingsGeoJson(),
            walkwaysGeoJson = repository.getWalkwaysGeoJson()
        )
    }
}
