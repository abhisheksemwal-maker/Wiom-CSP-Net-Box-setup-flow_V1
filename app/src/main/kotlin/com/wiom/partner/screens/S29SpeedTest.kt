package com.wiom.partner.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.*
import com.wiom.partner.components.LightHeader
import com.wiom.partner.theme.*
import kotlinx.coroutines.delay

@Composable
fun S29SpeedTest(
    onNext: () -> Unit,
    onClose: () -> Unit
) {
    val composition by rememberLottieComposition(LottieCompositionSpec.Asset("img/speedmeter.json"))
    val progress by animateLottieCompositionAsState(
        composition,
        iterations = 1,
        isPlaying = true
    )
    var speedMbps by remember { mutableIntStateOf(0) }
    var testDone by remember { mutableStateOf(false) }
    var showSheet by remember { mutableStateOf(false) }
    var selectedCard by remember { mutableStateOf<String?>(null) }

    // Sync speed text to progress
    LaunchedEffect(progress) {
        speedMbps = (progress * 89).toInt().coerceIn(0, 89)
        if (progress >= 1f && !testDone) {
            testDone = true
            showSheet = true
        }
    }

    // Handle card selection with 400ms highlight then navigate
    LaunchedEffect(selectedCard) {
        if (selectedCard != null) {
            delay(400L)
            onNext()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize().background(Bg)
        ) {
            LightHeader(onClose = onClose)

            // Content
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Lottie animation container
                Box(
                    modifier = Modifier.size(240.dp),
                    contentAlignment = Alignment.Center
                ) {
                    LottieAnimation(
                        composition = composition,
                        progress = { progress },
                        modifier = Modifier.fillMaxSize()
                    )
                }

                Spacer(Modifier.height(24.dp))

                // Speed text
                Text(
                    text = "$speedMbps Mbps",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Pos,
                    textAlign = TextAlign.Center
                )
            }
        }

        // Scrim overlay: rgba(22,16,33,0.5)
        AnimatedVisibility(
            visible = showSheet,
            enter = fadeIn(),
            modifier = Modifier.fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0x80161021))
                    .clickable { showSheet = false },
                contentAlignment = Alignment.BottomCenter
            ) {}
        }

        // Bottom sheet
        AnimatedVisibility(
            visible = showSheet,
            enter = slideInVertically(initialOffsetY = { it }),
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                    .background(Color.White),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Handle bar
                Box(
                    modifier = Modifier
                        .padding(top = 12.dp, bottom = 24.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .width(120.dp)
                            .height(4.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(BorderDivider)
                    )
                }

                // Title
                Text(
                    text = "क्या स्पीड सही आ रही है?",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Pri,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 8.dp)
                )

                // Cards row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, top = 24.dp, bottom = 48.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // "नहीं" card
                    val isNoSelected = selectedCard == "no"
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(16.dp))
                            .background(if (isNoSelected) BrandSec else Bg)
                            .border(1.dp, Brand, RoundedCornerShape(16.dp))
                            .clickable {
                                if (selectedCard == null) selectedCard = "no"
                            }
                            .padding(vertical = 24.dp, horizontal = 12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "नहीं",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Brand
                        )
                    }

                    // "हाँ" card
                    val isYesSelected = selectedCard == "yes"
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(16.dp))
                            .background(if (isYesSelected) BrandSec else Bg)
                            .border(1.dp, Brand, RoundedCornerShape(16.dp))
                            .clickable {
                                if (selectedCard == null) selectedCard = "yes"
                            }
                            .padding(vertical = 24.dp, horizontal = 12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "हाँ",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Brand
                        )
                    }
                }
            }
        }
    }
}
