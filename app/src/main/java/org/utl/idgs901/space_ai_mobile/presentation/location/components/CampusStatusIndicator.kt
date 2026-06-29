package org.utl.idgs901.space_ai_mobile.presentation.location.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.utl.idgs901.space_ai_mobile.domain.location.model.CampusLocationState

@Composable
fun CampusStatusIndicator(
    state: CampusLocationState, 
    isLoading: Boolean = false,
    modifier: Modifier = Modifier
) {
    val statusColor by animateColorAsState(
        targetValue = when (state) {
            is CampusLocationState.Inside -> Color(0xFF10B981) // Emerald Green
            is CampusLocationState.Outside -> Color(0xFFEF4444) // Rose Red
            is CampusLocationState.Unknown -> if (isLoading) Color(0xFF3B82F6) else Color(0xFF64748B)
        },
        label = "statusColor"
    )
    
    val text = when (state) {
        is CampusLocationState.Inside -> "Activo"
        is CampusLocationState.Outside -> "Inactivo"
        is CampusLocationState.Unknown -> if (isLoading) "Sincronizando..." else "Buscando..."
    }

    // DEBUG: Add coordinates to help debugging (Hidden in final version)
    // Text(text = "${state}", modifier = Modifier.padding(8.dp))

    // Modern glass-style label
    Surface(
        modifier = modifier
            .padding(16.dp),
        color = Color.White.copy(alpha = 0.95f),
        shape = MaterialTheme.shapes.medium,
        tonalElevation = 2.dp,
        shadowElevation = 4.dp
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(horizontal = 12.dp, vertical = 8.dp)
        ) {
            // Status Dot with Glow/Ring
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(16.dp)
            ) {
                // Background pulse effect (static ring for now)
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape)
                        .background(statusColor.copy(alpha = 0.2f))
                )
                // Core dot
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(statusColor)
                )
            }
            
            Spacer(modifier = Modifier.width(8.dp))
            
            Column(verticalArrangement = Arrangement.Center) {
                Text(
                    text = text,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF1E293B),
                    lineHeight = 16.sp
                )
                if (state is CampusLocationState.Inside) {
                    Text(
                        text = "Campus UTL",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Medium,
                        color = statusColor,
                        letterSpacing = 0.5.sp
                    )
                }
            }
        }
    }
}
