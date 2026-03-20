package com.wiom.partner.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wiom.partner.theme.*
import com.wiom.partner.components.*
import com.wiom.partner.state.FlowViewModel

@Composable
fun S06SelfieReview(
    vm: FlowViewModel,
    onNext: () -> Unit,
    onRetake: () -> Unit,
    onClose: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize().background(Bg)) {
        // Header
        LightHeader(onClose = onClose)

        // Title ABOVE photo
        Text(
            "आप तो बिलकुल रॉकस्टार लग रहे हैं",
            fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Pri,
            lineHeight = 32.sp,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )

        // Photo preview
        Column(modifier = Modifier.weight(1f).padding(horizontal = 16.dp)) {
            Box(
                modifier = Modifier.fillMaxWidth().height(456.dp),
                contentAlignment = Alignment.TopEnd
            ) {
                // Display captured selfie or grey placeholder
                val bitmap = vm.selfieData
                if (bitmap != null) {
                    Image(
                        bitmap = bitmap.asImageBitmap(),
                        contentDescription = "Selfie",
                        modifier = Modifier.fillMaxSize().clip(RoundedCornerShape(10.dp)),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Box(
                        modifier = Modifier.fillMaxSize()
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(0xFFD9D9D9))
                    )
                }

                // Retake X button overlaid on photo
                Box(
                    modifier = Modifier.padding(12.dp).size(36.dp)
                        .clip(CircleShape).background(Color.Black.copy(alpha = 0.5f))
                        .clickable { onRetake() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Rounded.Close, "Retake", tint = Color.White, modifier = Modifier.size(20.dp))
                }
            }
        }

        // CTA
        CtaArea {
            PrimaryCta("कस्टमर के आधार कार्ड की फोटो लें", onClick = onNext)
        }
    }
}
