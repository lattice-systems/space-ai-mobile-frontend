package org.utl.idgs901.space_ai_mobile.core.designsystem.components

import androidx.compose.animation.core.*
import androidx.compose.animation.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.MicNone
import androidx.compose.material.icons.rounded.SmartToy
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import org.utl.idgs901.space_ai_mobile.core.designsystem.motion.LocalSettingsPreferences
import org.utl.idgs901.space_ai_mobile.core.designsystem.motion.spaceIAPressScale
import kotlin.math.cos
import kotlin.math.sin

/**
 * Premium Aurora Effect for AI backgrounds.
 * Uses a deep ambient blur to create a high-end atmosphere.
 */
@Composable
fun SpaceIAAuroraBackground(modifier: Modifier = Modifier) {
    val settings = LocalSettingsPreferences.current
    val infiniteTransition = rememberInfiniteTransition(label = "Aurora")
    
    val phase1 by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(if (settings.reduceAnimations) 40000 else 20000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "Phase1"
    )

    val phase2 by infiniteTransition.animateFloat(
        initialValue = 360f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(if (settings.reduceAnimations) 50000 else 25000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "Phase2"
    )

    Box(modifier = modifier.fillMaxSize().background(Color(0xFFF8FAFC))) {
        Canvas(modifier = Modifier.fillMaxSize().blur(80.dp)) {
            val canvasWidth = size.width
            val canvasHeight = size.height

            // Institutional Primary Glow
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(Color(0xFF0D47A1).copy(alpha = 0.15f), Color.Transparent),
                    center = Offset(
                        x = canvasWidth / 2 + (canvasWidth / 3) * cos(Math.toRadians(phase1.toDouble())).toFloat(),
                        y = canvasHeight / 2 + (canvasHeight / 4) * sin(Math.toRadians(phase1.toDouble())).toFloat()
                    ),
                    radius = canvasWidth * 0.8f
                )
            )

            // Azure Secondary Glow
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(Color(0xFF42A5F5).copy(alpha = 0.12f), Color.Transparent),
                    center = Offset(
                        x = canvasWidth / 2 + (canvasWidth / 4) * cos(Math.toRadians(phase2.toDouble())).toFloat(),
                        y = canvasHeight / 2 + (canvasHeight / 3) * sin(Math.toRadians(phase2.toDouble())).toFloat()
                    ),
                    radius = canvasWidth * 0.7f
                )
            )
        }
    }
}

/**
 * Premium Circular Halo with breathing effect.
 * Replaces the old rotating square for a more organic and high-end feel.
 */
@Composable
fun SpaceIAPremiumOrbitalHalo(
    modifier: Modifier = Modifier,
    primaryColor: Color = Color(0xFF0D47A1),
    secondaryColor: Color = Color(0xFF42A5F5)
) {
    val settings = LocalSettingsPreferences.current
    if (settings.reduceAnimations) {
        Box(
            modifier = modifier
                .size(140.dp)
                .background(
                    Brush.radialGradient(
                        colors = listOf(primaryColor.copy(alpha = 0.1f), Color.Transparent)
                    ),
                    CircleShape
                )
        )
        return
    }

    val infiniteTransition = rememberInfiniteTransition(label = "PremiumHalo")
    
    // Breathing animation (Scale)
    val scale by infiniteTransition.animateFloat(
        initialValue = 0.95f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "BreathingScale"
    )

    // Multi-phase opacity for a "living" effect
    val auraAlpha by infiniteTransition.animateFloat(
        initialValue = 0.15f,
        targetValue = 0.35f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "AuraAlpha"
    )

    // Organic movement for the glow center
    val phase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(15000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "GlowPhase"
    )

    Canvas(
        modifier = modifier
            .size(160.dp)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .blur(24.dp)
    ) {
        val radius = size.minDimension / 2
        val center = Offset(size.width / 2, size.height / 2)
        
        // Organic offset for the primary glow
        val offset = Offset(
            x = (radius * 0.1f * cos(Math.toRadians(phase.toDouble()))).toFloat(),
            y = (radius * 0.1f * sin(Math.toRadians(phase.toDouble()))).toFloat()
        )

        // Primary Halo Layer
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(primaryColor.copy(alpha = auraAlpha), Color.Transparent),
                center = center + offset,
                radius = radius
            ),
            radius = radius
        )

        // Secondary Accenting Layer
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(secondaryColor.copy(alpha = auraAlpha * 0.6f), Color.Transparent),
                center = center - offset,
                radius = radius * 0.8f
            ),
            radius = radius * 0.8f
        )
    }
}

/**
 * Premium Hero component for AI Chat.
 * Combines the halo, a floating glass container, and the AI icon.
 */
@Composable
fun SpaceIAPremiumChatHero(
    modifier: Modifier = Modifier,
    icon: ImageVector = Icons.Rounded.SmartToy
) {
    val infiniteTransition = rememberInfiniteTransition(label = "HeroFloat")
    
    // Floating animation for the whole container
    val floatY by infiniteTransition.animateFloat(
        initialValue = -5f,
        targetValue = 5f,
        animationSpec = infiniteRepeatable(
            animation = tween(3500, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "FloatY"
    )

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.graphicsLayer { translationY = floatY }
    ) {
        SpaceIAPremiumOrbitalHalo()
        
        Surface(
            modifier = Modifier
                .size(86.dp)
                .border(
                    width = 1.dp,
                    brush = Brush.linearGradient(
                        colors = listOf(
                            Color.White.copy(alpha = 0.8f),
                            Color.White.copy(alpha = 0.2f)
                        )
                    ),
                    shape = RoundedCornerShape(28.dp)
                ),
            shape = RoundedCornerShape(28.dp),
            color = Color.White.copy(alpha = 0.95f),
            tonalElevation = 8.dp,
            shadowElevation = 16.dp
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.size(44.dp),
                    tint = Color(0xFF0D47A1)
                )
            }
        }
    }
}

/**
 * Orbital Glow Animation for AI Logo area.
 * Inspired by Gemini Live and Apple Intelligence.
 */
@Composable
fun SpaceIAOrbitalGlow(
    color: Color = Color(0xFF0D47A1),
    modifier: Modifier = Modifier
) {
    val settings = LocalSettingsPreferences.current
    val infiniteTransition = rememberInfiniteTransition(label = "Orbital")
    
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(10000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "Rotation"
    )

    val scale by infiniteTransition.animateFloat(
        initialValue = 0.95f,
        targetValue = 1.1f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "Scale"
    )

    Box(contentAlignment = Alignment.Center, modifier = modifier) {
        if (!settings.reduceAnimations) {
            Canvas(modifier = Modifier.size(130.dp).graphicsLayer {
                rotationZ = rotation
                scaleX = scale
                scaleY = scale
            }.blur(16.dp)) {
                drawCircle(
                    brush = Brush.sweepGradient(
                        colors = listOf(
                            color.copy(alpha = 0f),
                            color.copy(alpha = 0.3f),
                            color.copy(alpha = 0f)
                        )
                    ),
                    radius = size.width / 2
                )
            }
        }
    }
}

/**
 * Animated Orb for Voice Recording.
 * No blinking, just smooth breathing and glow.
 */
@Composable
fun SpaceIAVoiceOrb(
    isActive: Boolean,
    modifier: Modifier = Modifier
) {
    val settings = LocalSettingsPreferences.current
    val infiniteTransition = rememberInfiniteTransition(label = "VoiceOrb")
    
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = if (isActive) 1.4f else 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "Scale"
    )

    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.2f,
        targetValue = if (isActive) 0.5f else 0.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "Alpha"
    )

    Box(contentAlignment = Alignment.Center, modifier = modifier) {
        if (isActive && !settings.reduceAnimations) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .graphicsLayer {
                        scaleX = scale
                        scaleY = scale
                    }
                    .blur(12.dp)
                    .background(Color(0xFF0D47A1).copy(alpha = glowAlpha), CircleShape)
            )
        }
        
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(Color(0xFF0D47A1), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            if (isActive) {
                Box(
                    modifier = Modifier
                        .size(14.dp)
                        .background(Color.White, RoundedCornerShape(2.dp))
                )
            } else {
                androidx.compose.material3.Icon(
                    imageVector = Icons.Rounded.MicNone,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}
