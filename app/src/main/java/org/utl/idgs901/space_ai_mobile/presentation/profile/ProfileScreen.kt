package org.utl.idgs901.space_ai_mobile.presentation.profile

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import org.utl.idgs901.space_ai_mobile.core.designsystem.components.*
import org.utl.idgs901.space_ai_mobile.core.designsystem.motion.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onBackClick: () -> Unit,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val scrollState = rememberScrollState()
    val haptics = rememberSpaceIAHaptics()

    LaunchedEffect(uiState.updateSuccess) {
        if (uiState.updateSuccess) {
            haptics.success()
            // Show toast or snackbar (optional)
            viewModel.resetUpdateSuccess()
        }
    }
    
    LaunchedEffect(uiState.passwordError) {
        if (uiState.passwordError != null) {
            haptics.error()
        }
    }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F7FA))
    ) {
        val screenWidth = this.maxWidth
        val isWide = screenWidth > 600.dp
        
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = { Text("Perfil", fontWeight = FontWeight.Bold) },
                    navigationIcon = {
                        IconButton(onClick = {
                            haptics.lightClick()
                            onBackClick()
                        }) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Atrás")
                        }
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = Color.White
                    )
                )
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .verticalScroll(scrollState)
                    .padding(horizontal = if (isWide) screenWidth * 0.15f else 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (uiState.isLoading) {
                    ProfileSkeleton()
                } else {
                    ProfileContent(
                        uiState = uiState,
                        viewModel = viewModel
                    )
                }
                Spacer(modifier = Modifier.height(40.dp))
            }
        }
    }
}

@Composable
fun ProfileContent(
    uiState: ProfileUiState,
    viewModel: ProfileViewModel
) {
    val user = uiState.userProfile ?: return
    
    Spacer(modifier = Modifier.height(24.dp))
    
    // Header Section
    ProfileHeader(user)
    
    Spacer(modifier = Modifier.height(32.dp))
    
    // Personal Info Card
    InfoCard(
        title = "Información Personal",
        icon = Icons.Default.Person,
        items = listOf(
            "Nombre completo" to user.name,
            "Matrícula" to user.matricula,
            "Grupo" to user.group
        ),
        index = 1
    )
    
    Spacer(modifier = Modifier.height(16.dp))
    
    // Academic Info Card
    InfoCard(
        title = "Información Académica",
        icon = Icons.Default.School,
        items = listOf(
            "Carrera" to user.career,
            "División" to user.division,
            "Campus" to user.campus
        ),
        index = 2
    )
    
    Spacer(modifier = Modifier.height(16.dp))
    
    // Contact Info Card
    InfoCard(
        title = "Información de Contacto",
        icon = Icons.Default.Email,
        items = listOf(
            "Correo" to user.email,
            "Teléfono" to user.phone
        ),
        index = 3
    )
    
    Spacer(modifier = Modifier.height(32.dp))
    
    // Security Section
    SecuritySection(
        uiState = uiState,
        viewModel = viewModel,
        index = 4
    )
}

@Composable
fun ProfileHeader(user: org.utl.idgs901.space_ai_mobile.domain.model.UserProfile) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.spaceIAStaggeredEntrance(0)
    ) {
        Surface(
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .border(3.dp, Color(0xFF0D47A1).copy(alpha = 0.3f), CircleShape)
                .spaceIAPressScale(),
            color = Color.White,
            tonalElevation = 8.dp
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    Icons.Default.AccountCircle,
                    contentDescription = null,
                    modifier = Modifier.size(100.dp),
                    tint = Color(0xFF0D47A1)
                )
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = user.name,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
        
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(top = 4.dp)
        ) {
            Icon(
                Icons.Default.Verified,
                contentDescription = null,
                tint = Color(0xFF4CAF50),
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "Cuenta verificada",
                fontSize = 14.sp,
                color = Color.Gray
            )
        }
        
        Text(
            text = user.career,
            fontSize = 14.sp,
            color = Color(0xFF0D47A1),
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}

@Composable
fun InfoCard(
    title: String,
    icon: ImageVector,
    items: List<Pair<String, String>>,
    index: Int
) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .spaceIAStaggeredEntrance(index)
            .spaceIAPressScale(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(icon, contentDescription = null, tint = Color(0xFF0D47A1), modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(12.dp))
                Text(text = title, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.Black)
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            items.forEachIndexed { i, item ->
                Column(modifier = Modifier.padding(vertical = 8.dp)) {
                    Text(text = item.first, fontSize = 12.sp, color = Color.Gray, letterSpacing = 0.5.sp)
                    Text(text = item.second, fontSize = 16.sp, fontWeight = FontWeight.Medium, color = Color.Black)
                }
                if (i < items.size - 1) {
                    Divider(color = Color(0xFFF0F0F0), thickness = 1.dp)
                }
            }
        }
    }
}

@Composable
fun SecuritySection(
    uiState: ProfileUiState,
    viewModel: ProfileViewModel,
    index: Int
) {
    val haptics = LocalHapticFeedback.current

    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .spaceIAStaggeredEntrance(index),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Security, contentDescription = null, tint = Color(0xFF0D47A1), modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(12.dp))
                Text(text = "Seguridad", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.Black)
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Cambiar contraseña", fontSize = 14.sp, color = Color.Gray)
            
            Spacer(modifier = Modifier.height(24.dp))
            
            SpaceIAPremiumTextField(
                value = uiState.currentPassword,
                onValueChange = viewModel::onCurrentPasswordChange,
                label = "Contraseña actual",
                placeholder = "••••••••",
                leadingIcon = Icons.Default.Lock,
                isPassword = true,
                isPasswordVisible = uiState.isPasswordVisible,
                onTogglePassword = {
                    haptics.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                    viewModel.togglePasswordVisibility()
                }
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            SpaceIAPremiumTextField(
                value = uiState.newPassword,
                onValueChange = viewModel::onNewPasswordChange,
                label = "Nueva contraseña",
                placeholder = "••••••••",
                leadingIcon = Icons.Default.VpnKey,
                isPassword = true,
                isPasswordVisible = uiState.isPasswordVisible,
                onTogglePassword = viewModel::togglePasswordVisibility
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            SpaceIAPremiumTextField(
                value = uiState.confirmPassword,
                onValueChange = viewModel::onConfirmPasswordChange,
                label = "Confirmar nueva contraseña",
                placeholder = "••••••••",
                leadingIcon = Icons.Default.CheckCircle,
                isPassword = true,
                isPasswordVisible = uiState.isPasswordVisible,
                onTogglePassword = viewModel::togglePasswordVisibility,
                isError = uiState.passwordError != null,
                errorMessage = uiState.passwordError
            )
            
            Spacer(modifier = Modifier.height(32.dp))
            
            SpaceIAPremiumButton(
                text = "Actualizar contraseña",
                onClick = viewModel::updatePassword,
                isLoading = uiState.isUpdatingPassword
            )
        }
    }
}

@Composable
fun ProfileSkeleton() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(24.dp))
        Box(
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .spaceIAShimmer()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Box(modifier = Modifier.width(200.dp).height(24.dp).clip(RoundedCornerShape(4.dp)).spaceIAShimmer())
        Spacer(modifier = Modifier.height(8.dp))
        Box(modifier = Modifier.width(150.dp).height(16.dp).clip(RoundedCornerShape(4.dp)).spaceIAShimmer())
        
        Spacer(modifier = Modifier.height(48.dp))
        
        repeat(3) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .spaceIAShimmer()
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
