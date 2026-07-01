package org.utl.idgs901.space_ai_mobile.domain.chatbot.repository

import kotlinx.coroutines.flow.Flow
import org.utl.idgs901.space_ai_mobile.domain.chatbot.model.ChatMessage

interface ChatRepository {
    fun getMessages(): Flow<List<ChatMessage>>
    suspend fun sendMessage(content: String): Result<Unit>
    suspend fun clearHistory()
}
