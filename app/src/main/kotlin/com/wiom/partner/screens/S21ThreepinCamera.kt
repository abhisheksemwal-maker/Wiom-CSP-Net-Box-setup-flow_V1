package com.wiom.partner.screens

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.FlashOn
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.exifinterface.media.ExifInterface
import com.wiom.partner.camera.CameraPreviewView
import com.wiom.partner.components.CameraHeader
import com.wiom.partner.state.FlowViewModel
import com.wiom.partner.theme.*
import androidx.compose.material3.Icon
import java.io.File

private val CameraBg = Color(0xFF4D4D4D)
private val ShutterOuter = Color(0xFFD9D9D9)
private val ShutterBorder = Color(0xFFE8E4F0)

@Composable
fun S21ThreepinCamera(
    vm: FlowViewModel,
    onCapture: () -> Unit,
    onClose: () -> Unit
) {
    val context = LocalContext.current
    var imageCapture by remember { mutableStateOf<ImageCapture?>(null) }

    Box(modifier = Modifier.fillMaxSize().background(CameraBg)) {
        // Live rear camera feed — fills screen
        CameraPreviewView(
            modifier = Modifier.fillMaxSize(),
            isFrontCamera = false,
            onImageCapture = { imageCapture = it }
        )

        // Header overlay (z on top)
        Column(modifier = Modifier.fillMaxSize()) {
            CameraHeader(
                title = "व्योम वाले 3 पिन की फोटो लें",
                onClose = onClose
            )

            Spacer(Modifier.weight(1f))

            // Bottom area: shutter centered, flash absolute right
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 48.dp)
            ) {
                // Shutter: 64x64 outer #D9D9D9, 2dp border #E8E4F0, 54x54 inner #FAF9FC
                Box(
                    modifier = Modifier.align(Alignment.Center)
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(ShutterOuter)
                        .border(2.dp, ShutterBorder, CircleShape)
                        .clickable {
                            val ic = imageCapture ?: return@clickable
                            val file = File(context.cacheDir, "threepin_${System.currentTimeMillis()}.jpg")
                            val outputOptions = ImageCapture.OutputFileOptions.Builder(file).build()
                            ic.takePicture(
                                outputOptions,
                                ContextCompat.getMainExecutor(context),
                                object : ImageCapture.OnImageSavedCallback {
                                    override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                                        val raw = BitmapFactory.decodeFile(file.absolutePath) ?: run { onCapture(); return }
                                        val exif = ExifInterface(file.absolutePath)
                                        val orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
                                        val matrix = Matrix()
                                        when (orientation) {
                                            ExifInterface.ORIENTATION_ROTATE_90 -> matrix.postRotate(90f)
                                            ExifInterface.ORIENTATION_ROTATE_180 -> matrix.postRotate(180f)
                                            ExifInterface.ORIENTATION_ROTATE_270 -> matrix.postRotate(270f)
                                        }
                                        val bitmap = Bitmap.createBitmap(raw, 0, 0, raw.width, raw.height, matrix, true)
                                        vm.threepinPhotoData = bitmap
                                        onCapture()
                                    }
                                    override fun onError(e: ImageCaptureException) {
                                        onCapture()
                                    }
                                }
                            )
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Box(Modifier.size(54.dp).clip(CircleShape).background(Bg))
                }

                // Flash icon: top:80 from screen top → but we're in bottom area, so place absolute
                // Spec says: absolute top:80 right:16. Since header is ~100dp tall, place flash at right:16
                // In bottom area context, align top-end
            }
        }

        // Flash icon: absolute top:80 right:16
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 80.dp, end = 16.dp)
                .size(48.dp),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                Icons.Rounded.FlashOn,
                contentDescription = "Flash",
                tint = Bg,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}
