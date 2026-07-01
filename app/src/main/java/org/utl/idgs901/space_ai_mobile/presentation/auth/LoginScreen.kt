package org.utl.idgs901.space_ai_mobile.presentation.auth

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.utl.idgs901.space_ai_mobile.core.designsystem.components.*
import org.utl.idgs901.space_ai_mobile.core.designsystem.motion.*

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LoginScreen(
    windowSizeClass: WindowSizeClass,
    viewModel: LoginViewModel = hiltViewModel(),
    onNavigateToDashboard: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val haptic = LocalHapticFeedback.current
    val shakeState = rememberSpaceIAShakeState()
    var showSuccess by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is LoginEffect.NavigateToDashboard -> {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress) // Use LongPress as proxy for Confirm if not available
                    showSuccess = true
                    delay(1500)
                    onNavigateToDashboard()
                }
                is LoginEffect.ShowSnackbar -> {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    shakeState.shake()
                    snackbarHostState.showSnackbar(effect.message)
                }
            }
        }
    }

    SpaceIAPremiumBackground {
        LoginContent(
            uiState = uiState,
            snackbarHostState = snackbarHostState,
            shakeState = shakeState,
            onEvent = viewModel::onEvent
        )

        if (showSuccess) {
            SuccessOverlay()
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LoginContent(
    uiState: LoginUiState,
    snackbarHostState: SnackbarHostState,
    shakeState: SpaceIAShakeState,
    onEvent: (LoginEvent) -> Unit
) {
    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 24.dp)
                .imePadding(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // HERO SECTION (30-40%)
            Spacer(modifier = Modifier.height(64.dp))
            HeroSection()
            
            Spacer(modifier = Modifier.height(48.dp))

            // LOGIN FORM
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 500.dp)
                    .spaceIAShake(shakeState)
                    .spaceIAStaggeredEntrance(2)
                    .shadow(16.dp, RoundedCornerShape(32.dp)),
                shape = RoundedCornerShape(32.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.92f))
            ) {
                Column(
                    modifier = Modifier
                        .padding(32.dp)
                        .fillMaxWidth()
                ) {
                    Text(
                        text = "Acceso Seguro",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1E293B)
                    )
                    Text(
                        text = "Usa tus credenciales universitarias.",
                        fontSize = 14.sp,
                        color = Color.Gray,
                        modifier = Modifier.padding(top = 4.dp)
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    SpaceIAPremiumTextField(
                        value = uiState.email,
                        onValueChange = { onEvent(LoginEvent.EmailChanged(it)) },
                        label = "Correo Electrónico",
                        placeholder = "ej. alex@utl.edu.mx",
                        leadingIcon = Icons.Default.Email,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Email,
                            imeAction = ImeAction.Next
                        ),
                        isError = uiState.emailError != null,
                        errorMessage = uiState.emailError
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    SpaceIAPremiumTextField(
                        value = uiState.password,
                        onValueChange = { onEvent(LoginEvent.PasswordChanged(it)) },
                        label = "Contraseña",
                        placeholder = "••••••••",
                        leadingIcon = Icons.Default.Lock,
                        isPassword = true,
                        isPasswordVisible = uiState.isPasswordVisible,
                        onTogglePassword = { onEvent(LoginEvent.TogglePasswordVisibility) },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Done
                        ),
                        isError = uiState.passwordError != null,
                        errorMessage = uiState.passwordError
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    SpaceIAPremiumButton(
                        text = "Iniciar Sesión",
                        onClick = { onEvent(LoginEvent.SubmitLogin) },
                        isLoading = uiState.isLoading,
                        enabled = uiState.isFormValid
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        HorizontalDivider(modifier = Modifier.weight(1f), color = Color.LightGray.copy(alpha = 0.5f))
                        Text(
                            text = "o",
                            modifier = Modifier.padding(horizontal = 16.dp),
                            color = Color.Gray,
                            fontSize = 14.sp
                        )
                        HorizontalDivider(modifier = Modifier.weight(1f), color = Color.LightGray.copy(alpha = 0.5f))
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    MicrosoftSignInButton(
                        onClick = { /* TODO */ },
                        modifier = Modifier.spaceIAStaggeredEntrance(3)
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
            
            // FOOTER SECTION
            FooterSection()
            Spacer(modifier = Modifier.height(32.dp))
        }

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

@Composable
private fun HeroSection() {
    val infiniteTransition = rememberInfiniteTransition(label = "LogoFloat")
    val floatAnim by infiniteTransition.animateFloat(
        initialValue = -6f,
        targetValue = 6f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "Floating"
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.spaceIAStaggeredEntrance(0)
    ) {
        Surface(
            modifier = Modifier
                .size(84.dp)
                .graphicsLayer { translationY = floatAnim }
                .shadow(12.dp, RoundedCornerShape(24.dp)),
            shape = RoundedCornerShape(24.dp),
            color = Color.White
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    Icons.Default.Face, 
                    contentDescription = null, 
                    tint = Color(0xFF0D47A1),
                    modifier = Modifier.size(48.dp)
                )
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Text(
            text = "Bienvenido a",
            fontSize = 18.sp,
            color = Color(0xFF0D47A1),
            fontWeight = FontWeight.Medium,
            letterSpacing = 1.sp
        )
        
        Text(
            text = "SpaceIA",
            fontSize = 48.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color(0xFF0D47A1),
            letterSpacing = (-1.5).sp
        )
        
        Text(
            text = "Tu acceso inteligente al campus",
            fontSize = 16.sp,
            color = Color(0xFF0D47A1).copy(alpha = 0.7f),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}

@Composable
private fun FooterSection() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.spaceIAStaggeredEntrance(4)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("¿Problemas al entrar? ", color = Color.Gray, fontSize = 14.sp)
            Text(
                "Soporte IT", 
                color = Color(0xFF0D47A1), 
                fontSize = 14.sp, 
                fontWeight = FontWeight.Bold
            )
        }
        
        Spacer(modifier = Modifier.height(12.dp))
        
        Text(
            text = "v1.2.5 • Universidad Tecnológica",
            fontSize = 11.sp,
            color = Color.LightGray,
            letterSpacing = 0.5.sp
        )
    }
}

@Composable
private fun SuccessOverlay() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White.copy(alpha = 0.95f))
            .clickable(enabled = false) {},
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Surface(
                modifier = Modifier
                    .size(120.dp)
                    .spaceIAStaggeredEntrance(0),
                shape = RoundedCornerShape(60.dp),
                color = Color(0xFF4CAF50).copy(alpha = 0.1f)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = Color(0xFF4CAF50),
                        modifier = Modifier.size(72.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(32.dp))
            Text(
                "¡Acceso Concedido!",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1B5E20)
            )
            Text(
                "Iniciando sesión en SpaceIA...",
                fontSize = 16.sp,
                color = Color.Gray,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}
