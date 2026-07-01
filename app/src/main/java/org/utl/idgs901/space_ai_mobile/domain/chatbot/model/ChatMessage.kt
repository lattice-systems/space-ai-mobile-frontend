package org.utl.idgs901.space_ai_mobile.domain.chatbot.model

import java.time.Instant

enum class ChatSender {
    USER,
    SPACE_IA
}

data class ChatMessage(
    val id: String,
    val content: String,
    val sender: ChatSender,
    val timestamp: Instant = Instant.now(),
    val isError: Boolean = false
)
