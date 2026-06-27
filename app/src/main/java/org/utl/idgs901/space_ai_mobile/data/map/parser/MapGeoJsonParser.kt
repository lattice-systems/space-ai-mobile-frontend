package org.utl.idgs901.space_ai_mobile.data.map.parser

import org.json.JSONObject
import org.utl.idgs901.space_ai_mobile.domain.map.model.Building
import javax.inject.Inject

class MapGeoJsonParser @Inject constructor() {
    fun parseBuildings(json: String): List<Building> {
        val buildings = mutableListOf<Building>()
        val root = JSONObject(json)
        val features = root.getJSONArray("features")
        
        for (i in 0 until features.length()) {
            val feature = features.getJSONObject(i)
            val properties = feature.getJSONObject("properties")
            
            buildings.add(Building(
                id = properties.getString("id"),
                name = properties.getString("name"),
                category = properties.getString("category"),
                levels = properties.getInt("building:levels"),
                height = properties.getDouble("height"),
                color = properties.optString("color", "#546E7A"),
                icon = properties.optString("icon", "school")
            ))
        }
        return buildings
    }

    fun extractBoundary(json: String): List<Pair<Double, Double>> {
        val points = mutableListOf<Pair<Double, Double>>()
        val root = JSONObject(json)
        val features = root.getJSONArray("features")
        for (i in 0 until features.length()) {
            val feature = features.getJSONObject(i)
            val geometry = feature.getJSONObject("geometry")
            if (geometry.getString("type") == "Polygon") {
                val rings = geometry.getJSONArray("coordinates")
                val outerRing = rings.getJSONArray(0)
                for (j in 0 until outerRing.length()) {
                    val point = outerRing.getJSONArray(j)
                    points.add(point.getDouble(1) to point.getDouble(0)) // lat, lng
                }
            }
        }
        return points
    }
}
