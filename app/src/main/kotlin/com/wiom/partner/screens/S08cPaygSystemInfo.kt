package com.wiom.partner.screens

import android.media.MediaPlayer
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wiom.partner.theme.*
import com.wiom.partner.components.*
import com.wiom.partner.util.AssetImage
import kotlinx.coroutines.delay

@Composable
fun S08cPaygSystemInfo(onNext: () -> Unit) {
    val context = LocalContext.current
    var progress by remember { mutableFloatStateOf(0f) }
    var audioComplete by remember { mutableStateOf(false) }

    // Play audio
    DisposableEffect(Unit) {
        val mp = try {
            val afd = context.assets.openFd("img/payg_precheck.mp3")
            MediaPlayer().apply { setDataSource(afd.fileDescriptor, afd.startOffset, afd.length); afd.close(); prepare(); start() }
        } catch (e: Exception) { null }
        onDispose { mp?.release() }
    }

    // Progress over 17s
    LaunchedEffect(Unit) {
        val start = System.currentTimeMillis()
        while (progress < 1f) { delay(200); progress = ((System.currentTimeMillis() - start).toFloat() / 17000f).coerceAtMost(1f) }
        delay(250); audioComplete = true
    }

    Column(modifier = Modifier.fillMaxSize().background(Bg)) {
        // No header — content at y:194, left-aligned
        Column(modifier = Modifier.weight(1f).padding(start = 16.dp, end = 16.dp, top = 194.dp)) {
            AssetImage("img/payg_speaker.webp", modifier = Modifier.size(108.dp))
            Spacer(Modifier.height(24.dp))
            Text("PayG – सिस्टम जानकारी", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Pri, lineHeight = 32.sp)
            Spacer(Modifier.height(8.dp))
            Text("₹300 सिर्फ़ सिक्योरिटी फीस है", fontSize = 24.sp, color = Pri, lineHeight = 32.sp)
            Spacer(Modifier.height(24.dp))
            // Info card
            Column(
                modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(8.dp))
                    .background(InfoBg).padding(start = 12.dp, end = 12.dp, top = 16.dp, bottom = 12.dp)
            ) {
                Box(Modifier.clip(RoundedCornerShape(12.dp)).background(Info).padding(horizontal = 12.dp, vertical = 4.dp)) {
                    Text("अब से 2 दिन का नेट मिलेगा", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFFFAF9FC))
                }
                Spacer(Modifier.height(12.dp))
                Row {
                    Box(Modifier.width(2.dp).height(48.dp).background(Info))
                    Spacer(Modifier.width(8.dp))
                    Column {
                        Text("सेटअप के बाद 2 दिन का नेट मिलता है।", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Pri, lineHeight = 24.sp)
                        Text("इसके बाद रिचार्ज कस्टमर अपनी मर्ज़ी से करता है", fontSize = 14.sp, color = Pri, lineHeight = 24.sp)
                    }
                }
            }
        }
        // Progress bar
        if (!audioComplete) {
            Box(Modifier.padding(horizontal = 24.dp, vertical = 16.dp)) {
                LinearProgressIndicator(progress = progress, modifier = Modifier.fillMaxWidth().height(4.dp).clip(RoundedCornerShape(2.dp)), color = Brand, trackColor = N100)
            }
        }
        // Ghost CTA
        AnimatedVisibility(visible = audioComplete, enter = fadeIn()) {
            Box(Modifier.padding(horizontal = 16.dp, vertical = 16.dp)) { GhostCta("ठीक है", onClick = onNext) }
        }
    }
}
