package org.utl.idgs901.space_ai_mobile.presentation.dashboard

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.utl.idgs901.space_ai_mobile.core.designsystem.motion.SpaceIAMotion
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
fun DashboardScreen() {
    var selectedTab by remember { mutableStateOf<DashboardTab>(DashboardTab.Identity) }

    Scaffold(
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
                        color = Color.LightGray
                    ) {
                        Icon(Icons.Default.Person, contentDescription = "Perfil", modifier = Modifier.padding(6.dp))
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
