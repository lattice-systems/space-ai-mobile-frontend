package org.utl.idgs901.space_ai_mobile.presentation.settings.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.utl.idgs901.space_ai_mobile.core.designsystem.components.SpaceIAPremiumButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrivacyPolicyScreen(onBackClick: () -> Unit) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Política de Privacidad", fontWeight = FontWeight.Bold) },
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
                .padding(24.dp)
        ) {
            Text(
                text = "Tu privacidad es importante para SpaceIA.",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0D47A1)
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = "En SpaceIA, nos comprometemos a proteger tus datos personales y a ser transparentes sobre cómo los utilizamos. Esta aplicación recopila datos de ubicación para la navegación en el campus y métricas de uso para mejorar el servicio universitario.",
                fontSize = 16.sp,
                lineHeight = 24.sp
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Text(text = "1. Recopilación de Datos", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Text(
                text = "Recopilamos información académica, ubicación GPS y datos técnicos del dispositivo para garantizar la seguridad y funcionalidad del Smart Campus.",
                modifier = Modifier.padding(vertical = 8.dp)
            )
            
            Text(text = "2. Uso de la Información", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Text(
                text = "Los datos se utilizan exclusivamente para fines institucionales, navegación inteligente y alertas de seguridad dentro de la UTL.",
                modifier = Modifier.padding(vertical = 8.dp)
            )
            
            Spacer(modifier = Modifier.height(32.dp))
            
            SpaceIAPremiumButton(
                text = "Aceptar y Continuar",
                onClick = onBackClick
            )
        }
    }
}
