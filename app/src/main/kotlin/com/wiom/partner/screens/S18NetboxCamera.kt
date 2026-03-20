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
import androidx.compose.material3.Icon
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
import java.io.File

private val ShutterOuter = Color(0xFFD9D9D9)
private val ShutterBorder = Color(0xFFE8E4F0)

@Composable
fun S18NetboxCamera(
    vm: FlowViewModel,
    onCapture: () -> Unit,
    onClose: () -> Unit
) {
    val context = LocalContext.current
    var imageCapture by remember { mutableStateOf<ImageCapture?>(null) }

    // Full screen, bg:#4D4D4D
    Box(modifier = Modifier.fillMaxSize().background(Color(0xFF4D4D4D))) {
        // Camera video fills entire screen — rear camera
        CameraPreviewView(
            modifier = Modifier.fillMaxSize(),
            isFrontCamera = false,
            onImageCapture = { imageCapture = it }
        )

        // Header overlay (z:2): close button padding:4, icon #FAF9FC
        // Title: padding:[8,16,24,16] fs:24 fw:700 tc:#FAF9FC
        Column(modifier = Modifier.fillMaxSize()) {
            CameraHeader(
                title = "नेट बॉक्स की फोटो लें",
                onClose = onClose
            )

            Spacer(Modifier.weight(1f))

            // Flash: absolute top:80 right:16 → position relative to top
            // (placed in the column flow after header, via Box overlay below)
        }

        // Flash icon: absolute top:80 right:16, flash_on icon #FAF9FC
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 80.dp, end = 16.dp)
                .size(48.dp)
                .clickable { /* toggle flash */ },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                Icons.Rounded.FlashOn,
                contentDescription = "Flash",
                tint = Bg, // #FAF9FC
                modifier = Modifier.size(24.dp)
            )
        }

        // Shutter: absolute bottom:48, centered, 64x64 outer #D9D9D9, 2px border #E8E4F0, 54x54 inner #FAF9FC
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 48.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .background(ShutterOuter)
                    .border(2.dp, ShutterBorder, CircleShape)
                    .clickable {
                        val ic = imageCapture ?: return@clickable
                        val file = File(context.cacheDir, "netbox_${System.currentTimeMillis()}.jpg")
                        val outputOptions = ImageCapture.OutputFileOptions.Builder(file).build()
                        ic.takePicture(
                            outputOptions,
                            ContextCompat.getMainExecutor(context),
                            object : ImageCapture.OnImageSavedCallback {
                                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                                    val raw = BitmapFactory.decodeFile(file.absolutePath) ?: run { onCapture(); return }
                                    // EXIF rotation fix (rear camera — no mirror)
                                    val exif = ExifInterface(file.absolutePath)
                                    val orientation = exif.getAttributeInt(
                                        ExifInterface.TAG_ORIENTATION,
                                        ExifInterface.ORIENTATION_NORMAL
                                    )
                                    val matrix = Matrix()
                                    when (orientation) {
                                        ExifInterface.ORIENTATION_ROTATE_90 -> matrix.postRotate(90f)
                                        ExifInterface.ORIENTATION_ROTATE_180 -> matrix.postRotate(180f)
                                        ExifInterface.ORIENTATION_ROTATE_270 -> matrix.postRotate(270f)
                                    }
                                    val bitmap = Bitmap.createBitmap(raw, 0, 0, raw.width, raw.height, matrix, true)
                                    vm.netboxPhotoData = bitmap
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
                // 54x54 inner circle #FAF9FC
                Box(
                    modifier = Modifier
                        .size(54.dp)
                        .clip(CircleShape)
                        .background(Bg)
                )
            }
        }
    }
}
