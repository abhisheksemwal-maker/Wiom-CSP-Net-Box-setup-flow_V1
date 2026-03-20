package com.wiom.partner.screens

import android.media.MediaPlayer
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.Canvas
import com.wiom.partner.R
import com.wiom.partner.components.LightHeader
import com.wiom.partner.theme.*
import com.wiom.partner.util.AssetImage
import androidx.compose.ui.layout.ContentScale
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.LinearEasing
import kotlinx.coroutines.delay

@Composable
fun S23WiringCheck(
    onNext: () -> Unit,
    onClose: () -> Unit
) {
    val context = LocalContext.current

    // States: timer phase → checkbox phase
    var timerSeconds by remember { mutableStateOf(59) }
    var showTimer by remember { mutableStateOf(true) }
    var showCheckbox by remember { mutableStateOf(false) }
    var checked by remember { mutableStateOf(false) }

    // Spinner rotation animation
    val infiniteTransition = rememberInfiniteTransition(label = "spinner")
    val spinnerAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1000, easing = LinearEasing)
        ),
        label = "spinnerRotation"
    )

    // Play audio: wiring_instructions.mp3 (12.3s), checkbox after 13.5s
    DisposableEffect(Unit) {
        val mp = try {
            val afd = context.assets.openFd("img/wiring_instructions.mp3")
            MediaPlayer().apply {
                setDataSource(afd.fileDescriptor, afd.startOffset, afd.length)
                afd.close()
                prepare()
                start()
            }
        } catch (e: Exception) { null }
        onDispose { mp?.release() }
    }

    // Timer countdown: real seconds (59, 58, 57...), checkbox after audio (~13.5s)
    LaunchedEffect(Unit) {
        val startTime = System.currentTimeMillis()
        while (timerSeconds > 0) {
            delay(1000)
            timerSeconds--
            val elapsed = System.currentTimeMillis() - startTime
            if (elapsed >= 13500 && !showCheckbox) {
                showTimer = false
                showCheckbox = true
            }
        }
        if (!showCheckbox) {
            showTimer = false
            showCheckbox = true
        }
    }

    // On checkbox checked → 450ms delay → onNext
    LaunchedEffect(checked) {
        if (checked) {
            delay(450)
            onNext()
        }
    }

    Column(modifier = Modifier.fillMaxSize().background(Bg)) {
        // Header: padding:4, close #352D42, "मदद" fs:14 fw:600 tc:Brand
        LightHeader(onClose = onClose, showHelp = true)

        // Body padding:[8,16,16,16]
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 16.dp)
        ) {
            // Title fs:24 fw:700, margin-bottom:24
            Text(
                text = "फोटो लेने से पहले पक्का करें कि वायरिंग सही से की है",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Pri,
                lineHeight = 32.sp
            )

            Spacer(Modifier.height(24.dp))

            // wiring_examples.png: width:100% r:10 margin-bottom:24
            AssetImage(
                path = "img/wiring_examples.png",
                contentDescription = "Wiring examples",
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp)),
                contentScale = ContentScale.FillWidth
            )

            Spacer(Modifier.height(24.dp))

            // Timer state (center-aligned, gap:8)
            if (showTimer) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // 20px spinner (border-width:2) — Canvas arc
                        Canvas(modifier = Modifier.size(20.dp)) {
                            val strokeWidth = 2.dp.toPx()
                            val diameter = size.minDimension
                            val arcSize = Size(diameter - strokeWidth, diameter - strokeWidth)
                            val topLeft = Offset(strokeWidth / 2, strokeWidth / 2)

                            // Spinning arc: Brand, 90° sweep (no track circle — matches WebView)
                            drawArc(
                                color = Color(0xFFD9008D), // Brand
                                startAngle = spinnerAngle - 90f,
                                sweepAngle = 90f,
                                useCenter = false,
                                topLeft = topLeft,
                                size = arcSize,
                                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                            )
                        }

                        // Timer text: "00:59" fs:24 fw:700
                        Text(
                            text = "00:%02d".format(timerSeconds),
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = Pri
                        )
                    }

                    // Hint text fs:14
                    Text(
                        text = "इसमें 1-2 मिनट का समय लग सकता है",
                        fontSize = 14.sp,
                        color = Pri,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }

            // Checkbox state
            AnimatedVisibility(
                visible = showCheckbox,
                enter = fadeIn() + slideInVertically()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { if (!checked) checked = true },
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Checkbox: Image(painterResource(R.drawable.checkbox_checked/unchecked))
                    Image(
                        painter = painterResource(
                            if (checked) R.drawable.checkbox_checked else R.drawable.checkbox_unchecked
                        ),
                        contentDescription = if (checked) "Checked" else "Unchecked",
                        modifier = Modifier.size(24.dp)
                    )

                    // "मैंने सही से वायरिंग कर ली है" fs:16 fw:700
                    Text(
                        text = "मैंने सही से वायरिंग कर ली है",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Pri
                    )
                }
            }
        }
    }
}
