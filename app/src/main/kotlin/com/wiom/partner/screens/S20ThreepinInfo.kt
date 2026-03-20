package com.wiom.partner.screens

import android.media.MediaPlayer
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wiom.partner.R
import com.wiom.partner.components.LightHeader
import com.wiom.partner.theme.*
import com.wiom.partner.util.AssetImage
import kotlinx.coroutines.delay

@Composable
fun S20ThreepinInfo(
    onNext: () -> Unit,
    onClose: () -> Unit
) {
    val context = LocalContext.current
    var timerSeconds by remember { mutableIntStateOf(59) }
    var showCheckbox by remember { mutableStateOf(false) }
    var checked by remember { mutableStateOf(false) }

    // Play audio: threepin_instructions.mp3 (2s)
    DisposableEffect(Unit) {
        val mp = try {
            val afd = context.assets.openFd("img/threepin_instructions.mp3")
            MediaPlayer().apply {
                setDataSource(afd.fileDescriptor, afd.startOffset, afd.length)
                afd.close()
                prepare()
                start()
            }
        } catch (e: Exception) { null }
        onDispose { mp?.release() }
    }

    // Timer countdown from 59, checkbox appears after 4.5s
    LaunchedEffect(Unit) {
        val startTime = System.currentTimeMillis()
        while (timerSeconds > 0) {
            delay(1000)
            val elapsed = (System.currentTimeMillis() - startTime) / 1000
            timerSeconds = (59 - elapsed.toInt()).coerceAtLeast(0)
            if ((System.currentTimeMillis() - startTime) >= 4500 && !showCheckbox) {
                showCheckbox = true
            }
        }
    }

    // On checkbox checked: 450ms delay → onNext
    LaunchedEffect(checked) {
        if (checked) {
            delay(450)
            onNext()
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().background(Bg)
    ) {
        // Header: padding:4, close #352D42, "मदद" fs:14 fw:600 tc:Brand
        LightHeader(onClose = onClose, showHelp = true)

        // Scrollable content
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {
            // Title area (bg:#FAF9FC): padding:[8,16,16,16], gap:4
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Bg)
                    .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 16.dp)
            ) {
                // Title: fs:24 fw:700
                Text(
                    text = "फोटो में व्योम वाला 3 पिन साफ दिखना चाहिए",
                    color = Pri,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 32.sp
                )
                Spacer(Modifier.height(4.dp))
                // Subtitle: fs:16 fw:400
                Text(
                    text = "नेट बॉक्स का एडाप्टर हमेशा व्योम वाले 3 पिन में ही लगाएं, फोटो में 3 पिन साफ दिखना चाहिए",
                    color = Pri,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal,
                    lineHeight = 24.sp
                )
            }

            // Body: padding:0 16
            Column(
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                // Example images: threepin_examples.png width:328 max-width:100% height:auto
                AssetImage(
                    path = "img/threepin_examples.png",
                    contentDescription = "3-pin examples",
                    modifier = Modifier.fillMaxWidth(),
                    contentScale = androidx.compose.ui.layout.ContentScale.FillWidth
                )
                Spacer(Modifier.height(24.dp))

                // Timer (center-aligned, gap:8)
                if (!showCheckbox) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            // 20px spinner (Canvas arc)
                            SpinnerArc(size = 20)

                            // "00:59" fs:24 fw:700
                            Text(
                                text = String.format("00:%02d", timerSeconds),
                                color = Pri,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(Modifier.height(8.dp))
                        // Hint text: fs:14
                        Text(
                            text = "इसमें 1-2 मिनट का समय लग सकता है",
                            color = Pri,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Normal
                        )
                    }
                }

                // Checkbox: "व्योम वाला 3 पिन सही से लगा दिया है" fs:16 fw:700
                AnimatedVisibility(visible = showCheckbox, enter = fadeIn()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { if (!checked) checked = true },
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Checkbox SVG: 24x24
                        Image(
                            painter = painterResource(
                                if (checked) R.drawable.checkbox_checked else R.drawable.checkbox_unchecked
                            ),
                            contentDescription = null,
                            modifier = Modifier.size(24.dp)
                        )
                        // Label: fs:16 fw:700
                        Text(
                            text = "व्योम वाला 3 पिन सही से लगा दिया है",
                            color = Pri,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(Modifier.height(24.dp))
        }
    }
}
