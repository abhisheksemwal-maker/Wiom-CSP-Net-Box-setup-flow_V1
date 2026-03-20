# Wiom Expert App — Net Box Setup Flow
## Design Specification for Human Reviewers

**Version:** 1.0
**Date:** 20 March 2026
**Prototype:** Android APK (WebView), single HTML file with JS navigation
**Screens:** 33 screens + 5 overlays/bottom sheets
**Language:** Hindi (primary), English (secondary/labels)
**Figma source:** File `PwrhO9KnvdZ4NNYwz3B85d`, section "Test android flow"

---

## 1. Overview

### What this prototype covers
A complete, linear walk-through of the Wiom Net Box (WiFi router) installation flow as performed by a field technician ("Expert") or Partner. The flow begins at the task list and ends at a lottery/reward screen, covering every step a technician performs on-site at a customer's home.

### Who uses this app
- **Wiom Experts** (technicians employed by Wiom) — tasks are assigned via FCM push notification
- **Partners** (independent operators using Partner Plus App) — can self-assign tasks or delegate to team members

The setup journey is identical for both user types. Only task assignment differs.

### Business model context
Wiom operates a **Pay-As-You-Go (PayG)** model:
- Customer pays only **Rs 300 security deposit** at installation
- Customer gets **2 days free internet** after setup, then recharges as needed (Rs 23/day, Rs 100/7 days)
- The technician must NOT collect any payment beyond Rs 300
- ISP plan recharge (100 Mbps, 30-day) is an internal system action, not a customer payment

### Design system
- **Brand pink:** `#D9008D`
- **Header purple:** `#443152`
- **Background:** `#FAF9FC`
- **Font:** Noto Sans Devanagari (Hindi), Noto Sans (English)
- **Icons:** Material Icons Round + custom Figma SVG exports
- **CTA buttons:** Full-width, 48px height, 16px border-radius, brand pink background
- **Cards:** White background, 1px border `#E8E4F0`, 12-16px border-radius

---

## 2. Screen-by-Screen Walkthrough

### S1 — Task List (Home)
**Module:** 2 (Task List & Pre-Visit)
**Header:** Dark purple (`#443152`) with Wiom logo, partner name "Rohit Kumar Chaurasia", animated rating pill (4.8 star with silver rotating border stroke), hamburger menu.
**Tabs:** "मेरे काम" (active, white underline) | "मेरी कमाई" (inactive).
**Content:** Single pink task card (`#FFE5F6`) with:
- Count "02" (48px bold)
- Two overlapping customer avatars (32px circles)
- "कस्टमर प्रतीक्षा कर रहे हैं" (Customers are waiting)
- Circular pink CTA arrow button (72px, brand pink, forward arrow)

**Interaction:** Tap the task card to start a fresh flow (clears saved progress) and navigate to S2.
**Note:** App always launches on S1, even after a kill/restart. Flow progress is saved to localStorage but user must explicitly resume from S2.

---

### S2 — Task Detail (Snap-scroll Carousel)
**Module:** 2 (Task List & Pre-Visit)
**Header:** Dark purple app bar with back arrow (returns to S1).
**Content:** A dark-background screen with a snap-scrolling carousel of task cards. Each card shows:
- Customer name and address
- Step progress ring (SVG circle, e.g., "0/8", "3/8")
- Deadline information
- "काम पूरा करें" CTA (Complete the task)

**Interaction:** Tap "काम पूरा करें" to enter the setup flow. If there is saved progress (from a previous interrupted session), the step ring shows the last reached step and the flow resumes from that point. If PayG acknowledgement was already accepted, S3 is skipped.

---

### S3 — PayG Acknowledgement
**Module:** 3 (PayG Acknowledgement Gate)
**Condition:** Shown for the first 5 installations only (feature flag in production). In prototype, always shown unless previously accepted in the same session.
**Content:**
- Speaker illustration (108x108, left-aligned)
- Headline: "PayG सेटअप में सिर्फ Rs 300 सिक्योरिटी फीस होती है"
- Subtitle: "कस्टमर सिर्फ Rs 300 सिक्योरिटी फीस ही पे करता है, नेट बॉक्स के लिए"
- Audio progress bar (4px, pink on lavender track)

**Interaction:**
1. Audio (`payg_acceptance.mp3`, 10s) plays automatically on entry
2. Pink progress bar animates over 10 seconds
3. After audio completes (10.25s), progress bar hides and checkbox appears
4. Checkbox text: "मैंने समझ लिया है, इस सेटअप में Rs 300 के अलावा कोई और पेमेंट नहीं होती है"
5. Tapping checkbox fills it and auto-transitions to S4 after 450ms
6. Acceptance is persisted — returning to this screen skips it

**Edge case (production):** IVR audio plays at this step to reinforce the Rs 300-only rule. If technician has completed 5+ installations, this screen is skipped entirely.

---

### S4 — Transfer Info (Customer Details)
**Module:** 4 (3-Pin Carry Confirmation) + Module 5 (Arrival Confirmation)
**Header:** Light background, back arrow, title "नया सेटअप", three-dot menu.
**Content:**
- **Deadline card:** Bordered card with alarm icon: "12 January से पहले नेट चालू करना है"
- **Customer info card:** Elevated (shadow + border) with:
  - Person icon + "Himanshu Singh"
  - Call button (opens phone dialer) + Direction button
  - Pin icon + full address
  - Speed: "100 Mbps"
  - Family number row (hidden initially, revealed after call)
- **Info tip bar:** Purple background below card: "कस्टमर को कॉल करके, अपने पहुंचने का समय बता दें"
- **ISP prefill card:** Green card (hidden initially, revealed after call): "सेटअप पर 15 मिनट बचाएँ — अब ISP अकाउंट की डिटेल्स पहले भर लें"
- **CTA:** "सेटअप शुरू करें"

**Interaction:**
1. Tapping the call button opens the device phone dialer (`tel:+919876543210`)
2. When user returns to app, family number row and ISP prefill card are revealed (2s delay)
3. Tapping "सेटअप शुरू करें" triggers the **3-pin plug dialog** first

**Sub-overlay: 3-Pin Plug Dialog**
- Image of 3-pin plug on purple info background
- Title: "नेट बॉक्स के साथ 3-पिन प्लग लगाना भी ज़रूरी है"
- Warning box (orange border-left): "3-पिन के बिना सेट अप अधूरा रहेगा"
- Checkbox: "मैंने 3-पिन प्लग रख लिया है"
- Checking the box auto-dismisses (600ms) and opens the Arrival dialog

**Sub-overlay: Arrival Confirmation Dialog**
- House illustration (96x96)
- "क्या आप कस्टमर के घर पर हैं?"
- CTA: "हाँ, पहुँच गया हूँ" — navigates to S5

**Edge case (production):** The 3-pin dialog is gated by a partner-level feature flag. If the partner doesn't handle 3-pin distribution, this dialog is skipped. The arrival confirmation triggers a `ROHIT_ARRIVED` backend event.

---

### S5 — Selfie Camera
**Module:** 6 (Selfie Capture)
**Header:** Dark purple, back arrow, title "सेल्फी खींचें", "मदद" link.
**Content:**
- Full-screen front camera viewfinder
- Instruction text: "Wiom Expert की टी-शर्ट पहने हुए सेल्फी लें"
- Circular shutter button (64px, grey ring with white inner circle) at bottom center

**Interaction:** Tapping the shutter captures a still frame from the front camera video stream, stops the camera, and navigates to S6 with the captured image.

**Edge case (production):** Technician must be wearing the Wiom Expert T-shirt. The photo is uploaded for compliance verification.

---

### S6 — Selfie Review
**Module:** 6 (Selfie Capture)
**Header:** Dark purple, back arrow, title "रॉकस्टार! 🤩", "मदद" link.
**Content:**
- Captured selfie photo displayed in a rounded container
- "रॉकस्टार" compliment text (motivational feedback)
- CTA: "कस्टमर के आधार कार्ड की फोटो लें" — navigates to S8 (Aadhaar capture flow)

**Interaction:** Single CTA advances to Aadhaar capture. No retake option shown in prototype (production would offer retake).

---

### S7 — Aadhaar Camera (Rear)
**Module:** 7 (Aadhaar Capture / KYC)
**Content:**
- Full-screen rear camera viewfinder
- Dashed guide rectangle overlay (80% width, 200px height) to help frame the Aadhaar card
- Instruction text: "आधार कार्ड के आगे का फोटो लें" (or back side, depending on state)
- Shutter button at bottom

**Interaction:** Capture photo and return to S8 for review. This screen is visited twice — once for front, once for back of Aadhaar card.

---

### S8 — Aadhaar Capture Flow (3-state screen)
**Module:** 7 (Aadhaar Capture / KYC)
**Header:** Light background, close button, "मदद" link.
This screen has three internal states:

**State 1 — Front prompt:**
- Title: "आधार कार्ड की फोटो लें"
- "आगे की फोटो" section with camera icon and "फोटो लें" button
- "पीछे की फोटो" section (greyed out / pending)

**State 2 — Front captured, back prompt:**
- Front photo thumbnail displayed with "फिर से लें" retake option
- "पीछे की फोटो" now active with "फोटो लें" button
- Tapping "फोटो लें" opens S7 camera for back photo

**State 3 — Both captured:**
- Both front and back thumbnails displayed side by side
- Retake options for each
- Title hidden
- CTA: "आगे बढ़ें" — navigates to S8C

**Interaction:** Progressive capture flow. Front photo must be taken before back. Both photos are stored in JS memory and shown on later screens (S11 customer details, bottom sheet).

---

### S8C — PayG System Info (Audio Screen)
**Module:** 8 (PayG System Jankari)
**Header:** Light background, close button, "मदद" link.
**Content:**
- Speaker illustration with PayG branding
- Headline reinforcing the Rs 300-only rule
- Audio progress bar (pink on lavender track)

**Interaction:**
1. Audio (`payg_precheck.mp3`, 17s) plays automatically
2. Progress bar animates over 17 seconds
3. After 18.5s, progress bar hides and CTA "समझ गया" appears
4. CTA navigates to S12 (Payment Checklist) on earlier version, or S9 (Loading)

**Purpose:** Reinforces the PayG model before the technician collects payment. Ensures they understand only Rs 300 should be collected.

---

### S9 — Loading
**Module:** Transition screen
**Content:** Simple loading spinner with transition text.
**Interaction:** Auto-transitions to S10 (Switch-On) after 2 seconds.

---

### S10 — Switch-On Net Box
**Module:** 10 (Switch-On & Provisioning)
**Content:** Instructions to physically switch on the Net Box device. Image of the router with power button highlighted.
**Interaction:** Checkbox confirmation, then auto-transitions to S11.

---

### S11 — Customer Details
**Module:** 11 (Share Customer Details & ISP Profile Creation)
**Header:** Light background, close button, "मदद" link.
**Content:**
- Title: "कस्टमर डिटेल्स शेयर करें"
- Customer name, address, family number
- Aadhaar card thumbnails (populated from S8 captures)
- "Save" button to save Aadhaar images to device storage via Android bridge
- CTA: "ISP अकाउंट बनायें" — triggers ISP Bottom Sheet

**Bottom Sheet: Customer Details** (triggered by eye icon or info tap)
- Draggable bottom sheet (drag handle + drag-to-dismiss)
- Customer info card: name, address, family number
- Aadhaar card section with front/back thumbnails

**Interaction:** CTA opens the ISP method selection bottom sheet. The Aadhaar images are also populated into the customer details bottom sheet.

---

### S12 — Payment Checklist (6-state Animation)
**Module:** 9 (Payment Collection & Verification)
**Header:** Close button + "मदद" link.
**Title:** "Himanshu Singh जी से व्योम ऐप द्वारा पेमेंट करने को कहें"
**Content:** Purple card (`#F1EDF7`) with animated checklist:

| Time | State | Description |
|------|-------|-------------|
| 0s | State 1 | WiFi done (green tick), Aadhaar pending (grey circle), Payment pending (grey circle) |
| 1.0s | State 2 | Aadhaar shows pink arc spinner |
| 1.6s | State 3 | Aadhaar done (green tick), connector bar turns green |
| 2.2s | State 4 | Payment shows pink arc spinner |
| 3.0s | State 5 | Payment text changes to "पेमेंट करें", PayG info card appears below |
| 4.2s | State 6 | Payment done (green tick), text changes to "पेमेंट हो गयी", PayG card hides |
| 5.4s | Auto-advance | Navigates to S13 |

**Interaction:** Fully automated animation. No user input required. The 6-state animation simulates the backend payment verification process.

---

### S13 — Net Box Power-Up Timer
**Module:** 10 (Switch-On & Provisioning)
**Header:** Close button + "मदद" link.
**Content:**
- Large countdown timer display (starts at 00:15 in prototype, 00:59 in production)
- Timer text in large bold font
- Audio (`power_on.mp3`, 8s) plays automatically

**Interaction:** Timer counts down from 15 seconds (prototype) or 59 seconds (production = 300s provisioning wait). When timer reaches 00:00, auto-navigates to S14.

---

### S14 — Net Box Switch-On Confirmation
**Module:** 10 (Switch-On & Provisioning)
**Header:** Close button + "मदद" link.
**Title:** "व्योम नेट बॉक्स को स्विच ऑन करें"
**Content:**
- Net Box image with power button mask (328px wide, rounded corners)
- Checkbox: "नेट बॉक्स ऑन कर दिया है"

**Interaction:**
1. Tapping anywhere on the body area triggers a shake animation on the checkbox row (guides user to tap the checkbox specifically)
2. Checking the box auto-transitions to S11 (Customer Details) after 600ms

---

### S15 — ISP Recharge Audio
**Module:** 12 (PayG Education: Create 30-Day 100 Mbps Plan)
**Header:** Light background, close button, "मदद" link.
**Content:**
- Speaker illustration
- Educational content about the ISP 100 Mbps 30-day plan
- Audio progress bar

**Interaction:**
1. Audio (`isp_recharge.mp3`, 9.2s) plays automatically
2. Progress bar animates over 9.2 seconds
3. After 10.5s, CTA appears
4. CTA opens the ISP Bottom Sheet

**Purpose:** Educates technician that the ISP plan recharge is an internal Wiom action (not a customer payment).

---

### ISP Bottom Sheet
**Module:** 14 (Fill ISP Details)
**Content:** Two selection cards:
- "खुद से, पोर्टल पर" (Self, on portal) — icon of phone
- "ऑफिस भेजकर" (Send to office) — icon of office

**Interaction:**
1. Tapping a card highlights it (pink background `#FFCCED`, pink border `#D9008D`)
2. After 400ms micro-interaction delay:
   - "Portal" opens the ISP portal URL (`crmh.myion.in/Login.aspx`) in external Chrome browser
   - "Office" opens WhatsApp with pre-filled message
3. After 1s, navigates to S16 (ISP form)

---

### S16 — ISP Details Form (PPPoE)
**Module:** 14 (Fill ISP Details)
**Header:** Light background, close button, "मदद" link.
**Title:** "ISP डिटेल्स भरें"
**Content:** Progressive disclosure form with DS Input-Field component states:

**ISP Type Selection (Radio buttons):**
- PPPoE (default selected)
- Static IP
- DHCP
Each radio has: pink highlight when selected, brand-color dot

**PPPoE Form Fields (progressive reveal):**
1. **Username** — text input with inline submit arrow. On submit: spinner (1.5s) then green checkmark. Field becomes read-only. Reveals VLAN dropdown.
2. **Password** — password input with eye toggle for visibility
3. **VLAN** — custom dropdown (TAG / TRANSPARENT / UNTAG). Opens inline dropdown list. Selecting TAG reveals VLAN ID field. Selecting any value reveals Net Box ID field.
4. **VLAN ID** — number input (shown only if TAG selected, valid range 128-1492)
5. **Net Box ID** — text input with inline submit arrow. On submit: spinner (1.5s) then green checkmark. Field becomes read-only.

**Input Field States (DS Component 258:6045):**
| State | Label weight | Border color | Text weight | Background |
|-------|-------------|--------------|-------------|------------|
| Default | Bold (700) | `#D7D3E0` | — | `#FAF9FC` |
| Active (focused) | Normal (400) | `#352D42` | 700 | `#FAF9FC` |
| Filled (blurred) | Normal (400) | `#D7D3E0` | 700 | `#FAF9FC` |
| Validated | Normal (400) | `#008043` | 700 | `#FAF9FC` |
| Pre-filled | Normal (400) | `#D7D3E0` | 700, `#665E75` | `#D7D3E0` |
| Error | Normal (400) | `#D92130` | 700 | `#FAF9FC` |

**CTA:** "नेट बॉक्स तैयार करें" — appears only when all required fields are validated (username validated + password filled + VLAN selected + net box ID validated + VLAN ID valid if TAG). Tapping CTA opens the WiFi Connect popup.

**Edge case (production):** Net Box ID validation can return 4 error types (Error Code 255): not found, faulty, already in use, not assigned to this partner.

---

### WiFi Connect Popup (Android Material 3 Dialog)
**Module:** 19 (WiFi Auto-Connect & Permissions)
**Style:** Android Material 3 dialog — white card, 28px border-radius, centered content.
**Content:**
- Green WiFi icon in green circle
- Title: "WiFi से कनेक्ट करें"
- Body: "नेट बॉक्स के WiFi नेटवर्क से कनेक्ट करें ताकि सेटअप पूरा हो सके"
- Network name pill: "Wiom_GX412365" with lock icon
- Buttons: "रद्द करें" (cancel, grey) | "कनेक्ट करें" (connect, green)

**Interaction:** "कनेक्ट करें" dismisses the popup and navigates to S17 (Placement Check). "रद्द करें" dismisses the popup only.

---

### S17 — Net Box Placement Check
**Module:** 16 (Net Box Placement Proof)
**Header:** Light background, close button, "मदद" link.
**Title:** "नेट बॉक्स सही जगह लगाया है?"
**Content:**
- Audio speaker icon with countdown timer (starts at 00:59)
- Example images showing correct/incorrect placement
- Instructions about keeping Net Box away from fridge, TV, metallic objects

**Interaction:**
1. Audio (`netbox_placement.mp3`, ~10s) plays automatically
2. Timer counts down visually from 59
3. After ~12s (audio duration + 2s buffer), timer state hides and checkbox appears
4. Checkbox: "नेट बॉक्स सही जगह पर लगाया गया है"
5. Checking the box auto-transitions to S18 (camera) after 450ms

**Edge case (production):** Placement rules: away from fridge (electromagnetic interference), away from TV (signal obstruction), away from metallic objects, elevated position preferred.

---

### S18 — Net Box Camera (Rear)
**Module:** 16 (Net Box Placement Proof)
**Header:** Light background, back arrow.
**Content:**
- Full-screen rear camera viewfinder
- Instruction text for Net Box photo
- Shutter button at bottom

**Interaction:** Capture photo, stop camera, navigate to S19 with captured image.

---

### S19 — Net Box Photo Review
**Module:** 16 (Net Box Placement Proof)
**Content:**
- Captured Net Box photo displayed
- "फिर से लें" (Retake) option — returns to S18
- CTA: "आगे बढ़ें" — navigates to S20

---

### S20 — 3-Pin Plug Info (Audio Screen)
**Module:** 17 (3-Pin Proof Photo)
**Header:** Light background, close button, "मदद" link.
**Title:** "3-पिन प्लग लगाया है?"
**Content:**
- Speaker icon with countdown timer (starts at 00:59)
- Example images of correctly installed 3-pin plug

**Interaction:**
1. Audio (`threepin_instructions.mp3`, ~2s) plays automatically
2. Timer counts down visually
3. After 4.5s, timer hides and checkbox appears
4. Checkbox: "3-पिन प्लग सही से लगा दिया है"
5. Checking the box auto-transitions to S21 after 450ms

---

### S21 — 3-Pin Camera (Rear)
**Module:** 17 (3-Pin Proof Photo)
**Content:** Full-screen rear camera viewfinder with shutter button. Captures proof that the 3-pin plug is physically installed.
**Interaction:** Capture photo, navigate to S22.

---

### S22 — 3-Pin Photo Review
**Module:** 17 (3-Pin Proof Photo)
**Content:**
- Captured 3-pin plug photo displayed
- "फिर से लें" (Retake) — returns to S21
- CTA: "आगे बढ़ें" — navigates to S23

---

### S23 — Wiring Check (Audio Screen)
**Module:** 18 (Wiring + Device Proof)
**Header:** Light background, close button, "मदद" link.
**Title:** "वायरिंग सही तरीके से की है?"
**Content:**
- Speaker icon with countdown timer (starts at 00:59)
- Example images showing correct wiring (displayed below title)

**Interaction:**
1. Audio (`wiring_instructions.mp3`, 12.3s) plays automatically
2. Timer counts down visually
3. After 13.5s (audio + 1.2s buffer), timer hides and checkbox appears
4. Checkbox: "वायरिंग सही तरीके से की गयी है"
5. Checking the box auto-transitions to S24 after 450ms

---

### S24 — Wiring Camera (Rear)
**Module:** 18 (Wiring + Device Proof)
**Content:** Full-screen rear camera viewfinder. Captures proof of correct wiring installation.
**Interaction:** Capture photo, navigate to S25.

---

### S25 — Wiring Photo Review
**Module:** 18 (Wiring + Device Proof)
**Content:**
- Captured wiring photo displayed
- "फिर से लें" (Retake) — returns to S24
- CTA: "आगे बढ़ें" — navigates to S26

---

### S26 — Loading (Net Box Preparing)
**Module:** Transition screen
**Header:** Light background, close button, "मदद" link.
**Content:**
- Large green circle (96px) with green spinner inside
- "नेट बॉक्स तैयार किया जा रहा है..." (Net Box is being prepared...)

**Interaction:** Auto-transitions to S27 (Success) after 3 seconds. No user input.

---

### S27 — Success (Net Box Ready)
**Module:** 10 (Provisioning complete)
**Header:** Light background, close button, "मदद" link.
**Content:**
- Large green circle (96px) with green tick icon
- "नेट बॉक्स चालू हो गया है" (Net Box has been activated)
- CTA: "ऑप्टिकल पॉवर चेक करें" — navigates to S28

---

### S28 — Optical Power Check
**Module:** 20 (Optical Power Check)
**Header:** Light background, close button, "मदद" link.
**Content:**
- Green circle with optical power illustration
- Animated counter: starts at "0 dB", counts down to "-21 dB" (~150ms per step)
- Status text: "नेट बॉक्स तैयार किया जा रहा है..." during counting
- On reaching -21 dB:
  - Value turns green (`#008043`)
  - Status changes to "ऑप्टिकल पॉवर सही है"
  - CTA becomes visible: "अब नेट स्पीड चेक करें"

**Interaction:** CTA is hidden until the counter reaches -21 dB, then navigates to S29.

**Edge case (production):** Optimal range is approximately -8 to -25 dBm. Values outside this range trigger troubleshooting steps (check fiber connection, clean connector, escalate).

---

### S29 — Speed Test
**Module:** 21 (Speed Test)
**Header:** Light background, close button, "मदद" link.
**Content:**
- Lottie speedometer gauge animation (240x240px, inline JSON data)
- Speed counter: "0 Mbps" incrementing to "89 Mbps" synchronized with Lottie animation
- Text turns green when speed exceeds 80 Mbps

**Interaction:**
1. Lottie animation plays once (not looped)
2. Speed text updates in sync with animation progress
3. After animation completes (showing "89 Mbps" in green):
   - 1s delay, then bottom sheet slides up

**Bottom Sheet: Speed Confirmation**
- "क्या स्पीड सही आ रही है?" (Is the speed correct?)
- Two cards: "नहीं" (No) | "हाँ" (Yes) — both with pink brand border
- Tapping either card highlights it (pink bg), then navigates to S30 after 400ms

---

### S30 — Recharge Info (Audio Screen)
**Module:** 22 (ISP Plan Recharge) + Module 23 (PayG Education)
**No header** — content-only screen (intentional design: feels like a "moment", not a form).
**Content:**
- Speaker illustration (108x108, left-aligned)
- Headline: "अब कस्टमर अगले 2 दिन तक नेट का इस्तमाल कर सकते हैं"
- Subtitle: "इसके बाद रिचार्ज कस्टमर अपनी मर्ज़ी से करता है"
- Audio progress bar at bottom

**Interaction:**
1. Audio (`recharge_info.mp3`, 10.3s) plays automatically
2. Progress bar animates over 10.3 seconds
3. After 11s, progress bar hides and ghost CTA appears
4. Ghost CTA: "समझ गया" (Understood) — transparent background, pink text, no fill
5. Navigates to S31

**Purpose:** Educates the technician that the customer gets 2 days free, after which they recharge on their own. This prevents technicians from incorrectly telling customers they need to pay more.

---

### S31 — Happy Code & Rating
**Module:** 23 (Happy Code + Rating)
**Content:**
- Illustration (240x240)
- Instructions to get the customer to share their Happy Code
- CTA navigates to S32

**Purpose:** The Happy Code is a 4-digit OTP that the customer receives in their app. The technician must obtain this code to verify the installation was completed satisfactorily.

---

### S32 — Happy Code OTP Entry
**Module:** 23 (Happy Code Verification)
**Content:**
- 4 OTP digit boxes (bordered squares, brand pink border when filled)
- Full numeric keypad (0-9, backspace) — styled as rounded pill buttons on dark background

**Interaction:**
1. Tap number keys to enter digits
2. Each digit fills the next empty box and changes its border to brand pink
3. Backspace removes the last entered digit
4. After 4th digit is entered, auto-transitions to S33 after 500ms

---

### S33 — Lottery (Completion Reward)
**Module:** 23 (Gamification)
**Content:** Full-screen lottery/reward image (`lottery_screen.webp`) covering the entire viewport.
**Interaction:** Tapping the bottom area (120px tap target) triggers "फिर से चलायें" (Play again) which:
1. Clears all flow state (Happy Code, PayG acceptance, resume screen)
2. Resets to S1 (Task List)

**Purpose:** Gamification element — technicians earn lottery entries for completed installations. Incentivizes quality and speed.

---

### Exit Setup Dialog (accessible from any screen with close button)
**Content:**
- Warning icon (orange, 96px circle)
- "सेटअप पर काम जारी है!" (Setup work is in progress!)
- "क्या आप अभी भी इसे रोकना चाहते हैं?" (Do you still want to stop?)
- Primary CTA: "नहीं" (No) — dismisses dialog, stays in flow
- Secondary CTA: "हाँ, सेटअप रोकें" (Yes, stop setup) — saves progress, returns to S2

**Behavior:** Exiting saves the current screen to localStorage. The step ring on S2 updates to show progress. Next time the user enters, they can resume from where they left off.

---

### Completion Overlay
**Content:**
- Green check circle (96px)
- "सेटअप पूरा हो गया!" (Setup complete!)
- "Himanshu Singh जी का नेट बॉक्स सफलतापूर्वक सेटअप हो गया है"
- CTA: "होम पर जायें" — clears all state, returns to S1

---

## 3. Audio Screens Reference Table

| Screen | Audio File | Duration | Behavior After Audio | Checkbox/CTA Text |
|--------|-----------|----------|---------------------|-------------------|
| S3 | `payg_acceptance.mp3` | 10s | Checkbox appears at 10.25s | "मैंने समझ लिया है..." |
| S8C | `payg_precheck.mp3` | 17s | CTA appears at 18.5s | "समझ गया" |
| S13 | `power_on.mp3` | 8s | Timer counts down (15s proto / 59s prod) | Auto-advance at 00:00 |
| S15 | `isp_recharge.mp3` | 9.2s | CTA appears at 10.5s | Opens ISP Bottom Sheet |
| S17 | `netbox_placement.mp3` | ~10s | Checkbox appears at 12s | "नेट बॉक्स सही जगह पर..." |
| S20 | `threepin_instructions.mp3` | ~2s | Checkbox appears at 4.5s | "3-पिन प्लग सही से लगा..." |
| S23 | `wiring_instructions.mp3` | 12.3s | Checkbox appears at 13.5s | "वायरिंग सही तरीके से..." |
| S30 | `recharge_info.mp3` | 10.3s | Ghost CTA appears at 11s | "समझ गया" |

**Common pattern:** Audio plays automatically on screen entry. Progress bar animates over audio duration. After audio + buffer, the interaction element (checkbox or CTA) appears. User cannot skip the audio or interact before it completes.

---

## 4. Navigation Flow Diagram

```
S1 (Task List)
 |
 v
S2 (Task Detail — carousel, resume point)
 |
 v
S3 (PayG Acknowledgement — audio 10s → checkbox)
 |  [skip if already accepted]
 v
S4 (Transfer Info)
 ├── [call button → phone dialer → reveals family# + ISP card on return]
 ├── [CTA "सेटअप शुरू करें"]
 |    |
 |    v
 |   [3-Pin Dialog — checkbox]
 |    |
 |    v
 |   [Arrival Dialog — "हाँ, पहुँच गया हूँ"]
 |
 v
S5 (Selfie Camera — FRONT)
 |
 v
S6 (Selfie Review — "रॉकस्टार")
 |
 v
S8 (Aadhaar Capture — 3 states: front → back → both)
 |    [via S7 camera × 2]
 |
 v
S8C (PayG System Info — audio 17s → CTA)
 |
 v
S12 (Payment Checklist — 6-state animation, ~5.4s)
 |
 v
S13 (Net Box Power-Up Timer — 15s countdown)
 |
 v
S14 (Switch-On Confirm — checkbox)
 |
 v
S11 (Customer Details → ISP अकाउंट बनायें)
 |
 v
S15 (ISP Recharge Audio — 9.2s → CTA)
 |
 v
[ISP Bottom Sheet — portal vs office]
 |
 v
S16 (ISP Form — PPPoE: username → VLAN → Net Box ID)
 |    [CTA appears when all fields validated]
 |
 v
[WiFi Connect Popup — Material 3 dialog]
 |
 v
S17 (Placement Check — audio ~10s → timer → checkbox)
 |
 v
S18 (Net Box Camera — REAR) → S19 (Net Box Photo Review)
 |
 v
S20 (3-Pin Info — audio ~2s → checkbox)
 |
 v
S21 (3-Pin Camera — REAR) → S22 (3-Pin Photo Review)
 |
 v
S23 (Wiring Check — audio 12.3s → checkbox)
 |
 v
S24 (Wiring Camera — REAR) → S25 (Wiring Photo Review)
 |
 v
S26 (Loading — 3s spinner)
 |
 v
S27 (Success — "नेट बॉक्स चालू हो गया है")
 |
 v
S28 (Optical Power — 0 → -21 dB counter)
 |
 v
S29 (Speed Test — Lottie gauge → bottom sheet)
 |
 v
S30 (Recharge Info — audio 10.3s → ghost CTA)
 |
 v
S31 (Happy Code Rating — illustration + CTA)
 |
 v
S32 (OTP Entry — 4 digits + numeric keypad)
 |
 v
S33 (Lottery — full-screen image → reset to S1)
```

**Exit flow (available from most screens):**
```
[Close button] → Exit Dialog → "हाँ, सेटअप रोकें" → S2 (with saved progress)
                              → "नहीं" → dismiss, stay in current screen
```

---

## 5. Known Limitations

### No backend integration
- All data is hardcoded (customer name "Himanshu Singh", address, family number)
- Payment checklist animation is simulated (no real payment gateway)
- Net Box ID validation is simulated (spinner → always succeeds)
- Optical power check is simulated (always reaches -21 dB)
- Speed test is simulated (always shows 89 Mbps)

### Linear flow only
- No branching for error states (e.g., Net Box ID not found, payment failed, optical power out of range)
- No branching for ISP types other than PPPoE (Static IP and DHCP forms exist in HTML but are not connected to subsequent screens)
- No "speed not correct" path after speed test (both Yes and No navigate to S30)

### No real backend events
- `ROHIT_ARRIVED` event not fired
- No FCM push notification handling
- No Firestore state listeners
- No real camera upload to cloud storage

### Timer differences
- S13 countdown: 15s in prototype vs 59s (production) or 300s (full provisioning)
- Audio buffer times may differ slightly from production

### Missing features
- No task reassignment (Partner Plus specific)
- No "Tickets" tab (Expert App specific)
- No error dialogs (Error Code 255 variants for Net Box ID)
- No offline handling / retry logic
- No "मदद" (Help) screen — button is present but non-functional
- No back navigation from most inner screens (by design — linear flow)
- No selfie retake from S6
- Menu button on S1 is non-functional

### Camera behavior
- Camera defaults to `facingMode: ideal` with fallback to any available camera
- On desktop browsers, the single webcam will be used for all camera screens regardless of front/rear designation
- Photo quality is JPEG at 85% quality from canvas capture

### State persistence
- Flow progress persists in localStorage across app kills
- APK version check (`APK_VERSION = 'v29'`) clears stale state on new APK install
- PayG acceptance persists separately from flow progress

---

## 6. Figma References

### Source file
- **File key:** `PwrhO9KnvdZ4NNYwz3B85d` (claude-playground)
- **Section:** "Test android flow" (node `200:7929`)

### Key Figma node IDs per screen
| Screen | Figma Node | Description |
|--------|-----------|-------------|
| S1 | `199:4938` | Task List (home) |
| S2 | `200:4961` | Task Detail |
| S3 | `219:16949` → `219:17009` | PayG Acknowledgement (audio → checkbox) |
| S4 | `200:5143` | Transfer Info |
| S4 dialogs | `200:5259`, `200:5119`, `200:5286` | 3-pin plug, Arrival, 3-pin checkbox |
| S5 | `200:5853` | Selfie Camera |
| S6 | `200:5769`, `200:6631` | Selfie Review |
| S7 | `200:6879` | Aadhaar Camera |
| S8 | `200:6691`, `200:6748`, `200:6814` | Aadhaar review states |
| S8C | `216:16152` → `216:16253` | PayG System Info |
| S12 | `198:74631` → `198:75885` | Payment Checklist (6 states) |
| S13 | `200:5440` | Net Box Power-Up Timer |
| S14 | `198:68231` | Switch-On Confirm |
| S15 | `224:17548` → `224:17632` | ISP Recharge Audio |
| S16 | `198:72096` | ISP Details Form |
| S17 | `198:67430` → `198:67505` | Placement Check |
| S18 | `198:67138` | Net Box Camera |
| S19 | `198:76119` | Net Box Photo Review |
| S20 | `198:76202` → `198:76346` | 3-Pin Info |
| S21 | `198:76502` | 3-Pin Camera |
| S22 | `198:76415` | 3-Pin Photo Review |
| S23 | `198:67210` → `198:67577` | Wiring Check |
| S24 | `198:67735` | Wiring Camera |
| S25 | `198:67648` | Wiring Photo Review |
| S26 | `198:66888` | Loading |
| S27 | `198:67052` | Success |
| S28 | `271:6046` | Optical Power Check |
| S29 | `271:6945` | Speed Test |
| S30 | `271:7604` → `271:7659` | Recharge Info |
| S31 | `271:7162` | Happy Code Rating |
| S32 | `271:7507` | OTP Entry |
| S33 | `198:68615` | Lottery |
| ISP Sheet | `224:17502` | ISP method bottom sheet |
| WiFi Popup | — | Android Material 3 dialog (custom) |
| Customer BS | `198:72375` | Customer Details bottom sheet |
| Exit Dialog | `215:15949` | Exit Setup confirmation |

### Design System file
- **File key:** `T0klEs1aPBk7BOVZonc8JC` (CA_Design System / Wiom Design Library)
- DS Input-Field component: `258:6045`

---

## 7. Testing Checklist

### Flow navigation (10 items)
- [ ] **TC-01:** App launches on S1 (Task List). Verify header shows partner name, rating pill animates, task count "02" displays.
- [ ] **TC-02:** Tap task card on S1 — navigates to S2 (Task Detail). Back arrow returns to S1.
- [ ] **TC-03:** Complete full flow S1 through S33 without interruption. Verify every screen transition is smooth and correct.
- [ ] **TC-04:** On S33 (Lottery), tap bottom area — flow resets to S1. Verify all state is cleared (localStorage wiped).
- [ ] **TC-05:** Mid-flow, tap close button, confirm exit via "हाँ, सेटअप रोकें" — returns to S2. Step ring shows correct progress. Re-entering flow resumes from last screen.
- [ ] **TC-06:** Mid-flow, tap close button, dismiss exit via "नहीं" — stays on current screen.
- [ ] **TC-07:** After accepting PayG (S3), exit and re-enter flow — S3 is skipped (goes directly to S4).
- [ ] **TC-08:** Kill app and reopen — always shows S1 (never auto-navigates to mid-flow screen).
- [ ] **TC-09:** Verify new APK version clears stale localStorage (change `APK_VERSION`, reopen — flow starts fresh).
- [ ] **TC-10:** Bottom sheets (Customer Details, Speed Confirmation, ISP method) dismiss on outside tap or drag-down.

### Audio screens (8 items)
- [ ] **TC-11:** S3 — Audio plays for 10s. Progress bar fills. Checkbox appears only after audio completes. Cannot interact during audio.
- [ ] **TC-12:** S8C — Audio plays for 17s. CTA "समझ गया" appears after 18.5s. Cannot advance before audio completes.
- [ ] **TC-13:** S13 — Audio plays. Timer counts down from 15. Auto-navigates to S14 at 00:00.
- [ ] **TC-14:** S15 — Audio plays for 9.2s. CTA appears after 10.5s.
- [ ] **TC-15:** S17 — Audio plays. After ~12s, timer hides and checkbox appears. Checkbox triggers transition to S18.
- [ ] **TC-16:** S20 — Audio plays. After 4.5s, checkbox appears. Checkbox triggers transition to S21.
- [ ] **TC-17:** S23 — Audio plays for 12.3s. After 13.5s, checkbox appears. Checkbox triggers transition to S24.
- [ ] **TC-18:** S30 — Audio plays for 10.3s. Ghost CTA "समझ गया" appears after 11s.

### Camera screens (5 items)
- [ ] **TC-19:** S5 — Front camera activates. Shutter captures photo. Photo appears on S6 review screen.
- [ ] **TC-20:** S7 — Rear camera activates. Dashed guide rectangle visible. Capture works for both front and back Aadhaar.
- [ ] **TC-21:** S18 — Rear camera activates. Capture works. Photo shows on S19 review. Retake returns to S18 with camera reactivated.
- [ ] **TC-22:** S21 — Rear camera activates. Capture + review + retake cycle works correctly.
- [ ] **TC-23:** S24 — Rear camera activates. Capture + review + retake cycle works correctly.

### Interactive elements (8 items)
- [ ] **TC-24:** S4 — Call button opens phone dialer. On return, family number and ISP card appear.
- [ ] **TC-25:** S4 — 3-pin dialog checkbox fills, then arrival dialog appears. "हाँ, पहुँच गया हूँ" navigates to S5.
- [ ] **TC-26:** S8 — Aadhaar 3-state flow: front prompt → front captured + back prompt → both captured + CTA. Retake options work for each side.
- [ ] **TC-27:** S12 — Payment checklist animation runs through all 6 states automatically, ending at S13 transition.
- [ ] **TC-28:** S14 — Tapping body area shakes the checkbox row (guides user). Tapping checkbox specifically checks it and transitions to S11.
- [ ] **TC-29:** S16 — ISP form progressive reveal: username submit → VLAN dropdown → Net Box ID submit. Validation spinners and green checks appear correctly. CTA shows only when all fields are valid.
- [ ] **TC-30:** S28 — Optical power counter runs from 0 to -21 dB. Value and status turn green at -21. CTA becomes visible.
- [ ] **TC-31:** S29 — Lottie speedometer animates. Speed text syncs. Bottom sheet appears after animation. Both "हाँ" and "नहीं" navigate to S30.
- [ ] **TC-32:** S32 — Numeric keypad enters digits into OTP boxes. Backspace deletes. Auto-transitions to S33 after 4th digit.

### Visual / design fidelity (3 items)
- [ ] **TC-33:** Verify all Hindi text renders correctly (Noto Sans Devanagari loaded).
- [ ] **TC-34:** Verify brand pink (`#D9008D`) used consistently for CTAs, links, checkboxes, progress bars.
- [ ] **TC-35:** Verify all Aadhaar photos captured on S7/S8 appear correctly on S11 (Customer Details) and in the Customer Details bottom sheet.

---

## Appendix: Master Document Module Mapping

| Prototype Screen(s) | Master Doc Module | Module Name |
|---------------------|-------------------|-------------|
| S1, S2 | Module 2 | Task List & Pre-Visit |
| S3 | Module 3 | PayG Acknowledgement Gate |
| S4 (3-pin dialog) | Module 4 | 3-Pin Carry Confirmation |
| S4 (arrival dialog) | Module 5 | Arrival Confirmation |
| S5, S6 | Module 6 | Selfie Capture |
| S7, S8 | Module 7 | Aadhaar Capture (KYC) |
| S8C | Module 8 | PayG System Jankari |
| S12 | Module 9 | Payment Collection |
| S13, S14 | Module 10 | Switch-On & Provisioning |
| S11 | Module 11 | Customer Details & ISP Profile |
| S15 | Module 12 | PayG Education (ISP Plan) |
| S16 | Module 14 | Fill ISP Details |
| WiFi Popup | Module 19 | WiFi Auto-Connect |
| S17, S18, S19 | Module 16 | Net Box Placement Proof |
| S20, S21, S22 | Module 17 | 3-Pin Proof Photo |
| S23, S24, S25 | Module 18 | Wiring + Device Proof |
| S26, S27 | Module 10 | Provisioning Complete |
| S28 | Module 20 | Optical Power Check |
| S29 | Module 21 | Speed Test |
| S30 | Module 22 + 23 | ISP Plan Recharge + PayG Education |
| S31, S32 | Module 23 | Happy Code Verification |
| S33 | Module 23 | Gamification (Lottery) |

**Note:** Module 13 (Net Box ID Entry & Validation) is folded into S16 (ISP Form) where Net Box ID is one of the progressive-reveal fields. Module 15 (WiFi Name Setup) is represented by the prefilled network name in the WiFi Connect Popup.
