package com.wiom.partner.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.PhotoCamera
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.FlashOn
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.exifinterface.media.ExifInterface
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import com.wiom.partner.theme.*
import com.wiom.partner.components.*
import com.wiom.partner.camera.CameraPreviewView
import com.wiom.partner.state.FlowViewModel
import java.io.File

private enum class AadhaarScreen { CAMERA_FRONT, CAMERA_BACK, FLOW }
private enum class FlowState { FRONT_PROMPT, FRONT_CAPTURED, BOTH_CAPTURED }

@Composable
fun S08AadhaarCapture(
    vm: FlowViewModel? = null,
    onNext: () -> Unit,
    onClose: () -> Unit
) {
    val context = LocalContext.current
    var screen by remember { mutableStateOf(AadhaarScreen.FLOW) }
    var flowState by remember { mutableStateOf(FlowState.FRONT_PROMPT) }
    var frontBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var backBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var imageCapture by remember { mutableStateOf<ImageCapture?>(null) }

    when (screen) {
        // ═══ CAMERA VIEWFINDER (S07 in WebView) ═══
        AadhaarScreen.CAMERA_FRONT, AadhaarScreen.CAMERA_BACK -> {
            val isBack = screen == AadhaarScreen.CAMERA_BACK
            Box(modifier = Modifier.fillMaxSize().background(Color.Black)) {
                // Live rear camera feed
                CameraPreviewView(
                    modifier = Modifier.fillMaxSize(),
                    isFrontCamera = false,
                    onImageCapture = { imageCapture = it }
                )

                // Dark overlay with transparent cutout for guide rect
                // Uses graphicsLayer + BlendMode.Clear to punch a hole
                Box(
                    modifier = Modifier.fillMaxSize()
                        .graphicsLayer { alpha = 0.99f } // required for BlendMode.Clear to work
                        .drawWithContent {
                            // Draw dark overlay
                            drawRect(Color.Black.copy(alpha = 0.55f))
                            // Punch transparent hole for guide rect
                            val hPad = 16.dp.toPx()
                            val rectW = size.width - hPad * 2
                            val rectH = 199.dp.toPx()
                            val rectY = size.height / 2 - rectH / 2 - 30.dp.toPx()
                            drawRoundRect(
                                color = Color.Black,
                                topLeft = androidx.compose.ui.geometry.Offset(hPad, rectY),
                                size = androidx.compose.ui.geometry.Size(rectW, rectH),
                                cornerRadius = androidx.compose.ui.geometry.CornerRadius(16.dp.toPx()),
                                blendMode = BlendMode.Clear
                            )
                            // Draw dashed border on the cutout
                            drawRoundRect(
                                color = Color(0xB3FAF9FC),
                                topLeft = androidx.compose.ui.geometry.Offset(hPad, rectY),
                                size = androidx.compose.ui.geometry.Size(rectW, rectH),
                                cornerRadius = androidx.compose.ui.geometry.CornerRadius(16.dp.toPx()),
                                style = Stroke(
                                    width = 2.dp.toPx(),
                                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f))
                                )
                            )
                            drawContent()
                        },
                    contentAlignment = Alignment.Center
                ) {
                    // Guide text positioned well above the cutout
                    Text(
                        "आधार कार्ड को इस क्षेत्र में रखें",
                        fontSize = 24.sp, fontWeight = FontWeight.Bold,
                        color = Color(0xFFFAF9FC), lineHeight = 32.sp,
                        modifier = Modifier.padding(horizontal = 16.dp).offset(y = (-180).dp)
                    )
                }

                // Close button: top-left
                Box(
                    Modifier.padding(start = 4.dp, top = 28.dp).size(48.dp)
                        .clickable { screen = AadhaarScreen.FLOW },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Rounded.Close, "Close", tint = Color.White, modifier = Modifier.size(24.dp))
                }

                // Flash icon: top-right
                Icon(
                    Icons.Rounded.FlashOn, "Flash", tint = Color.White,
                    modifier = Modifier.align(Alignment.TopEnd).padding(end = 16.dp, top = 40.dp).size(24.dp)
                )

                // Shutter button: bottom center
                Box(modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 24.dp)) {
                    Box(
                        modifier = Modifier.size(64.dp).clip(CircleShape)
                            .background(Color(0xFFD9D9D9))
                            .border(2.dp, Color(0xFFE8E4F0), CircleShape)
                            .clickable {
                                val ic = imageCapture ?: run {
                                    if (!isBack) flowState = FlowState.FRONT_CAPTURED else flowState = FlowState.BOTH_CAPTURED
                                    screen = AadhaarScreen.FLOW
                                    return@clickable
                                }
                                val file = File(context.cacheDir, "aadhaar_${if (isBack) "back" else "front"}_${System.currentTimeMillis()}.jpg")
                                ic.takePicture(
                                    ImageCapture.OutputFileOptions.Builder(file).build(),
                                    ContextCompat.getMainExecutor(context),
                                    object : ImageCapture.OnImageSavedCallback {
                                        override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                                            val raw = BitmapFactory.decodeFile(file.absolutePath) ?: run {
                                                if (!isBack) flowState = FlowState.FRONT_CAPTURED else flowState = FlowState.BOTH_CAPTURED
                                                screen = AadhaarScreen.FLOW; return
                                            }
                                            // Fix EXIF rotation (rear camera)
                                            val exif = ExifInterface(file.absolutePath)
                                            val orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
                                            val matrix = Matrix()
                                            when (orientation) {
                                                ExifInterface.ORIENTATION_ROTATE_90 -> matrix.postRotate(90f)
                                                ExifInterface.ORIENTATION_ROTATE_180 -> matrix.postRotate(180f)
                                                ExifInterface.ORIENTATION_ROTATE_270 -> matrix.postRotate(270f)
                                            }
                                            val bmp = Bitmap.createBitmap(raw, 0, 0, raw.width, raw.height, matrix, true)
                                            if (!isBack) {
                                                frontBitmap = bmp; flowState = FlowState.FRONT_CAPTURED
                                                vm?.aadhaarState = vm?.aadhaarState?.copy(front = true, frontData = bmp) ?: vm?.aadhaarState!!
                                            } else {
                                                backBitmap = bmp; flowState = FlowState.BOTH_CAPTURED
                                                vm?.aadhaarState = vm?.aadhaarState?.copy(back = true, backData = bmp) ?: vm?.aadhaarState!!
                                            }
                                            screen = AadhaarScreen.FLOW
                                        }
                                        override fun onError(e: ImageCaptureException) {
                                            if (!isBack) flowState = FlowState.FRONT_CAPTURED else flowState = FlowState.BOTH_CAPTURED
                                            screen = AadhaarScreen.FLOW
                                        }
                                    }
                                )
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Box(Modifier.size(54.dp).clip(CircleShape).background(Bg))
                    }
                }
            }
        }

        // ═══ FLOW SCREEN (S08 in WebView) ═══
        AadhaarScreen.FLOW -> {
            Column(modifier = Modifier.fillMaxSize().background(Bg)) {
                // Header: close + मदद
                LightHeader(onClose = onClose)

                // Title
                Text(
                    "कस्टमर से आधार कार्ड मांगे और उसकी एक अच्छी सी फोटो लें",
                    fontSize = 24.sp, fontWeight = FontWeight.Bold, lineHeight = 32.sp, color = Pri,
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 16.dp)
                )

                // Body
                Column(
                    modifier = Modifier.weight(1f).verticalScroll(rememberScrollState())
                        .padding(horizontal = 16.dp)
                ) {
                    when (flowState) {
                        FlowState.FRONT_PROMPT -> {
                            AadhaarPromptCard("आधार : Front") { screen = AadhaarScreen.CAMERA_FRONT }
                        }
                        FlowState.FRONT_CAPTURED -> {
                            AadhaarCapturedCard(frontBitmap) { frontBitmap = null; flowState = FlowState.FRONT_PROMPT }
                            Spacer(Modifier.height(16.dp))
                            AadhaarPromptCard("आधार : Back") { screen = AadhaarScreen.CAMERA_BACK }
                        }
                        FlowState.BOTH_CAPTURED -> {
                            AadhaarCapturedCard(frontBitmap) { frontBitmap = null; flowState = FlowState.FRONT_PROMPT }
                            Spacer(Modifier.height(16.dp))
                            AadhaarCapturedCard(backBitmap) { backBitmap = null; flowState = FlowState.FRONT_CAPTURED }
                        }
                    }
                }

                // CTA: only in state 3
                if (flowState == FlowState.BOTH_CAPTURED) {
                    CtaArea { PrimaryCta("आगे बढ़ें", onClick = onNext) }
                }
            }
        }
    }
}

@Composable
private fun AadhaarPromptCard(label: String, onClick: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(16.dp))
            .background(N100).clickable { onClick() }.padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Text(label, fontSize = 24.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center, color = Pri)
        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Icon(Icons.Outlined.PhotoCamera, null, tint = Hint, modifier = Modifier.size(64.dp))
            Text("+ फोटो लें", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Brand)
        }
    }
}

@Composable
private fun AadhaarCapturedCard(bitmap: Bitmap?, onRetake: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxWidth().height(199.dp)
            .clip(RoundedCornerShape(16.dp)).background(Color(0xFFD9D9D9))
    ) {
        // Display captured photo or grey placeholder
        if (bitmap != null) {
            androidx.compose.foundation.Image(
                bitmap = bitmap.asImageBitmap(),
                contentDescription = "Aadhaar",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }
        // Retake X: 24×24, top-right
        Box(
            Modifier.align(Alignment.TopEnd).padding(12.dp).size(24.dp)
                .clip(CircleShape).background(Color.Black.copy(alpha = 0.5f))
                .clickable { onRetake() },
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Rounded.Close, "Retake", tint = Color.White, modifier = Modifier.size(16.dp))
        }
    }
}
