package com.wiom.partner.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wiom.partner.components.LightHeader
import com.wiom.partner.theme.*
import kotlinx.coroutines.delay

@Composable
fun S26Loading(
    onNext: () -> Unit,
    onClose: () -> Unit
) {
    // Auto-transition after 3s
    LaunchedEffect(Unit) {
        delay(3000L)
        onNext()
    }

    // Spinner rotation
    val infiniteTransition = rememberInfiniteTransition(label = "spinner")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation"
    )

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
            // Green circle with Canvas arc spinner
            Box(
                modifier = Modifier
                    .size(96.dp)
                    .clip(CircleShape)
                    .background(GreenBg),
                contentAlignment = Alignment.Center
            ) {
                Canvas(modifier = Modifier.size(48.dp)) {
                    val strokeWidth = 4.dp.toPx()
                    // Track
                    drawArc(
                        color = GreenBg,
                        startAngle = 0f,
                        sweepAngle = 360f,
                        useCenter = false,
                        style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                    )
                    // Active arc
                    drawArc(
                        color = Pos,
                        startAngle = rotation - 90f,
                        sweepAngle = 90f,
                        useCenter = false,
                        style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            Text(
                text = "नेट बॉक्स तैयार किया जा रहा है...",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                lineHeight = 28.sp,
                color = Pri,
                textAlign = TextAlign.Center
            )
        }

        // Hidden CTA spacer (preserves layout consistency with S27)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Bg)
                .padding(16.dp)
        ) {
            // Invisible box matching CTA height
            Spacer(Modifier.fillMaxWidth().height(48.dp))
        }
    }
}
