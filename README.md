# Expert App Net Box Setup Flow — Native Kotlin/Compose Build

Native Android app built with Jetpack Compose, replicating the Wiom Expert (Technician) Net Box installation flow.

## APK

**Download:** `Expert App Net Box Setup Flow_Semwal_Kotlin build_V1.apk` (32.5 MB) — install on any Android device.

**WebView version:** Switch to `main` branch for the pixel-accurate WebView prototype with full specs.

## Build

**Requirements:**
- JDK 17
- Android SDK (compileSdk 34, minSdk 24)

**Commands:**
```bash
# Build
./gradlew assembleDebug

# Install on connected device
adb install -r app/build/outputs/apk/debug/app-debug.apk

# Launch
adb shell am start -n com.wiom.partner.native/com.wiom.partner.MainActivity
```

## Project Structure

```
app/src/main/
├── kotlin/com/wiom/partner/
│   ├── MainActivity.kt              # Single Activity, Compose host
│   ├── navigation/
│   │   ├── Screen.kt                # 33 screen routes (sealed class)
│   │   └── NavGraph.kt              # Navigation wiring, exit/WiFi dialogs
│   ├── state/
│   │   └── FlowViewModel.kt         # SharedPreferences resume, step map, photo state
│   ├── theme/
│   │   ├── Color.kt                 # Wiom DS tokens (Brand, Pri, Sec, Bg, etc.)
│   │   └── WiomTheme.kt             # MaterialTheme wrapper
│   ├── components/
│   │   ├── WiomHeader.kt            # DarkHeader, LightHeader, CameraHeader
│   │   ├── WiomCta.kt               # PrimaryCta, GhostCta, CtaArea
│   │   ├── ExitDialog.kt            # "सेटअप पर काम जारी है!" warning
│   │   ├── WifiConnectDialog.kt     # Material 3 WiFi connect popup
│   │   └── CustomerDetailsSheet.kt  # Bottom sheet with customer info + Aadhaar
│   ├── camera/
│   │   └── CameraPreview.kt         # CameraX PreviewView + ImageCapture
│   ├── util/
│   │   ├── AssetImage.kt            # Load PNG/WebP from assets/img/
│   │   └── AudioPlayer.kt           # MediaPlayer wrapper
│   └── screens/
│       ├── S01TaskList.kt           # Task list with ticket card
│       ├── S02TaskDetail.kt         # Vertical pager, cricket timeline
│       ├── S03PaygAcceptance.kt     # Audio + checkbox (₹300 gate)
│       ├── S04TransferInfo.kt       # Customer card, call, 3-pin/arrival dialogs
│       ├── S05SelfieCamera.kt       # CameraX front, EXIF rotation
│       ├── S06SelfieReview.kt       # Captured selfie display
│       ├── S08AadhaarCapture.kt     # 3-state: front→back→review, CameraX rear
│       ├── S08cPaygSystemInfo.kt    # Audio + purple info card
│       ├── S11CustomerDetails.kt    # Customer info + Aadhaar thumbnails
│       ├── S12PaymentChecklist.kt   # 6-state animated stepper
│       ├── S13PowerUpTimer.kt       # Countdown + audio + router image
│       ├── S14SwitchOnConfirm.kt    # Checkbox confirmation
│       ├── S15IspRechargeAudio.kt   # Audio + ISP bottom sheet
│       ├── S16IspForm.kt            # PPPoE/Static IP/DHCP, progressive reveal
│       ├── S17PlacementCheck.kt     # Audio + timer + checkbox + example images
│       ├── S18NetboxCamera.kt       # CameraX rear + capture
│       ├── S19NetboxReview.kt       # Photo review + retake
│       ├── S20ThreepinInfo.kt       # Audio + timer + checkbox
│       ├── S21ThreepinCamera.kt     # CameraX rear + capture
│       ├── S22ThreepinReview.kt     # Photo review + retake
│       ├── S23WiringCheck.kt        # Audio + timer + checkbox
│       ├── S24WiringCamera.kt       # CameraX rear + capture
│       ├── S25WiringReview.kt       # Photo review + retake
│       ├── S26Loading.kt            # Green spinner, 3s auto-transition
│       ├── S27Success.kt            # Green tick + CTA
│       ├── S28OpticalPower.kt       # 0→-21 dB counter animation
│       ├── S29SpeedTest.kt          # Lottie gauge + bottom sheet
│       ├── S30RechargeInfo.kt       # Audio + ghost CTA (no header)
│       ├── S31HappyCodeRating.kt    # Illustration + CTA
│       ├── S32OtpEntry.kt           # 4-digit OTP + numeric keypad
│       └── S33Lottery.kt            # Full-screen image + reset
├── assets/img/                       # 70+ files: audio, images, Lottie, SVGs
└── res/drawable/                     # 16 Android Vector Drawables (SVG→XML)
```

## Flow

```
S1 (Task List) → S2 (Task Detail) → S3 (PayG Audio) → S4 (Transfer Info)
→ 3-Pin Dialog → Arrival Dialog → S5 (Selfie Camera) → S6 (Selfie Review)
→ S8 (Aadhaar 3-state) → S8C (PayG System Info) → S12 (Payment Checklist)
→ S13 (Power-Up Timer) → S14 (Switch-On) → S11 (Customer Details)
→ S15 (ISP Audio) → ISP Bottom Sheet → S16 (ISP Form) → WiFi Popup
→ S17 (Placement Check) → S18 (Netbox Camera) → S19 (Netbox Review)
→ S20 (3-Pin Info) → S21 (3-Pin Camera) → S22 (3-Pin Review)
→ S23 (Wiring Check) → S24 (Wiring Camera) → S25 (Wiring Review)
→ S26 (Loading) → S27 (Success) → S28 (Optical Power) → S29 (Speed Test)
→ S30 (Recharge Info) → S31 (Rating) → S32 (OTP Entry) → S33 (Lottery) → S1
```

## Key Features

| Feature | Implementation |
|---------|---------------|
| Camera | CameraX with front/rear toggle, EXIF rotation fix |
| Audio | MediaPlayer from bundled assets (8 screens) |
| Speed Test | Lottie Compose (`img/speedmeter.json`) |
| Resume | SharedPreferences — exit mid-flow, resume on return |
| Icons | Android Vector Drawables (16 SVG→XML conversions) |
| Forms | PPPoE progressive reveal, Static IP (5 fields), DHCP readout |
| Dialogs | Exit warning, WiFi connect, 3-pin plug, arrival confirmation |
| Bottom Sheets | ISP method selection, customer details, speed confirmation |

## Dependencies

- Jetpack Compose BOM 2024.01.00
- CameraX 1.3.1
- Lottie Compose 6.3.0
- Accompanist System UI Controller 0.34.0
- Navigation Compose 2.7.6
- ExifInterface 1.3.7

## Reference

- **Design specs:** See `SPEC_HUMAN.md` and `SPEC_AI.md` on `main` branch
- **WebView prototype:** See `main` branch for the pixel-accurate WebView build
- **Master doc:** Wiom Net Box Setup (Installation) — 35 modules, PayG model
