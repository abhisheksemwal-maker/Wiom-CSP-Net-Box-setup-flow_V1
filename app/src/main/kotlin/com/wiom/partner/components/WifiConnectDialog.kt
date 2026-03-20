package com.wiom.partner.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.Wifi
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog

// Exact match of WebView ov-wifi: Android Material 3 style WiFi connect popup
// Width: calc(100% - 48px), max-width:312px, r:28, shadow 0 8px 32px rgba(0,0,0,0.25)

@Composable
fun WifiConnectDialog(
    onDismiss: () -> Unit,
    onConnect: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .widthIn(max = 312.dp)
                .fillMaxWidth()
                .shadow(32.dp, RoundedCornerShape(28.dp), spotColor = Color.Black.copy(alpha = 0.25f))
                .clip(RoundedCornerShape(28.dp))
                .background(Color.White)
        ) {
            // Icon: 48×48 green circle with wifi icon
            Box(
                modifier = Modifier.fillMaxWidth().padding(start = 24.dp, end = 24.dp, top = 24.dp, bottom = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier.size(48.dp).clip(CircleShape).background(Color(0xFFE8F5E9)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Rounded.Wifi, null, tint = Color(0xFF2E7D32), modifier = Modifier.size(24.dp))
                }
            }

            // Title: center, fs:18 fw:600 tc:#1C1B1F
            Text(
                "WiFi से कनेक्ट करें",
                fontSize = 18.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF1C1B1F),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp)
            )

            // Body: center, fs:14 fw:400 tc:#49454F
            Text(
                "नेट बॉक्स के WiFi नेटवर्क से कनेक्ट करें ताकि सेटअप पूरा हो सके",
                fontSize = 14.sp, color = Color(0xFF49454F), lineHeight = 20.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth().padding(start = 24.dp, end = 24.dp, top = 12.dp)
            )

            // Network name card: bg:#F5F5F5 r:12 p:[12,16] gap:12
            Row(
                modifier = Modifier.fillMaxWidth()
                    .padding(start = 24.dp, end = 24.dp, top = 16.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFF5F5F5))
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Rounded.Wifi, null, tint = Color(0xFF49454F), modifier = Modifier.size(20.dp))
                Spacer(Modifier.width(12.dp))
                Text("Wiom_GX412365", fontSize = 14.sp, fontWeight = FontWeight.Medium, color = Color(0xFF1C1B1F))
                Spacer(Modifier.weight(1f))
                Icon(Icons.Rounded.Lock, null, tint = Color(0xFF2E7D32), modifier = Modifier.size(18.dp))
            }

            // Buttons: right-aligned, gap:8
            Row(
                modifier = Modifier.fillMaxWidth().padding(start = 24.dp, end = 24.dp, top = 8.dp, bottom = 24.dp),
                horizontalArrangement = Arrangement.End
            ) {
                // Cancel
                Box(
                    modifier = Modifier.clip(RoundedCornerShape(20.dp))
                        .clickable { onDismiss() }
                        .padding(horizontal = 24.dp, vertical = 10.dp)
                ) {
                    Text("रद्द करें", fontSize = 14.sp, fontWeight = FontWeight.Medium, color = Color(0xFF49454F))
                }
                Spacer(Modifier.width(8.dp))
                // Connect
                Box(
                    modifier = Modifier.clip(RoundedCornerShape(20.dp))
                        .background(Color(0xFF2E7D32))
                        .clickable { onConnect() }
                        .padding(horizontal = 24.dp, vertical = 10.dp)
                ) {
                    Text("कनेक्ट करें", fontSize = 14.sp, fontWeight = FontWeight.Medium, color = Color.White)
                }
            }
        }
    }
}
