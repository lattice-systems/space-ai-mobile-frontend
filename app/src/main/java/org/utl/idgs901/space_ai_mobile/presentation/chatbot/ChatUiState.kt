package org.utl.idgs901.space_ai_mobile.presentation.chatbot

import org.utl.idgs901.space_ai_mobile.domain.chatbot.model.ChatMessage

data class ChatUiState(
    val messages: List<ChatMessage> = emptyList(),
    val isAiThinking: Boolean = false,
    val isRecording: Boolean = false,
    val recordingTimeText: String = "00:00",
    val inputText: String = "",
    val userName: String = "Alex", // Mock for now
    val suggestedQuestions: List<String> = listOf(
        "📚 ¿Cuál es mi próxima clase?",
        "📍 ¿Cómo llego a Biblioteca?",
        "📅 ¿Qué eventos hay hoy?",
        "🤖 Ayúdame con mis materias",
        "🍽 ¿Qué hay en cafetería?",
        "🏫 ¿Dónde está Rectoría?"
    )
)
