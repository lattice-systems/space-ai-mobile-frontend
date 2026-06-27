package org.utl.idgs901.space_ai_mobile.data.map.parser

import org.json.JSONObject
import org.utl.idgs901.space_ai_mobile.domain.map.model.Building
import javax.inject.Inject

class GeoJsonParser @Inject constructor() {
    fun parseBuildings(json: String): List<Building> {
        val buildings = mutableListOf<Building>()
        val root = JSONObject(json)
        val features = root.getJSONArray("features")

        for (i in 0 until features.length()) {
            val feature = features.getJSONObject(i)
            val properties = feature.getJSONObject("properties")

            val id = properties.optString("id", "building_$i")
            val name = properties.optString("name", "Edificio")
            val category = properties.optString("category", "academic")
            val levels = properties.optInt("building:levels", 1)
            val height = properties.optDouble("height", 4.0 * levels)
            val color = properties.optString("color", "#546E7A")
            val icon = properties.optString("icon", "school")

            // Calculate centroid from polygon coordinates
            val geometry = feature.getJSONObject("geometry")
            val coordinates = geometry.getJSONArray("coordinates").getJSONArray(0)
            var sumLat = 0.0
            var sumLon = 0.0
            val count = coordinates.length()
            for (j in 0 until count) {
                val coord = coordinates.getJSONArray(j)
                sumLon += coord.getDouble(0)
                sumLat += coord.getDouble(1)
            }
            val centroidLat = if (count > 0) sumLat / count else 0.0
            val centroidLon = if (count > 0) sumLon / count else 0.0

            buildings.add(
                Building(
                    id = id,
                    name = name,
                    category = category,
                    levels = levels,
                    height = height,
                    color = color,
                    icon = icon,
                    latitude = centroidLat,
                    longitude = centroidLon
                )
            )
        }
        return buildings
    }
}
