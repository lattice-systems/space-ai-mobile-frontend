package org.utl.idgs901.space_ai_mobile.data.navigation.parser

import android.content.Context
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import dagger.hilt.android.qualifiers.ApplicationContext
import org.utl.idgs901.space_ai_mobile.domain.navigation.model.CampusGraph
import org.utl.idgs901.space_ai_mobile.domain.navigation.model.GraphEdge
import org.utl.idgs901.space_ai_mobile.domain.navigation.model.GraphNode
import javax.inject.Inject

class CampusGraphParser @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val gson = Gson()

    fun parseGraph(): CampusGraph {
        return try {
            val jsonString = context.assets.open("maps/campus_graph.json").bufferedReader().use { it.readText() }
            val dto = gson.fromJson(jsonString, CampusGraphDto::class.java)
            
            val nodes = dto.nodes.map { nodeDto ->
                GraphNode(
                    id = nodeDto.id,
                    latitude = nodeDto.lat,
                    longitude = nodeDto.lng
                )
            }
            
            val edges = dto.edges.map { edgeDto ->
                GraphEdge(
                    from = edgeDto.from,
                    to = edgeDto.to,
                    distance = edgeDto.distance,
                    walkingTime = edgeDto.time
                )
            }
            
            CampusGraph(nodes, edges)
        } catch (e: Exception) {
            e.printStackTrace()
            CampusGraph(emptyList(), emptyList())
        }
    }
}

private data class CampusGraphDto(
    @SerializedName("nodes") val nodes: List<NodeDto>,
    @SerializedName("edges") val edges: List<EdgeDto>
)

private data class NodeDto(
    @SerializedName("id") val id: String,
    @SerializedName("lat") val lat: Double,
    @SerializedName("lng") val lng: Double
)

private data class EdgeDto(
    @SerializedName("from") val from: String,
    @SerializedName("to") val to: String,
    @SerializedName("distance") val distance: Double,
    @SerializedName("time") val time: Double
)
