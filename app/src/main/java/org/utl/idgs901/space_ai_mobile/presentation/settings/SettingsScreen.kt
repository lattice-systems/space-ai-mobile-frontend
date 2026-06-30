package org.utl.idgs901.space_ai_mobile.presentation.settings

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import org.utl.idgs901.space_ai_mobile.core.designsystem.motion.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onBackClick: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val haptics = rememberSpaceIAHaptics()
    val nativeHaptics = LocalHapticFeedback.current

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
                    title = { Text("Configuración", fontWeight = FontWeight.Bold) },
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
            if (uiState.isLoading) {
                SettingsSkeleton(modifier = Modifier.padding(innerPadding))
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .padding(horizontal = if (isWide) screenWidth * 0.1f else 16.dp),
                    contentPadding = PaddingValues(vertical = 24.dp),
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    item {
                        SettingsHeader()
                    }

                    // Accessibility Section
                    item {
                        SettingsSection(
                            title = "Accesibilidad",
                            icon = Icons.Default.AccessibilityNew,
                            index = 1
                        ) {
                            Column {
                                SettingSwitchItem(
                                    label = "Reducir animaciones",
                                    description = "Disminuye efectos visuales y motion design",
                                    checked = uiState.preferences.reduceAnimations,
                                    onCheckedChange = {
                                        nativeHaptics.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                        viewModel.updateReduceAnimations(it)
                                    }
                                )
                                SettingSwitchItem(
                                    label = "Alto contraste",
                                    description = "Incrementa contraste y visibilidad",
                                    checked = uiState.preferences.highContrast,
                                    onCheckedChange = viewModel::updateHighContrast
                                )
                                SettingOptionItem(
                                    label = "Texto grande",
                                    description = "Tamaño de fuente: ${uiState.preferences.fontSize}",
                                    onClick = { /* Open selection dialog */ }
                                )
                                SettingSwitchItem(
                                    label = "Lectura de pantalla",
                                    description = "Compatibilidad avanzada con TalkBack",
                                    checked = uiState.preferences.screenReaderEnabled,
                                    onCheckedChange = viewModel::updateScreenReader
                                )
                                SettingSwitchItem(
                                    label = "Feedback háptico",
                                    description = "Activar/Desactivar vibraciones",
                                    checked = uiState.preferences.hapticFeedbackEnabled,
                                    onCheckedChange = viewModel::updateHapticFeedback
                                )
                                SettingSwitchItem(
                                    label = "Modo accesibilidad avanzada",
                                    description = "Botones más grandes y mayor separación",
                                    checked = uiState.preferences.advancedAccessibilityMode,
                                    onCheckedChange = viewModel::updateAdvancedAccessibility
                                )
                            }
                        }
                    }

                    // Privacy Section
                    item {
                        SettingsSection(
                            title = "Privacidad",
                            icon = Icons.Default.PrivacyTip,
                            index = 2
                        ) {
                            Column {
                                SettingInfoItem(
                                    label = "Permisos",
                                    description = "Ubicación: Permitido",
                                    onClick = { /* Open system settings */ }
                                )
                                SettingInfoItem(
                                    label = "Uso de datos",
                                    description = "WiFi: 25 MB | Datos: 8 MB",
                                    onClick = { }
                                )
                                SettingOptionItem(
                                    label = "Política de privacidad",
                                    description = "Ver versión 2026.07",
                                    onClick = { }
                                )
                            }
                        }
                    }

                    // About Section
                    item {
                        SettingsSection(
                            title = "Acerca de",
                            icon = Icons.Default.Info,
                            index = 3
                        ) {
                            Column {
                                SettingInfoItem(
                                    label = "Versión",
                                    description = "SpaceIA Mobile ${uiState.version} (${uiState.buildDate})",
                                    showArrow = false
                                )
                                SettingInfoItem(
                                    label = "Créditos",
                                    description = "Universidad Tecnológica de León",
                                    showArrow = false
                                )
                                SettingOptionItem(
                                    label = "Términos y condiciones",
                                    onClick = { }
                                )
                                SettingInfoItem(
                                    label = "Contacto de soporte",
                                    description = uiState.supportEmail,
                                    onClick = { }
                                )
                            }
                        }
                    }
                    
                    item {
                        Spacer(modifier = Modifier.height(24.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun SettingsHeader() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
            .spaceIAStaggeredEntrance(0)
    ) {
        Text(
            text = "⚙ Configuración",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
        Text(
            text = "Personaliza tu experiencia en SpaceIA.",
            fontSize = 16.sp,
            color = Color.Gray,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}

@Composable
fun SettingsSection(
    title: String,
    icon: ImageVector,
    index: Int,
    content: @Composable () -> Unit
) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .spaceIAStaggeredEntrance(index),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(icon, contentDescription = null, tint = Color(0xFF0D47A1), modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(12.dp))
                Text(text = title, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color.Black)
            }
            content()
        }
    }
}

@Composable
fun SettingSwitchItem(
    label: String,
    description: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = label, fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
            Text(text = description, fontSize = 12.sp, color = Color.Gray)
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = Color(0xFF0D47A1),
                uncheckedThumbColor = Color.White,
                uncheckedTrackColor = Color.LightGray
            )
        )
    }
}

@Composable
fun SettingOptionItem(
    label: String,
    description: String? = null,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = label, fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
            if (description != null) {
                Text(text = description, fontSize = 12.sp, color = Color.Gray)
            }
        }
        Icon(Icons.Default.ChevronRight, contentDescription = null, tint = Color.LightGray)
    }
}

@Composable
fun SettingInfoItem(
    label: String,
    description: String? = null,
    showArrow: Boolean = true,
    onClick: (() -> Unit)? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .then(if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = label, fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
            if (description != null) {
                Text(text = description, fontSize = 12.sp, color = Color.Gray)
            }
        }
        if (showArrow) {
            Icon(Icons.Default.ChevronRight, contentDescription = null, tint = Color.LightGray)
        }
    }
}

@Composable
fun SettingsSkeleton(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Box(modifier = Modifier.width(200.dp).height(32.dp).clip(RoundedCornerShape(8.dp)).spaceIAShimmer())
        Box(modifier = Modifier.width(250.dp).height(20.dp).clip(RoundedCornerShape(4.dp)).spaceIAShimmer())
        
        repeat(3) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(28.dp))
                    .spaceIAShimmer()
            )
        }
    }
}
