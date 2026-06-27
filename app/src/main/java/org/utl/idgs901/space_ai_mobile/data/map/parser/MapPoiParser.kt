package org.utl.idgs901.space_ai_mobile.data.map.parser

import org.json.JSONArray
import org.utl.idgs901.space_ai_mobile.domain.map.model.Poi
import javax.inject.Inject

class MapPoiParser @Inject constructor() {
    fun parsePois(json: String): List<Poi> {
        val pois = mutableListOf<Poi>()
        val array = JSONArray(json)
        for (i in 0 until array.length()) {
            val item = array.getJSONObject(i)
            pois.add(Poi(
                item.getString("id"),
                item.getString("name"),
                item.getString("category"),
                item.getString("icon")
            ))
        }
        return pois
    }
}
