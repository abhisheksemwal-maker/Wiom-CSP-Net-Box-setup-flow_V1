package com.wiom.partner.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ConfirmationNumber
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wiom.partner.theme.*

@Composable
fun CustomerDetailsBottomSheet(
    visible: Boolean,
    onDismiss: () -> Unit
) {
    // Full-screen scrim + bottom sheet
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF160A21).copy(alpha = 0.5f)) // rgba(22,16,33,0.5)
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) { onDismiss() },
            contentAlignment = Alignment.BottomCenter
        ) {
            AnimatedVisibility(
                visible = visible,
                enter = slideInVertically(initialOffsetY = { it }),
                exit = slideOutVertically(targetOffsetY = { it })
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.7f) // max-height:70vh
                        .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                        .background(Bg) // bg:#FAF9FC
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ) { /* block scrim tap */ }
                ) {
                    // Handle: 120x4, bg:#D7D3E0, r:4, padding:[12 top, 24 bottom]
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 12.dp, bottom = 24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .width(120.dp)
                                .height(4.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .background(BorderDivider) // #D7D3E0
                        )
                    }

                    // Title: "कस्टमर डिटेल्स" fs:24 fw:700 lh:32, padding:[8,16,8,16]
                    Text(
                        text = "कस्टमर डिटेल्स",
                        color = Pri,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        lineHeight = 32.sp,
                        modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 8.dp)
                    )

                    // Content: padding:[24,16,48,16], gap:16
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .verticalScroll(rememberScrollState())
                            .padding(start = 16.dp, end = 16.dp, top = 24.dp, bottom = 48.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // Customer Info Card: stroke:#E8E4F0/1dp r:12 p:[16,12], gap:12
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(1.dp, N200, RoundedCornerShape(12.dp)) // #E8E4F0
                                .clip(RoundedCornerShape(12.dp))
                                .padding(horizontal = 12.dp, vertical = 16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            // Name row: person icon + name
                            DetailRow(
                                icon = Icons.Rounded.Person,
                                text = "Himanshu Singh",
                                fontSize = 16,
                                fontWeight = FontWeight.Bold
                            )
                            // Address row: location_on + address
                            DetailRow(
                                icon = Icons.Rounded.LocationOn,
                                text = "H.No 123, Sector 45, Noida, UP 201301",
                                fontSize = 14,
                                fontWeight = FontWeight.Normal,
                                lineHeight = 20
                            )
                            // Family number row: confirmation_number + number
                            DetailRow(
                                icon = Icons.Rounded.ConfirmationNumber,
                                text = "फैमिली नंबर : 98765 43210",
                                fontSize = 14,
                                fontWeight = FontWeight.Normal
                            )
                        }

                        // Aadhaar Card: stroke:#E8E4F0/1dp r:12 p:[16,12], gap:8
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(1.dp, N200, RoundedCornerShape(12.dp))
                                .clip(RoundedCornerShape(12.dp))
                                .padding(horizontal = 12.dp, vertical = 16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            // "आधार कार्ड" fs:14 fw:600
                            Text(
                                text = "आधार कार्ड",
                                color = Pri,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            // Row gap:8: 2 image slots (flex:1, height:80, r:10, bg:#D9D9D9)
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(80.dp)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(Color(0xFFD9D9D9))
                                )
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(80.dp)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(Color(0xFFD9D9D9))
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun DetailRow(
    icon: ImageVector,
    text: String,
    fontSize: Int,
    fontWeight: FontWeight,
    lineHeight: Int? = null
) {
    Row(
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Icon(
            icon,
            contentDescription = null,
            tint = Hint, // #A7A1B2
            modifier = Modifier.size(24.dp)
        )
        Text(
            text = text,
            color = Pri,
            fontSize = fontSize.sp,
            fontWeight = fontWeight,
            lineHeight = (lineHeight ?: (fontSize + 4)).sp
        )
    }
}
