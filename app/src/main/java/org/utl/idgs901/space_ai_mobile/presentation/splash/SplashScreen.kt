package org.utl.idgs901.space_ai_mobile.presentation.splash

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import org.utl.idgs901.space_ai_mobile.core.designsystem.motion.SpaceIAMotion

@Composable
fun SplashScreen(
    viewModel: SplashViewModel = hiltViewModel(),
    onNavigateToLogin: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.shouldNavigateToLogin) {
        if (uiState.shouldNavigateToLogin) {
            onNavigateToLogin()
        }
    }

    var startExitAnim by remember { mutableStateOf(false) }
    val exitAlpha by animateFloatAsState(
        targetValue = if (startExitAnim) 0f else 1f,
        animationSpec = tween(SpaceIAMotion.Duration.Normal),
        label = "ExitAlpha"
    )

    LaunchedEffect(uiState.shouldNavigateToLogin) {
        if (uiState.shouldNavigateToLogin) {
            startExitAnim = true
        }
    }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .alpha(exitAlpha)
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFE3F2FD),
                        Color(0xFFBBDEFB),
                        Color(0xFF90CAF9)
                    )
                )
            )
            .windowInsetsPadding(WindowInsets.safeDrawing)
    ) {
        val screenWidth = this.maxWidth
        val isSmallScreen = screenWidth < 360.dp

        // Premium Background Particles
        BackgroundParticles()

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            AnimatedLogoSection(isSmallScreen)
            
            Spacer(modifier = Modifier.height(32.dp))
            
            AnimatedTextSection()
        }

        // Premium Loading Section
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 64.dp)
                .width(screenWidth * 0.5f)
        ) {
            LinearProgressIndicator(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp)
                    .graphicsLayer(alpha = 0.6f),
                color = Color(0xFF0D47A1),
                trackColor = Color(0xFF0D47A1).copy(alpha = 0.1f)
            )
        }
    }
}

@Composable
private fun AnimatedLogoSection(isSmallScreen: Boolean) {
    val infiniteTransition = rememberInfiniteTransition(label = "LogoFloating")
    
    val floatAnim by infiniteTransition.animateFloat(
        initialValue = -10f,
        targetValue = 10f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = SpaceIAMotion.Easing.Smooth),
            repeatMode = RepeatMode.Reverse
        ),
        label = "Floating"
    )

    val scaleAnim = remember { Animatable(0.5f) }
    val alphaAnim = remember { Animatable(0f) }

    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.7f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = SpaceIAMotion.Easing.Smooth),
            repeatMode = RepeatMode.Reverse
        ),
        label = "Glow"
    )

    LaunchedEffect(Unit) {
        scaleAnim.animateTo(
            targetValue = 1f,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessLow
            )
        )
        alphaAnim.animateTo(1f, tween(1000))
    }

    Box(contentAlignment = Alignment.Center) {
        // Institutional Glow
        Surface(
            modifier = Modifier
                .size(if (isSmallScreen) 120.dp else 150.dp)
                .graphicsLayer {
                    scaleX = scaleAnim.value * 1.2f
                    scaleY = scaleAnim.value * 1.2f
                    alpha = alphaAnim.value * glowAlpha
                },
            shape = RoundedCornerShape(40.dp),
            color = Color(0xFF0D47A1).copy(alpha = 0.2f)
        ) {}

        Surface(
            modifier = Modifier
                .size(if (isSmallScreen) 100.dp else 128.dp)
                .graphicsLayer {
                    translationY = floatAnim
                    scaleX = scaleAnim.value
                    scaleY = scaleAnim.value
                    alpha = alphaAnim.value
                }
                .clip(RoundedCornerShape(32.dp)),
            shape = RoundedCornerShape(32.dp),
            color = Color.White,
            shadowElevation = 16.dp
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = Icons.Default.Face,
                    contentDescription = "Logotipo de SpaceIA",
                    tint = Color(0xFF0D47A1),
                    modifier = Modifier.size(if (isSmallScreen) 50.dp else 64.dp)
                )
            }
        }
    }
}

@Composable
private fun AnimatedTextSection() {
    var startTextAnim by remember { mutableStateOf(false) }
    
    val alphaText by animateFloatAsState(
        targetValue = if (startTextAnim) 1f else 0f,
        animationSpec = tween(1500, delayMillis = 300),
        label = "AlphaText"
    )

    val slideText by animateDpAsState(
        targetValue = if (startTextAnim) 0.dp else 20.dp,
        animationSpec = tween(1000, delayMillis = 300, easing = EaseOutCubic),
        label = "SlideText"
    )

    LaunchedEffect(Unit) {
        startTextAnim = true
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .offset(y = slideText)
            .alpha(alphaText)
    ) {
        Text(
            text = "SpaceIA",
            fontSize = 42.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color(0xFF0D47A1),
            letterSpacing = (-1).sp,
            modifier = Modifier.graphicsLayer {
                alpha = alphaText
                scaleX = 0.9f + (0.1f * alphaText)
                scaleY = 0.9f + (0.1f * alphaText)
            }
        )
        
        Text(
            text = "Transformando la experiencia del campus",
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF0D47A1).copy(alpha = 0.7f),
            textAlign = TextAlign.Center,
            letterSpacing = 0.5.sp,
            modifier = Modifier.padding(top = 8.dp, start = 32.dp, end = 32.dp)
        )
    }
}

@Composable
private fun BackgroundParticles() {
    val infiniteTransition = rememberInfiniteTransition(label = "Particles")
    
    val movement by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(10000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "Movement"
    )

    Canvas(modifier = Modifier.fillMaxSize()) {
        val particles = listOf(
            Offset(size.width * 0.2f, size.height * 0.3f),
            Offset(size.width * 0.8f, size.height * 0.2f),
            Offset(size.width * 0.5f, size.height * 0.8f),
            Offset(size.width * 0.1f, size.height * 0.7f),
            Offset(size.width * 0.9f, size.height * 0.6f)
        )

        particles.forEachIndexed { index, offset ->
            val animatedOffset = Offset(
                x = offset.x + (index * 20 * movement),
                y = offset.y - (index * 30 * movement)
            )
            
            drawCircle(
                color = Color.White.copy(alpha = 0.2f),
                radius = (10 + (index * 5)).toFloat(),
                center = animatedOffset
            )
        }
    }
}
