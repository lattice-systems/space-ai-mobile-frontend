package org.utl.idgs901.space_ai_mobile.data.chatbot.repository

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.utl.idgs901.space_ai_mobile.domain.chatbot.model.ChatMessage
import org.utl.idgs901.space_ai_mobile.domain.chatbot.model.ChatSender
import org.utl.idgs901.space_ai_mobile.domain.chatbot.repository.ChatRepository
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MockChatRepositoryImpl @Inject constructor() : ChatRepository {

    private val _messages = MutableStateFlow<List<ChatMessage>>(emptyList())

    override fun getMessages(): Flow<List<ChatMessage>> = _messages.asStateFlow()

    override suspend fun sendMessage(content: String): Result<Unit> {
        // Add User Message
        val userMessage = ChatMessage(
            id = UUID.randomUUID().toString(),
            content = content,
            sender = ChatSender.USER
        )
        _messages.update { it + userMessage }

        // Simulate AI Response
        delay(1500)
        
        val aiResponse = ChatMessage(
            id = UUID.randomUUID().toString(),
            content = generateMockResponse(content),
            sender = ChatSender.SPACE_IA
        )
        _messages.update { it + aiResponse }
        
        return Result.success(Unit)
    }

    override suspend fun clearHistory() {
        _messages.value = emptyList()
    }

    private fun generateMockResponse(userPrompt: String): String {
        val lowerPrompt = userPrompt.lowercase()
        return when {
            lowerPrompt.contains("clase") || lowerPrompt.contains("clases") -> 
                "Tu próxima clase es Ingeniería de Software con el Prof. Torres en el Edificio G, aula 302 a las 4:00 PM."
            lowerPrompt.contains("biblioteca") || lowerPrompt.contains("llego") -> 
                "Para llegar a la Biblioteca Central, sal del edificio actual y dirígete hacia el norte, pasando el área de cafetería. Está justo frente al auditorio."
            lowerPrompt.contains("evento") || lowerPrompt.contains("hoy") -> 
                "Hoy tenemos la feria de tecnología en el patio central y una conferencia sobre IA en el aula magna a las 6:00 PM."
            lowerPrompt.contains("materias") || lowerPrompt.contains("ayuda") -> 
                "¡Claro! Puedo ayudarte con tus materias de IDGS-801. Actualmente estás cursando: Desarrollo Móvil, Seguridad de la Información y Gestión de Proyectos. ¿En cuál necesitas apoyo?"
            else -> "¡Hola! Soy SpaceIA, tu asistente universitario. He recibido tu mensaje: \"$userPrompt\". ¿En qué más puedo apoyarte hoy?"
        }
    }
}
