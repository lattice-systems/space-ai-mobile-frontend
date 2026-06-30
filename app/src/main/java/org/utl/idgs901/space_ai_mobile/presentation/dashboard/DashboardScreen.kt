package org.utl.idgs901.space_ai_mobile.presentation.dashboard

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.launch
import org.utl.idgs901.space_ai_mobile.core.designsystem.motion.SpaceIAHaptics
import org.utl.idgs901.space_ai_mobile.core.designsystem.motion.SpaceIAMotion
import org.utl.idgs901.space_ai_mobile.core.designsystem.motion.SpaceIADrawerItem
import org.utl.idgs901.space_ai_mobile.core.designsystem.motion.rememberSpaceIAHaptics
import org.utl.idgs901.space_ai_mobile.core.designsystem.motion.spaceIAPressScale
import org.utl.idgs901.space_ai_mobile.presentation.identity.IdentityScreen
import org.utl.idgs901.space_ai_mobile.presentation.map.CampusMapScreen

sealed class DashboardTab(val route: String, val icon: ImageVector, val label: String) {
    object Home : DashboardTab("home", Icons.Default.Home, "Inicio")
    object Identity : DashboardTab("identity", Icons.Default.Contactless, "QR")
    object Map : DashboardTab("map", Icons.Default.Explore, "Mapa")
    object AI : DashboardTab("ai", Icons.Default.SmartToy, "IA")
    object Academic : DashboardTab("academic", Icons.Default.School, "Académico")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    windowSizeClass: WindowSizeClass,
    viewModel: DashboardViewModel = hiltViewModel(),
    onNavigateToLogin: () -> Unit,
    onNavigateToProfile: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val haptics = rememberSpaceIAHaptics()
    val nativeHaptics = LocalHapticFeedback.current
    var selectedTab by remember { mutableStateOf<DashboardTab>(DashboardTab.Identity) }

    LaunchedEffect(uiState.isLoggedOut) {
        if (uiState.isLoggedOut) {
            onNavigateToLogin()
        }
    }

    val drawerWidthFraction = when (windowSizeClass.widthSizeClass) {
        WindowWidthSizeClass.Compact -> 0.85f
        WindowWidthSizeClass.Medium -> 0.70f
        else -> 0.50f
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = true,
        scrimColor = Color.Black.copy(alpha = 0.32f),
        drawerContent = {
            BoxWithConstraints {
                val sheetWidth = this.maxWidth * drawerWidthFraction
                ModalDrawerSheet(
                    modifier = Modifier.width(sheetWidth),
                    drawerContainerColor = Color.White,
                    drawerShape = RoundedCornerShape(topEnd = 32.dp, bottomEnd = 32.dp),
                    windowInsets = WindowInsets.statusBars
                ) {
                    // Drawer Header
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                brush = Brush.verticalGradient(
                                    colors = listOf(Color(0xFF0D47A1), Color(0xFF1976D2))
                                )
                            )
                            .padding(24.dp)
                    ) {
                        Surface(
                            modifier = Modifier
                                .size(80.dp)
                                .clip(CircleShape)
                                .border(2.dp, Color.White.copy(alpha = 0.5f), CircleShape)
                                .spaceIAPressScale(),
                            color = Color.White.copy(alpha = 0.2f)
                        ) {
                            Icon(
                                Icons.Default.Person, 
                                contentDescription = "Foto de perfil",
                                modifier = Modifier.padding(16.dp),
                                tint = Color.White
                            )
                        }
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        Text(
                            text = uiState.user?.name ?: "Usuario",
                            color = Color.White,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                        
                        Text(
                            text = uiState.user?.role ?: "Estudiante",
                            color = Color.White.copy(alpha = 0.8f),
                            fontSize = 12.sp,
                            letterSpacing = 0.5.sp
                        )
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        Surface(
                            color = Color.White.copy(alpha = 0.15f),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = uiState.user?.folio ?: "",
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                color = Color.White,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Drawer Items
                    Column(modifier = Modifier.padding(8.dp)) {
                        SpaceIADrawerItem(
                            icon = Icons.Default.Person,
                            label = "Perfil",
                            description = "Información personal",
                            onClick = { 
                                haptics.lightClick()
                                scope.launch { 
                                    drawerState.close()
                                    onNavigateToProfile()
                                }
                            }
                        )
                        
                        SpaceIADrawerItem(
                            icon = Icons.Default.Settings,
                            label = "Configuración",
                            description = "Ajustes de la app",
                            onClick = { 
                                nativeHaptics.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                scope.launch { drawerState.close() }
                            }
                        )
                        
                        SpaceIADrawerItem(
                            icon = Icons.Default.Help,
                            label = "Ayuda",
                            description = "Soporte técnico",
                            onClick = { 
                                nativeHaptics.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                scope.launch { drawerState.close() }
                            }
                        )
                        
                        Divider(modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp), color = Color.LightGray.copy(alpha = 0.5f))
                        
                        SpaceIADrawerItem(
                            icon = Icons.Default.Logout,
                            label = "Cerrar sesión",
                            onClick = {
                                nativeHaptics.performHapticFeedback(HapticFeedbackType.LongPress)
                                viewModel.logout()
                            }
                        )
                    }
                    
                    Spacer(modifier = Modifier.weight(1f))
                    
                    Text(
                        text = "SpaceIA v1.0.0",
                        modifier = Modifier.padding(24.dp).align(Alignment.CenterHorizontally),
                        color = Color.Gray,
                        fontSize = 12.sp
                    )
                }
            }
        }
    ) {
        Scaffold(
            modifier = Modifier.blur(if (drawerState.isAnimationRunning || drawerState.isOpen) 4.dp else 0.dp),
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            text = "SpaceIA",
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0D47A1),
                            fontSize = 20.sp
                        )
                    },
                    navigationIcon = {
                        Surface(
                            modifier = Modifier
                                .padding(start = 16.dp)
                                .size(36.dp)
                                .clip(CircleShape)
                                .spaceIAPressScale(),
                            color = Color(0xFF0D47A1).copy(alpha = 0.1f),
                            onClick = {
                                haptics.lightClick()
                                scope.launch { drawerState.open() }
                            }
                        ) {
                            Icon(
                                Icons.Default.Person, 
                                contentDescription = "Abrir menú", 
                                modifier = Modifier.padding(6.dp),
                                tint = Color(0xFF0D47A1)
                            )
                        }
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = Color.White
                    )
                )
            },
            bottomBar = {
                NavigationBar(
                    containerColor = Color.White,
                    tonalElevation = 8.dp
                ) {
                    val tabs = listOf(
                        DashboardTab.Home,
                        DashboardTab.Identity,
                        DashboardTab.Map,
                        DashboardTab.AI,
                        DashboardTab.Academic
                    )
                    
                    tabs.forEach { tab ->
                        NavigationBarItem(
                            selected = selectedTab == tab,
                            onClick = { selectedTab = tab },
                            icon = { 
                                Icon(
                                    tab.icon, 
                                    contentDescription = tab.label,
                                    modifier = Modifier.animateContentSize()
                                ) 
                            },
                            label = { Text(tab.label, fontSize = 10.sp) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = Color(0xFF0D47A1),
                                selectedTextColor = Color(0xFF0D47A1),
                                unselectedIconColor = Color.Gray,
                                unselectedTextColor = Color.Gray,
                                indicatorColor = Color(0xFF0D47A1).copy(alpha = 0.1f)
                            )
                        )
                    }
                }
            }
        ) { innerPadding ->
            AnimatedContent(
                targetState = selectedTab,
                transitionSpec = {
                    fadeIn(animationSpec = tween(300)) + scaleIn(initialScale = 0.98f) togetherWith
                    fadeOut(animationSpec = tween(300))
                },
                label = "TabContent"
            ) { targetTab ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    when (targetTab) {
                        DashboardTab.Identity -> Box(modifier = Modifier.fillMaxSize().padding(innerPadding)) { IdentityScreen() }
                        DashboardTab.Map -> CampusMapScreen(onMoreInfoClick = { selectedTab = DashboardTab.AI })
                        else -> {
                            Box(modifier = Modifier.fillMaxSize().padding(innerPadding), contentAlignment = Alignment.Center) {
                                Text("Próximamente: ${targetTab.label}", color = Color.Gray)
                            }
                        }
                    }
                }
            }
        }
    }
}
