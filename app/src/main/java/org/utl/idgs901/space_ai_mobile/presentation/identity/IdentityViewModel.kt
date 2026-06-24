package org.utl.idgs901.space_ai_mobile.presentation.identity

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
import org.utl.idgs901.space_ai_mobile.domain.usecase.GetQrUseCase
import java.time.Duration
import java.time.Instant
import javax.inject.Inject

@HiltViewModel
class IdentityViewModel @Inject constructor(
    private val getQrUseCase: GetQrUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(IdentityUiState())
    val uiState: StateFlow<IdentityUiState> = _uiState.asStateFlow()

    private var timerJob: Job? = null
    
    // Mock user ID for Sprint 1
    private val currentUserId = "alex_sterling_8892"

    init {
        refreshQr()
    }

    fun refreshQr() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                val qr = getQrUseCase(currentUserId)
                _uiState.update { it.copy(qrIdentity = qr, isLoading = false) }
                startTimer(qr.expiresAt)
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, errorMessage = e.message) }
            }
        }
    }

    private fun startTimer(expiresAt: Instant) {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (true) {
                val now = Instant.now()
                val remaining = Duration.between(now, expiresAt)
                
                if (remaining.isNegative || remaining.isZero) {
                    _uiState.update { it.copy(remainingTimeText = "00:00") }
                    refreshQr() // Auto-refresh when expired
                    break
                }
                
                val minutes = remaining.toMinutes()
                val seconds = remaining.seconds % 60
                val timeText = String.format("%02d:%02d", minutes, seconds)
                
                _uiState.update { it.copy(remainingTimeText = timeText) }
                delay(1000)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
    }
}
