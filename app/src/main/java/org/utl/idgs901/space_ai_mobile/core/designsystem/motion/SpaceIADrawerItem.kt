package org.utl.idgs901.space_ai_mobile.core.designsystem.motion

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SpaceIADrawerItem(
    icon: ImageVector,
    label: String,
    description: String? = null,
    isSelected: Boolean = false,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.97f else 1f,
        animationSpec = SpaceIAAnimationSpecs.springMediumBouncy(),
        label = "Scale"
    )

    val elevation by animateDpAsState(
        targetValue = if (isPressed) 2.dp else 0.dp,
        animationSpec = SpaceIAAnimationSpecs.springLowBouncy(),
        label = "Elevation"
    )

    val backgroundColor by animateColorAsState(
        targetValue = when {
            isSelected -> Color(0xFF0D47A1).copy(alpha = 0.1f)
            isPressed -> Color.LightGray.copy(alpha = 0.2f)
            else -> Color.Transparent
        },
        label = "Background"
    )

    val contentColor by animateColorAsState(
        targetValue = if (isSelected) Color(0xFF0D47A1) else Color.DarkGray,
        label = "ContentColor"
    )

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 4.dp)
            .scale(scale)
            .clickable(
                interactionSource = interactionSource,
                indication = null, // Custom feedback
                onClick = onClick
            ),
        shape = RoundedCornerShape(16.dp),
        color = backgroundColor,
        tonalElevation = elevation
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = contentColor,
                modifier = Modifier.size(24.dp)
            )
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column {
                Text(
                    text = label,
                    fontSize = 16.sp,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                    color = contentColor
                )
                if (description != null) {
                    Text(
                        text = description,
                        fontSize = 12.sp,
                        color = contentColor.copy(alpha = 0.6f)
                    )
                }
            }
        }
    }
}
