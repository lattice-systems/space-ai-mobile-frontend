package org.utl.idgs901.space_ai_mobile.presentation.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun LoginScreen(
    windowSizeClass: WindowSizeClass,
    viewModel: LoginViewModel = hiltViewModel(),
    onNavigateToDashboard: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is LoginEffect.NavigateToDashboard -> onNavigateToDashboard()
                is LoginEffect.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(effect.message)
                }
            }
        }
    }

    LoginContent(
        uiState = uiState,
        snackbarHostState = snackbarHostState,
        onEvent = viewModel::onEvent
    )
}

@Composable
fun LoginContent(
    uiState: LoginUiState,
    snackbarHostState: SnackbarHostState,
    onEvent: (LoginEvent) -> Unit
) {
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFE3F2FD),
                        Color(0xFFBBDEFB)
                    )
                )
            )
            .windowInsetsPadding(WindowInsets.safeDrawing)
    ) {
        val screenWidth = this.maxWidth
        val screenHeight = this.maxHeight
        
        val horizontalPadding = when {
            screenWidth < 360.dp -> 16.dp
            screenWidth < 600.dp -> 24.dp
            else -> 32.dp
        }
        
        val cardMaxWidth = 600.dp
        val logoSize = if (screenHeight < 600.dp) 48.dp else 64.dp
        val spacingScale = if (screenHeight < 700.dp) 0.7f else 1.0f

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .imePadding()
                .padding(horizontal = horizontalPadding, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height((40 * spacingScale).dp))
            
            Surface(
                modifier = Modifier
                    .sizeIn(minWidth = logoSize, minHeight = logoSize)
                    .size(logoSize),
                shape = RoundedCornerShape(16.dp),
                color = Color.White,
                shadowElevation = 4.dp
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.Face,
                        contentDescription = "Logotipo",
                        tint = Color(0xFF003C8F),
                        modifier = Modifier.size((logoSize.value * 0.5f).dp)
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = "SpaceIA",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF003C8F)
            )
            
            Text(
                text = "SISTEMA OPERATIVO DIGITAL",
                fontSize = 12.sp,
                letterSpacing = 2.sp,
                color = Color(0xFF003C8F),
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center
            )
            
            Spacer(modifier = Modifier.height((48 * spacingScale).dp))
            
            Card(
                modifier = Modifier
                    .widthIn(max = cardMaxWidth)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(horizontalPadding)
                        .padding(vertical = (32 * spacingScale).dp)
                        .fillMaxWidth()
                ) {
                    Text(
                        text = "Bienvenido a SpaceIA",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                    
                    Text(
                        text = "Inicia sesión para gestionar tu experiencia en el campus.",
                        fontSize = 14.sp,
                        color = Color.Gray,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp)
                    )
                    
                    Spacer(modifier = Modifier.height((32 * spacingScale).dp))
                    
                    Text(
                        text = "Correo Electrónico",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.DarkGray
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    TextField(
                        value = uiState.email,
                        onValueChange = { onEvent(LoginEvent.EmailChanged(it)) },
                        placeholder = { Text("ej. jdoe@universidad.edu", color = Color.LightGray) },
                        leadingIcon = { 
                            Icon(Icons.Default.Email, contentDescription = null, tint = Color.Gray) 
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp)),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color(0xFFF5F7FA),
                            unfocusedContainerColor = Color(0xFFF5F7FA),
                            disabledContainerColor = Color(0xFFF5F7FA),
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        ),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Email,
                            imeAction = ImeAction.Next
                        ),
                        isError = uiState.emailError != null,
                        singleLine = true
                    )
                    
                    Spacer(modifier = Modifier.height((24 * spacingScale).dp))
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Contraseña",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.DarkGray
                        )
                        TextButton(
                            onClick = { /* TODO */ },
                            contentPadding = PaddingValues(0.dp)
                        ) {
                            Text(
                                text = "¿Olvidaste tu contraseña?",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF003C8F)
                            )
                        }
                    }
                    TextField(
                        value = uiState.password,
                        onValueChange = { onEvent(LoginEvent.PasswordChanged(it)) },
                        placeholder = { Text("........", color = Color.LightGray) },
                        leadingIcon = { 
                            Icon(Icons.Default.Lock, contentDescription = null, tint = Color.Gray) 
                        },
                        trailingIcon = {
                            IconButton(onClick = { onEvent(LoginEvent.TogglePasswordVisibility) }) {
                                Icon(
                                    imageVector = if (uiState.isPasswordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                    contentDescription = null,
                                    tint = Color.Gray
                                )
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp)),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color(0xFFF5F7FA),
                            unfocusedContainerColor = Color(0xFFF5F7FA),
                            disabledContainerColor = Color(0xFFF5F7FA),
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        ),
                        visualTransformation = if (uiState.isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Done
                        ),
                        isError = uiState.passwordError != null,
                        singleLine = true
                    )
                    
                    Spacer(modifier = Modifier.height((32 * spacingScale).dp))
                    
                    Button(
                        onClick = { onEvent(LoginEvent.SubmitLogin) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF003C8F)),
                        enabled = !uiState.isLoading && uiState.isFormValid
                    ) {
                        if (uiState.isLoading) {
                            CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                        } else {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("Iniciar Sesión", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                                Spacer(modifier = Modifier.width(8.dp))
                                Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null)
                            }
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    OutlinedButton(
                        onClick = { /* TODO */ },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(12.dp),
                        border = ButtonDefaults.outlinedButtonBorder.copy(width = 1.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Face, 
                            contentDescription = null, 
                            tint = Color(0xFF003C8F),
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Usar Login Biométrico", 
                            color = Color(0xFF424242), 
                            fontWeight = FontWeight.Medium,
                            fontSize = 14.sp
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.weight(1f))
            Spacer(modifier = Modifier.height(32.dp))
            
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Row {
                    Text(text = "¿No tienes una cuenta? ", color = Color.Gray, fontSize = 14.sp)
                    Text(
                        text = "Contactar a Registro", 
                        color = Color(0xFF003C8F), 
                        fontSize = 14.sp, 
                        fontWeight = FontWeight.Bold
                    )
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "Política de Privacidad", color = Color.Gray, fontSize = 12.sp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(modifier = Modifier.size(4.dp).background(Color.LightGray, RoundedCornerShape(2.dp)))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Términos de Servicio", color = Color.Gray, fontSize = 12.sp)
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
        }
        
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}
