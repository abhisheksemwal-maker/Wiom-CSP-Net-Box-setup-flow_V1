package com.wiom.partner.state

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

data class AadhaarState(
    var front: Boolean = false,
    var back: Boolean = false,
    var frontData: Bitmap? = null,
    var backData: Bitmap? = null
)

class FlowViewModel : ViewModel() {
    // Navigation state
    var currentScreen by mutableStateOf("s1")

    // Persistence (loaded from SharedPreferences)
    var paygAccepted by mutableStateOf(false)
    var resumeScreen: String? by mutableStateOf(null)

    // Camera captures
    var selfieData: Bitmap? by mutableStateOf(null)
    var aadhaarState by mutableStateOf(AadhaarState())
    var netboxPhotoData: Bitmap? by mutableStateOf(null)
    var threepinPhotoData: Bitmap? by mutableStateOf(null)
    var wiringPhotoData: Bitmap? by mutableStateOf(null)

    // OTP
    var happyCode by mutableStateOf("")

    // Exit dialog
    var showExitDialog by mutableStateOf(false)

    // WiFi connect dialog (between S16 and S17)
    var showWifiDialog by mutableStateOf(false)

    private var prefs: SharedPreferences? = null

    // Step map (same as WebView JS)
    private val stepMap = mapOf(
        "s3" to 1, "s4" to 1, "s5" to 2, "s6" to 2, "s7" to 3, "s8" to 3,
        "s8c" to 4, "s9" to 4, "s10" to 5, "s11" to 5,
        "s15" to 6, "s16" to 6,
        "s17" to 7, "s18" to 7, "s19" to 7, "s20" to 7, "s21" to 7, "s22" to 7,
        "s23" to 7, "s24" to 7, "s25" to 7,
        "s26" to 8, "s27" to 8, "s28" to 8, "s29" to 8, "s30" to 8, "s31" to 8,
        "s32" to 8, "s33" to 8, "s12" to 8, "s13" to 8, "s14" to 8
    )

    fun getStepCount(screenId: String): Int = stepMap[screenId] ?: 0

    // Initialize from SharedPreferences
    fun init(context: Context) {
        prefs = context.getSharedPreferences("wiom_flow", Context.MODE_PRIVATE)
        resumeScreen = prefs?.getString("resume_screen", null)
        paygAccepted = prefs?.getBoolean("payg_accepted", false) ?: false
    }

    // Called on every navigation (equivalent of WebView go() saving to localStorage)
    fun onNavigate(screenId: String) {
        currentScreen = screenId
        // Persist flow progress (only flow screens, not s1/s2)
        if (screenId != "s1" && screenId != "s2") {
            resumeScreen = screenId
            prefs?.edit()?.putString("resume_screen", screenId)?.apply()
        }
    }

    // Called when PayG checkbox is accepted
    fun acceptPayg() {
        paygAccepted = true
        prefs?.edit()?.putBoolean("payg_accepted", true)?.apply()
    }

    // Called from s1 task card → clears resume, goes to s2
    // Does NOT clear PayG acceptance (user already consented)
    fun startFresh() {
        resumeScreen = null
        prefs?.edit()?.remove("resume_screen")?.apply()
    }

    // Called from s2 "काम पूरा करें" → returns target screen
    fun getResumeTarget(): String {
        val saved = resumeScreen
        return if (saved != null) {
            // Skip s3 if PayG already accepted
            if (saved == "s3" && paygAccepted) "s4" else saved
        } else {
            // Fresh start
            if (paygAccepted) "s4" else "s3"
        }
    }

    // Called from exit dialog "हाँ, सेटअप रोकें" → saves current screen, goes to s1
    fun exitSetup() {
        resumeScreen = currentScreen
        prefs?.edit()?.putString("resume_screen", currentScreen)?.apply()
    }

    // Full reset (completion or "फिर से चलायें")
    fun resetFlow() {
        paygAccepted = false
        resumeScreen = null
        happyCode = ""
        selfieData = null
        aadhaarState = AadhaarState()
        netboxPhotoData = null
        threepinPhotoData = null
        wiringPhotoData = null
        prefs?.edit()?.clear()?.apply()
    }
}
