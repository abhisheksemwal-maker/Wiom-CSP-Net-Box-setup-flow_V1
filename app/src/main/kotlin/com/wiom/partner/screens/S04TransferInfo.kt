package com.wiom.partner.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.EmojiObjects
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.zIndex
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.wiom.partner.theme.*
import com.wiom.partner.components.*
import androidx.compose.foundation.Image
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.wiom.partner.R
import com.wiom.partner.util.AssetImage
import kotlinx.coroutines.delay

@Composable
fun S04TransferInfo(
    onNext: () -> Unit,
    onCall: () -> Unit,
    onClose: () -> Unit,
    onExitToHome: () -> Unit = {}
) {
    var showThreePinDialog by remember { mutableStateOf(false) }
    var showArrivalDialog by remember { mutableStateOf(false) }
    var showExitDialog by remember { mutableStateOf(false) }
    var threePinChecked by remember { mutableStateOf(false) }
    var showPostCall by remember { mutableStateOf(false) }

    // Auto-transition from 3-pin to arrival dialog
    LaunchedEffect(threePinChecked) {
        if (threePinChecked) {
            delay(600)
            showThreePinDialog = false
            showArrivalDialog = true
        }
    }

    Column(modifier = Modifier.fillMaxSize().background(Bg)) {
        // ── Header: back + "नया सेटअप" + more_vert ──
        Row(
            modifier = Modifier.fillMaxWidth().height(56.dp).padding(horizontal = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(modifier = Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically) {
                Box(Modifier.size(48.dp).clickable { showExitDialog = true }, contentAlignment = Alignment.Center) {
                    Icon(Icons.Rounded.ArrowBack, "Back", tint = Pri, modifier = Modifier.size(24.dp))
                }
                Spacer(Modifier.width(4.dp))
                Text("नया सेटअप", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Pri, maxLines = 1, overflow = TextOverflow.Ellipsis)
            }
            Box(Modifier.size(48.dp).clickable { }, contentAlignment = Alignment.Center) {
                Image(painterResource(R.drawable.ic_more), null, modifier = Modifier.size(24.dp))
            }
        }

        // ── Body ──
        Column(
            modifier = Modifier.weight(1f).verticalScroll(rememberScrollState()).padding(horizontal = 16.dp)
        ) {
            // ── Deadline card ──
            Row(
                modifier = Modifier.fillMaxWidth()
                    .border(1.dp, Border, RoundedCornerShape(12.dp))
                    .padding(horizontal = 12.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Image(painterResource(R.drawable.ic_alarm), null, modifier = Modifier.size(24.dp))
                Text("12 January", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = WarnTxt)
                Text("से पहले नेट चालू करना है", fontSize = 16.sp, color = Pri)
            }

            Spacer(Modifier.height(16.dp))

            // ── Customer Info Card (elevated + tip bar) ──
            Column(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.fillMaxWidth()
                        .zIndex(1f)
                        .shadow(3.dp, RoundedCornerShape(12.dp))
                        .clip(RoundedCornerShape(12.dp))
                        .background(Card)
                        .border(1.dp, Border, RoundedCornerShape(12.dp))
                        .padding(horizontal = 12.dp, vertical = 16.dp)
                ) {
                    // Name + call/direction
                    Row(Modifier.fillMaxWidth().padding(bottom = 12.dp), verticalAlignment = Alignment.CenterVertically) {
                        Image(painterResource(R.drawable.ic_user), null, modifier = Modifier.size(24.dp))
                        Spacer(Modifier.width(8.dp))
                        Text("Himanshu Singh", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Pri, modifier = Modifier.weight(1f))
                        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                            Image(painterResource(R.drawable.ic_call_btn), null, modifier = Modifier.size(32.dp).clickable {
                                onCall()
                                showPostCall = true
                            })
                            Image(painterResource(R.drawable.ic_direction_btn), null, modifier = Modifier.size(32.dp))
                        }
                    }
                    // Address
                    Row(Modifier.fillMaxWidth().padding(bottom = 12.dp), verticalAlignment = Alignment.Top) {
                        Image(painterResource(R.drawable.ic_pin), null, modifier = Modifier.size(24.dp))
                        Spacer(Modifier.width(8.dp))
                        Text("G 69, Mata mandir, 3rd street, Ghaziabad Uttar Pradesh", fontSize = 14.sp, color = Pri, lineHeight = 20.sp, modifier = Modifier.weight(1f))
                    }
                    // Speed
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Image(painterResource(R.drawable.ic_speed), null, modifier = Modifier.size(24.dp))
                        Text("100 Mbps", fontSize = 14.sp, color = Pri)
                        Text("स्पीड", fontSize = 12.sp, color = Hint)
                    }
                    // Family number (post-call)
                    AnimatedVisibility(visible = showPostCall, enter = fadeIn()) {
                        Row(Modifier.padding(top = 12.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Image(painterResource(R.drawable.ic_ticket), null, modifier = Modifier.size(24.dp))
                            Text("फैमिली नंबर : 98765 43210", fontSize = 14.sp, color = Pri)
                        }
                    }
                }

                // Tip bar: connected, -12dp overlap, info-bg
                Row(
                    modifier = Modifier.fillMaxWidth().offset(y = (-12).dp)
                        .clip(RoundedCornerShape(bottomStart = 12.dp, bottomEnd = 12.dp))
                        .background(InfoBg).padding(start = 12.dp, end = 12.dp, top = 23.dp, bottom = 12.dp),
                    verticalAlignment = Alignment.Top, horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(Icons.Rounded.EmojiObjects, null, tint = Info, modifier = Modifier.size(24.dp))
                    Text("कस्टमर को कॉल करके, अपने पहुंचने का समय बता दें", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = Info, lineHeight = 16.sp)
                }
            }

        }

        // ── ISP prefill card (post-call) — outside scrollable, near CTA ──
        AnimatedVisibility(visible = showPostCall, enter = fadeIn(), modifier = Modifier.padding(horizontal = 16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp)).background(Color(0xFFE1FAED))
                        .border(1.dp, Color(0xFFA5E5C6), RoundedCornerShape(8.dp))
                        .padding(start = 12.dp, end = 12.dp, top = 16.dp, bottom = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(Modifier.weight(1f)) {
                        Row(
                            modifier = Modifier.clip(RoundedCornerShape(12.dp)).background(Pos)
                                .border(1.dp, Border, RoundedCornerShape(12.dp))
                                .padding(horizontal = 12.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Image(painterResource(R.drawable.ic_rocket), null, modifier = Modifier.size(24.dp))
                            Text("सेटअप पर 15 मिनट बचाएँ", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFFFAF9FC))
                        }
                        Spacer(Modifier.height(12.dp))
                        Text("अब ISP अकाउंट की डिटेल्स पहले भर लें", fontSize = 16.sp, color = Pri, modifier = Modifier.padding(start = 8.dp))
                    }
                    Image(painterResource(R.drawable.ic_arrow_right), null, modifier = Modifier.size(36.dp))
                }
        }

        // ── CTA ──
        CtaArea { PrimaryCta("सेटअप शुरू करें", onClick = { showThreePinDialog = true }) }
    }

    // ══ 3-Pin Plug Dialog ══
    if (showThreePinDialog) {
        Dialog(onDismissRequest = { }) {
            Column(
                modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(24.dp))
                    .background(Card).padding(horizontal = 24.dp, vertical = 32.dp)
            ) {
                // Plug image — full-width container, image fills
                Box(
                    Modifier.fillMaxWidth().height(200.dp).clip(RoundedCornerShape(12.dp)).background(InfoBg),
                    contentAlignment = Alignment.Center
                ) {
                    AssetImage("img/threepin_plug.webp", modifier = Modifier.fillMaxSize(), contentScale = ContentScale.Fit)
                }
                Spacer(Modifier.height(24.dp))
                Text("नेट बॉक्स के साथ 3-पिन प्लग लगाना भी ज़रूरी है", fontSize = 20.sp, fontWeight = FontWeight.Bold, lineHeight = 28.sp, color = Pri)
                Spacer(Modifier.height(12.dp))
                // Warning box
                Box(
                    Modifier.fillMaxWidth().clip(RoundedCornerShape(8.dp)).background(WarnBg)
                        .padding(start = 12.dp, end = 12.dp, top = 16.dp, bottom = 12.dp)
                ) {
                    Row {
                        Box(Modifier.width(2.dp).height(24.dp).background(Warn))
                        Spacer(Modifier.width(8.dp))
                        Text("3-पिन के बिना सेट अप अधूरा रहेगा", fontSize = 16.sp, color = WarnTxt, lineHeight = 24.sp)
                    }
                }
                Spacer(Modifier.height(24.dp))
                // Checkbox: using same SVG checkbox as WebView
                Row(
                    Modifier.clickable { if (!threePinChecked) threePinChecked = true },
                    verticalAlignment = Alignment.Top, horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    AssetImage(
                        if (threePinChecked) "img/checkbox_checked.webp" else "img/checkbox_unchecked.webp",
                        modifier = Modifier.size(24.dp)
                    )
                    Text("मैंने 3-पिन प्लग रख लिया है", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Pri)
                }
            }
        }
    }

    // ══ Exit Dialog ══
    if (showExitDialog) {
        ExitDialog(
            onDismiss = { showExitDialog = false },
            onConfirmExit = { showExitDialog = false; onExitToHome() }
        )
    }

    // ══ Arrival Dialog ══
    if (showArrivalDialog) {
        Dialog(onDismissRequest = { }) {
            Column(
                modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(24.dp))
                    .background(Card).padding(horizontal = 24.dp, vertical = 32.dp)
            ) {
                Image(painterResource(R.drawable.house_graphic), null, modifier = Modifier.size(96.dp))
                Spacer(Modifier.height(24.dp))
                Text("क्या आप कस्टमर के घर पर हैं?", fontSize = 24.sp, fontWeight = FontWeight.Bold, lineHeight = 32.sp, color = Pri)
                Spacer(Modifier.height(24.dp))
                PrimaryCta("हाँ, पहुँच गया हूँ", onClick = { showArrivalDialog = false; onNext() })
            }
        }
    }
}
