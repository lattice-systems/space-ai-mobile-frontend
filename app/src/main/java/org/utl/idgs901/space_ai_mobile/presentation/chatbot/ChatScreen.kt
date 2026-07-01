package org.utl.idgs901.space_ai_mobile.presentation.chatbot

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Send
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.utl.idgs901.space_ai_mobile.core.designsystem.components.SpaceIAAuroraBackground
import org.utl.idgs901.space_ai_mobile.core.designsystem.components.SpaceIAPremiumChatHero
import org.utl.idgs901.space_ai_mobile.core.designsystem.components.SpaceIAVoiceOrb
import org.utl.idgs901.space_ai_mobile.core.designsystem.motion.*
import org.utl.idgs901.space_ai_mobile.domain.chatbot.model.ChatMessage
import org.utl.idgs901.space_ai_mobile.domain.chatbot.model.ChatSender

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    viewModel: ChatViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val scrollState = rememberLazyListState()
    val haptics = rememberSpaceIAHaptics()

    // Auto-scroll logic
    LaunchedEffect(uiState.messages.size, uiState.isAiThinking) {
        if (uiState.messages.isNotEmpty() || uiState.isAiThinking) {
            scrollState.animateScrollToItem(
                if (uiState.isAiThinking) uiState.messages.size else uiState.messages.size - 1
            )
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        SpaceIAAuroraBackground()

        Scaffold(
            containerColor = Color.Transparent
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                Column(modifier = Modifier.fillMaxSize()) {
                    // Chat Content
                    LazyColumn(
                        state = scrollState,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 24.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // Reference-based Hero Section
                        if (uiState.messages.isEmpty() && !uiState.isAiThinking) {
                            item {
                                ReferenceHeroSection(uiState.userName)
                            }
                            
                            item {
                                Spacer(modifier = Modifier.height(32.dp))
                                SuggestedQuestionsGrid(
                                    questions = uiState.suggestedQuestions,
                                    onQuestionClick = {
                                        haptics.lightClick()
                                        viewModel.sendSuggestedQuestion(it)
                                    }
                                )
                            }
                            
                            item {
                                Spacer(modifier = Modifier.height(24.dp))
                                Box(
                                    modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "Comienza una conversación con SpaceIA",
                                        fontSize = 13.sp,
                                        color = Color.Gray.copy(alpha = 0.6f),
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }
                        }

                        // Messages
                        items(uiState.messages, key = { it.id }) { message ->
                            PremiumMessageBubble(message)
                        }

                        // Thinking State
                        if (uiState.isAiThinking) {
                            item {
                                ThinkingIndicator()
                            }
                        }
                    }

                    // Multimodal Input
                    PremiumInputBar(
                        uiState = uiState,
                        onInputChange = viewModel::onInputChange,
                        onSend = {
                            viewModel.sendMessage()
                            haptics.success()
                        },
                        onToggleVoice = {
                            viewModel.toggleRecording()
                            haptics.lightClick()
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun ChatHeader() {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding(),
        color = Color.White.copy(alpha = 0.6f),
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 20.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.size(36.dp),
                shape = CircleShape,
                color = Color(0xFF0D47A1).copy(alpha = 0.1f)
            ) {
                Icon(Icons.Default.Person, contentDescription = "Perfil", modifier = Modifier.padding(6.dp))
            }
            
            Spacer(modifier = Modifier.width(12.dp))
            
            Text(
                text = "SpaceIA",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0D47A1)
            )
        }
    }
}

@Composable
fun ReferenceHeroSection(userName: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SpaceIAPremiumChatHero(
            modifier = Modifier.spaceIAStaggeredEntrance(0)
        )

        Spacer(modifier = Modifier.height(48.dp))

        Text(
            text = "Buenos días, $userName",
            fontSize = 32.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color(0xFF0D47A1),
            modifier = Modifier.spaceIAStaggeredEntrance(1)
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Soy SpaceIA. ¿Cómo puedo ayudarte hoy en tu experiencia universitaria?",
            fontSize = 16.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center,
            lineHeight = 24.sp,
            modifier = Modifier.spaceIAStaggeredEntrance(2)
        )
    }
}

@Composable
fun SuggestedQuestionsGrid(
    questions: List<String>,
    onQuestionClick: (String) -> Unit
) {
    // Implementing a grid-like flow with rows for suggested questions
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // First Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            SuggestedChip(questions[0], Modifier.weight(1f), 3, onQuestionClick)
            SuggestedChip(questions[1], Modifier.weight(1.2f), 4, onQuestionClick)
        }
        // Second Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            SuggestedChip(questions[2], Modifier.weight(1.2f), 5, onQuestionClick)
            SuggestedChip(questions[3], Modifier.weight(1f), 6, onQuestionClick)
        }
    }
}

@Composable
fun SuggestedChip(
    text: String,
    modifier: Modifier = Modifier,
    index: Int,
    onClick: (String) -> Unit
) {
    Surface(
        modifier = modifier
            .spaceIAStaggeredEntrance(index)
            .spaceIAPressScale()
            .clickable { onClick(text) },
        shape = RoundedCornerShape(16.dp),
        color = Color.White.copy(alpha = 0.9f),
        border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
        tonalElevation = 2.dp
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF1E293B),
            maxLines = 1
        )
    }
}

@Composable
fun PremiumMessageBubble(message: ChatMessage) {
    val isUser = message.sender == ChatSender.USER
    
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = if (isUser) Alignment.CenterEnd else Alignment.CenterStart
    ) {
        Column(
            horizontalAlignment = if (isUser) Alignment.End else Alignment.Start,
            modifier = Modifier.fillMaxWidth(0.85f)
        ) {
            Surface(
                shape = RoundedCornerShape(
                    topStart = 20.dp,
                    topEnd = 20.dp,
                    bottomStart = if (isUser) 20.dp else 4.dp,
                    bottomEnd = if (isUser) 4.dp else 20.dp
                ),
                color = if (isUser) Color(0xFF0D47A1) else Color.White.copy(alpha = 0.95f),
                tonalElevation = if (isUser) 4.dp else 2.dp,
                border = if (isUser) null else BorderStroke(1.dp, Color(0xFFF1F5F9))
            ) {
                if (!isUser) {
                    TypewriterText(
                        text = message.content,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                        color = Color(0xFF1E293B)
                    )
                } else {
                    Text(
                        text = message.content,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                        color = Color.White,
                        fontSize = 15.sp,
                        lineHeight = 22.sp
                    )
                }
            }
        }
    }
}

@Composable
fun TypewriterText(text: String, modifier: Modifier = Modifier, color: Color = Color.Unspecified) {
    var displayedText by remember { mutableStateOf("") }
    LaunchedEffect(text) {
        displayedText = ""
        text.forEach { char ->
            displayedText += char
            delay(8)
        }
    }
    Text(text = displayedText, modifier = modifier, color = color, fontSize = 15.sp, lineHeight = 22.sp)
}

@Composable
fun ThinkingIndicator() {
    Row(
        modifier = Modifier.padding(start = 4.dp, top = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(contentAlignment = Alignment.Center) {
            Icon(
                Icons.Rounded.SmartToy,
                contentDescription = null,
                modifier = Modifier.size(18.dp).spaceIAPulse(Color(0xFF0D47A1)),
                tint = Color(0xFF0D47A1)
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = "SpaceIA está pensando...",
            fontSize = 13.sp,
            color = Color.Gray,
            modifier = Modifier.spaceIAShimmer()
        )
    }
}

@Composable
fun PremiumInputBar(
    uiState: ChatUiState,
    onInputChange: (String) -> Unit,
    onSend: () -> Unit,
    onToggleVoice: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),
        shape = RoundedCornerShape(32.dp),
        color = Color.White.copy(alpha = 0.95f),
        tonalElevation = 12.dp,
        shadowElevation = 8.dp,
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.5f))
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { /* Add functionality */ }) {
                Icon(Icons.Default.AddCircleOutline, contentDescription = "Adjuntar", tint = Color.Gray)
            }
            
            if (uiState.isRecording) {
                Row(modifier = Modifier.weight(1f).padding(start = 12.dp), verticalAlignment = Alignment.CenterVertically) {
                    Text("Escuchando...", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0D47A1))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(uiState.recordingTimeText, fontSize = 12.sp, color = Color.Gray)
                }
            } else {
                TextField(
                    value = uiState.inputText,
                    onValueChange = onInputChange,
                    modifier = Modifier.weight(1f),
                    placeholder = { Text("Pregunta a SpaceIA...", color = Color.Gray) },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        errorIndicatorColor = Color.Transparent
                    ),
                    maxLines = 5,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                    keyboardActions = KeyboardActions(onSend = { onSend() })
                )
            }
            
            SpaceIAVoiceOrb(
                isActive = uiState.isRecording,
                modifier = Modifier
                    .size(44.dp)
                    .clickable(onClick = onToggleVoice)
            )
            
            if (!uiState.isRecording && uiState.inputText.isNotBlank()) {
                Spacer(modifier = Modifier.width(8.dp))
                FilledIconButton(
                    onClick = onSend,
                    modifier = Modifier.size(44.dp).spaceIAPressScale(),
                    colors = IconButtonDefaults.filledIconButtonColors(containerColor = Color(0xFF0D47A1))
                ) {
                    Icon(Icons.AutoMirrored.Rounded.Send, contentDescription = "Enviar", modifier = Modifier.size(20.dp))
                }
            }
        }
    }
}
