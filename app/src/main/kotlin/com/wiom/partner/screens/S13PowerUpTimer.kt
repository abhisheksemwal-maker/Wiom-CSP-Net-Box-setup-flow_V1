package com.wiom.partner.screens

import android.media.MediaPlayer
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wiom.partner.theme.*
import com.wiom.partner.components.*
import com.wiom.partner.util.AssetImage
import kotlinx.coroutines.delay

@Composable
fun S13PowerUpTimer(onNext: () -> Unit) {
    val context = LocalContext.current
    var seconds by remember { mutableIntStateOf(15) }

    // Play audio
    DisposableEffect(Unit) {
        val mp = try {
            val afd = context.assets.openFd("img/power_on.mp3")
            MediaPlayer().apply { setDataSource(afd.fileDescriptor, afd.startOffset, afd.length); afd.close(); prepare(); start() }
        } catch (e: Exception) { null }
        onDispose { mp?.release() }
    }

    // Countdown + auto-transition
    LaunchedEffect(Unit) {
        while (seconds > 0) { delay(1000); seconds-- }
        delay(500); onNext()
    }

    // Spinner
    val infiniteTransition = rememberInfiniteTransition(label = "spin")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f, targetValue = 360f,
        animationSpec = infiniteRepeatable(tween(600, easing = LinearEasing)), label = "rot"
    )

    Column(modifier = Modifier.fillMaxSize().background(Bg)) {
        LightHeader(onClose = { })
        Text(
            "व्योम नेट बॉक्स को स्विच ऑन करें",
            fontSize = 24.sp, fontWeight = FontWeight.Bold, lineHeight = 32.sp, color = Pri,
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 16.dp)
        )
        Column(modifier = Modifier.weight(1f).padding(horizontal = 16.dp)) {
            // Router image: r:16
            Box(Modifier.fillMaxWidth().clip(RoundedCornerShape(16.dp)).background(N200)) {
                AssetImage("img/netbox_power_masked.png", modifier = Modifier.fillMaxWidth(), contentScale = ContentScale.FillWidth)
            }
            Spacer(Modifier.height(24.dp))
            // Timer: centered
            Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    androidx.compose.foundation.Canvas(modifier = Modifier.size(20.dp)) {
                        drawArc(color = N200, startAngle = 0f, sweepAngle = 360f, useCenter = false,
                            style = androidx.compose.ui.graphics.drawscope.Stroke(3.dp.toPx()))
                        drawArc(color = Brand, startAngle = rotation, sweepAngle = 90f, useCenter = false,
                            style = androidx.compose.ui.graphics.drawscope.Stroke(3.dp.toPx(), cap = androidx.compose.ui.graphics.StrokeCap.Round))
                    }
                    Text("00:${seconds.toString().padStart(2, '0')}", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Pri)
                }
                Text("इसमें 1-2 मिनट का समय लग सकता है", fontSize = 14.sp, color = Pri)
            }
        }
    }
}
