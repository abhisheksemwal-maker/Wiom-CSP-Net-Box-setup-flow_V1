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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.wiom.partner.components.CtaArea
import com.wiom.partner.components.LightHeader
import com.wiom.partner.components.PrimaryCta
import com.wiom.partner.state.FlowViewModel
import com.wiom.partner.theme.*

@Composable
fun S19NetboxReview(
    vm: FlowViewModel,
    onNext: () -> Unit,
    onRetake: () -> Unit,
    onClose: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize().background(Bg)
    ) {
        // Header: padding:4, close #352D42, "मदद" fs:14 fw:600 tc:Brand
        LightHeader(onClose = onClose, showHelp = true)

        // Body: padding:0 16
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp)
        ) {
            // Photo: 100% width x 502dp, r:10, bg:#D9D9D9
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(502.dp),
                contentAlignment = Alignment.TopEnd
            ) {
                // Photo or placeholder
                val bitmap = vm.netboxPhotoData
                if (bitmap != null) {
                    Image(
                        bitmap = bitmap.asImageBitmap(),
                        contentDescription = "Net Box photo",
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(10.dp)),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(0xFFD9D9D9))
                    )
                }

                // Retake X: absolute top:12 right:12, 36x36 circle bg:rgba(0,0,0,0.5), close icon 20sp #FAF9FC
                Box(
                    modifier = Modifier
                        .padding(top = 12.dp, end = 12.dp)
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(Color.Black.copy(alpha = 0.5f))
                        .clickable { onRetake() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Rounded.Close,
                        contentDescription = "Retake",
                        tint = Bg, // #FAF9FC
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }

        // CTA: padding:16, "अब 3 पिन वाले प्लग की फोटो लें" bg:Brand
        CtaArea {
            PrimaryCta(
                text = "अब 3 पिन वाले प्लग की फोटो लें",
                onClick = onNext
            )
        }
    }
}
