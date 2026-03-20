package com.wiom.partner.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Backspace
import androidx.compose.material.icons.rounded.KeyboardReturn
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wiom.partner.components.LightHeader
import com.wiom.partner.theme.*
import com.wiom.partner.util.AssetImage
import kotlinx.coroutines.delay

@Composable
fun S32OtpEntry(
    onCodeComplete: () -> Unit,
    onClose: () -> Unit
) {
    var code by remember { mutableStateOf("") }
    var navigated by remember { mutableStateOf(false) }

    // Auto-navigate after 4 digits with 500ms delay
    LaunchedEffect(code) {
        if (code.length == 4 && !navigated) {
            navigated = true
            delay(500L)
            onCodeComplete()
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().background(Bg)
    ) {
        LightHeader(onClose = onClose)

        // Title: padding [8,16,16,16]
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 16.dp)
        ) {
            Text(
                text = "लॉटरी पाने के लिए हैप्पी कोड डालें",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Pri
            )

            Spacer(Modifier.height(24.dp))

            // 4 OTP boxes: flex row, gap:8, each flex:1 h:48 r:12
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                repeat(4) { index ->
                    val digit = code.getOrNull(index)
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .border(1.dp, N200, RoundedCornerShape(12.dp))
                            .background(Bg),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = digit?.toString() ?: "",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Pri,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }

        // Pink hint bar: bg:#F9DFEE r:12 p:8, gap:8
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(PinkHint)
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Bulb icon: 17×24
            AssetImage(
                path = "img/ic_bulb_pink.webp",
                contentDescription = "Hint",
                modifier = Modifier.width(17.dp).height(24.dp)
            )

            // Hint text: fs:14 fw:400 tc:#191919
            Text(
                text = "कस्टमर से उनकी ऐप में आए हैप्पी कोड को मांगे",
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
                color = Color(0xFF191919),
                lineHeight = 19.sp
            )
        }

        Spacer(Modifier.weight(1f))

        // Numeric keypad: bg:#161021 padding:[16,31,16,31]
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Pri)
                .padding(start = 31.dp, end = 31.dp, top = 16.dp, bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            // Row 1: 1 2 3
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                listOf("1", "2", "3").forEach { key ->
                    KeypadKey(
                        label = key,
                        modifier = Modifier.weight(1f),
                        onClick = { if (code.length < 4) code += key }
                    )
                }
            }
            // Row 2: 4 5 6
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                listOf("4", "5", "6").forEach { key ->
                    KeypadKey(
                        label = key,
                        modifier = Modifier.weight(1f),
                        onClick = { if (code.length < 4) code += key }
                    )
                }
            }
            // Row 3: 7 8 9
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                listOf("7", "8", "9").forEach { key ->
                    KeypadKey(
                        label = key,
                        modifier = Modifier.weight(1f),
                        onClick = { if (code.length < 4) code += key }
                    )
                }
            }
            // Row 4: backspace / 0 / enter
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                // Backspace: bg:#FFD3CC, icon backspace tc:#E01E00
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(41.dp)
                        .clip(CircleShape)
                        .background(BackspaceKeyBg)
                        .clickable { if (code.isNotEmpty()) code = code.dropLast(1) },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Rounded.Backspace,
                        contentDescription = "Delete",
                        tint = BackspaceKeyFg,
                        modifier = Modifier.size(24.dp)
                    )
                }

                // 0
                KeypadKey(
                    label = "0",
                    modifier = Modifier.weight(1f),
                    onClick = { if (code.length < 4) code += "0" }
                )

                // Enter/Submit: bg:Brand, icon keyboard_return tc:#FAF9FC
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(41.dp)
                        .clip(CircleShape)
                        .background(Brand)
                        .clickable {
                            if (code.length == 4 && !navigated) {
                                navigated = true
                                // onCodeComplete will be called by LaunchedEffect
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Rounded.KeyboardReturn,
                        contentDescription = "Submit",
                        tint = Bg,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun KeypadKey(
    label: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .height(41.dp)
            .clip(CircleShape)
            .background(Color.White.copy(alpha = 0.1f))
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            fontSize = 21.sp,
            fontWeight = FontWeight.Normal,
            color = Bg
        )
    }
}
