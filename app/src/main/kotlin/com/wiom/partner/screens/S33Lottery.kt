package com.wiom.partner.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.wiom.partner.util.AssetImage

@Composable
fun S33Lottery(
    onReset: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // Full-screen image: width:100% height:100% object-fit:cover
        AssetImage(
            path = "img/lottery_screen.webp",
            contentDescription = "Lottery",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Tap target: position:absolute bottom:0, width:100% height:120
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .align(Alignment.BottomCenter)
                .clickable { onReset() }
        )
    }
}
