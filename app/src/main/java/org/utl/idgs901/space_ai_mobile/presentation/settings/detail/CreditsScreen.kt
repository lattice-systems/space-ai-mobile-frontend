package org.utl.idgs901.space_ai_mobile.presentation.settings.detail

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreditsScreen(onBackClick: () -> Unit) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Créditos", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Atrás")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Surface(
                modifier = Modifier.size(100.dp),
                shape = CircleShape,
                color = Color(0xFF0D47A1).copy(alpha = 0.1f)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(Icons.Default.School, contentDescription = null, modifier = Modifier.size(60.dp), tint = Color(0xFF0D47A1))
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Text(
                text = "Universidad Tecnológica de León",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            
            Text(
                text = "Smart Campus Ecosystem",
                fontSize = 14.sp,
                color = Color.Gray
            )
            
            Spacer(modifier = Modifier.height(32.dp))
            
            CreditSection(
                title = "Equipo SpaceIA",
                names = listOf("Juan Pablo Rea Cano", "Lattice Systems Team")
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            CreditSection(
                title = "Tecnologías Utilizadas",
                names = listOf("Kotlin 2.x", "Jetpack Compose", "Material Design 3", "Hilt", "DataStore Proto", "Geofencing API")
            )
            
            Spacer(modifier = Modifier.height(48.dp))
            
            Text(
                text = "© 2026 Universidad Tecnológica de León",
                fontSize = 12.sp,
                color = Color.LightGray
            )
        }
    }
}

@Composable
fun CreditSection(title: String, names: List<String>) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = title, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color(0xFF0D47A1))
        names.forEach { name ->
            Text(text = name, modifier = Modifier.padding(top = 4.dp), fontSize = 14.sp)
        }
    }
}
