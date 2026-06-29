package org.utl.idgs901.space_ai_mobile.core.designsystem.motion

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

/**
 * Scale effect on press, returns to 1.0f on release with a spring animation.
 */
fun Modifier.spaceIAPressScale() = composed {
    val scale = remember { Animatable(1f) }
    val scope = rememberCoroutineScope()

    this.pointerInput(Unit) {
        detectTapGestures(
            onPress = {
                scope.launch {
                    scale.animateTo(
                        0.97f,
                        animationSpec = tween(SpaceIAMotion.Duration.ExtraFast, easing = SpaceIAMotion.Easing.Standard)
                    )
                }
                try {
                    awaitRelease()
                } finally {
                    scope.launch {
                        scale.animateTo(
                            1f,
                            animationSpec = SpaceIAAnimationSpecs.springMediumBouncy()
                        )
                    }
                }
            }
        )
    }.scale(scale.value)
}

/**
 * Shake animation for error feedback.
 */
@Composable
fun rememberSpaceIAShakeState(): SpaceIAShakeState {
    return remember { SpaceIAShakeState() }
}

class SpaceIAShakeState {
    val shakeOffset = Animatable(0f)

    suspend fun shake() {
        shakeOffset.animateTo(
            targetValue = 0f,
            animationSpec = keyframes {
                durationMillis = 500
                -20f at 100 with LinearEasing
                20f at 200 with LinearEasing
                -15f at 300 with LinearEasing
                15f at 400 with LinearEasing
                0f at 500 with LinearEasing
            }
        )
    }
}

fun Modifier.spaceIAShake(state: SpaceIAShakeState) = this.offset {
    IntOffset(state.shakeOffset.value.roundToInt(), 0)
}

/**
 * Shimmer effect for loading states.
 */
fun Modifier.spaceIAShimmer() = composed {
    val transition = rememberInfiniteTransition(label = "Shimmer")
    val translateAnim by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "Translate"
    )

    val shimmerColors = listOf(
        androidx.compose.ui.graphics.Color.LightGray.copy(alpha = 0.6f),
        androidx.compose.ui.graphics.Color.LightGray.copy(alpha = 0.2f),
        androidx.compose.ui.graphics.Color.LightGray.copy(alpha = 0.6f),
    )

    val brush = androidx.compose.ui.graphics.Brush.linearGradient(
        colors = shimmerColors,
        start = androidx.compose.ui.geometry.Offset.Zero,
        end = androidx.compose.ui.geometry.Offset(x = translateAnim, y = translateAnim)
    )

    this.background(brush)
}

/**
 * Staggered entrance animation for items.
 */
fun Modifier.spaceIAStaggeredEntrance(
    index: Int,
    delayStep: Int = 80
) = composed {
    val animatedProgress = remember { Animatable(0f) }
    
    LaunchedEffect(Unit) {
        animatedProgress.animateTo(
            targetValue = 1f,
            animationSpec = tween(
                durationMillis = SpaceIAMotion.Duration.Medium,
                delayMillis = index * delayStep,
                easing = SpaceIAMotion.Easing.Emphasized
            )
        )
    }

    this.graphicsLayer {
        alpha = animatedProgress.value
        translationY = (1f - animatedProgress.value) * 50.dp.toPx()
        scaleX = 0.95f + (0.05f * animatedProgress.value)
        scaleY = 0.95f + (0.05f * animatedProgress.value)
    }
}

/**
 * Pulse animation for indicators.
 */
fun Modifier.spaceIAPulse(
    color: androidx.compose.ui.graphics.Color,
    enabled: Boolean = true
) = composed {
    if (!enabled) return@composed this

    val infiniteTransition = rememberInfiniteTransition(label = "Pulse")
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.4f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = SpaceIAMotion.Easing.Smooth),
            repeatMode = RepeatMode.Restart
        ),
        label = "Scale"
    )
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.6f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = SpaceIAMotion.Easing.Smooth),
            repeatMode = RepeatMode.Restart
        ),
        label = "Alpha"
    )

    this.graphicsLayer {
        scaleX = scale
        scaleY = scale
        this.alpha = alpha
    }
}
