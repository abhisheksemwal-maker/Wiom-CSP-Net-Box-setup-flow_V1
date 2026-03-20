package com.wiom.partner.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wiom.partner.theme.*

@Composable
fun PrimaryCta(
    text: String,
    onClick: () -> Unit,
    enabled: Boolean = true,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(if (enabled) Brand else CtaDis)
            .then(if (enabled) Modifier.clickable { onClick() } else Modifier),
        contentAlignment = Alignment.Center
    ) {
        Text(text, color = Bg, fontSize = 16.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun GhostCta(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Bg)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(text, color = Brand, fontSize = 16.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun CtaArea(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(Bg)
            .padding(horizontal = 16.dp, vertical = 16.dp)
    ) {
        content()
    }
}
