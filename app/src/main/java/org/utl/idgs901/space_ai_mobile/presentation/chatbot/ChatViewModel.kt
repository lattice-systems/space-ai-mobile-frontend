package org.utl.idgs901.space_ai_mobile.presentation.chatbot

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.utl.idgs901.space_ai_mobile.domain.chatbot.usecase.*
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val getChatMessagesUseCase: GetChatMessagesUseCase,
    private val sendChatMessageUseCase: SendChatMessageUseCase,
    private val clearChatHistoryUseCase: ClearChatHistoryUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ChatUiState())
    val uiState: StateFlow<ChatUiState> = _uiState.asStateFlow()

    private var recordingJob: Job? = null
    private var recordingSeconds = 0

    init {
        observeMessages()
    }

    private fun observeMessages() {
        viewModelScope.launch {
            getChatMessagesUseCase().collect { messages ->
                _uiState.update { it.copy(
                    messages = messages,
                    isAiThinking = false
                ) }
            }
        }
    }

    fun onInputChange(text: String) {
        _uiState.update { it.copy(inputText = text) }
    }

    fun sendMessage() {
        val content = _uiState.value.inputText
        if (content.isBlank()) return

        viewModelScope.launch {
            _uiState.update { it.copy(inputText = "", isAiThinking = true) }
            sendChatMessageUseCase(content)
        }
    }

    fun sendSuggestedQuestion(question: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isAiThinking = true) }
            sendChatMessageUseCase(question)
        }
    }

    fun toggleRecording() {
        val currentlyRecording = _uiState.value.isRecording
        if (currentlyRecording) {
            stopRecording()
        } else {
            startRecording()
        }
    }

    private fun startRecording() {
        _uiState.update { it.copy(isRecording = true, recordingTimeText = "00:00") }
        recordingSeconds = 0
        recordingJob = viewModelScope.launch {
            while (true) {
                delay(1000)
                recordingSeconds++
                val minutes = recordingSeconds / 60
                val seconds = recordingSeconds % 60
                _uiState.update { it.copy(recordingTimeText = String.format("%02d:%02d", minutes, seconds)) }
            }
        }
    }

    private fun stopRecording() {
        recordingJob?.cancel()
        _uiState.update { it.copy(isRecording = false) }
        // In a real app, here we would process the audio
        // For now, let's simulate a transcribed message
        if (recordingSeconds > 1) {
            onInputChange("Consulta de voz de ejemplo...")
            sendMessage()
        }
    }

    fun clearHistory() {
        viewModelScope.launch {
            clearChatHistoryUseCase()
        }
    }
}
