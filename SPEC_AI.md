# SPEC_AI.md -- Wiom Expert App Net Box Setup Flow

Technical specification for AI coding agents (Claude, Cursor, Copilot).
Last updated: 2026-03-20. APK_VERSION: v29.

---

## 1. Architecture

**Single-page Android WebView app.** One `index.html` file (~2770 lines) contains all HTML, CSS, and JavaScript. No frameworks, no bundler, no router library.

### Core pattern

```
Kotlin MainActivity
  └─ WebView loads file:///android_asset/index.html
       ├─ <style> block: all CSS including design tokens
       ├─ <div class="scr" id="sN"> x 33 screens (display:none by default)
       ├─ <div class="ov" id="ov-*"> overlays (fixed, z-index:100)
       ├─ <div class="bs-overlay" id="bs-*"> bottom sheets (fixed, z-index:200)
       └─ <script> block: all JS (~1090 lines)
```

### Navigation

- `go(id)` -- the ONLY navigation function. Toggles `.on` class on `<div class="scr">` elements.
  - Hides all screens: `document.querySelectorAll('.scr').forEach(s => s.classList.remove('on'))`
  - Shows target: `document.getElementById(id).classList.add('on')`
  - Sets `currentScreen = id`
  - Persists to `localStorage.wiom_resume_screen` (flow screens only, NOT s1/s2)
  - Auto-starts cameras, audio, animations, timers based on screen ID (see switch block in `go()`)

### Screen visibility

```css
.scr { display:none; position:absolute; top:0; left:0; width:100vw; height:100vh; }
.scr.on { display:flex; z-index:10; }
```

Each screen is a flex column: header (fixed) + `.bd` (scrollable body, flex:1) + CTA area (flex-shrink:0).

### Overlays

```css
.ov { display:none; position:fixed; z-index:100; background:rgba(22,16,33,0.5); }
.ov.on { display:flex; }
```

- `showOv(id)` -- adds `.on` class
- `hideOv(id)` -- removes `.on` class

### Bottom Sheets

```css
.bs-overlay { display:none; position:fixed; z-index:200; background:rgba(22,16,33,0.5); }
.bs-overlay.on { display:block; }
.bs-panel { position:absolute; bottom:0; border-radius:24px 24px 0 0; transition:transform 0.3s; }
```

- `showBs(id)` -- adds `.on`, sets panel `transform:translateY(0)`
- `hideBs(id)` -- slides panel down via `translateY(100%)`, removes `.on` after 300ms
- Drag-to-dismiss on handle via touchstart/touchmove/touchend (threshold: 100px)
- Backdrop click dismisses: `onclick="if(event.target===this)hideBs('bs-customer')"`

---

## 2. Screen Registry

33 screens total. Format: **ID | Figma node | Step (of 8) | Description | Entry trigger | Exit action**

### Task List & Detail

| ID | Figma | Step | Description | Entry | Exit |
|----|-------|------|-------------|-------|------|
| s1 | 199:4938 | -- | Task list (home). Two task cards in horizontal scroll. Always shows on app launch. | App launch / `resetFlow()` | `resumeFlow()` on card 1 CTA, `go('s3')` on card 2 CTA |
| s2 | 200:4961 | -- | Task detail card (NOT USED in current flow -- cards are inline on s1). Reserved. | -- | -- |

### Setup Flow Screens

| ID | Figma | Step | Description | Entry | Exit |
|----|-------|------|-------------|-------|------|
| s3 | 219:16949 | 1 | PayG acceptance audio (10s) + checkbox consent | `resumeFlow()` from s1 | `paygAgree()` -> s4 |
| s4 | 200:5143 | 1 | Transfer info: customer details, call button, ISP prefill card. Two overlay dialogs (ov-3pin, ov-arrival). | s3 checkbox or resume | `ov-3pin` checkbox -> `ov-arrival` -> s5 |
| s5 | 200:5853 | 2 | Selfie camera (FRONT). Wiom Expert T-shirt selfie. | ov-arrival "yes" | `captureSelfie()` -> s6 |
| s6 | 200:5769 | 2 | Selfie review ("rockstar" compliment) | s5 capture | CTA -> s8 |
| s7 | 200:6879 | 3 | Aadhaar camera (REAR). Dashed guide rectangle. | s8 "photo" tap | `captureAadhaar()` -> s8 |
| s8 | 200:6691 | 3 | Aadhaar capture flow: 3 states (front prompt, front+back prompt, both captured). | s6 CTA / s7 capture | s8c (via CTA after both captured) |
| s8c | 216:16152 | 4 | PayG system info audio (17s). Purple info card. Ghost CTA "okay". | s8 CTA | Ghost CTA -> s12 |
| s9 | -- | 4 | Loading spinner (unused in main flow but auto-transitions to s10 after 2s) | `go('s9')` | Auto -> s10 (2s) |
| s10 | -- | 5 | Switch on Net Box (with image + checkbox) | s9 auto | Checkbox -> s11 |
| s11 | 200:5924 | 5 | Customer details summary (name, address, Aadhaar thumbs, download button) | s10 checkbox / s14 checkbox | CTA -> s15 |
| s12 | 198:74631 | 8 | Payment checklist (6-state animation: WiFi done, Aadhaar spinner->tick, Payment spinner->tick) | s8c CTA | Auto -> s13 (at 5.4s) |
| s13 | 200:5440 | 8 | Power-up countdown timer (15s, with audio) | s12 auto | Timer 0 -> s14 |
| s14 | 198:68231 | 8 | Net Box switch-on confirm (image + checkbox) | s13 timer done | Checkbox -> s11 |
| s15 | 224:17548 | 6 | ISP recharge audio (9.2s). Education screen. Ghost CTA. | s11 CTA | Ghost CTA -> ov-isp-sheet |
| s16 | 198:72096 | 6 | ISP details form. Radio: PPPoE/Static IP/DHCP. Progressive field reveal. | ov-isp-sheet card select | CTA -> ov-wifi |
| s17 | 198:67430 | 7 | Net Box placement check. Audio (10s) + timer (59s) + checkbox. | ov-wifi "connect" | Checkbox -> s18 |
| s18 | 198:67138 | 7 | Net Box photo camera (REAR) | s17 checkbox | `captureNetboxPhoto()` -> s19 |
| s19 | 198:76119 | 7 | Net Box photo review | s18 capture | CTA -> s20 |
| s20 | 198:76202 | 7 | 3-pin plug info + audio (2s). Timer + checkbox. | s19 CTA | Checkbox -> s21 |
| s21 | 198:76502 | 7 | 3-pin plug camera (REAR) | s20 checkbox | `captureThreepinPhoto()` -> s22 |
| s22 | 198:76415 | 7 | 3-pin plug photo review | s21 capture | CTA -> s23 |
| s23 | 198:67210 | 7 | Wiring check + audio (12.3s). Timer + checkbox. | s22 CTA | Checkbox -> s24 |
| s24 | 198:67735 | 7 | Wiring photo camera (REAR) | s23 checkbox | `captureWiringPhoto()` -> s25 |
| s25 | 198:67648 | 7 | Wiring photo review | s24 capture | CTA -> s26 |
| s26 | 198:66888 | 8 | Loading -- "Net Box preparing" (green spinner) | s25 CTA | Auto -> s27 (3s) |
| s27 | 198:67052 | 8 | Success -- "Net Box chalu ho gaya" (green tick) | s26 auto | CTA -> s28 |
| s28 | 271:6046 | 8 | Optical power check (animated counter 0 -> -21 dB) | s27 CTA | CTA -> s29 (after -21 reached) |
| s29 | 271:6945 | 8 | Speed test (Lottie speedometer, 0 -> 89 Mbps) | s28 CTA | Lottie complete -> bs-speed -> s30 |
| s30 | 271:7604 | 8 | Recharge info audio (10.3s). Ghost CTA. | bs-speed selection | Ghost CTA -> s31 |
| s31 | 271:7162 | 8 | Happy code rating prompt (illustration + CTA) | s30 CTA | CTA -> s32 |
| s32 | 271:7507 | 8 | Happy code OTP entry (4-digit keypad) | s31 CTA | Auto -> s33 (after 4 digits) |
| s33 | 198:68615 | 8 | Lottery celebration (full-screen image) | s32 auto | Tap bottom -> `resetFlow()` -> s1 |

### Overlays

| ID | Description | Trigger |
|----|-------------|---------|
| ov-3pin | 3-pin plug dialog with image + checkbox | s4 CTA "setup shuru" |
| ov-arrival | "Are you at customer's home?" | ov-3pin checkbox |
| ov-wifi | Android Material 3 WiFi connect dialog | s16 CTA `onNetboxReady()` |
| ov-isp-sheet | ISP account creation choice (portal vs office) | s15 ghost CTA |
| ov-complete | Setup complete celebration | `showComplete()` (currently unused, superseded by s12->s13 flow) |
| ov-exit | Exit confirmation ("setup par kaam jaari hai") | `confirmExit()` from close buttons |

### Bottom Sheets

| ID | Description | Trigger |
|----|-------------|---------|
| bs-customer | Customer details (name, address, Aadhaar photos) | s16 "customer details dekhen" link |
| bs-speed | Speed confirmation ("kya speed sahi aa rahi hai?") | s29 Lottie complete (after 1s) |

---

## 3. State Management

### Global variables

```javascript
var currentScreen = 's1';           // Active screen ID
var APK_VERSION = 'v29';            // Bump on each deploy to clear stale state
var resumeScreen = null;            // Saved screen for app-kill resume
var camStream = null;               // Active camera MediaStream
var captureCanvas = document.createElement('canvas');  // Reusable for snapPhoto()
var happyCode = '';                 // 4-digit OTP accumulator (s32)
var aadhaarState = {front:false, back:false, frontData:null, backData:null};
var netboxPhotoData = null;         // Captured Net Box photo (s18)
var threepinPhotoData = null;       // Captured 3-pin photo (s21)
var wiringPhotoData = null;         // Captured wiring photo (s24)
```

### Timer/interval variables

```javascript
var s3ProgressInterval = null;      // PayG acceptance audio progress
var paygProgressInterval = null;    // PayG system info audio progress
var ispProgressInterval = null;     // ISP recharge audio progress
var s30ProgressInterval = null;     // Recharge info audio progress
var timerInterval = null;           // s13 power-up countdown
var s17TimerInterval = null;        // s17 placement countdown
var s20TimerInterval = null;        // s20 3-pin countdown
var s23TimerInterval = null;        // s23 wiring countdown
var s28Interval = null;             // s28 optical power counter
var s29Anim = null;                 // s29 Lottie animation instance
```

### localStorage keys

| Key | Type | Purpose |
|-----|------|---------|
| `wiom_resume_screen` | string (screen ID) | Resume point after app kill. Set by `go()` for flow screens (not s1/s2). Cleared by `resetFlow()`, `exitSetup()` -> s2, and ov-complete. |
| `wiom_payg_accepted` | `'1'` or absent | PayG consent persists across sessions. Skip s3 on resume if set. Cleared by `resetFlow()`. |
| `wiom_apk_version` | string | Compared against `APK_VERSION` at startup. Mismatch clears all state (new APK install). |

### Startup sequence (IIFE at load)

1. Check `APK_VERSION` vs `localStorage.wiom_apk_version`. If mismatch: clear `wiom_resume_screen` and `wiom_payg_accepted`, update stored version.
2. Read `resumeScreen` from `localStorage.wiom_resume_screen`.
3. Remove `.on` from all screens. Add `.on` to s1. Set `currentScreen = 's1'`.
4. App always launches on s1. User taps card CTA to enter flow.

### Resume logic

`resumeFlow()` (called from s1 card 1 CTA):
- If `resumeScreen` exists: update step ring, navigate to saved screen (skip s3 if PayG already accepted).
- If no `resumeScreen`: go to s3 (or s4 if PayG accepted).

`startFreshFlow()`:
- Clears `wiom_resume_screen` only (NOT PayG consent). Goes to s2.

`exitSetup()`:
- Saves `currentScreen` to `localStorage`. Updates step ring on s2. Goes to s2.

---

## 4. Step Map

The step progress ring on the task cards maps screen IDs to step numbers (out of 8):

```javascript
function getStepCount(screenId) {
  var map = {
    's3':1, 's4':1,
    's5':2, 's6':2,
    's7':3, 's8':3,
    's8c':4, 's9':4,
    's10':5, 's11':5,
    's15':6, 's16':6,
    's17':7, 's18':7, 's19':7, 's20':7, 's21':7, 's22':7, 's23':7, 's24':7, 's25':7,
    's26':8, 's27':8, 's28':8, 's29':8, 's30':8, 's31':8, 's32':8, 's33':8,
    's12':8, 's13':8, 's14':8
  };
  return map[screenId] || 0;
}
```

Step ring rendering: SVG circle, circumference = 94.25, `stroke-dashoffset = 94.25 - (94.25 * steps / 8)`.

| Step | Screens | Phase |
|------|---------|-------|
| 1 | s3, s4 | PayG consent + transfer info |
| 2 | s5, s6 | Selfie capture |
| 3 | s7, s8 | Aadhaar capture |
| 4 | s8c, s9 | PayG system info |
| 5 | s10, s11 | Net Box switch-on + customer details |
| 6 | s15, s16 | ISP details |
| 7 | s17--s25 | Placement, photos (netbox, 3-pin, wiring) |
| 8 | s26--s33, s12--s14 | Provisioning, speed test, happy code, lottery |

---

## 5. Audio Screens Pattern

All audio screens follow one pattern:

1. `<audio>` element with `preload="auto"` and `src` pointing to `img/*.mp3`
2. Start function resets UI, plays audio at `currentTime=0`, starts progress bar interval
3. Progress bar: `<div>` with `width:0%` animated via `setInterval` (200ms ticks) over audio duration
4. After audio + buffer: hide progress bar, reveal CTA (checkbox or ghost button)

| Screen | Audio file | Duration | Buffer | After-audio action | Next |
|--------|-----------|----------|--------|-------------------|------|
| s3 | `payg_acceptance.mp3` | 10s | +0.25s | Show checkbox (agree to PayG) | s4 |
| s8c | `payg_precheck.mp3` | 17s | +1.5s | Show ghost CTA "okay" | s12 |
| s13 | `power_on.mp3` | 8s (within 15s timer) | -- | Timer reaches 0 | s14 |
| s15 | `isp_recharge.mp3` | 9.2s | +1.3s | Show ghost CTA "okay" | ov-isp-sheet |
| s17 | `netbox_placement.mp3` | 10s | +2s | Replace timer with checkbox | s18 (via checkbox) |
| s20 | `threepin_instructions.mp3` | 2s | +2.5s | Replace timer with checkbox | s21 (via checkbox) |
| s23 | `wiring_instructions.mp3` | 12.3s | +1.2s | Replace timer with checkbox | s24 (via checkbox) |
| s30 | `recharge_info.mp3` | 10.3s | +0.7s | Show ghost CTA "samajh gaya" | s31 |

### Audio screen template

```
<div class="scr" id="sN">
  <audio id="sN-audio" preload="auto" src="img/audio_file.mp3"></audio>
  <div class="bd" style="padding:194px 16px 0 16px">
    <!-- Speaker illustration 108x108 -->
    <!-- Headline text -->
    <!-- Subtitle text (optional) -->
  </div>
  <!-- Progress bar -->
  <div id="sN-bar-wrap" style="padding:0 24px 16px;flex-shrink:0">
    <div style="width:100%;height:4px;background:#F1EDF7;border-radius:2px;overflow:hidden">
      <div id="sN-progress" style="width:0%;height:100%;background:#D9008D;..."></div>
    </div>
  </div>
  <!-- CTA (hidden initially) -->
  <div id="sN-cta" style="display:none;padding:0 16px 24px;flex-shrink:0">
    <button ...>CTA text</button>
  </div>
</div>
```

### Timer screens (s17, s20, s23) variant

These show a 00:59 countdown timer + spinner while audio plays, then swap to a checkbox after audio+buffer.

```
<div id="sN-timer-state">  <!-- visible initially -->
  <div class="spinner"></div>
  <span id="sN-timer">00:59</span>
  <span>hint text</span>
</div>
<div id="sN-checkbox-state" style="display:none">  <!-- shown after audio -->
  <div class="cb"></div>
  <span>confirmation text</span>
</div>
```

---

## 6. Camera Screens Pattern

### Core camera functions

```javascript
startCam(videoId, facing)
// - Stops existing stream
// - Requests getUserMedia with {facingMode: {ideal: facing}}
// - Fallback: retries with {video: true} if facingMode fails
// - Sets stream on video element

stopCam()
// - Stops all tracks on camStream
// - Sets camStream = null

snapPhoto(videoId)
// - Returns null if video not ready
// - Draws video frame to captureCanvas
// - Returns canvas.toDataURL('image/jpeg', 0.85)
```

### Camera screen registry

| Screen | Video element ID | Facing | Purpose | Capture function |
|--------|-----------------|--------|---------|-----------------|
| s5 | `cam-selfie` | `'user'` (front) | Selfie with Wiom T-shirt | `captureSelfie()` |
| s7 | `cam-aadhaar` | `'environment'` (rear) | Aadhaar card (front or back) | `captureAadhaar()` |
| s18 | `cam-netbox` | `'environment'` (rear) | Net Box placement photo | `captureNetboxPhoto()` |
| s21 | `cam-threepin` | `'environment'` (rear) | 3-pin plug photo | `captureThreepinPhoto()` |
| s24 | `cam-wiring` | `'environment'` (rear) | Wiring + Net Box photo | `captureWiringPhoto()` |

### Camera lifecycle in go()

```javascript
// Auto-start on enter
if(id==='s5')  startCam('cam-selfie','user');
if(id==='s7')  startCam('cam-aadhaar','environment');
if(id==='s18') startCam('cam-netbox','environment');
if(id==='s21') startCam('cam-threepin','environment');
if(id==='s24') startCam('cam-wiring','environment');

// Auto-stop on leave (any non-camera screen)
if(id!=='s5' && id!=='s7' && id!=='s18' && id!=='s21' && id!=='s24') stopCam();
```

### Camera screen layout

Two variants:

**Full-screen camera (s7, s18, s21, s24):**
```html
<div style="position:absolute;inset:0;background:#4D4D4D;z-index:1">
  <video id="cam-*" autoplay playsinline style="width:100%;height:100%;object-fit:cover"></video>
</div>
<!-- Header overlay: position:relative;z-index:2, white text -->
<!-- Shutter button: position:absolute;bottom:48px;z-index:2 -->
```

**Selfie camera (s5):**
```html
<div style="background:#000;flex:1;display:flex;flex-direction:column">
  <video id="cam-selfie" style="transform:scaleX(-1)"></video>  <!-- mirrored -->
</div>
```

### Photo review pattern (s6, s19, s22, s25)

```html
<div class="bd">
  <div style="position:relative;border-radius:10px;overflow:hidden">
    <img id="*-preview" style="width:100%;height:502px;object-fit:cover" />
    <!-- Retake X: absolute top-right, 36x36 circle, semi-transparent -->
    <div onclick="retake*()">close icon</div>
  </div>
</div>
<div style="padding:16px;flex-shrink:0">
  <button class="cta" onclick="go('next')">CTA text</button>
</div>
```

---

## 7. Animation Screens

### s28: Optical Power Counter

```javascript
function startOpticalPowerCheck() {
  var current = 0;
  // Count from 0 to -21, 150ms per step
  s28Interval = setInterval(function(){
    current--;
    valueEl.textContent = current + ' dB';
    if(current <= -21) {
      // Stop, show green, reveal CTA
      clearInterval(s28Interval);
      valueEl.style.color = '#008043';
      statusEl.textContent = 'optical power sahi hai';
      ctaWrap.style.visibility = 'visible';
    }
  }, 150);
}
```
Total animation time: 21 steps x 150ms = ~3.15s.

### s29: Lottie Speed Test

- Lottie library loaded via `img/lottie.min.js`
- Animation data **inlined as JS variable** `SPEEDMETER_DATA` (line 12 of index.html, ~10KB JSON)
- NOT loaded from file (WebView blocks XHR to `file://` paths)

```javascript
function startSpeedTest() {
  s29Anim = lottie.loadAnimation({
    container: document.getElementById('s29-lottie'),
    renderer: 'svg',
    loop: false,
    autoplay: true,
    animationData: SPEEDMETER_DATA  // inlined JSON, NOT a path
  });

  s29Anim.addEventListener('enterFrame', function(e) {
    var pct = e.currentTime / totalFrames;
    var speed = Math.round(pct * 89);
    speedEl.textContent = speed + ' Mbps';
    speedEl.style.color = speed >= 80 ? '#008043' : '#161021';
  });

  s29Anim.addEventListener('complete', function() {
    speedEl.textContent = '89 Mbps';
    setTimeout(function(){ showBs('bs-speed'); }, 1000);
  });
}
```

### s12: Payment Checklist Animation

6-state sequential animation using `setTimeout` chain:

| Time | State | Action |
|------|-------|--------|
| 0s | Initial | WiFi done (green tick), Aadhaar + Payment pending (grey circles) |
| 1.0s | 2 | Aadhaar: grey circle -> pink spinner |
| 1.6s | 3 | Aadhaar: spinner -> green tick. Bar 2 turns green. |
| 2.2s | 4 | Payment: grey circle -> pink spinner |
| 3.0s | 5 | Payment text changes to "payment karein". PayG info card appears. |
| 4.2s | 6 | Payment: spinner -> green tick. Text: "payment ho gayi". PayG card hidden. |
| 5.4s | -- | Auto-navigate to s13 |

### s13: Power-Up Countdown Timer

- Starts at 15 seconds (production would be 59)
- Plays `power_on.mp3` (8s IVR audio)
- Timer counts down 1/second
- At 0: auto-navigates to s14

---

## 8. Kotlin WebView Shell

**File:** `app/src/main/kotlin/com/wiom/partner/MainActivity.kt`

### WebView configuration

```kotlin
webView.settings.apply {
    javaScriptEnabled = true
    domStorageEnabled = true        // localStorage works
    allowFileAccess = true
    allowContentAccess = true
    mediaPlaybackRequiresUserGesture = false  // audio autoplay works
    cacheMode = WebSettings.LOAD_DEFAULT
}
```

### URL interception

`shouldOverrideUrlLoading` intercepts:
- `tel:` -- opens phone dialer
- `whatsapp:` -- opens WhatsApp
- `https://wa.me` -- opens WhatsApp

All other URLs load normally in WebView.

### WebChromeClient

- `onPermissionRequest` -- auto-grants all (camera for getUserMedia)
- `onShowFileChooser` -- launches native camera via `ACTION_IMAGE_CAPTURE`
- Camera photos saved via `FileProvider` at `com.wiom.partner.fileprovider`

### NativeBridge JS Interface

Accessible in JS as `window.Android`:

```kotlin
@JavascriptInterface fun openExternal(url: String)
// Opens URL in system browser/app

@JavascriptInterface fun getBatteryLevel(): Int
// Returns 0-100 battery percentage

@JavascriptInterface fun isWifiConnected(): Boolean
// Returns true if connected to WiFi

@JavascriptInterface fun saveImage(dataUrl: String, filename: String): String
// Decodes base64, saves to Pictures/wiom aadhar card images/*.jpg
// Returns file path or "error:message"
// Shows Toast on success/failure
```

### Permissions

- `CAMERA` -- requested at startup (`onCreate`) + on file chooser
- `INTERNET` -- for Google Fonts (CDN), WhatsApp links
- `WRITE_EXTERNAL_STORAGE` (maxSdkVersion 28) -- legacy image save

### AndroidManifest.xml

- `screenOrientation="portrait"` -- locked
- `configChanges="orientation|screenSize|keyboardHidden"` -- prevents activity recreation
- `usesCleartextTraffic="true"` -- allows HTTP (for review server)

---

## 9. CSS Design Tokens

All tokens defined as CSS custom properties on `:root`:

### Brand

| Property | Value | Usage |
|----------|-------|-------|
| `--brand` | `#D9008D` | Primary CTA, links, progress bars, brand accents |
| `--brand-light` | `#FFE5F6` | Light brand background (secondary buttons) |
| `--brand-sec` | `#FFCCED` | Brand secondary (step ring track, selected ISP radio bg) |

### Headers

| Property | Value | Usage |
|----------|-------|-------|
| `--header` | `#443152` | Dark header background, status bar |
| `--header-light` | `#60506C` | Rating pill inner background |

### Text

| Property | Value | Usage |
|----------|-------|-------|
| `--pri` | `#161021` | Primary text color |
| `--sec` | `#665E75` | Secondary text |
| `--hint` | `#A7A1B2` | Hint text, placeholders, disabled |

### Surfaces

| Property | Value | Usage |
|----------|-------|-------|
| `--bg` | `#FAF9FC` | Page background |
| `--card` | `#FFFFFF` | Card background |
| `--n100` | `#F1EDF7` | Lavender background (checklist, prompts) |
| `--n200` | `#E8E4F0` | Subtle borders, spinner track |

### Borders

| Property | Value | Usage |
|----------|-------|-------|
| `--border` | `#E8E4F0` | Standard border |
| `--border-focus` | `#352D42` | Focused input border |

### Semantic

| Property | Value | Usage |
|----------|-------|-------|
| `--pos` | `#008043` | Success (green ticks, optical power, speed) |
| `--neg` | `#D92130` | Error (late state, red indicators) |
| `--warn` | `#FF8000` | Warning icon |
| `--warn-txt` | `#B85C00` | Warning text |
| `--warn-bg` | `#FFE6CC` | Warning background |
| `--info` | `#6D17CE` | Info purple (PayG tags) |
| `--info-bg` | `#F1E5FF` | Info background (purple cards) |
| `--cta-dis` | `#A7A1B2` | Disabled CTA |
| `--green-bg` | `#C9F0DD` | Green circle backgrounds (success states) |

### Shadows

| Property | Value | Usage |
|----------|-------|-------|
| `--sh1` | `0 1px 3px rgba(0,0,0,0.15)` | Card shadow, header shadow |
| `--sh2` | `0 2px 6px rgba(0,0,0,0.15)` | Overlay dialog shadow |

### Hardcoded colors (NOT tokenized, used inline)

| Color | Where |
|-------|-------|
| `#161021` | Direct text color (same as `--pri`) |
| `#352D42` | Close icon color, focused border |
| `#D9D9D9` | Photo placeholder grey |
| `#F9DFEE` | Pink hint bar background (s32) |
| `#FFCCED` | Selected card highlight |
| `#C9F0DD` | Green circle bg (also `--green-bg`) |
| `#E1FAED` | ISP prefill card bg (green) |
| `#A5E5C6` | ISP prefill card border |
| `#FFE6CC` | Warning bg (also `--warn-bg`) |
| `#FFD3CC` | Backspace key bg (s32 keypad) |
| `#E01E00` | Backspace icon color |
| `#FFD888` | Lottery CTA yellow |
| `#FFBDB3` | Lottery CTA border |

---

## 10. Build & Deploy

### Prerequisites

| Tool | Path |
|------|------|
| JDK 17 | `C:/Program Files/Microsoft/jdk-17.0.18.8-hotspot` |
| Android SDK | `C:/Users/Abhishek Semwal/AppData/Local/Android/Sdk` |
| ADB | `C:/Users/Abhishek Semwal/AppData/Local/Android/Sdk/platform-tools/adb.exe` |

### Build

From project root (`C:/Users/Abhishek Semwal/wiom-apk/`):

```bash
# Using Gradle wrapper (recommended)
./gradlew assembleDebug

# Or with explicit JDK:
"C:/Program Files/Microsoft/jdk-17.0.18.8-hotspot/bin/java" \
  -classpath "gradle/wrapper/gradle-wrapper.jar" \
  org.gradle.wrapper.GradleWrapperMain assembleDebug
```

### APK output

```
app/build/outputs/apk/debug/app-debug.apk
```

### Deploy to device

```bash
ADB="C:/Users/Abhishek Semwal/AppData/Local/Android/Sdk/platform-tools/adb.exe"

# List connected devices
"$ADB" devices

# Install (replace existing)
"$ADB" -s <device_id> install -r app/build/outputs/apk/debug/app-debug.apk
```

Target device: OPPO CPH2649 (serial `190c6d27`).

### Review server deploy

The review server has a one-click build+deploy endpoint:

```bash
curl -X POST http://localhost:8080/api/deploy
```

This runs `gradlew assembleDebug` + `adb install -r` in sequence.

### Gradle configuration

- **AGP:** 8.2.0
- **Kotlin:** 1.9.22
- **minSdk:** 24 (Android 7.0)
- **targetSdk:** 34 (Android 14)
- **compileSdk:** 34
- **JVM target:** 17

---

## 11. File Structure

```
wiom-apk/
  build.gradle                    # Top-level: AGP 8.2.0, Kotlin 1.9.22
  settings.gradle                 # Root project "WiomPartner", includes :app
  gradle.properties               # useAndroidX=true, jvmargs
  local.properties                # sdk.dir
  gradlew / gradlew.bat           # Gradle wrapper scripts
  gradle/wrapper/                 # Gradle wrapper JAR + properties
  review-server.js                # Node.js dev server (port 8080)
  design-review.json              # Latest review feedback (written by review tool)
  FLOW_MAP.md                     # Figma prototype flow documentation
  VISUAL_AUDIT.md                 # Visual audit notes
  SESSION_CONTEXT.md              # Session-specific context
  SPEC_AI.md                      # THIS FILE
  backups/                        # Auto-saved APK + HTML snapshots (every 30 min)
    review-history/               # Timestamped review JSON backups
  screenshots/                    # Screenshot captures
  app/
    build.gradle                  # App module: dependencies, SDK config
    src/main/
      AndroidManifest.xml         # Permissions, activity, FileProvider
      kotlin/com/wiom/partner/
        MainActivity.kt           # WebView shell + NativeBridge
      assets/
        index.html                # THE APP (2770 lines, all screens)
        review.html               # Review tool UI (served by review-server.js)
        img/
          lottie.min.js           # Lottie animation library
          *.mp3                   # 8 audio files (see Audio section)
          *.svg                   # Icons and illustrations
          *.png / *.webp          # Photos, illustrations, UI assets
      res/
        xml/file_paths.xml        # FileProvider paths config
        values/                   # Themes, strings
        mipmap-*/                 # App icon
```

### Key files by edit frequency

| File | What you edit | How often |
|------|--------------|-----------|
| `app/src/main/assets/index.html` | Screens, CSS, JS logic | Every change |
| `app/src/main/kotlin/.../MainActivity.kt` | Native bridge, permissions | Rarely |
| `app/build.gradle` | Dependencies, SDK versions | Rarely |
| `review-server.js` | Dev tooling endpoints | Occasionally |

---

## 12. Adding New Screens

Step-by-step guide for adding screen `s34`:

### Step 1: Add HTML

Insert a new `<div class="scr">` before the `<script>` tag (but after the last screen and overlays):

```html
<!-- ===== S34: SCREEN NAME (Figma NODE_ID) ===== -->
<div class="scr" id="s34">
  <!-- Header (pick variant: dark, light, camera, or none) -->
  <div style="display:flex;align-items:center;padding:4px;background:#FAF9FC">
    <button style="background:none;border:none;cursor:pointer;display:flex;padding:12px"
      onclick="confirmExit()">
      <span class="mi" style="font-size:24px;color:#352D42">close</span>
    </button>
    <div style="flex:1"></div>
    <div style="padding:12px">
      <span style="font-size:14px;font-weight:600;color:var(--brand);cursor:pointer">help</span>
    </div>
  </div>

  <!-- Scrollable body -->
  <div class="bd" style="padding:0 16px">
    <!-- Content here -->
  </div>

  <!-- CTA pinned to bottom -->
  <div style="padding:16px;flex-shrink:0;background:#FAF9FC">
    <button class="cta" onclick="go('s35')">CTA Text</button>
  </div>
</div>
```

### Step 2: Register in go() function

Add any auto-actions inside the `go()` function's switch block:

```javascript
// In go(id) function, before the closing }
if(id==='s34'){ /* start camera, play audio, run animation, etc. */ }
```

### Step 3: Add to step map

Update `getStepCount()`:

```javascript
var map = {
  // ... existing entries ...
  's34': 8  // or appropriate step number
};
```

### Step 4: Wire navigation

Update the CTA or auto-transition on the PREVIOUS screen to call `go('s34')`.

### Step 5: Add supporting JS (if needed)

Add functions at the bottom of the `<script>` block. Follow existing naming conventions:
- Audio: `startS34Audio()`, interval var `s34ProgressInterval`
- Camera: `captureS34Photo()`, data var `s34PhotoData`, retake function
- Timer: `s34TimerInterval`, countdown function

### Step 6: Add assets

Place images/audio in `app/src/main/assets/img/`. Use `.webp` for photos, `.svg` for icons, `.mp3` for audio.

---

## 13. Common Patterns

### Header variants

**Dark header (s1 only):**
```html
<div style="background:var(--header);height:56px;padding:12px 16px;display:flex;align-items:center">
  <!-- Logo + name + rating + menu -->
</div>
```

**Light header (most screens):**
```html
<div style="display:flex;align-items:center;padding:4px;background:#FAF9FC">
  <button onclick="confirmExit()"><span class="mi" style="color:#352D42">close</span></button>
  <div style="flex:1"></div>
  <div style="padding:12px"><span style="color:var(--brand);font-size:14px;font-weight:600">help text</span></div>
</div>
```

**Camera header (transparent, white text):**
```html
<div style="position:relative;z-index:2">
  <div style="display:flex;align-items:center;padding:4px">
    <button onclick="stopCam();go('prev')"><span class="mi" style="color:#FAF9FC">close</span></button>
  </div>
  <div style="padding:8px 16px 24px">
    <p style="font-size:24px;font-weight:700;color:#FAF9FC">Title</p>
  </div>
</div>
```

### CTA positioning

```html
<!-- Standard: pinned to bottom, no shadow -->
<div style="padding:0 16px 24px;flex-shrink:0">
  <button class="cta" onclick="go('next')">Text</button>
</div>

<!-- With shadow separator (ISP form) -->
<div style="padding:16px;flex-shrink:0;background:#FAF9FC;box-shadow:0 -2px 8px rgba(0,0,0,0.1)">
  <button class="cta" onclick="action()">Text</button>
</div>

<!-- Ghost CTA (audio screens, no border, brand text on white) -->
<button style="width:100%;height:48px;border-radius:16px;background:#FAF9FC;border:none;color:#D9008D;
  font-family:inherit;font-size:16px;font-weight:700;cursor:pointer;display:flex;align-items:center;
  justify-content:center" onclick="go('next')">Text</button>
```

### Checkbox component

```html
<div style="display:flex;align-items:center;gap:12px;cursor:pointer" onclick="toggleLogic(this)">
  <div class="cb" id="unique-cb-id"></div>
  <p style="font-size:16px;font-weight:700;color:#161021">Label text</p>
</div>
```

- `.cb` -- 24x24 using `checkbox_unchecked.svg` background
- `.cb.on` -- swaps to `checkbox_checked.svg`
- Toggle: `c.classList.add('on')` then `setTimeout(() => go('next'), 450)`

### Dialog overlay

```html
<div class="ov" id="ov-name">
  <div class="fi" style="background:var(--card);border-radius:24px;padding:32px 24px;
    width:calc(100% - 48px);max-width:312px;box-shadow:var(--sh2)">
    <!-- Optional illustration -->
    <img src="img/graphic.svg" style="width:96px;height:96px;margin-bottom:24px" />
    <!-- Title -->
    <p style="font-size:24px;font-weight:700;margin-bottom:24px">Title</p>
    <!-- Action -->
    <button style="width:100%;height:48px;border-radius:16px;background:var(--brand);color:#faf9fc;
      font-family:inherit;font-size:16px;font-weight:700;border:none;cursor:pointer"
      onclick="hideOv('ov-name');nextAction()">Button</button>
  </div>
</div>
```

`.fi` class applies `fadeIn` animation (0.3s ease, translateY 8px -> 0).

### Bottom sheet

```html
<div class="bs-overlay" id="bs-name" onclick="if(event.target===this)hideBs('bs-name')">
  <div class="bs-panel" style="padding-bottom:48px">
    <!-- Drag handle -->
    <div style="padding:12px 0 24px;display:flex;justify-content:center">
      <div style="width:120px;height:4px;background:#D7D3E0;border-radius:4px"></div>
    </div>
    <!-- Title -->
    <div style="padding:0 16px 8px">
      <p style="font-size:24px;font-weight:700">Title</p>
    </div>
    <!-- Content -->
    <div style="padding:24px 16px">
      <!-- ... -->
    </div>
  </div>
</div>
```

### ISP form field (DS Input-Field states)

CSS classes on `.isp-field`:
- Default: label bold, border `#D7D3E0`
- `.active` (focused): label normal weight, border `--border-focus` (`#352D42`), caret `#D9008D`
- `.filled` (blurred with value): label normal weight, border `#D7D3E0`, text bold
- `.validated`: border `#008043` (green)
- `.prefilled`: bg `#D7D3E0`, text `#665E75`, readonly
- `.error`: border `#D92130`, shows `.isp-error` subtext

```html
<div class="isp-field">
  <label>Field Name</label>
  <div class="isp-input">
    <span class="mi">icon_name</span>
    <input type="text" placeholder="Placeholder" id="field-id" />
  </div>
</div>
```

### Aadhaar 3-state flow (s8)

The Aadhaar screen manages 3 visual states via show/hide:

| State | Visible elements | Trigger |
|-------|-----------------|---------|
| 1: Front prompt | `s8-front-prompt` | Initial / `retakeFront()` |
| 2: Front captured + back prompt | `s8-review` | First `captureAadhaar()` |
| 3: Both captured | `s8-both` + `s8-cta` | Second `captureAadhaar()` |

State tracked in `aadhaarState = {front, back, frontData, backData}`.

### Shake animation (guide user to checkbox)

Some body divs have `onclick="shakeCheckbox('cb-row-id')"` which applies a 0.5s horizontal shake animation to draw attention to an untapped checkbox.

---

## 14. Known Issues / Constraints

### No backend
- All data is hardcoded (customer name "Himanshu Singh", address, family number)
- ISP validation is simulated (1.5s spinner, always succeeds)
- Payment is simulated (checklist animation, no real transaction)
- Speed test shows fixed 89 Mbps result
- Happy code accepts any 4 digits

### Camera
- Requires runtime `CAMERA` permission grant (requested at startup)
- `getUserMedia` fallback chain: ideal facingMode -> any camera
- Front camera mirror: `transform: scaleX(-1)` on selfie video
- Photo quality: JPEG 85%

### Lottie
- Animation data MUST be inlined as a JS variable (`SPEEDMETER_DATA`)
- `animationData` property, NOT `path` -- WebView blocks XHR to `file://` URLs
- Library loaded from local `img/lottie.min.js`

### Audio
- `mediaPlaybackRequiresUserGesture = false` set in Kotlin -- autoplay works
- Audio files preloaded via `<audio preload="auto">`
- Some devices may still require a user gesture for first audio play (caught with `.catch()`)

### Fonts
- Google Fonts loaded via CDN (requires internet on first launch, cached after)
- Material Icons Round + Outlined both loaded
- Noto Sans Devanagari for Hindi, Noto Sans for English
- Poppins used only for the timer display on s7

### WebView quirks
- `localStorage` persists across app kills (Android WebView default)
- `file:///android_asset/` URLs cannot make XHR/fetch to external URLs (CORS)
- Back button: `onBackPressed` calls `webView.goBack()` (navigates WebView history)
- Status bar color set to `#443152` in Kotlin (matches header)

### Review tool
- `review-server.js` serves `app/src/main/assets/` as static files on port 8080
- Review tool at `http://localhost:8080/review.html`
- Review feedback saved to `design-review.json`
- `/api/reload` endpoint signals the review tool to refresh
- APK backups auto-saved every 30 minutes to `backups/`

### Screen ordering in HTML
- Screens are NOT in flow order in the HTML. s12/s13/s14 appear AFTER s25.
- The `go()` function handles all navigation regardless of DOM order.
- When adding screens, position doesn't matter for functionality, but prefer inserting near related screens for readability.

### Timer durations
- s13 power-up timer: 15s (testing value; production would be 59s)
- s17/s20/s23 visual countdown: starts at 59, but checkbox appears based on audio duration + buffer (not timer value)

---

## 15. Quick Reference: go() Auto-Actions

Complete list of actions triggered by `go(id)`:

```
go('s3')  -> startPaygAcceptanceAudio()
go('s5')  -> startCam('cam-selfie', 'user')
go('s7')  -> startCam('cam-aadhaar', 'environment')
go('s8c') -> startPaygAudio()
go('s9')  -> setTimeout(-> go('s10'), 2000)
go('s11') -> populate Aadhaar images from aadhaarState
go('s12') -> runChecklist()
go('s13') -> startPowerUpTimer()
go('s15') -> startIspAudio()
go('s17') -> startPlacementCheck()
go('s18') -> startCam('cam-netbox', 'environment')
go('s20') -> startThreepinCheck()
go('s21') -> startCam('cam-threepin', 'environment')
go('s23') -> startWiringCheck()
go('s24') -> startCam('cam-wiring', 'environment')
go('s26') -> setTimeout(-> go('s27'), 3000)
go('s28') -> startOpticalPowerCheck()
go('s29') -> startSpeedTest()
go('s30') -> startRechargeAudio()
go('s32') -> reset happyCode + updateHappyCodeDisplay()

// Leaving any non-camera screen: stopCam()
// Leaving s29: hide bs-speed
// All flow screens (not s1/s2): save to localStorage
```
