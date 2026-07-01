package org.utl.idgs901.space_ai_mobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.ui.Modifier
import dagger.hilt.android.AndroidEntryPoint
import org.utl.idgs901.space_ai_mobile.presentation.navigation.AppNavigation
import org.utl.idgs901.space_ai_mobile.presentation.ui.theme.SpaceAiMobileTheme

import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import org.utl.idgs901.space_ai_mobile.presentation.MainViewModel

import androidx.compose.runtime.CompositionLocalProvider
import org.utl.idgs901.space_ai_mobile.core.designsystem.motion.LocalSettingsPreferences

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val windowSizeClass = calculateWindowSizeClass(this)
            val settings by viewModel.settings.collectAsState()

            CompositionLocalProvider(LocalSettingsPreferences provides settings) {
                SpaceAiMobileTheme(
                    highContrast = settings.highContrast,
                    fontScale = settings.fontScale
                ) {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        AppNavigation(windowSizeClass = windowSizeClass)
                    }
                }
            }
        }
    }
}
