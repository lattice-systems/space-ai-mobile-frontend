package org.utl.idgs901.space_ai_mobile.data.navigation.repository

import org.utl.idgs901.space_ai_mobile.data.navigation.parser.CampusGraphParser
import org.utl.idgs901.space_ai_mobile.domain.navigation.model.CampusGraph
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GraphCache @Inject constructor(
    private val parser: CampusGraphParser
) {
    private var cachedGraph: CampusGraph? = null

    fun getGraph(): CampusGraph {
        return cachedGraph ?: parser.parseGraph().also { cachedGraph = it }
    }
}
