package org.utl.idgs901.space_ai_mobile.data.map.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.utl.idgs901.space_ai_mobile.data.map.datasource.CampusAssetDataSource
import org.utl.idgs901.space_ai_mobile.data.map.parser.GeoJsonParser
import org.utl.idgs901.space_ai_mobile.data.map.parser.PoiParser
import org.utl.idgs901.space_ai_mobile.domain.map.model.Building
import org.utl.idgs901.space_ai_mobile.domain.map.model.Poi
import org.utl.idgs901.space_ai_mobile.domain.map.repository.CampusRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CampusRepositoryImpl @Inject constructor(
    private val dataSource: CampusAssetDataSource,
    private val geoJsonParser: GeoJsonParser,
    private val poiParser: PoiParser
) : CampusRepository {

    // In-memory cache to avoid re-reading assets
    private var cachedBuildings: List<Building>? = null
    private var cachedPois: List<Poi>? = null
    private var cachedBuildingsGeoJson: String? = null
    private var cachedWalkwaysGeoJson: String? = null

    override suspend fun getBuildings(): List<Building> = withContext(Dispatchers.IO) {
        cachedBuildings ?: run {
            val buildingsJson = dataSource.readAsset("maps/utl_3d.geojson")
            val poisJson = dataSource.readAsset("maps/pois.json")
            val buildings = geoJsonParser.parseBuildings(buildingsJson)
            val pois = poiParser.parsePois(poisJson)

            buildings.map { building ->
                val poi = pois.find { it.id == building.id }
                if (poi != null) {
                    building.copy(icon = poi.icon)
                } else {
                    building
                }
            }.also { cachedBuildings = it }
        }
    }

    override suspend fun getPois(): List<Poi> = withContext(Dispatchers.IO) {
        cachedPois ?: run {
            val json = dataSource.readAsset("maps/pois.json")
            poiParser.parsePois(json).also { cachedPois = it }
        }
    }

    override suspend fun getBuildingsGeoJson(): String = withContext(Dispatchers.IO) {
        cachedBuildingsGeoJson ?: run {
            dataSource.readAsset("maps/utl_3d.geojson").also { cachedBuildingsGeoJson = it }
        }
    }

    override suspend fun getWalkwaysGeoJson(): String = withContext(Dispatchers.IO) {
        cachedWalkwaysGeoJson ?: run {
            dataSource.readAsset("maps/utl_walkways.geojson").also { cachedWalkwaysGeoJson = it }
        }
    }
}
