package org.utl.idgs901.space_ai_mobile.core.designsystem.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import org.utl.idgs901.space_ai_mobile.core.designsystem.motion.SpaceIAMotion

@Composable
fun SpaceIAPremiumBackground(
    content: @Composable () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "BackgroundAnim")
    
    val phase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(25000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "Phase"
    )

    Box(modifier = Modifier.fillMaxSize()) {
        // Dynamic Ambient Gradient
        Canvas(modifier = Modifier.fillMaxSize()) {
            val centerOffset = Offset(
                size.width * (0.5f + 0.15f * kotlin.math.cos(phase * 2 * kotlin.math.PI.toFloat())),
                size.height * (0.5f + 0.15f * kotlin.math.sin(phase * 2 * kotlin.math.PI.toFloat()))
            )
            
            val brush = Brush.radialGradient(
                colors = listOf(
                    Color(0xFFE3F2FD),
                    Color(0xFFBBDEFB),
                    Color(0xFFE1F5FE).copy(alpha = 0.9f)
                ),
                center = centerOffset,
                radius = size.minDimension * 1.8f
            )
            drawRect(brush = brush)
        }

        // Lighting Depth Effect
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(Color.White.copy(alpha = 0.3f), Color.Transparent),
                    center = Offset(size.width * 0.2f, size.height * 0.1f),
                    radius = size.minDimension * 0.8f
                )
            )
        }

        // Subtle Institutional Particles
        BackgroundParticles()

        content()
    }
}

@Composable
private fun BackgroundParticles() {
    val infiniteTransition = rememberInfiniteTransition(label = "Particles")
    
    val movement by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(20000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "Movement"
    )

    Canvas(modifier = Modifier.fillMaxSize()) {
        val particles = listOf(
            Offset(size.width * 0.1f, size.height * 0.15f),
            Offset(size.width * 0.9f, size.height * 0.1f),
            Offset(size.width * 0.5f, size.height * 0.8f),
            Offset(size.width * 0.05f, size.height * 0.7f),
            Offset(size.width * 0.95f, size.height * 0.6f),
            Offset(size.width * 0.35f, size.height * 0.45f),
            Offset(size.width * 0.75f, size.height * 0.5f)
        )

        particles.forEachIndexed { index, offset ->
            val sinX = kotlin.math.sin((movement + index * 0.1f) * 2 * kotlin.math.PI.toFloat())
            val cosY = kotlin.math.cos((movement + index * 0.15f) * 2 * kotlin.math.PI.toFloat())
            
            val animatedOffset = Offset(
                x = offset.x + (20 * sinX),
                y = offset.y - (30 * movement) + (10 * cosY)
            )
            
            drawCircle(
                color = Color.White.copy(alpha = 0.1f),
                radius = (8 + (index * 3)).toFloat(),
                center = animatedOffset
            )
        }
    }
}
