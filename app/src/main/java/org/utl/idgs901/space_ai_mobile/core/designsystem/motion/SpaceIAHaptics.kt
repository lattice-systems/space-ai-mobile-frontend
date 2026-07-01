package org.utl.idgs901.space_ai_mobile.core.designsystem.motion

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext

class SpaceIAHaptics(
    context: Context,
    private val hapticsEnabled: Boolean = true
) {
    private val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
        vibratorManager.defaultVibrator
    } else {
        @Suppress("DEPRECATION")
        context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    }

    fun success() {
        if (!hapticsEnabled) return
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createWaveform(longArrayOf(0, 10, 50, 10), -1))
        }
    }

    fun error() {
        if (!hapticsEnabled) return
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createWaveform(longArrayOf(0, 50, 100, 50, 100, 50), -1))
        }
    }

    fun lightClick() {
        if (!hapticsEnabled) return
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            vibrator.vibrate(VibrationEffect.createPredefined(VibrationEffect.EFFECT_CLICK))
        }
    }
}

@Composable
fun rememberSpaceIAHaptics(): SpaceIAHaptics {
    val context = LocalContext.current
    val settings = LocalSettingsPreferences.current
    return remember(settings.hapticsEnabled) { 
        SpaceIAHaptics(context, settings.hapticsEnabled) 
    }
}
