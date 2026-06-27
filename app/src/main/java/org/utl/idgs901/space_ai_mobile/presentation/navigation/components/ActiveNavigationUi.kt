package org.utl.idgs901.space_ai_mobile.presentation.navigation.components

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.utl.idgs901.space_ai_mobile.domain.navigation.model.*
import org.utl.idgs901.space_ai_mobile.presentation.navigation.NavigationSessionUiState

@Composable
fun NavigationInstructionCard(
    state: NavigationSessionUiState,
    modifier: Modifier = Modifier
) {
    val instruction = state.currentInstruction ?: return

    AnimatedVisibility(
        visible = state.isNavigating && !state.hasArrived,
        enter = slideInVertically(initialOffsetY = { -it }) + fadeIn(),
        exit = slideOutVertically(targetOffsetY = { -it }) + fadeOut(),
        modifier = modifier
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            color = Color(0xFF1E293B),
            shape = RoundedCornerShape(24.dp),
            shadowElevation = 8.dp
        ) {
            Row(
                modifier = Modifier.padding(24.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                InstructionIcon(instruction.type)
                Spacer(modifier = Modifier.width(20.dp))
                Column {
                    Text(
                        text = instruction.text,
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    if (state.activeNavigation != null) {
                        Text(
                            text = "A ${state.remainingDistance}",
                            color = Color.White.copy(alpha = 0.7f),
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ArrivalOverlay(
    visible: Boolean,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(
        visible = visible,
        enter = scaleIn() + fadeIn(),
        exit = scaleOut() + fadeOut(),
        modifier = modifier
    ) {
        Card(
            modifier = Modifier.padding(32.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(32.dp),
            elevation = CardDefaults.cardElevation(12.dp)
        ) {
            Column(
                modifier = Modifier.padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    Icons.Default.Celebration,
                    contentDescription = null,
                    tint = Color(0xFF4CAF50),
                    modifier = Modifier.size(64.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    "¡Has llegado!",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1E293B)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "Has completado tu ruta con éxito.",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(24.dp))
                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0D47A1)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Finalizar")
                }
            }
        }
    }
}

@Composable
private fun InstructionIcon(type: InstructionType) {
    val icon = when (type) {
        InstructionType.START -> Icons.Default.PlayCircle
        InstructionType.STRAIGHT -> Icons.Default.ArrowUpward
        InstructionType.LEFT -> Icons.Default.TurnLeft
        InstructionType.RIGHT -> Icons.Default.TurnRight
        InstructionType.ARRIVED -> Icons.Default.LocationOn
    }
    
    Box(
        modifier = Modifier
            .size(56.dp)
            .background(Color.White.copy(alpha = 0.1f), RoundedCornerShape(16.dp)),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            icon,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(32.dp)
        )
    }
}
