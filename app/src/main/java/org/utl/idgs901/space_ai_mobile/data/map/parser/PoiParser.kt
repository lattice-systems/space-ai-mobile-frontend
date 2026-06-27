package org.utl.idgs901.space_ai_mobile.data.map.parser

import org.json.JSONArray
import org.utl.idgs901.space_ai_mobile.domain.map.model.Poi
import javax.inject.Inject

class PoiParser @Inject constructor() {
    fun parsePois(json: String): List<Poi> {
        val pois = mutableListOf<Poi>()
        val array = JSONArray(json)
        for (i in 0 until array.length()) {
            val item = array.getJSONObject(i)
            pois.add(
                Poi(
                    id = item.getString("id"),
                    name = item.getString("name"),
                    category = item.getString("category"),
                    icon = item.getString("icon"),
                    latitude = item.optDouble("latitude", 0.0),
                    longitude = item.optDouble("longitude", 0.0)
                )
            )
        }
        return pois
    }
}
