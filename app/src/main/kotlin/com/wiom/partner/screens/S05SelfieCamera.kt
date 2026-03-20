package com.wiom.partner.screens

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import androidx.camera.core.ImageCapture
import androidx.exifinterface.media.ExifInterface
import androidx.camera.core.ImageCaptureException
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.wiom.partner.theme.*
import com.wiom.partner.components.*
import com.wiom.partner.camera.CameraPreviewView
import com.wiom.partner.state.FlowViewModel
import java.io.File

private val ShutterOuter = Color(0xFFD9D9D9)
private val ShutterBorder = Color(0xFFE8E4F0)

@Composable
fun S05SelfieCamera(
    vm: FlowViewModel,
    onCapture: () -> Unit,
    onClose: () -> Unit
) {
    val context = LocalContext.current
    var imageCapture by remember { mutableStateOf<ImageCapture?>(null) }

    Box(modifier = Modifier.fillMaxSize().background(Color.Black)) {
        // Live camera feed — front camera, mirrored
        CameraPreviewView(
            modifier = Modifier.fillMaxSize(),
            isFrontCamera = true,
            onImageCapture = { imageCapture = it }
        )

        // Header overlay
        Column(modifier = Modifier.fillMaxSize()) {
            CameraHeader(
                title = "व्योम एक्सपर्ट टी-शर्ट पहने हुए एक अच्छी सी सेल्फी लीजिए",
                onClose = onClose
            )

            Spacer(Modifier.weight(1f))

            // Shutter: 64x64, 2dp border, 54x54 inner
            Box(
                modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier.size(64.dp).clip(CircleShape)
                        .background(ShutterOuter)
                        .border(2.dp, ShutterBorder, CircleShape)
                        .clickable {
                            val ic = imageCapture ?: return@clickable
                            val file = File(context.cacheDir, "selfie_${System.currentTimeMillis()}.jpg")
                            val outputOptions = ImageCapture.OutputFileOptions.Builder(file).build()
                            ic.takePicture(
                                outputOptions,
                                ContextCompat.getMainExecutor(context),
                                object : ImageCapture.OnImageSavedCallback {
                                    override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                                        val raw = BitmapFactory.decodeFile(file.absolutePath) ?: run { onCapture(); return }
                                        // Fix EXIF rotation + mirror for front camera
                                        val exif = ExifInterface(file.absolutePath)
                                        val orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
                                        val matrix = Matrix()
                                        when (orientation) {
                                            ExifInterface.ORIENTATION_ROTATE_90 -> matrix.postRotate(90f)
                                            ExifInterface.ORIENTATION_ROTATE_180 -> matrix.postRotate(180f)
                                            ExifInterface.ORIENTATION_ROTATE_270 -> matrix.postRotate(270f)
                                        }
                                        matrix.postScale(-1f, 1f) // mirror for selfie
                                        val bitmap = Bitmap.createBitmap(raw, 0, 0, raw.width, raw.height, matrix, true)
                                        vm.selfieData = bitmap
                                        onCapture()
                                    }
                                    override fun onError(e: ImageCaptureException) {
                                        // Still navigate even if capture fails
                                        onCapture()
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
}
