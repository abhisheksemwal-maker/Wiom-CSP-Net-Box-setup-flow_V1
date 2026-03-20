package com.wiom.partner.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowForward
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wiom.partner.theme.*
import com.wiom.partner.util.AssetImage

@Composable
fun S01TaskList(onTaskTap: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize().background(Bg)) {
        // ── Header: 56dp, #443152 bg, shadow ──
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .shadow(3.dp)
                .background(Header)
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Wiom logo 32×32
            AssetImage(
                path = "img/wiom_logo.webp",
                modifier = Modifier
                    .size(32.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(Color(0xFF2F1940))
                    .border(1.dp, Border, RoundedCornerShape(4.dp))
            )
            Spacer(Modifier.width(12.dp))
            // Name: 16sp Bold, ellipsis, flex:1
            Text(
                "Rohit Kumar Chaurasia",
                color = Color(0xFFFAF9FC), fontSize = 16.sp, fontWeight = FontWeight.Bold,
                maxLines = 1, overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f)
            )
            // Rating pill: silver stroke animation border → inner bg HeaderLight
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(13.dp))
                    .background(HeaderLight)
                    .padding(horizontal = 8.dp, vertical = 1.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    // Gold star
                    Text("★", color = Color(0xFFF2E983), fontSize = 14.sp)
                    Text("4.8", color = Color(0xFFFAF9FC), fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                }
            }
            Spacer(Modifier.width(12.dp))
            Icon(Icons.Rounded.Menu, "Menu", tint = Color(0xFFFAF9FC), modifier = Modifier.size(24.dp))
        }

        // ── Tabs: 48dp, #443152 bg ──
        Row(modifier = Modifier.fillMaxWidth().height(48.dp).background(Header)) {
            // Active tab
            Box(modifier = Modifier.weight(1f).fillMaxHeight(), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxHeight()) {
                    Spacer(Modifier.weight(1f))
                    Text("मेरे काम", color = Color(0xFFFAF9FC), fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                    Spacer(Modifier.weight(1f))
                    Box(Modifier.fillMaxWidth().height(4.dp).background(Border))
                }
            }
            // Inactive tab
            Box(modifier = Modifier.weight(1f).fillMaxHeight(), contentAlignment = Alignment.Center) {
                Text("मेरी कमाई", color = Color(0xFFFAF9FC), fontSize = 14.sp)
            }
        }

        // ── Body: scrollable, 16dp padding ──
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Task card: bg BrandLight, border Brand, r:16, padding 24, flex row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(BrandLight)
                    .border(1.dp, Brand, RoundedCornerShape(16.dp))
                    .clickable { onTaskTap() }
                    .padding(24.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    // Big count: 48sp Bold
                    Text("02", fontSize = 48.sp, fontWeight = FontWeight.Bold, color = Pri, lineHeight = 64.sp)
                    Spacer(Modifier.height(16.dp))
                    // Overlapping avatars
                    Box {
                        AssetImage(
                            path = "img/avatar1.webp",
                            modifier = Modifier.size(32.dp).clip(CircleShape).border(2.dp, Card, CircleShape)
                        )
                        AssetImage(
                            path = "img/avatar2.webp",
                            modifier = Modifier.offset(x = 24.dp).size(32.dp).clip(CircleShape).border(2.dp, Card, CircleShape)
                        )
                    }
                    Spacer(Modifier.height(16.dp))
                    Text(
                        "कस्टमर प्रतीक्षा कर रहे हैं",
                        fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Pri, lineHeight = 28.sp
                    )
                }
                Spacer(Modifier.width(16.dp))
                // Arrow circle: 72×72, Brand bg, shadow
                Box(
                    modifier = Modifier
                        .size(72.dp)
                        .shadow(12.dp, CircleShape, spotColor = Brand.copy(alpha = 0.3f))
                        .clip(CircleShape)
                        .background(Brand),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Rounded.ArrowForward, "Go", tint = Color.White, modifier = Modifier.size(36.dp))
                }
            }
        }
    }
}
