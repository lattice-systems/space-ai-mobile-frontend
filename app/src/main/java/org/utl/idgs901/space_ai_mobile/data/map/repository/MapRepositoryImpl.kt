package org.utl.idgs901.space_ai_mobile.data.map.repository

import org.utl.idgs901.space_ai_mobile.data.map.datasource.MapAssetDataSource
import org.utl.idgs901.space_ai_mobile.data.map.parser.MapGeoJsonParser
import org.utl.idgs901.space_ai_mobile.data.map.parser.MapPoiParser
import org.utl.idgs901.space_ai_mobile.domain.map.model.Building
import org.utl.idgs901.space_ai_mobile.domain.map.model.Poi
import org.utl.idgs901.space_ai_mobile.domain.map.repository.MapRepository
import javax.inject.Inject

class MapRepositoryImpl @Inject constructor(
    private val dataSource: MapAssetDataSource,
    private val geoJsonParser: MapGeoJsonParser,
    private val poiParser: MapPoiParser
) : MapRepository {

    override suspend fun getBuildings(): List<Building> {
        val json = dataSource.readAsset("maps/utl_3d.geojson")
        return geoJsonParser.parseBuildings(json)
    }

    override suspend fun getPois(): List<Poi> {
        val json = dataSource.readAsset("maps/pois.json")
        return poiParser.parsePois(json)
    }

    override suspend fun getBuildingsGeoJson(): String {
        return dataSource.readAsset("maps/utl_3d.geojson")
    }

    override suspend fun getWalkwaysGeoJson(): String {
        return dataSource.readAsset("maps/utl_walkways.geojson")
    }

    override suspend fun getCampusBoundary(): List<Pair<Double, Double>> {
        val json = dataSource.readAsset("maps/utl_3d.geojson")
        return geoJsonParser.extractBoundary(json)
    }
}
