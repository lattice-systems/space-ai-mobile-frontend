package org.utl.idgs901.space_ai_mobile.domain.chatbot.usecase

import kotlinx.coroutines.flow.Flow
import org.utl.idgs901.space_ai_mobile.domain.chatbot.model.ChatMessage
import org.utl.idgs901.space_ai_mobile.domain.chatbot.repository.ChatRepository
import javax.inject.Inject

class GetChatMessagesUseCase @Inject constructor(private val repository: ChatRepository) {
    operator fun invoke(): Flow<List<ChatMessage>> = repository.getMessages()
}

class SendChatMessageUseCase @Inject constructor(private val repository: ChatRepository) {
    suspend operator fun invoke(content: String): Result<Unit> = repository.sendMessage(content)
}

class ClearChatHistoryUseCase @Inject constructor(private val repository: ChatRepository) {
    suspend operator fun invoke() = repository.clearHistory()
}
