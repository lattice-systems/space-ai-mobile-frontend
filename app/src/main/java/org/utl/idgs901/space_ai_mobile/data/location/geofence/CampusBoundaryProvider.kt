package org.utl.idgs901.space_ai_mobile.data.location.geofence

import android.content.Context
import android.util.Xml
import dagger.hilt.android.qualifiers.ApplicationContext
import org.xmlpull.v1.XmlPullParser
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CampusBoundaryProvider @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private var cachedBoundary: List<Pair<Double, Double>>? = null

    fun getCampusBoundary(): List<Pair<Double, Double>> {
        cachedBoundary?.let { return it }

        val boundary = mutableListOf<Pair<Double, Double>>()
        val nodes = mutableMapOf<String, Pair<Double, Double>>()
        val universityNodeRefs = mutableListOf<String>()

        try {
            context.assets.open("maps/map.osm").use { inputStream ->
                val parser = Xml.newPullParser()
                parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false)
                parser.setInput(inputStream, null)

                var eventType = parser.eventType
                var currentWayIsUniversity = false
                val currentWayNodes = mutableListOf<String>()

                while (eventType != XmlPullParser.END_DOCUMENT) {
                    val name = parser.name
                    when (eventType) {
                        XmlPullParser.START_TAG -> {
                            when (name) {
                                "node" -> {
                                    val id = parser.getAttributeValue(null, "id")
                                    val lat = parser.getAttributeValue(null, "lat").toDouble()
                                    val lon = parser.getAttributeValue(null, "lon").toDouble()
                                    nodes[id] = lat to lon
                                }
                                "way" -> {
                                    currentWayNodes.clear()
                                    currentWayIsUniversity = false
                                }
                                "nd" -> {
                                    val ref = parser.getAttributeValue(null, "ref")
                                    currentWayNodes.add(ref)
                                }
                                "tag" -> {
                                    val k = parser.getAttributeValue(null, "k")
                                    val v = parser.getAttributeValue(null, "v")
                                    if (k == "amenity" && v == "university") {
                                        currentWayIsUniversity = true
                                    }
                                }
                            }
                        }
                        XmlPullParser.END_TAG -> {
                            if (name == "way" && currentWayIsUniversity) {
                                universityNodeRefs.addAll(currentWayNodes)
                            }
                        }
                    }
                    eventType = parser.next()
                }
            }

            universityNodeRefs.forEach { ref ->
                nodes[ref]?.let { boundary.add(it) }
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }

        cachedBoundary = boundary
        return boundary
    }
}
