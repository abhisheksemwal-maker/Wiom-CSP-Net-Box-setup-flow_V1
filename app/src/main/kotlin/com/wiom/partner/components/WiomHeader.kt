package com.wiom.partner.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wiom.partner.theme.*

@Composable
fun DarkHeader(
    title: String,
    onBack: (() -> Unit)? = null,
    trailing: @Composable () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .background(Header)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (onBack != null) {
            Icon(
                Icons.Rounded.ArrowBack, contentDescription = "Back",
                tint = Bg, modifier = Modifier.size(24.dp).clickable { onBack() }
            )
            Spacer(Modifier.width(12.dp))
        }
        Text(
            text = title, color = Bg, fontSize = 16.sp, fontWeight = FontWeight.Bold,
            maxLines = 1, overflow = TextOverflow.Ellipsis, modifier = Modifier.weight(1f)
        )
        trailing()
    }
}

@Composable
fun LightHeader(
    onClose: () -> Unit,
    showHelp: Boolean = true,
    onHelp: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .background(Bg)
            .padding(horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.size(48.dp).clickable { onClose() },
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Rounded.Close, contentDescription = "Close", tint = BorderFocus, modifier = Modifier.size(24.dp))
        }
        Spacer(Modifier.weight(1f))
        if (showHelp) {
            Box(
                modifier = Modifier.padding(12.dp).clickable { onHelp() }
            ) {
                Text("मदद", color = Brand, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

@Composable
fun CameraHeader(
    title: String,
    onClose: () -> Unit,
    showGradient: Boolean = true
) {
    Column(
        modifier = Modifier.fillMaxWidth().then(
            if (showGradient) Modifier.background(
                brush = androidx.compose.ui.graphics.Brush.verticalGradient(
                    colors = listOf(Color.Black.copy(alpha = 0.6f), Color.Transparent)
                )
            ) else Modifier.background(Color.Transparent)
        )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.size(48.dp).clickable { onClose() },
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Rounded.Close, contentDescription = "Close", tint = Bg, modifier = Modifier.size(24.dp))
            }
        }
        Text(
            text = title, color = Bg, fontSize = 24.sp, fontWeight = FontWeight.Bold,
            lineHeight = 32.sp,
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 24.dp)
        )
    }
}
