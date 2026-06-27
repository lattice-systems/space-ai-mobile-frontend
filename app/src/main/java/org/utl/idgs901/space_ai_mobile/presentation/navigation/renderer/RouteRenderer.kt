package org.utl.idgs901.space_ai_mobile.presentation.navigation.renderer

import org.utl.idgs901.space_ai_mobile.domain.navigation.model.NavigationRoute
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RouteRenderer @Inject constructor() {

    fun generateGeoJson(route: NavigationRoute): String {
        val coordinates = route.path.joinToString(",") { node ->
            "[${node.longitude}, ${node.latitude}]"
        }
        
        return """
            {
                "type": "FeatureCollection",
                "features": [
                    {
                        "type": "Feature",
                        "geometry": {
                            "type": "LineString",
                            "coordinates": [$coordinates]
                        }
                    }
                ]
            }
        """.trimIndent()
    }
    
    /**
     * Generates a partial GeoJson based on a progress percentage (0.0 to 1.0)
     * for progressive drawing animation.
     */
    fun generateAnimatedGeoJson(route: NavigationRoute, progress: Float): String {
        if (route.path.isEmpty()) return ""
        if (progress >= 1f) return generateGeoJson(route)
        
        val totalNodes = route.path.size
        if (totalNodes < 2) return generateGeoJson(route)

        // Find how many nodes to include fully
        val floatIndex = progress * (totalNodes - 1)
        val fullNodesCount = floatIndex.toInt() + 1
        
        val partialNodes = route.path.take(fullNodesCount).toMutableList()
        
        // Add an interpolated point if between nodes
        if (fullNodesCount < totalNodes) {
            val startNode = route.path[fullNodesCount - 1]
            val endNode = route.path[fullNodesCount]
            val segmentProgress = floatIndex - floatIndex.toInt()
            
            val interpLat = startNode.latitude + (endNode.latitude - startNode.latitude) * segmentProgress
            val interpLng = startNode.longitude + (endNode.longitude - startNode.longitude) * segmentProgress
            
            // Temporary node-like pair for interpolation
            val coordinates = partialNodes.joinToString(",") { "[${it.longitude}, ${it.latitude}]" }
            return """
                {
                    "type": "FeatureCollection",
                    "features": [
                        {
                            "type": "Feature",
                            "geometry": {
                                "type": "LineString",
                                "coordinates": [$coordinates, [$interpLng, $interpLat]]
                            }
                        }
                    ]
                }
            """.trimIndent()
        }

        val coordinates = partialNodes.joinToString(",") { "[${it.longitude}, ${it.latitude}]" }
        return """
            {
                "type": "FeatureCollection",
                "features": [
                    {
                        "type": "Feature",
                        "geometry": {
                            "type": "LineString",
                            "coordinates": [$coordinates]
                        }
                    }
                ]
            }
        """.trimIndent()
    }
}
