package org.utl.idgs901.space_ai_mobile.presentation.splash

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.delay
import org.utl.idgs901.space_ai_mobile.core.designsystem.components.SpaceIAPremiumBackground
import org.utl.idgs901.space_ai_mobile.core.designsystem.motion.SpaceIAMotion

@Composable
fun SplashScreen(
    viewModel: SplashViewModel = hiltViewModel(),
    onNavigateToLogin: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    
    // Animation States
    var phase1Active by remember { mutableStateOf(false) } // Background/Lighting
    var phase2Active by remember { mutableStateOf(false) } // Logo
    var phase3Active by remember { mutableStateOf(false) } // SpaceIA Title
    var phase4Active by remember { mutableStateOf(false) } // Subtitle
    var phase5Active by remember { mutableStateOf(false) } // Transition Exit

    LaunchedEffect(Unit) {
        // Phase 1: Background (200ms)
        phase1Active = true
        delay(200)
        
        // Phase 2: Logo (500ms)
        phase2Active = true
        delay(500)
        
        // Phase 3: Title (300ms)
        phase3Active = true
        delay(300)
        
        // Phase 4: Subtitle (200ms)
        phase4Active = true
        delay(200)
        
        // Total so far: 1200ms. Wait for the rest to hit 1500ms
        delay(300)
        
        // Phase 5: Transition Exit
        phase5Active = true
        onNavigateToLogin()
    }

    val exitAlpha by animateFloatAsState(
        targetValue = if (phase5Active) 0f else 1f,
        animationSpec = tween(300),
        label = "ExitAlpha"
    )

    val exitScale by animateFloatAsState(
        targetValue = if (phase5Active) 1.1f else 1f,
        animationSpec = tween(300),
        label = "ExitScale"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .graphicsLayer {
                alpha = exitAlpha
                scaleX = exitScale
                scaleY = exitScale
            }
    ) {
        SpaceIAPremiumBackground {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                PremiumLogoSection(visible = phase2Active)
                
                Spacer(modifier = Modifier.height(32.dp))
                
                PremiumTextSection(
                    titleVisible = phase3Active,
                    subtitleVisible = phase4Active
                )
            }
        }
    }
}

@Composable
private fun PremiumLogoSection(visible: Boolean) {
    val infiniteTransition = rememberInfiniteTransition(label = "LogoFloating")
    val floatAnim by infiniteTransition.animateFloat(
        initialValue = -8f,
        targetValue = 8f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = SpaceIAMotion.Easing.Smooth),
            repeatMode = RepeatMode.Reverse
        ),
        label = "Floating"
    )

    val logoAlpha by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = tween(500),
        label = "LogoAlpha"
    )
    
    val logoScale by animateFloatAsState(
        targetValue = if (visible) 1f else 0.5f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "LogoScale"
    )

    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.2f,
        targetValue = 0.6f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = SpaceIAMotion.Easing.Smooth),
            repeatMode = RepeatMode.Reverse
        ),
        label = "Glow"
    )

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.graphicsLayer {
            alpha = logoAlpha
            scaleX = logoScale
            scaleY = logoScale
            translationY = floatAnim
        }
    ) {
        // Glow Effect
        Surface(
            modifier = Modifier
                .size(140.dp)
                .graphicsLayer {
                    alpha = glowAlpha * logoAlpha
                    scaleX = 1.2f
                    scaleY = 1.2f
                },
            shape = RoundedCornerShape(40.dp),
            color = Color(0xFF0D47A1).copy(alpha = 0.2f)
        ) {}

        Surface(
            modifier = Modifier
                .size(120.dp)
                .clip(RoundedCornerShape(32.dp)),
            shape = RoundedCornerShape(32.dp),
            color = Color.White,
            shadowElevation = 16.dp
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = Icons.Default.Face,
                    contentDescription = null,
                    tint = Color(0xFF0D47A1),
                    modifier = Modifier.size(60.dp)
                )
            }
        }
    }
}

@Composable
private fun PremiumTextSection(
    titleVisible: Boolean,
    subtitleVisible: Boolean
) {
    val titleAlpha by animateFloatAsState(
        targetValue = if (titleVisible) 1f else 0f,
        animationSpec = tween(300),
        label = "TitleAlpha"
    )
    
    val titleBlur by animateFloatAsState(
        targetValue = if (titleVisible) 0f else 10f,
        animationSpec = tween(300),
        label = "TitleBlur"
    )

    val subtitleAlpha by animateFloatAsState(
        targetValue = if (subtitleVisible) 1f else 0f,
        animationSpec = tween(200),
        label = "SubtitleAlpha"
    )
    
    val subtitleSlide by animateDpAsState(
        targetValue = if (subtitleVisible) 0.dp else 20.dp,
        animationSpec = tween(200, easing = EaseOutCubic),
        label = "SubtitleSlide"
    )

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "SpaceIA",
            fontSize = 48.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color(0xFF0D47A1),
            letterSpacing = (-1.5).sp,
            modifier = Modifier
                .alpha(titleAlpha)
                .blur(titleBlur.dp)
        )
        
        Text(
            text = "Tu acceso inteligente al campus",
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF0D47A1).copy(alpha = 0.7f),
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(top = 8.dp)
                .alpha(subtitleAlpha)
                .offset(y = subtitleSlide)
        )
    }
}
