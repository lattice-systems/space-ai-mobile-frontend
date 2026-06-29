package org.utl.idgs901.space_ai_mobile.core.designsystem.motion

import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing

object SpaceIAMotion {
    /**
     * DURATION TOKENS
     */
    object Duration {
        const val ExtraFast = 100
        const val Fast = 200
        const val Normal = 300
        const val Medium = 500
        const val Slow = 700
        const val ExtraSlow = 1000
        const val Splash = 1500
    }

    /**
     * EASING CURVES (Material 3 & Premium Patterns)
     */
    object Easing {
        val Standard = FastOutSlowInEasing
        val Decelerate = LinearOutSlowInEasing
        val Accelerate = CubicBezierEasing(0.4f, 0.0f, 1.0f, 1.0f)
        val Emphasized = CubicBezierEasing(0.2f, 0.0f, 0.0f, 1.0f)
        val Smooth = CubicBezierEasing(0.4f, 0.0f, 0.2f, 1.0f)
    }
}
