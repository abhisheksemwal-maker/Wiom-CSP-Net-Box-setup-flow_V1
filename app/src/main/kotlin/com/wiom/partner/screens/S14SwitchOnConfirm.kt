package com.wiom.partner.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wiom.partner.R
import com.wiom.partner.theme.*
import com.wiom.partner.components.*
import com.wiom.partner.util.AssetImage
import kotlinx.coroutines.delay

@Composable
fun S14SwitchOnConfirm(onNext: () -> Unit, onClose: () -> Unit) {
    var checked by remember { mutableStateOf(false) }

    LaunchedEffect(checked) {
        if (checked) { delay(600); onNext() }
    }

    Column(modifier = Modifier.fillMaxSize().background(Bg)) {
        // Header: close + मदद
        LightHeader(onClose = onClose)

        // Title
        Text(
            "व्योम नेट बॉक्स को स्विच ऑन करें",
            fontSize = 24.sp, fontWeight = FontWeight.Bold, lineHeight = 32.sp, color = Pri,
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 16.dp)
        )

        // Body
        Column(modifier = Modifier.weight(1f).padding(horizontal = 16.dp)) {
            // Router image: full width, r:16
            Box(Modifier.fillMaxWidth().clip(RoundedCornerShape(16.dp)).background(N200)) {
                AssetImage("img/netbox_power_masked.png", modifier = Modifier.fillMaxWidth(), contentScale = ContentScale.FillWidth)
            }
            Spacer(Modifier.height(24.dp))

            // Checkbox: same Figma SVG icons + exact text
            Row(
                modifier = Modifier.clickable { if (!checked) checked = true },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Image(
                    painterResource(if (checked) R.drawable.checkbox_checked else R.drawable.checkbox_unchecked),
                    null, modifier = Modifier.size(24.dp)
                )
                Text("नेट बॉक्स ऑन कर दिया है", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Pri)
            }
        }
    }
}
