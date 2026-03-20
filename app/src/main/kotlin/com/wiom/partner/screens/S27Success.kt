package com.wiom.partner.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wiom.partner.components.CtaArea
import com.wiom.partner.components.LightHeader
import com.wiom.partner.components.PrimaryCta
import com.wiom.partner.theme.*
import com.wiom.partner.util.AssetImage

@Composable
fun S27Success(
    onNext: () -> Unit,
    onClose: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize().background(Bg)
    ) {
        LightHeader(onClose = onClose)

        // Centered content
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Green circle with tick
            Box(
                modifier = Modifier
                    .size(96.dp)
                    .clip(CircleShape)
                    .background(GreenBg),
                contentAlignment = Alignment.Center
            ) {
                AssetImage(
                    path = "img/tick_green.webp",
                    contentDescription = "Success",
                    modifier = Modifier.size(48.dp)
                )
            }

            Spacer(Modifier.height(16.dp))

            Text(
                text = "नेट बॉक्स चालू हो गया है",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                lineHeight = 24.sp,
                color = Pri,
                textAlign = TextAlign.Center
            )
        }

        // CTA
        CtaArea {
            PrimaryCta(
                text = "ऑप्टिकल पॉवर चेक करें",
                onClick = onNext
            )
        }
    }
}
