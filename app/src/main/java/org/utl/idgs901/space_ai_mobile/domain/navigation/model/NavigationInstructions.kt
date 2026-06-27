package org.utl.idgs901.space_ai_mobile.domain.navigation.model

data class NavigationInstruction(
    val type: InstructionType,
    val text: String,
    val distance: Double,
    val node: GraphNode
)

enum class InstructionType {
    START,
    STRAIGHT,
    LEFT,
    RIGHT,
    ARRIVED
}
