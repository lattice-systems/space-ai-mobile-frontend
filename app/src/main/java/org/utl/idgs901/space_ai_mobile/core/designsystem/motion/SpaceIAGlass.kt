package org.utl.idgs901.space_ai_mobile.core.designsystem.motion

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/**
 * Premium Glassmorphism effect with subtle border and blur.
 */
fun Modifier.spaceIAGlassEffect(
    opacity: Float = 0.7f,
    blur: Float = 16f
) = this
    .blur(blur.dp)
    .drawBehind {
        drawRect(
            brush = Brush.verticalGradient(
                colors = listOf(
                    Color.White.copy(alpha = opacity),
                    Color.White.copy(alpha = opacity * 0.5f)
                )
            )
        )
    }
