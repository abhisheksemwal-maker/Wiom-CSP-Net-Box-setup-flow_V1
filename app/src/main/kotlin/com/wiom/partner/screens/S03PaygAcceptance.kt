package com.wiom.partner.screens

import android.media.MediaPlayer
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import com.wiom.partner.R
import com.wiom.partner.theme.*
import com.wiom.partner.util.AssetImage
import kotlinx.coroutines.delay

@Composable
fun S03PaygAcceptance(onNext: () -> Unit, onPaygAccepted: () -> Unit = {}) {
    val context = LocalContext.current
    var progress by remember { mutableFloatStateOf(0f) }
    var audioComplete by remember { mutableStateOf(false) }
    var checked by remember { mutableStateOf(false) }

    // Play audio from assets
    DisposableEffect(Unit) {
        val mp = try {
            val afd = context.assets.openFd("img/payg_acceptance.mp3")
            MediaPlayer().apply {
                setDataSource(afd.fileDescriptor, afd.startOffset, afd.length)
                afd.close()
                prepare()
                start()
            }
        } catch (e: Exception) { null }

        onDispose { mp?.release() }
    }

    // Animate progress bar over 10s
    LaunchedEffect(Unit) {
        val startTime = System.currentTimeMillis()
        val duration = 10000L
        while (progress < 1f) {
            delay(200)
            val elapsed = System.currentTimeMillis() - startTime
            progress = (elapsed.toFloat() / duration).coerceAtMost(1f)
        }
        delay(250)
        audioComplete = true
    }

    // Auto-navigate after checkbox
    LaunchedEffect(checked) {
        if (checked) {
            onPaygAccepted()
            delay(450)
            onNext()
        }
    }

    Column(modifier = Modifier.fillMaxSize().background(Bg)) {
        // ── Body: content at y:194 (matching WebView padding-top:194px) ──
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 24.dp, end = 24.dp, top = 194.dp)
        ) {
            // Speaker illustration: 108×108, left-aligned
            AssetImage(
                path = "img/payg_speaker.webp",
                modifier = Modifier.size(108.dp)
            )

            Spacer(Modifier.height(16.dp))

            // Headline: fs:24 fw:700
            Text(
                "PayG सेटअप में सिर्फ ₹300 सिक्योरिटी फीस होती है",
                fontSize = 24.sp, fontWeight = FontWeight.Bold,
                color = Pri, lineHeight = 32.sp
            )
            Spacer(Modifier.height(16.dp))

            // Subtitle: fs:16 fw:400
            Text(
                "कस्टमर सिर्फ ₹300 सिक्योरिटी फीस ही पे करता है, नेट बॉक्स के लिए",
                fontSize = 16.sp, color = Pri, lineHeight = 24.sp
            )
        }

        // ── Audio progress bar: pinned near bottom ──
        if (!audioComplete) {
            Box(modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp)) {
                LinearProgressIndicator(
                    progress = progress,
                    modifier = Modifier.fillMaxWidth().height(4.dp).clip(RoundedCornerShape(2.dp)),
                    color = Brand,
                    trackColor = N100
                )
            }
        }

        // ── Checkbox: appears after audio ──
        AnimatedVisibility(
            visible = audioComplete,
            enter = fadeIn() + slideInVertically { it / 2 }
        ) {
            Row(
                modifier = Modifier
                    .padding(horizontal = 24.dp, vertical = 16.dp)
                    .clickable { if (!checked) checked = true },
                verticalAlignment = Alignment.Top
            ) {
                // Checkbox: same Figma SVG icons as WebView
                Image(
                    painterResource(if (checked) R.drawable.checkbox_checked else R.drawable.checkbox_unchecked),
                    null, modifier = Modifier.size(24.dp)
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    "मैंने समझ लिया है, इस सेटअप में ₹300 के अलावा कोई और पेमेंट नहीं होती है",
                    fontSize = 16.sp, fontWeight = FontWeight.Bold,
                    color = Pri, lineHeight = 24.sp
                )
            }
        }
    }
}
