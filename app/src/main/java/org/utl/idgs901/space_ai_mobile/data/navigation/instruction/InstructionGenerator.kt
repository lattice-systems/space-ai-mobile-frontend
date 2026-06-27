package org.utl.idgs901.space_ai_mobile.data.navigation.instruction

import org.utl.idgs901.space_ai_mobile.domain.navigation.model.*
import javax.inject.Inject
import kotlin.math.*

class InstructionGenerator @Inject constructor() {

    fun generateInstructions(route: NavigationRoute, destinationName: String): List<NavigationInstruction> {
        val nodes = route.path
        if (nodes.isEmpty()) return emptyList()
        
        val instructions = mutableListOf<NavigationInstruction>()
        
        // 1. Start Instruction
        instructions.add(
            NavigationInstruction(
                type = InstructionType.START,
                text = "Inicia tu camino",
                distance = 0.0,
                node = nodes[0]
            )
        )
        
        if (nodes.size < 2) {
            instructions.add(generateArrival(nodes[0], destinationName))
            return instructions
        }

        // 2. Mid-route turns
        for (i in 1 until nodes.size - 1) {
            val prev = nodes[i - 1]
            val curr = nodes[i]
            val next = nodes[i + 1]
            
            val bearing1 = calculateBearing(prev.latitude, prev.longitude, curr.latitude, curr.longitude)
            val bearing2 = calculateBearing(curr.latitude, curr.longitude, next.latitude, next.longitude)
            
            val turnAngle = (bearing2 - bearing1 + 540) % 360 - 180
            
            val type = when {
                turnAngle < -20 -> InstructionType.LEFT
                turnAngle > 20 -> InstructionType.RIGHT
                else -> InstructionType.STRAIGHT
            }
            
            if (type != InstructionType.STRAIGHT) {
                val turnText = if (type == InstructionType.LEFT) "Gira a la izquierda" else "Gira a la derecha"
                instructions.add(
                    NavigationInstruction(
                        type = type,
                        text = turnText,
                        distance = 0.0, // Distance to next point will be set by tracker
                        node = curr
                    )
                )
            }
        }
        
        // 3. Final Arrival
        instructions.add(generateArrival(nodes.last(), destinationName))
        
        return instructions
    }

    private fun generateArrival(node: GraphNode, name: String) = NavigationInstruction(
        type = InstructionType.ARRIVED,
        text = "Has llegado a $name",
        distance = 0.0,
        node = node
    )

    private fun calculateBearing(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val phi1 = Math.toRadians(lat1)
        val phi2 = Math.toRadians(lat2)
        val deltaLambda = Math.toRadians(lon2 - lon1)
        
        val y = sin(deltaLambda) * cos(phi2)
        val x = cos(phi1) * sin(phi2) - sin(phi1) * cos(phi2) * cos(deltaLambda)
        
        return (Math.toDegrees(atan2(y, x)) + 360) % 360
    }
}
