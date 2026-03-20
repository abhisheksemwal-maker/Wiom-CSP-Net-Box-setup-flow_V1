package com.wiom.partner

import android.Manifest
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.Uri
import android.os.BatteryManager
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Base64
import android.webkit.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var webView: WebView
    private var filePathCallback: ValueCallback<Array<Uri>>? = null
    private var cameraPhotoPath: String? = null

    companion object {
        private const val FILE_CHOOSER_REQUEST = 1001
        private const val CAMERA_PERMISSION_REQUEST = 1002
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Use system status bar with header color
        window.statusBarColor = Color.parseColor("#443152")

        webView = WebView(this)
        setContentView(webView)

        webView.settings.apply {
            javaScriptEnabled = true
            domStorageEnabled = true
            allowFileAccess = true
            allowContentAccess = true
            useWideViewPort = true
            loadWithOverviewMode = true
            cacheMode = WebSettings.LOAD_DEFAULT
            mediaPlaybackRequiresUserGesture = false
        }

        // Add JS interface for native features
        webView.addJavascriptInterface(NativeBridge(), "Android")

        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, request: android.webkit.WebResourceRequest?): Boolean {
                val url = request?.url?.toString() ?: return false
                if (url.startsWith("tel:") || url.startsWith("whatsapp:") || url.startsWith("https://wa.me")) {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                    return true
                }
                return false
            }
        }

        // Handle file input and getUserMedia camera access
        webView.webChromeClient = object : WebChromeClient() {
            override fun onPermissionRequest(request: PermissionRequest) {
                runOnUiThread { request.grant(request.resources) }
            }

            override fun onShowFileChooser(
                webView: WebView,
                callback: ValueCallback<Array<Uri>>,
                fileChooserParams: FileChooserParams
            ): Boolean {
                filePathCallback?.onReceiveValue(null)
                filePathCallback = callback

                if (ContextCompat.checkSelfPermission(this@MainActivity, Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED
                ) {
                    ActivityCompat.requestPermissions(
                        this@MainActivity,
                        arrayOf(Manifest.permission.CAMERA),
                        CAMERA_PERMISSION_REQUEST
                    )
                    return true
                }

                launchCamera()
                return true
            }
        }

        // Request camera permission at startup so getUserMedia works
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.CAMERA), CAMERA_PERMISSION_REQUEST
            )
        }

        webView.loadUrl("file:///android_asset/index.html")
    }

    private fun launchCamera() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(packageManager) != null) {
            val photoFile = try {
                createImageFile()
            } catch (e: Exception) {
                null
            }
            photoFile?.let {
                val photoURI = FileProvider.getUriForFile(
                    this, "com.wiom.partner.fileprovider", it
                )
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                cameraPhotoPath = "file:${it.absolutePath}"
                startActivityForResult(takePictureIntent, FILE_CHOOSER_REQUEST)
            }
        }
    }

    private fun createImageFile(): File {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile("WIOM_${timeStamp}_", ".jpg", storageDir)
    }

    @Deprecated("Use registerForActivityResult")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == FILE_CHOOSER_REQUEST) {
            val results = when {
                resultCode != RESULT_OK -> null
                cameraPhotoPath != null -> arrayOf(Uri.parse(cameraPhotoPath))
                data?.data != null -> arrayOf(data.data!!)
                else -> null
            }
            filePathCallback?.onReceiveValue(results)
            filePathCallback = null
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_PERMISSION_REQUEST) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                launchCamera()
            } else {
                filePathCallback?.onReceiveValue(null)
                filePathCallback = null
            }
        }
    }

    @Deprecated("Use onBackPressedDispatcher")
    override fun onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack()
        } else {
            super.onBackPressed()
        }
    }

    // JS bridge for native device info
    inner class NativeBridge {
        @JavascriptInterface
        fun openExternal(url: String) {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }

        @JavascriptInterface
        fun getBatteryLevel(): Int {
            val batteryIntent = registerReceiver(null, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
            return if (batteryIntent != null) {
                val level = batteryIntent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
                val scale = batteryIntent.getIntExtra(BatteryManager.EXTRA_SCALE, -1)
                (level.toFloat() / scale * 100).toInt()
            } else 50
        }

        @JavascriptInterface
        fun isWifiConnected(): Boolean {
            val cm = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
            val ni = cm.activeNetworkInfo
            return ni != null && ni.type == ConnectivityManager.TYPE_WIFI
        }

        @JavascriptInterface
        fun saveImage(dataUrl: String, filename: String): String {
            return try {
                val base64Data = if (dataUrl.contains(",")) {
                    dataUrl.substring(dataUrl.indexOf(",") + 1)
                } else dataUrl
                val imageBytes = Base64.decode(base64Data, Base64.DEFAULT)

                val dir = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "wiom aadhar card images")
                } else {
                    @Suppress("DEPRECATION")
                    File(
                        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                        "wiom aadhar card images"
                    )
                }
                if (!dir.exists()) dir.mkdirs()

                val safeFilename = filename.replace(Regex("[^a-zA-Z0-9 _-]"), "") + ".jpg"
                val outFile = File(dir, safeFilename)
                FileOutputStream(outFile).use { it.write(imageBytes) }

                runOnUiThread {
                    Toast.makeText(this@MainActivity, "Saved: $safeFilename", Toast.LENGTH_SHORT).show()
                }
                outFile.absolutePath
            } catch (e: Exception) {
                runOnUiThread {
                    Toast.makeText(this@MainActivity, "Save failed: ${e.message}", Toast.LENGTH_SHORT).show()
                }
                "error:${e.message}"
            }
        }
    }
}
