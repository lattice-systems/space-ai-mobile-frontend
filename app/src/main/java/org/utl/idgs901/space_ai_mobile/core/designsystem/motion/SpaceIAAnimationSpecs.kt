package org.utl.idgs901.space_ai_mobile.core.designsystem.motion

import androidx.compose.animation.core.*
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

object SpaceIAAnimationSpecs {
    
    fun <T> springLowBouncy() = spring<T>(
        dampingRatio = Spring.DampingRatioLowBouncy,
        stiffness = Spring.StiffnessLow
    )

    fun <T> springMediumBouncy() = spring<T>(
        dampingRatio = Spring.DampingRatioMediumBouncy,
        stiffness = Spring.StiffnessMedium
    )

    fun <T> springNoBouncy() = spring<T>(
        dampingRatio = Spring.DampingRatioNoBouncy,
        stiffness = Spring.StiffnessMediumLow
    )

    fun <T> tweenNormal(easing: Easing = SpaceIAMotion.Easing.Standard) = tween<T>(
        durationMillis = SpaceIAMotion.Duration.Normal,
        easing = easing
    )

    fun <T> tweenFast(easing: Easing = SpaceIAMotion.Easing.Decelerate) = tween<T>(
        durationMillis = SpaceIAMotion.Duration.Fast,
        easing = easing
    )
}
