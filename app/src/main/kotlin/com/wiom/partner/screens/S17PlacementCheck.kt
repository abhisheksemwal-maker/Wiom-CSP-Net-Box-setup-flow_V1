package com.wiom.partner.screens

import android.media.MediaPlayer
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.Canvas
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
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
fun S17PlacementCheck(
    onNext: () -> Unit,
    onClose: () -> Unit
) {
    val context = LocalContext.current
    var timerSeconds by remember { mutableIntStateOf(59) }
    var showCheckbox by remember { mutableStateOf(false) }
    var checked by remember { mutableStateOf(false) }

    // Play audio: netbox_placement.mp3 (10s)
    DisposableEffect(Unit) {
        val mp = try {
            val afd = context.assets.openFd("img/netbox_placement.mp3")
            MediaPlayer().apply {
                setDataSource(afd.fileDescriptor, afd.startOffset, afd.length)
                afd.close()
                prepare()
                start()
            }
        } catch (e: Exception) { null }
        onDispose { mp?.release() }
    }

    // Timer countdown from 59, checkbox appears after 12s
    LaunchedEffect(Unit) {
        val startTime = System.currentTimeMillis()
        while (timerSeconds > 0) {
            delay(1000)
            val elapsed = (System.currentTimeMillis() - startTime) / 1000
            timerSeconds = (59 - elapsed.toInt()).coerceAtLeast(0)
            if (elapsed >= 12 && !showCheckbox) {
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
        // Header: padding:4, close #352D42, flex:1, "मदद" fs:14 fw:600 tc:Brand
        LightHeader(onClose = onClose, showHelp = true)

        // Scrollable content
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {
            // Title area: padding:[8,16,16,16]
            Column(
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 16.dp)
            ) {
                // Title: fs:24 fw:700
                Text(
                    text = "पक्का करें कि नेट बॉक्स किसी सही जगह रखा है",
                    color = Pri,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 32.sp
                )
                Spacer(Modifier.height(4.dp))
                // Subtitle: fs:16 fw:400
                Text(
                    text = "फ्रिज, टीवी और मेटल के सामान जैसे अलमारी से दूर है",
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
                // Example images: placement_examples.png width:328 max-width:100% height:auto, margin-bottom:24
                AssetImage(
                    path = "img/placement_examples.png",
                    contentDescription = "Placement examples",
                    modifier = Modifier.fillMaxWidth(),
                    contentScale = androidx.compose.ui.layout.ContentScale.FillWidth
                )
                Spacer(Modifier.height(24.dp))

                // Timer state (center-aligned, gap:8)
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
                        // Hint: "इसमें 1-2 मिनट का समय लग सकता है" fs:14
                        Text(
                            text = "इसमें 1-2 मिनट का समय लग सकता है",
                            color = Pri,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Normal
                        )
                    }
                }

                // Checkbox state (hidden initially, appears after 12s)
                AnimatedVisibility(visible = showCheckbox, enter = fadeIn()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { if (!checked) checked = true },
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp) // gap:12
                    ) {
                        // Checkbox SVG: 24x24
                        Image(
                            painter = painterResource(
                                if (checked) R.drawable.checkbox_checked else R.drawable.checkbox_unchecked
                            ),
                            contentDescription = null,
                            modifier = Modifier.size(24.dp)
                        )
                        // "मैंने सही जगह पर रख दिया है" fs:16 fw:700
                        Text(
                            text = "मैंने सही जगह पर रख दिया है",
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

/** Canvas arc spinner — NOT CircularProgressIndicator. size in dp. */
@Composable
internal fun SpinnerArc(size: Int, color: androidx.compose.ui.graphics.Color = Brand) {
    var sweepAngle by remember { mutableFloatStateOf(0f) }
    LaunchedEffect(Unit) {
        while (true) {
            delay(16)
            sweepAngle = (sweepAngle + 6f) % 360f
        }
    }
    Canvas(modifier = Modifier.size(size.dp)) {
        drawArc(
            color = color,
            startAngle = sweepAngle,
            sweepAngle = 270f,
            useCenter = false,
            style = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round),
            topLeft = Offset(1.dp.toPx(), 1.dp.toPx()),
            size = Size(this.size.width - 2.dp.toPx(), this.size.height - 2.dp.toPx())
        )
    }
}
