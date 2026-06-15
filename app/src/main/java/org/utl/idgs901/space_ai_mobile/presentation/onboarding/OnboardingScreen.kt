package org.utl.idgs901.space_ai_mobile.presentation.onboarding

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

data class OnboardingPage(
    val title: String,
    val description: String,
    val icon: ImageVector,
    val color: Color
)

val pages = listOf(
    OnboardingPage(
        title = "Smart Campus",
        description = "Bienvenido a SpaceIA, el ecosistema digital que transforma tu experiencia universitaria con inteligencia artificial.",
        icon = Icons.Default.School,
        color = Color(0xFF1565C0)
    ),
    OnboardingPage(
        title = "Navegación Inteligente",
        description = "Encuentra salones, laboratorios y oficinas fácilmente con nuestro motor de navegación guiada por IA.",
        icon = Icons.Default.Map,
        color = Color(0xFF00897B)
    ),
    OnboardingPage(
        title = "Asistente Virtual",
        description = "Resuelve dudas institucionales al instante con nuestro asistente basado en modelos de lenguaje avanzados.",
        icon = Icons.Default.AutoAwesome,
        color = Color(0xFF6A1B9A)
    )
)

@Composable
fun OnboardingScreen(
    onFinished: () -> Unit
) {
    val pagerState = rememberPagerState(pageCount = { pages.size })
    val scope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .windowInsetsPadding(WindowInsets.safeDrawing)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) { pageIndex ->
                OnboardingContent(pages[pageIndex])
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Pager Indicator
            Row(
                Modifier
                    .height(50.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                repeat(pages.size) { iteration ->
                    val color = if (pagerState.currentPage == iteration) 
                        pages[pagerState.currentPage].color 
                    else 
                        Color.LightGray
                    
                    Box(
                        modifier = Modifier
                            .padding(4.dp)
                            .clip(CircleShape)
                            .background(color)
                            .size(if (pagerState.currentPage == iteration) 12.dp else 8.dp)
                    )
                }
            }

            // Buttons
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
                    .height(80.dp)
            ) {
                AnimatedVisibility(
                    visible = pagerState.currentPage < pages.size - 1,
                    enter = fadeIn(),
                    exit = fadeOut(),
                    modifier = Modifier.align(Alignment.CenterStart)
                ) {
                    TextButton(onClick = onFinished) {
                        Text(
                            "Omitir",
                            color = Color.Gray,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                Button(
                    onClick = {
                        if (pagerState.currentPage < pages.size - 1) {
                            scope.launch {
                                pagerState.animateScrollToPage(pagerState.currentPage + 1)
                            }
                        } else {
                            onFinished()
                        }
                    },
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .height(56.dp)
                        .widthIn(min = 140.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = pages[pagerState.currentPage].color
                    )
                ) {
                    Text(
                        if (pagerState.currentPage == pages.size - 1) "Comenzar" else "Siguiente",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null)
                }
            }
        }
    }
}

@Composable
fun OnboardingContent(page: OnboardingPage) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Surface(
            modifier = Modifier.size(200.dp),
            shape = CircleShape,
            color = page.color.copy(alpha = 0.1f)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = page.icon,
                    contentDescription = null,
                    modifier = Modifier.size(100.dp),
                    tint = page.color
                )
            }
        }

        Spacer(modifier = Modifier.height(48.dp))

        Text(
            text = page.title,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1A1A1A),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = page.description,
            fontSize = 16.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center,
            lineHeight = 24.sp
        )
    }
}
