package org.utl.idgs901.space_ai_mobile.presentation.location.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.utl.idgs901.space_ai_mobile.domain.location.model.CampusLocationState

@Composable
fun CampusStatusIndicator(state: CampusLocationState, isLoading: Boolean = false) {
    val color = when (state) {
        is CampusLocationState.Inside -> Color(0xFF4CAF50)
        is CampusLocationState.Outside -> Color(0xFFF44336)
        is CampusLocationState.Unknown -> if (isLoading) Color(0xFF2196F3) else Color(0xFF757575)
    }
    
    val text = when (state) {
        is CampusLocationState.Inside -> "Dentro del campus"
        is CampusLocationState.Outside -> "Fuera del campus"
        is CampusLocationState.Unknown -> if (isLoading) "Obteniendo ubicación..." else "Ubicación desconocida"
    }

    Surface(
        color = color.copy(alpha = 0.1f),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.padding(end = 16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .background(color, RoundedCornerShape(4.dp))
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = text,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = color
            )
        }
    }
}
