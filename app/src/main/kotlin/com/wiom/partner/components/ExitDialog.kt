package com.wiom.partner.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.wiom.partner.theme.*

@Composable
fun ExitDialog(
    onDismiss: () -> Unit,
    onConfirmExit: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .background(Card)
                .padding(horizontal = 24.dp, vertical = 32.dp)
        ) {
            // Warning icon: 96×96, #FFE6CC bg, warning #FF8000
            Box(
                modifier = Modifier.size(96.dp).clip(CircleShape).background(WarnBg),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Rounded.Warning, null, tint = Warn, modifier = Modifier.size(48.dp))
            }
            Spacer(Modifier.height(24.dp))

            Text(
                "सेटअप पर काम जारी है!",
                fontSize = 24.sp, fontWeight = FontWeight.Bold, lineHeight = 32.sp, color = Pri
            )
            Spacer(Modifier.height(12.dp))

            Text(
                "क्या आप अभी भी इसे रोकना चाहते हैं?",
                fontSize = 16.sp, color = Pri, lineHeight = 24.sp
            )
            Spacer(Modifier.height(48.dp))

            // Primary: "नहीं" (stay)
            PrimaryCta("नहीं", onClick = onDismiss)
            Spacer(Modifier.height(12.dp))

            // Secondary: "हाँ, सेटअप रोकें" (exit)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(BrandLight)
                    .clickable { onConfirmExit() },
                contentAlignment = Alignment.Center
            ) {
                Text("हाँ, सेटअप रोकें", color = Brand, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}
