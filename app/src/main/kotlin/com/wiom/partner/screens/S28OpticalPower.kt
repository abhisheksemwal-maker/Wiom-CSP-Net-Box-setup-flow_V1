package com.wiom.partner.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wiom.partner.components.CtaArea
import com.wiom.partner.components.LightHeader
import com.wiom.partner.components.PrimaryCta
import com.wiom.partner.theme.*
import com.wiom.partner.util.AssetImage
import kotlinx.coroutines.delay

@Composable
fun S28OpticalPower(
    onNext: () -> Unit,
    onClose: () -> Unit
) {
    var currentDb by remember { mutableIntStateOf(0) }
    var animDone by remember { mutableStateOf(false) }

    // Animated counter 0 -> -21 dB at 150ms interval
    LaunchedEffect(Unit) {
        for (i in 0 downTo -21) {
            currentDb = i
            delay(150L)
        }
        animDone = true
    }

    Column(
        modifier = Modifier.fillMaxSize().background(Bg)
    ) {
        LightHeader(onClose = onClose)

        // Centered content
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Optical power illustration (96dp green circle with 64dp icon)
            Box(
                modifier = Modifier
                    .size(96.dp)
                    .clip(CircleShape)
                    .background(GreenBg),
                contentAlignment = Alignment.Center
            ) {
                AssetImage(
                    path = "img/optical_power.webp",
                    contentDescription = "Optical Power",
                    modifier = Modifier.size(64.dp)
                )
            }

            Spacer(Modifier.height(24.dp))

            // Value area
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Animated dB counter
                Text(
                    text = "$currentDb dB",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Pos,
                    textAlign = TextAlign.Center
                )

                // Status text
                Text(
                    text = if (animDone) "ऑप्टिकल पॉवर सही है" else "नेट बॉक्स तैयार किया जा रहा है...",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal,
                    color = if (animDone) Pos else Pri,
                    textAlign = TextAlign.Center
                )
            }
        }

        // CTA revealed after animation completes
        AnimatedVisibility(
            visible = animDone,
            enter = fadeIn() + slideInVertically(initialOffsetY = { it / 2 })
        ) {
            CtaArea {
                PrimaryCta(
                    text = "अब नेट स्पीड चेक करें",
                    onClick = onNext
                )
            }
        }

        // Spacer when CTA is hidden to preserve layout
        if (!animDone) {
            CtaArea {
                Spacer(Modifier.height(48.dp))
            }
        }
    }
}
