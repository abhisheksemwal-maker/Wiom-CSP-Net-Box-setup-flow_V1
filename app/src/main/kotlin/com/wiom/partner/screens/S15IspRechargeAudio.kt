package com.wiom.partner.screens

import android.media.MediaPlayer
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.ui.graphics.Color
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.shape.CircleShape
import com.wiom.partner.theme.*
import com.wiom.partner.components.*
import com.wiom.partner.util.AssetImage
import kotlinx.coroutines.delay

@Composable
fun S15IspRechargeAudio(onNext: () -> Unit, onOpenPortal: () -> Unit = {}, onOpenWhatsapp: () -> Unit = {}) {
    val context = LocalContext.current
    var progress by remember { mutableFloatStateOf(0f) }
    var audioComplete by remember { mutableStateOf(false) }
    var showIspSheet by remember { mutableStateOf(false) }

    // Play audio
    DisposableEffect(Unit) {
        val mp = try {
            val afd = context.assets.openFd("img/isp_recharge.mp3")
            MediaPlayer().apply { setDataSource(afd.fileDescriptor, afd.startOffset, afd.length); afd.close(); prepare(); start() }
        } catch (e: Exception) { null }
        onDispose { mp?.release() }
    }

    // Progress over 9.2s
    LaunchedEffect(Unit) {
        val start = System.currentTimeMillis()
        while (progress < 1f) { delay(200); progress = ((System.currentTimeMillis() - start).toFloat() / 10000f).coerceAtMost(1f) }
        delay(250); audioComplete = true
    }

    Column(modifier = Modifier.fillMaxSize().background(Bg)) {
        // No header — content at y:194, left-aligned (matching WebView)
        Column(modifier = Modifier.weight(1f).padding(start = 16.dp, end = 16.dp, top = 194.dp)) {
            // Speaker illustration: 108×108, left-aligned
            AssetImage("img/isp_speaker.png", modifier = Modifier.size(108.dp))
            Spacer(Modifier.height(16.dp))
            // Headline
            Text(
                "कस्टमर का 30 दिन, 100 Mbps का ISP रिचार्ज करें",
                fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Pri, lineHeight = 32.sp
            )
        }

        // Progress bar
        if (!audioComplete) {
            Box(Modifier.padding(start = 24.dp, end = 24.dp, bottom = 16.dp)) {
                LinearProgressIndicator(
                    progress = progress,
                    modifier = Modifier.fillMaxWidth().height(4.dp).clip(RoundedCornerShape(2.dp)),
                    color = Brand, trackColor = N100
                )
            }
        }

        // Ghost CTA after audio → opens ISP sheet
        AnimatedVisibility(visible = audioComplete, enter = fadeIn()) {
            Box(Modifier.padding(start = 16.dp, end = 16.dp, bottom = 24.dp)) {
                GhostCta("ठीक है", onClick = { showIspSheet = true })
            }
        }
    }

    // M6: Card selection highlight state + delayed navigation
    var selectedCard by remember { mutableStateOf<String?>(null) }
    LaunchedEffect(selectedCard) {
        if (selectedCard != null) {
            delay(400L)
            showIspSheet = false
            when (selectedCard) {
                "portal" -> { onOpenPortal(); onNext() }
                "office" -> { onOpenWhatsapp(); onNext() }
            }
        }
    }

    // ISP Bottom Sheet — full-screen overlay with bottom-anchored sheet
    // Matches WebView: bs-overlay (scrim) + bs-panel (slides up, r:24 top, padding-bottom:48)
    if (showIspSheet) {
        Box(
            modifier = Modifier.fillMaxSize()
                .background(Color(0x80161021)) // scrim: rgba(22,16,33,0.5)
                .clickable { showIspSheet = false },
            contentAlignment = Alignment.BottomCenter
        ) {
            // M5: slideUp animation on sheet panel
            AnimatedVisibility(
                visible = showIspSheet,
                enter = slideInVertically(initialOffsetY = { it })
            ) {
                // Sheet panel: full width, r:[24,24,0,0], bg:#FAF9FC
                Column(
                    modifier = Modifier.fillMaxWidth()
                        .clip(RoundedCornerShape(24.dp, 24.dp, 0.dp, 0.dp))
                        .background(Bg)
                        .clickable(enabled = false) {} // prevent scrim dismiss when tapping sheet
                        .padding(bottom = 48.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Handle: 120×4, bg:#D7D3E0, r:4, padding:12 top 24 bottom
                    Box(Modifier.padding(top = 12.dp, bottom = 24.dp).width(120.dp).height(4.dp)
                        .clip(RoundedCornerShape(4.dp)).background(BorderDivider))
                    // Title: padding:[0,16,8,16]
                    Text(
                        "ISP अकाउंट कैसे बनाना चाहते हैं?",
                        fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Pri, lineHeight = 32.sp,
                        modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 8.dp).fillMaxWidth()
                    )
                    // Cards: padding:[24,16,0,16], gap:16
                    Row(Modifier.fillMaxWidth().padding(start = 16.dp, end = 16.dp, top = 24.dp), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        // Card 1: खुद से, पोर्टल पर — M6: highlight on selection, M7: pink circle icon wrapper
                        val isPortalSelected = selectedCard == "portal"
                        Column(
                            modifier = Modifier.weight(1f)
                                .border(1.dp, if (isPortalSelected) Brand else Border, RoundedCornerShape(16.dp))
                                .clip(RoundedCornerShape(16.dp))
                                .background(if (isPortalSelected) BrandSec else Bg)
                                .clickable { if (selectedCard == null) selectedCard = "portal" }
                                .padding(horizontal = 16.dp, vertical = 32.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(24.dp)
                        ) {
                            AssetImage("img/ic_phone_portal.webp", modifier = Modifier.size(48.dp))
                            Text("खुद से, पोर्टल पर", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Pri, textAlign = TextAlign.Center)
                        }
                        // Card 2: ऑफिस भेजकर — M6: highlight on selection, M7: pink circle icon wrapper
                        val isOfficeSelected = selectedCard == "office"
                        Column(
                            modifier = Modifier.weight(1f)
                                .border(1.dp, if (isOfficeSelected) Brand else Border, RoundedCornerShape(16.dp))
                                .clip(RoundedCornerShape(16.dp))
                                .background(if (isOfficeSelected) BrandSec else Bg)
                                .clickable { if (selectedCard == null) selectedCard = "office" }
                                .padding(horizontal = 16.dp, vertical = 32.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(24.dp)
                        ) {
                            // M7: pink circle wrapper
                            AssetImage("img/ic_office_send.webp", modifier = Modifier.size(48.dp))
                            Text("ऑफिस भेजकर", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Pri, textAlign = TextAlign.Center)
                        }
                    }
                }
            }
        }
    }
}
