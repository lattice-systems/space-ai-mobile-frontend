package org.utl.idgs901.space_ai_mobile.presentation.settings.detail

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
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
fun TermsAndConditionsScreen(onBackClick: () -> Unit) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Términos y Condiciones", fontWeight = FontWeight.Bold) },
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
                text = "Términos de Uso de SpaceIA",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0D47A1)
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = "Al utilizar la aplicación SpaceIA Mobile, aceptas cumplir con las normativas universitarias de la Universidad Tecnológica de León y las políticas de seguridad informática del Smart Campus.",
                fontSize = 16.sp,
                lineHeight = 24.sp
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Text(text = "Uso Responsable", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Text(
                text = "El usuario se compromete a no realizar acciones que comprometan la seguridad de la red IoT o de los sistemas académicos del campus.",
                modifier = Modifier.padding(vertical = 8.dp)
            )
            
            Text(text = "Geocerca y Navegación", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Text(
                text = "Los servicios de navegación inteligente requieren el uso de GPS. El uso de estos servicios es voluntario pero necesario para una experiencia Smart Campus completa.",
                modifier = Modifier.padding(vertical = 8.dp)
            )
            
            Spacer(modifier = Modifier.height(32.dp))
            
            SpaceIAPremiumButton(
                text = "Aceptar Términos",
                onClick = onBackClick
            )
        }
    }
}
