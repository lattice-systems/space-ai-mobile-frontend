package org.utl.idgs901.space_ai_mobile.core.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.utl.idgs901.space_ai_mobile.core.designsystem.motion.spaceIAPressScale

@Composable
fun MicrosoftSignInButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    val haptic = LocalHapticFeedback.current

    OutlinedButton(
        onClick = {
            haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
            onClick()
        },
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .spaceIAPressScale(),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = Color.White,
            contentColor = Color(0xFF5E5E5E)
        ),
        border = ButtonDefaults.outlinedButtonBorder.copy(width = 1.dp),
        enabled = enabled
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            MicrosoftLogo()
            
            Spacer(modifier = Modifier.width(12.dp))
            
            Text(
                text = "Continuar con Microsoft",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 0.sp
            )
        }
    }
}

@Composable
private fun MicrosoftLogo() {
    Column(modifier = Modifier.size(20.dp)) {
        Row(modifier = Modifier.weight(1f)) {
            Box(modifier = Modifier.weight(1f).fillMaxHeight().padding(1.dp).background(Color(0xFFF25022)))
            Box(modifier = Modifier.weight(1f).fillMaxHeight().padding(1.dp).background(Color(0xFF7FBA00)))
        }
        Row(modifier = Modifier.weight(1f)) {
            Box(modifier = Modifier.weight(1f).fillMaxHeight().padding(1.dp).background(Color(0xFF00A4EF)))
            Box(modifier = Modifier.weight(1f).fillMaxHeight().padding(1.dp).background(Color(0xFFFFB900)))
        }
    }
}
