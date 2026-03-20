package com.wiom.partner.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wiom.partner.components.CtaArea
import com.wiom.partner.components.LightHeader
import com.wiom.partner.components.PrimaryCta
import com.wiom.partner.theme.*
import com.wiom.partner.util.AssetImage

@Composable
fun S31HappyCodeRating(
    onNext: () -> Unit,
    onClose: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize().background(Bg)
    ) {
        // LightHeader: close only, NO "मदद"
        LightHeader(onClose = onClose, showHelp = false)

        // Content: padding [40,16,16,16]
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 40.dp, bottom = 16.dp)
        ) {
            // Illustration: 240×240, centered, margin-bottom:24
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                AssetImage(
                    path = "img/happy_code_illust.webp",
                    contentDescription = "Happy Code Illustration",
                    modifier = Modifier.size(240.dp)
                )
            }

            Spacer(Modifier.height(24.dp))

            // Title
            Text(
                text = "कस्टमर से उनके नेट बॉक्स सेटअप के अनुभव को रेट करने के लिए कहें",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Pri,
                lineHeight = 32.sp
            )
        }

        // CTA
        CtaArea {
            PrimaryCta(
                text = "अब हैप्पी कोड डालें",
                onClick = onNext
            )
        }
    }
}
