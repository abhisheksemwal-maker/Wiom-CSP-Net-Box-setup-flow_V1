package com.wiom.partner.screens

import android.content.res.AssetFileDescriptor
import android.media.MediaPlayer
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wiom.partner.components.GhostCta
import com.wiom.partner.theme.*
import com.wiom.partner.util.AssetImage
import kotlinx.coroutines.delay

@Composable
fun S30RechargeInfo(
    onNext: () -> Unit
) {
    val context = LocalContext.current
    var audioProgress by remember { mutableFloatStateOf(0f) }
    var audioDone by remember { mutableStateOf(false) }

    // Audio playback with MediaPlayer in DisposableEffect
    DisposableEffect(Unit) {
        var mediaPlayer: MediaPlayer? = null
        try {
            val afd: AssetFileDescriptor = context.assets.openFd("img/recharge_info.mp3")
            mediaPlayer = MediaPlayer().apply {
                setDataSource(afd.fileDescriptor, afd.startOffset, afd.length)
                afd.close()
                prepare()
                start()
            }
        } catch (_: Exception) {
            // Audio file may not exist; proceed without audio
        }
        onDispose {
            try {
                mediaPlayer?.stop()
                mediaPlayer?.release()
            } catch (_: Exception) {}
        }
    }

    // Timer for progress bar and CTA visibility (10.3s audio, CTA after 11s)
    LaunchedEffect(Unit) {
        val totalMs = 10300L
        val stepMs = 100L
        var elapsed = 0L
        while (elapsed < totalMs) {
            delay(stepMs)
            elapsed += stepMs
            audioProgress = (elapsed.toFloat() / totalMs).coerceAtMost(1f)
        }
        audioProgress = 1f
        // Wait remaining time until 11s total
        delay(700L)
        audioDone = true
    }

    Column(
        modifier = Modifier.fillMaxSize().background(Bg)
    ) {
        // NO header — content starts at padding-top 194dp
        Spacer(Modifier.height(194.dp))

        // Content column
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Speaker image (left-aligned, NOT centered)
                AssetImage(
                    path = "img/speaker_recharge.webp",
                    contentDescription = "Speaker",
                    modifier = Modifier.size(108.dp)
                )

                // Headline
                Text(
                    text = "अब कस्टमर अगले 2 दिन तक नेट का इस्तमाल कर सकते हैं",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Pri,
                    lineHeight = 32.sp
                )

                // Subtitle (pure black, NOT Pri)
                Text(
                    text = "इसके बाद रिचार्ज कस्टमर अपनी मर्ज़ी से करता है",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color.Black,
                    lineHeight = 24.sp
                )
            }
        }

        // Progress bar: visible during audio, hidden after → replaced by CTA
        if (!audioDone) {
            // Progress bar pinned at bottom: padding [0,24,36,24]
            Box(
                modifier = Modifier.fillMaxWidth()
                    .padding(start = 24.dp, end = 24.dp, bottom = 36.dp)
            ) {
                Canvas(
                    modifier = Modifier.fillMaxWidth().height(4.dp)
                        .clip(RoundedCornerShape(4.dp))
                ) {
                    drawRect(color = N100)
                    drawRect(color = Brand, size = size.copy(width = size.width * audioProgress))
                }
            }
        } else {
            // Ghost CTA: padding [0,16,24,16]
            Box(
                modifier = Modifier.fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, bottom = 24.dp)
            ) {
                GhostCta(text = "समझ गया", onClick = onNext)
            }
        }
    }
}
