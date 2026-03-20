package com.wiom.partner.screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.animation.core.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wiom.partner.theme.*
import com.wiom.partner.components.*
import kotlinx.coroutines.delay

// Animation states matching WebView: 6 states over 5.4 seconds
private enum class CheckState { PENDING, SPINNING, DONE }

@Composable
fun S12PaymentChecklist(onComplete: () -> Unit) {
    // Step states
    var step2State by remember { mutableStateOf(CheckState.PENDING) }
    var step3State by remember { mutableStateOf(CheckState.PENDING) }
    var step3Text by remember { mutableStateOf("पेमेंट हो गयी") }
    var showPaygCard by remember { mutableStateOf(false) }
    var bar2Color by remember { mutableStateOf(BorderDivider) }

    // Animate bar2 color
    val animBar2 by animateColorAsState(bar2Color, label = "bar2")

    // 6-state animation matching WebView timing exactly
    LaunchedEffect(Unit) {
        // State 1: initial (WiFi done, others pending) — 1s
        delay(1000)
        // State 2: step 2 spinner
        step2State = CheckState.SPINNING
        delay(600)
        // State 3: step 2 done, bar2 green
        step2State = CheckState.DONE
        bar2Color = Pos
        delay(600)
        // State 4: step 3 spinner
        step3State = CheckState.SPINNING
        delay(800)
        // State 5: step 3 text changes + PayG card appears (1200ms hold)
        step3Text = "पेमेंट करें"
        showPaygCard = true
        delay(1200)
        // State 6: all done, PayG card hidden
        step3State = CheckState.DONE
        step3Text = "पेमेंट हो गयी"
        showPaygCard = false
        // Auto-transition after 1200ms
        delay(1200)
        onComplete()
    }

    Column(modifier = Modifier.fillMaxSize().background(Bg)) {
        // Header: close + मदद
        LightHeader(onClose = { })

        // Title
        Text(
            "Himanshu Singh जी से व्योम ऐप द्वारा पेमेंट करने को कहें",
            fontSize = 24.sp, fontWeight = FontWeight.Bold, lineHeight = 32.sp, color = Pri,
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 16.dp)
        )

        // Body
        Column(
            modifier = Modifier.weight(1f).verticalScroll(rememberScrollState()).padding(horizontal = 16.dp)
        ) {
            // Checklist card: N100 bg, r:16, relative positioning for bars
            Box(
                modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(16.dp)).background(N100)
                    .padding(horizontal = 16.dp, vertical = 24.dp)
            ) {
                // Vertical progress bars (absolute positioned)
                Column(modifier = Modifier.fillMaxWidth()) {
                    // Bar 1: between step 1 and step 2 — always green
                    Box(Modifier.padding(start = 11.dp).width(2.dp).height(0.dp)) // spacer for step 1 icon
                }

                Column(modifier = Modifier.fillMaxWidth()) {
                    // Step 1: WiFi — always done (green tick)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        GreenTick()
                        Text("WiFi का नाम रख दिया", fontSize = 16.sp, color = Pri)
                    }

                    // Bar between step 1-2
                    Box(Modifier.padding(start = 11.dp).width(2.dp).height(32.dp).background(Pos))

                    // Step 2: Aadhaar
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        StepIcon(step2State)
                        Text("आधार कार्ड और प्लग तैयार हैंं", fontSize = 16.sp, color = Pri)
                    }

                    // Bar between step 2-3
                    Box(Modifier.padding(start = 11.dp).width(2.dp).height(32.dp).background(animBar2))

                    // Step 3: Payment
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        StepIcon(step3State)
                        Text(step3Text, fontSize = 16.sp, color = Pri)
                    }
                }
            }

            // PayG info card (shown during state 5)
            if (showPaygCard) {
                Spacer(Modifier.height(16.dp))
                Column(
                    modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(8.dp))
                        .background(InfoBg).padding(start = 12.dp, end = 12.dp, top = 16.dp, bottom = 12.dp)
                ) {
                    Box(Modifier.clip(RoundedCornerShape(12.dp)).background(Info).padding(horizontal = 12.dp, vertical = 4.dp)) {
                        Text("PayG – सिस्टम जानकारी", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFFFAF9FC))
                    }
                    Spacer(Modifier.height(12.dp))
                    Row {
                        Box(Modifier.width(2.dp).height(66.dp).background(Info))
                        Spacer(Modifier.width(8.dp))
                        Column {
                            Text("₹300 सिक्योरिटी फीस होती है", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Pri, lineHeight = 22.sp)
                            Text("इसके अलावा कोई भी पेमेंट की अनुमति नहीं है", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Pri, lineHeight = 22.sp)
                            Text("कस्टमर अपनी ऐप में QR कोड से भी पेमेंट कर सकता है", fontSize = 14.sp, color = Pri, lineHeight = 22.sp)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun GreenTick() {
    Box(
        modifier = Modifier.size(24.dp).clip(CircleShape).background(Pos),
        contentAlignment = Alignment.Center
    ) {
        Icon(Icons.Rounded.Check, null, tint = Color.White, modifier = Modifier.size(16.dp))
    }
}

@Composable
private fun GreyRadio() {
    Box(
        modifier = Modifier.size(24.dp).clip(CircleShape)
            .background(Color.Transparent)
            .then(Modifier.padding(2.dp))
    ) {
        Box(Modifier.fillMaxSize().clip(CircleShape).background(Color.Transparent).padding(2.dp)) {
            // Grey ring
        }
    }
    // Simpler: just a grey ring
}

@Composable
private fun StepIcon(state: CheckState) {
    when (state) {
        CheckState.PENDING -> {
            // Grey stroke ring: matches WebView SVG circle r=10 stroke=#A7A1B2 stroke-width=2, no fill
            Canvas(modifier = Modifier.size(24.dp)) {
                drawCircle(
                    color = Hint,
                    radius = 10.dp.toPx(),
                    center = center,
                    style = androidx.compose.ui.graphics.drawscope.Stroke(width = 2.dp.toPx())
                )
            }
        }
        CheckState.SPINNING -> {
            // Pink arc spinner 20px — Canvas arc, matching WebView CSS border spinner
            val infiniteTransition = rememberInfiniteTransition(label = "spin")
            val rotation by infiniteTransition.animateFloat(
                initialValue = 0f, targetValue = 360f,
                animationSpec = infiniteRepeatable(tween(800, easing = LinearEasing)),
                label = "rotation"
            )
            Canvas(modifier = Modifier.size(24.dp)) {
                // Track circle (light)
                drawCircle(
                    color = BrandSec,
                    radius = 10.dp.toPx(),
                    center = center,
                    style = androidx.compose.ui.graphics.drawscope.Stroke(width = 2.dp.toPx())
                )
                // Spinning brand arc (90°)
                drawArc(
                    color = Brand,
                    startAngle = rotation,
                    sweepAngle = 90f,
                    useCenter = false,
                    style = androidx.compose.ui.graphics.drawscope.Stroke(
                        width = 2.dp.toPx(),
                        cap = androidx.compose.ui.graphics.StrokeCap.Round
                    )
                )
            }
        }
        CheckState.DONE -> {
            GreenTick()
        }
    }
}
