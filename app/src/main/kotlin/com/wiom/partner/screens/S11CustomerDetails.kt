package com.wiom.partner.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Download
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import com.wiom.partner.state.FlowViewModel
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wiom.partner.R
import com.wiom.partner.theme.*
import com.wiom.partner.components.*

@Composable
fun S11CustomerDetails(vm: FlowViewModel? = null, onNext: () -> Unit, onClose: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize().background(Bg)) {
        LightHeader(onClose = onClose)
        Text(
            "कस्टमर डिटेल्स",
            fontSize = 24.sp, fontWeight = FontWeight.Bold, lineHeight = 32.sp, color = Pri,
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 16.dp)
        )
        Column(modifier = Modifier.weight(1f).padding(horizontal = 16.dp)) {
            // Customer Info card
            Column(
                Modifier.fillMaxWidth().border(1.dp, Border, RoundedCornerShape(12.dp))
                    .background(Bg, RoundedCornerShape(12.dp)).padding(horizontal = 12.dp, vertical = 16.dp)
            ) {
                Row(Modifier.fillMaxWidth().padding(bottom = 12.dp), verticalAlignment = Alignment.CenterVertically) {
                    Image(painterResource(R.drawable.ic_user), null, modifier = Modifier.size(24.dp))
                    Spacer(Modifier.width(8.dp))
                    Text("Himanshu Singh", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Pri)
                }
                Row(Modifier.fillMaxWidth().padding(bottom = 12.dp), verticalAlignment = Alignment.Top) {
                    Image(painterResource(R.drawable.ic_pin), null, modifier = Modifier.size(24.dp))
                    Spacer(Modifier.width(8.dp))
                    Text("G 69, Mata mandir, 3rd street, Ghaziabad Uttar Pradesh", fontSize = 14.sp, color = Pri, lineHeight = 20.sp, modifier = Modifier.weight(1f))
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(painterResource(R.drawable.ic_ticket), null, modifier = Modifier.size(24.dp))
                    Spacer(Modifier.width(8.dp))
                    Text("फैमिली नंबर : 98765 43210", fontSize = 14.sp, color = Pri)
                }
            }
            Spacer(Modifier.height(16.dp))
            // Aadhaar card
            Column(
                Modifier.fillMaxWidth().border(1.dp, Border, RoundedCornerShape(12.dp))
                    .background(Bg, RoundedCornerShape(12.dp)).padding(horizontal = 12.dp, vertical = 16.dp)
            ) {
                Row(Modifier.fillMaxWidth().padding(bottom = 8.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text("आधार कार्ड", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Pri)
                    Box(Modifier.size(32.dp).clip(CircleShape).background(N200), contentAlignment = Alignment.Center) {
                        Icon(Icons.Rounded.Download, null, tint = BorderFocus, modifier = Modifier.size(20.dp))
                    }
                }
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    // Front Aadhaar
                    Box(Modifier.weight(1f).aspectRatio(148f / 80f).clip(RoundedCornerShape(10.dp)).background(Color(0xFFD9D9D9))) {
                        vm?.aadhaarState?.frontData?.let { bmp ->
                            Image(bitmap = bmp.asImageBitmap(), contentDescription = "Aadhaar Front",
                                modifier = Modifier.fillMaxSize(), contentScale = ContentScale.Crop)
                        }
                    }
                    // Back Aadhaar
                    Box(Modifier.weight(1f).aspectRatio(148f / 80f).clip(RoundedCornerShape(10.dp)).background(Color(0xFFD9D9D9))) {
                        vm?.aadhaarState?.backData?.let { bmp ->
                            Image(bitmap = bmp.asImageBitmap(), contentDescription = "Aadhaar Back",
                                modifier = Modifier.fillMaxSize(), contentScale = ContentScale.Crop)
                        }
                    }
                }
            }
        }
        CtaArea { PrimaryCta("ISP अकाउंट बनायें", onClick = onNext) }
    }
}
