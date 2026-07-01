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

import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.compose.ui.platform.LocalLifecycleOwner

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onBackClick: () -> Unit,
    onNavigateToPrivacy: () -> Unit,
    onNavigateToTerms: () -> Unit,
    onNavigateToCredits: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val haptics = rememberSpaceIAHaptics()
    val nativeHaptics = LocalHapticFeedback.current
    val clipboardManager = LocalClipboardManager.current
    val lifecycleOwner = LocalLifecycleOwner.current
    var showFontSizeDialog by remember { mutableStateOf(false) }
    var showDataUsageDialog by remember { mutableStateOf(false) }

    // Refresh permissions when returning to the screen
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                viewModel.refreshPermissions()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    // Visual configuration based on Advanced Accessibility Mode and Font Size
    val advancedMode = uiState.preferences.advancedAccessibility
    val fontScale = uiState.preferences.fontScale
    
    val fontSizeLabel = when (fontScale) {
        1.2f -> "Grande"
        1.4f -> "Extra grande"
        else -> "Normal"
    }
    
    val verticalPadding = (if (advancedMode) 24.dp else 16.dp) * (if (advancedMode) 1.2f else 1.0f)
    val labelFontSize = (if (advancedMode) 18.sp else 16.sp) * fontScale
    val descFontSize = (if (advancedMode) 14.sp else 12.sp) * fontScale

    val reduceAnimations = uiState.preferences.reduceAnimations

    if (showFontSizeDialog) {
        AlertDialog(
            onDismissRequest = { showFontSizeDialog = false },
            title = { Text("Tamaño de texto", fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    listOf("Normal" to 1.0f, "Grande" to 1.2f, "Extra grande" to 1.4f).forEach { (label, scale) ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    haptics.success()
                                    viewModel.updateFontScale(scale)
                                    showFontSizeDialog = false
                                }
                                .padding(vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = fontScale == scale,
                                onClick = null
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(text = label, fontSize = 16.sp)
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showFontSizeDialog = false }) {
                    Text("Cerrar")
                }
            },
            shape = RoundedCornerShape(24.dp),
            containerColor = Color.White
        )
    }

    if (showDataUsageDialog) {
        AlertDialog(
            onDismissRequest = { showDataUsageDialog = false },
            title = { Text("Estadísticas de Red", fontWeight = FontWeight.Bold) },
            text = {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(text = "Consumo del mes actual:", fontSize = 14.sp, color = Color.Gray)
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "Red WiFi:", fontWeight = FontWeight.Medium)
                        Text(text = uiState.wifiUsage, color = Color(0xFF0D47A1))
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "Datos Móviles:", fontWeight = FontWeight.Medium)
                        Text(text = uiState.mobileUsage, color = Color(0xFF0D47A1))
                    }
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    Text(
                        text = "Nota: El consumo es un estimado basado en las estadísticas del dispositivo.",
                        fontSize = 12.sp,
                        color = Color.LightGray,
                        lineHeight = 16.sp
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = { showDataUsageDialog = false }) {
                    Text("Entendido")
                }
            },
            shape = RoundedCornerShape(24.dp),
            containerColor = Color.White
        )
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
                    title = { Text("Configuración", fontWeight = FontWeight.Bold) },
                    navigationIcon = {
                        IconButton(onClick = {
                            haptics.lightClick()
                            onBackClick()
                        }) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Regresar a la pantalla anterior")
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
                        SettingsHeader(reduceAnimations = reduceAnimations)
                    }

                    // Accessibility Section
                    item {
                        SettingsSection(
                            title = "Accesibilidad",
                            icon = Icons.Default.AccessibilityNew,
                            index = 1,
                            reduceAnimations = reduceAnimations
                        ) {
                            Column {
                                SettingSwitchItem(
                                    label = "Reducir animaciones",
                                    description = "Disminuye efectos visuales y motion design",
                                    checked = uiState.preferences.reduceAnimations,
                                    labelFontSize = labelFontSize,
                                    descFontSize = descFontSize,
                                    verticalPadding = verticalPadding,
                                    reduceAnimations = reduceAnimations,
                                    onCheckedChange = {
                                        haptics.success()
                                        viewModel.updateReduceAnimations(it)
                                    }
                                )
                                SettingSwitchItem(
                                    label = "Alto contraste",
                                    description = "Incrementa contraste y visibilidad",
                                    checked = uiState.preferences.highContrast,
                                    labelFontSize = labelFontSize,
                                    descFontSize = descFontSize,
                                    verticalPadding = verticalPadding,
                                    reduceAnimations = reduceAnimations,
                                    onCheckedChange = {
                                        haptics.success()
                                        viewModel.updateHighContrast(it)
                                    }
                                )
                                SettingOptionItem(
                                    label = "Texto grande",
                                    description = "Tamaño actual: $fontSizeLabel",
                                    labelFontSize = labelFontSize,
                                    descFontSize = descFontSize,
                                    verticalPadding = verticalPadding,
                                    reduceAnimations = reduceAnimations,
                                    onClick = { 
                                        haptics.lightClick()
                                        showFontSizeDialog = true 
                                    }
                                )
                                SettingSwitchItem(
                                    label = "Feedback háptico",
                                    description = "Activar/Desactivar vibraciones",
                                    checked = uiState.preferences.hapticsEnabled,
                                    labelFontSize = labelFontSize,
                                    descFontSize = descFontSize,
                                    verticalPadding = verticalPadding,
                                    reduceAnimations = reduceAnimations,
                                    onCheckedChange = {
                                        haptics.success()
                                        viewModel.updateHapticsEnabled(it)
                                    }
                                )
                                SettingSwitchItem(
                                    label = "Modo accesibilidad avanzada",
                                    description = "Botones más grandes y mayor separación",
                                    checked = uiState.preferences.advancedAccessibility,
                                    labelFontSize = labelFontSize,
                                    descFontSize = descFontSize,
                                    verticalPadding = verticalPadding,
                                    reduceAnimations = reduceAnimations,
                                    onCheckedChange = {
                                        haptics.success()
                                        viewModel.updateAdvancedAccessibility(it)
                                    }
                                )
                            }
                        }
                    }

                    // Privacy Section
                    item {
                        SettingsSection(
                            title = "Privacidad",
                            icon = Icons.Default.PrivacyTip,
                            index = 2,
                            reduceAnimations = reduceAnimations
                        ) {
                            Column {
                                SettingInfoItem(
                                    label = "Permisos",
                                    description = "Ubicación: ${if (uiState.locationPermission) "Permitido" else "Denegado"}",
                                    labelFontSize = labelFontSize,
                                    descFontSize = descFontSize,
                                    verticalPadding = verticalPadding,
                                    reduceAnimations = reduceAnimations,
                                    onClick = { 
                                        haptics.lightClick()
                                        viewModel.openSystemSettings()
                                    }
                                )
                                SettingInfoItem(
                                    label = "Uso de datos",
                                    description = "WiFi: ${uiState.wifiUsage} | Datos: ${uiState.mobileUsage}",
                                    labelFontSize = labelFontSize,
                                    descFontSize = descFontSize,
                                    verticalPadding = verticalPadding,
                                    reduceAnimations = reduceAnimations,
                                    onClick = { 
                                        haptics.success()
                                        showDataUsageDialog = true
                                    }
                                )
                                SettingOptionItem(
                                    label = "Política de privacidad",
                                    description = "Ver versión 2026.07",
                                    labelFontSize = labelFontSize,
                                    descFontSize = descFontSize,
                                    verticalPadding = verticalPadding,
                                    reduceAnimations = reduceAnimations,
                                    onClick = { 
                                        haptics.lightClick()
                                        onNavigateToPrivacy()
                                    }
                                )
                            }
                        }
                    }

                    // About Section
                    item {
                        SettingsSection(
                            title = "Acerca de",
                            icon = Icons.Default.Info,
                            index = 3,
                            reduceAnimations = reduceAnimations
                        ) {
                            Column {
                                SettingInfoItem(
                                    label = "Versión",
                                    description = "SpaceIA Mobile ${uiState.appVersion}",
                                    showArrow = false,
                                    labelFontSize = labelFontSize,
                                    descFontSize = descFontSize,
                                    verticalPadding = verticalPadding,
                                    reduceAnimations = reduceAnimations
                                )
                                SettingOptionItem(
                                    label = "Créditos",
                                    description = "Universidad Tecnológica de León",
                                    labelFontSize = labelFontSize,
                                    descFontSize = descFontSize,
                                    verticalPadding = verticalPadding,
                                    reduceAnimations = reduceAnimations,
                                    onClick = { 
                                        haptics.lightClick()
                                        onNavigateToCredits()
                                    }
                                )
                                SettingOptionItem(
                                    label = "Términos y condiciones",
                                    labelFontSize = labelFontSize,
                                    descFontSize = descFontSize,
                                    verticalPadding = verticalPadding,
                                    reduceAnimations = reduceAnimations,
                                    onClick = { 
                                        haptics.lightClick()
                                        onNavigateToTerms()
                                    }
                                )
                                SettingInfoItem(
                                    label = "Contacto de soporte",
                                    description = uiState.supportEmail,
                                    labelFontSize = labelFontSize,
                                    descFontSize = descFontSize,
                                    verticalPadding = verticalPadding,
                                    reduceAnimations = reduceAnimations,
                                    onClick = { 
                                        haptics.success()
                                        clipboardManager.setText(AnnotatedString(uiState.supportEmail))
                                    }
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
fun SettingsHeader(reduceAnimations: Boolean = false) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
            .then(if (!reduceAnimations) Modifier.spaceIAStaggeredEntrance(0) else Modifier)
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
    reduceAnimations: Boolean = false,
    content: @Composable () -> Unit
) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .then(if (!reduceAnimations) Modifier.spaceIAStaggeredEntrance(index) else Modifier),
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
    onCheckedChange: (Boolean) -> Unit,
    labelFontSize: androidx.compose.ui.unit.TextUnit = 16.sp,
    descFontSize: androidx.compose.ui.unit.TextUnit = 12.sp,
    verticalPadding: androidx.compose.ui.unit.Dp = 16.dp,
    reduceAnimations: Boolean = false
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .then(if (!reduceAnimations) Modifier.spaceIAPressScale() else Modifier)
            .clickable { onCheckedChange(!checked) }
            .padding(horizontal = 16.dp, vertical = verticalPadding)
            .semantics { 
                contentDescription = "$label: ${if (checked) "Activado" else "Desactivado"}. $description"
            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = label, fontWeight = FontWeight.SemiBold, fontSize = labelFontSize)
            Text(text = description, fontSize = descFontSize, color = Color.Gray)
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
    onClick: () -> Unit,
    labelFontSize: androidx.compose.ui.unit.TextUnit = 16.sp,
    descFontSize: androidx.compose.ui.unit.TextUnit = 12.sp,
    verticalPadding: androidx.compose.ui.unit.Dp = 16.dp,
    reduceAnimations: Boolean = false
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .then(if (!reduceAnimations) Modifier.spaceIAPressScale() else Modifier)
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = verticalPadding)
            .semantics { 
                contentDescription = "$label. $description. Toca para cambiar."
            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = label, fontWeight = FontWeight.SemiBold, fontSize = labelFontSize)
            if (description != null) {
                Text(text = description, fontSize = descFontSize, color = Color.Gray)
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
    onClick: (() -> Unit)? = null,
    labelFontSize: androidx.compose.ui.unit.TextUnit = 16.sp,
    descFontSize: androidx.compose.ui.unit.TextUnit = 12.sp,
    verticalPadding: androidx.compose.ui.unit.Dp = 16.dp,
    reduceAnimations: Boolean = false
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .then(
                if (onClick != null) {
                    Modifier.then(if (!reduceAnimations) Modifier.spaceIAPressScale() else Modifier).clickable(onClick = onClick)
                } else Modifier
            )
            .padding(horizontal = 16.dp, vertical = verticalPadding)
            .semantics { 
                contentDescription = "$label: $description"
            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = label, fontWeight = FontWeight.SemiBold, fontSize = labelFontSize)
            if (description != null) {
                Text(text = description, fontSize = descFontSize, color = Color.Gray)
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
