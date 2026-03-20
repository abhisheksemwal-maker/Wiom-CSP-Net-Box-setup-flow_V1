# VISUAL UI AUDIT — Figma vs Build
## Screen-by-Screen Specification

---

## SCREEN 01: Task List (199:4938)
**Frame:** 360×720dp | **Background:** `#FAF9FC` (bg_surface)

### A. Status Bar (24dp)
- **Height:** 24dp
- **Background:** `#443152` (bg_partner_header)
- **Time text:** "04:35 PM" — Roboto Medium, 14dp, `#FAF9FC` (dark_fg), left-aligned at x=8
- **Icons (right):** WiFi (16×16, filled) + Cellular (16×16, filled) + "50%" Roboto Medium 14dp + Battery (16×16, filled)
- **Icon color:** `#FAF9FC`
- **Note:** These are SYSTEM status bar icons — in APK, use Android's real status bar, not fake HTML

### B. App Bar / Header (56dp)
- **Height:** 56dp
- **Background:** `#443152` (bg_partner_header)
- **Shadow:** 1dp — `0px 1px 3px rgba(0,0,0,0.15)` (Elevation-DOWN/1dp-shadow)
- **Padding:** horizontal 16dp, vertical 12dp
- **Layout:** Row — [Logo] [12dp gap] [Name] [24dp gap] [Rating Badge] [24dp gap] [Menu]

#### B1. Wiom Expert Logo
- **Size:** 32×32dp
- **Background:** `#2F1940` (custom dark purple)
- **Border:** 1px solid `#E8E4F0` (stroke_neutral_pri)
- **Border radius:** 4dp (radius-below-24dp)
- **Content:** Custom Wiom illustration (NOT a Material icon) — must use exported `wiom_logo.webp`

#### B2. Partner Name
- **Font:** Noto Sans, Display Bold, 16dp, weight 700
- **Line height:** 24dp
- **Color:** `#FAF9FC` (positive_fg / dark_fg)
- **Max width:** 159dp
- **Overflow:** `text-overflow: ellipsis; white-space: nowrap; overflow: hidden`
- **Text:** "Rohit Kumar Chaurasia" (truncates to "Rohit Kumar Chaur...")

#### B3. Rating Badge
- **Container:** Pill shape, `border-radius: 20dp`
- **Background:** `#60506C` (bg_partner_light_purple)
- **Border:** 1px solid `#443152` (matches header)
- **Height:** 28dp
- **Padding:** ~4dp 8dp
- **Layout:** Row — [Star icon 16×16] [4dp gap] [Text]
- **Star icon:** Custom golden star (NOT Material `star` icon) — use exported `star_badge.webp` or the custom Figma asset (node I199:4972). It's a filled golden star with slightly different shape than Material.
- **Text:** "4.8" — Noto Sans, Display SemiBold, 14dp, weight 600, line-height 20dp, `#FAF9FC`

#### B4. Menu Icon
- **Size:** 24×24dp
- **Icon:** `menu` — three horizontal lines. This appears to be the standard hamburger. From Figma it's a custom SVG (node 199:4977), but visually it's a standard hamburger menu. Use Material `menu` icon.
- **Color:** `#FAF9FC`

### C. Tabs (48dp)
- **Height:** 48dp
- **Background:** `#443152` (same as header, seamless)
- **Layout:** Two equal-width tabs, centered text

#### C1. Active Tab ("मेरे काम")
- **Font:** Noto Sans, Display SemiBold, 14dp, weight 600, line-height 20dp
- **Color:** `#FAF9FC`
- **Border-bottom:** 4px solid `#E8E4F0` (stroke_neutral_pri) — this is a WHITE/light underline
- **Alignment:** center

#### C2. Inactive Tab ("मेरी कमाई")
- **Font:** Noto Sans, Display Regular, 14dp, weight 400, line-height 20dp
- **Color:** `#FAF9FC` (same color, but lighter weight distinguishes it)
- **Border-bottom:** none (no underline)

### D. Task Card
- **Position:** Centered horizontally, starts at y=152dp from top (below header+tabs+24dp spacing)
- **Background:** `#FFE5F6` (bg_brand_pri)
- **Border:** 1px solid `#D9008D` (stroke_brand_focus / stroke_selection)
- **Border radius:** 16dp (radius-main-card)
- **Padding:** 24dp horizontal, 24dp vertical
- **Layout:** Row — [Left content area 192dp] [16dp gap] [CTA arrow]

#### D1. Count Number
- **Text:** "02"
- **Font:** Noto Sans, Display Bold, 48dp, weight 700
- **Line height:** 64dp
- **Color:** `#161021` (pri_text)

#### D2. Customer Avatars
- **Layout:** Overlapping row (negative margin)
- **Each avatar:** 32×32dp, circular (`border-radius: 50%`)
- **Border:** 2px solid `#FFFFFF` (white ring around each)
- **Overlap:** -8dp margin-left on second avatar
- **Content:** Real customer photos (exported as `avatar1.webp`, `avatar2.webp`)
- **Margin top:** 16dp below count

#### D3. Status Text
- **Text:** "कस्टमर प्रतीक्षा कर रहे हैं"
- **Font:** Noto Sans, Display Bold, 20dp, weight 700
- **Line height:** 28dp
- **Color:** `#161021` (pri_text)
- **Width:** 192dp (left content area)
- **Margin top:** 16dp below avatars

#### D4. CTA Arrow Button
- **Size:** 72×72dp (NOT 56px!)
- **Shape:** Circle, `border-radius: 36dp`
- **Background:** `#D9008D` (pri_cta_bg)
- **Shadow:** `0 4px 12px rgba(217,0,141,0.3)` (brand shadow)
- **Position:** Right-aligned, vertically centered in card
- **Arrow icon:** 36×36dp — this is NOT a standard `arrow_forward`. It's the `arrow_back` icon at 36dp, **rotated 180° and flipped vertically** (per Figma: `-scale-y-100 rotate-180`). Visually it looks like a right-pointing arrow but built from a flipped back arrow.
- **Icon color:** `#FAF9FC`

### E. Page Background
- **Below tabs:** `#FAF9FC` (bg_surface)
- **No additional elements** — just the single task card on light bg

---

## SCREEN 02: Task Detail (200:4961)
**Frame:** 360×720dp

### A. Status Bar (24dp) — SAME as Screen 01
- Dark mode, `#443152` bg

### B. App Bar (56dp)
- **Background:** `#443152`
- **Shadow:** 1dp — `0px 1px 3px rgba(0,0,0,0.15)`
- **Layout:** Row — [Back arrow 24×24] [16dp gap] [Title]
- **Back arrow:** `arrow_back` icon, 24×24dp, `#FAF9FC`
- **Title:** "मेरे काम (1)" — Noto Sans, Display Bold, 16dp, weight 700, `#FAF9FC`, ellipsis overflow

### C. Dark Background Region
- **NOT solid black, NOT a linear gradient**
- **It's a large dark shape with curved bottom edges** — looks like a radial/spotlight effect where the dark bg curves inward at the bottom corners, creating a bowl shape behind the card
- **Color:** Very dark purple/black (appears to be `#161021` or similar with some curve/mask)
- **The card sits vertically centered** on top of this dark region
- **Below the dark region:** `#FAF9FC` bg shows through at bottom corners

### D. Installation Ticket Card
- **Width:** 328dp (360 - 16 - 16)
- **Position:** Centered, starts roughly at y=280dp (vertically centered in the dark region)
- **Background:** `#FAF9FC` (bg_neutral_pri) for body, `#FFE5F6` (bg_brand_pri) for header
- **Border:** 1px solid `#D9008D` (stroke_selection) — on LEFT, RIGHT, and TOP edges
- **Border radius:** 16dp (radius-main-card) — top-left and top-right
- **Overflow:** hidden (CTA at bottom clips to card corners)

#### D1. Card Header ("नया सेटअप" section)
- **Background:** `#FFE5F6` (bg_brand_pri)
- **Border-bottom:** 1px solid `#D9008D` (separating header from body)
- **Padding:** 24dp top, 16dp bottom, 16dp horizontal
- **Layout:** Row — [Label left] [Badge right]

##### Label
- **Text:** "नया सेटअप"
- **Font:** Noto Sans, Bold, 20dp, weight 700
- **Line height:** 28dp
- **Color:** `#161021` (pri_text)

##### Step Indicator Badge (0/8)
- **Size:** 36×36dp
- **Shape:** Circle
- **Stroke:** This is a **circular progress indicator (SVG ring)**
  - Outer ring: 32dp diameter, rotated 180°
  - Ring stroke color: appears to be `#D9008D` (brand) at ~0% fill (empty state)
  - Background: transparent or very faint pink
- **Text inside:** "0/8"
  - Font: Noto Sans, Regular, 12dp, weight 400
  - Line height: 16dp
  - Color: `#D9008D` (bg_brand_focus)
  - Alignment: center both axes

#### D2. Card Body
- **Background:** `#FAF9FC`
- **Padding:** 16dp all sides

##### Customer Name Row
- **Layout:** Row — [Person icon 24×24] [12dp gap] [Name text flex:1] [Phone icon 24×24]
- **Person icon:** FILLED person icon (not outlined!) — `#665E75` (sec) color, 24×24dp
- **Name:** "Himanshu Singh" — Noto Sans, Bold, 16dp, weight 700, line-height 24dp, `#161021`
- **Phone icon:** FILLED phone icon — `#D9008D` (brand color, NOT black). Custom phone receiver icon from Figma.

##### Address Row
- **Layout:** Row — [Pin icon 24×24] [12dp gap] [Address text flex:1]
- **Pin icon:** FILLED location pin — `#665E75` (sec), 24×24dp. Custom Figma pin icon (slightly different from Material `location_on`).
- **Address:** "G 69, Mata mandir, 3rd street, Ghaziabad Uttar Pradesh" — Noto Sans, Regular, 16dp, weight 400, line-height 24dp, `#161021`

#### D3. Timeline Row (Run Timeline)
- **Height:** ~32dp for icon row + labels below
- **Layout:** Row — [Ball 24×24] [12dp gap] [Progress bar flex:1 (216dp)] [12dp gap] [Bat 32×32]

##### Cricket Ball
- **Size:** 24×24dp
- **Content:** Custom cricket ball illustration (red/pink stitching on white ball) — NOT a Material icon
- **Source:** `cricket_ball.webp`
- **Position:** Left-aligned, vertically centered with the progress bar

##### Progress Bar
- **Container:** Full width between ball and bat (~216dp)
- **Height:** 6dp
- **Track:** Grey — appears to be a component with two bars stacked:
  - **Top bar (filled portion):** `#008043` (positive/green), ~38% width
  - **Bottom bar (remaining):** `#E8E4F0` (neutral-200) grey
- **Border radius:** 3dp
- **Note:** The ball and bat sit at the EDGES of this bar (touching/overlapping), not beside it with gaps

##### Cricket Bat + Stumps
- **Size:** 32×32dp
- **Content:** Custom cricket bat and stumps illustration — NOT a Material icon
- **Source:** `cricket_bat.webp`
- **Position:** Right-aligned, vertically centered

##### Timeline Labels (below bar)
- **Layout:** Row — [Left time] [flex spacer] [Right deadline]
- **Left:** "10:00am" — Noto Sans, Regular, 12dp, weight 400, line-height 16dp, `#161021`
- **Right:** "12 January" (SemiBold, 12dp, 600) + " तक काम पूरा करें" (Regular, 12dp, 400, `#161021`)

#### D4. CTA (Card bottom, integrated)
- **Background:** `#D9008D` (pri_cta_bg)
- **Border:** 1px solid `#D9008D` on bottom + left + right
- **Border radius:** 16dp bottom-left, 16dp bottom-right (matches card radius)
- **Height:** ~56dp (padding 16dp + text 24dp + padding 16dp)
- **Padding:** 16dp all sides
- **Text:** "काम पूरा करें" — Noto Sans, Bold, 16dp, weight 700, line-height 24dp, `#FAF9FC`, center-aligned
- **This CTA is PART of the card** (not a separate floating button below)

---

## SCREEN 03: Task Detail — Expanded View (200:5040)
**Frame:** 360×720dp | **Background:** `#FAF9FC`

### Structure
Same header as S01 (PA_Headers: Status Bar 24dp + App Bar 56dp + Tabs with "मेरे काम (1)" active). Below that, a scrollable card area.

### Key Differences from S02
- S03 is the **expanded/tapped state** — shows the full task list view with the card inline (not on a dark background like S02)
- Has **filter tabs** below the main tabs: "टू डू (06)" (SemiBold, active) | "इन-प्रोग्रेस (05)" (Regular, inactive)
- Filter tab colors: `#FAF9FC` text on dark header
- Shows **Trusted Customer (0)** section header and a "Resolve Now" link (`#D92B90`)
- Shows **New Leads (2)** section header
- The task card is shown inline with full details (same cricket timeline, same layout as S02's card body)

### Card Content (same as S02 body)
- "नया सेटअप" header (Bold 20dp) + "THC123420" ticket ID (Regular 16dp, `#665E75`)
- Step badge "0/8" (Regular 12dp, `#D9008D`)
- Customer name "Himanshu Singh" (Bold 16dp) + secondary "Sanjay Singh Chauhan" (Regular 16dp)
- Address (Regular 16dp)
- Cricket timeline (ball → progress bar → bat)
- Timeline labels: "10:00am" + "12 January तक काम पूरा करें"
- "02:15pm से पहले समाधान करें" (SemiBold 14dp)
- CTA: "काम पूरा करें" (Bold 16dp, `#FAF9FC` on brand bg)

### Build Note
This screen may be skippable in the APK — it's essentially the task list with the card expanded inline. In a linear demo flow, S01→S02 is sufficient. **Consider merging S01+S03 or skipping S03.**

---

## SCREEN 04: PayG Acknowledgement — Unchecked (200:7193)
**Frame:** 360×720dp | **Background:** `#FAF9FC`

### A. Header (80dp)
- **PA_Headers instance** (different from S01 header)
- Status Bar (24dp): text color `#161021` (dark text = light status bar, NOT the dark `#443152` header)
- App Bar (56dp): title "कस्टमर कान्फर्मैशन" — Noto Sans Display Bold, 16dp, `#161021`
- **Note:** This header has a LIGHT background (`#FAF9FC`), not the dark purple of S01/S02

### B. Content Area (centered)
- **Position:** starts at y≈212dp, x=24dp, width=312dp

#### B1. Graphic Icon
- **Size:** 96×96dp
- **Type:** INSTANCE — a custom illustration (NOT Material icon)
- **Content:** Likely the PayG/money-related graphic — export as asset

#### B2. Headline
- **Text:** "PayG सेटअप में सिर्फ ₹300 सिक्योरिटी फीस होती है"
- **Font:** Noto Sans Bold, 24dp, weight 700
- **Line height:** 32dp
- **Color:** `#6D17CE` (info purple — NOT brand pink!)
- **Width:** 312dp
- **Margin top:** 112dp from top of content frame (below graphic + 16dp gap)

#### B3. Subtitle
- **Text:** "कस्टमर सिर्फ ₹300 सिक्योरिटी फीस ही पे करता है"
- **Font:** Noto Sans Regular, 16dp, weight 400
- **Line height:** 24dp
- **Color:** `#000000` (pure black — NOT `#161021`)
- **Width:** 312dp

### C. Checkbox Row (y≈640dp)
- **Position:** x=24dp, width=312dp, height=48dp
- **Layout:** Row — [Checkbox 24×24] [8dp gap] [Text]

#### C1. Checkbox
- **Size:** 24×24dp
- **Type:** INSTANCE — `checkbox-tick` component
- **State:** UNCHECKED (empty square with border)

#### C2. Checkbox Label
- **Text:** "मैंने समझ लिया है, इस सेटअप में ₹300 के अलावा कोई और पेमेंट नहीं है"
- **Font:** Noto Sans Display Bold, 16dp, weight 700
- **Color:** `#161021`
- **Width:** 280dp (wraps to ~2 lines)

### D. CTA Button (y=656dp)
- **Size:** 328×48dp (16dp margin each side)
- **Background:** `#D9008D` (pri_cta_bg)
- **Border radius:** assumed 16dp (consistent with other CTAs)
- **Text:** "समझ गया" — Noto Sans Bold, 16dp, `#FAF9FC`, center-aligned
- **State:** Disabled until checkbox is checked (greyed out in S04, active in S05)

---

## SCREEN 05: PayG Acknowledgement — Checked (200:7204)
**Frame:** 360×720dp | **Background:** `#FAF9FC`

### Identical to S04 except:
- **Checkbox state:** CHECKED (tick visible inside the 24×24 checkbox)
- **CTA state:** ACTIVE (full brand pink, clickable)
- **No CTA button node visible** at top level — the "actions" node from S04 is absent, meaning this auto-transitions after checkbox is checked
- Same headline, subtitle, checkbox label text

### Build Note
S04 and S05 are the same screen with two states. Build as **one screen** with checkbox toggle:
- Unchecked → CTA disabled (grey or reduced opacity)
- Checked → CTA enabled → auto-transitions to S06

---

## SCREEN 06: Transfer Info (200:5143)
**Frame:** 360×720dp | **Background:** `#FAF9FC`

### A. Header (80dp)
- Status Bar (24dp) + Header (56dp) with back arrow
- Title area below: "नया सेटअप" (Bold 16dp) + "मदद" link (Display SemiBold 14dp, `#D9008D`)
- Light bg header (not dark purple)

### B. Information Container (y=88dp, width=328dp)
- **Position:** x=16dp, height=264dp (auto-layout, grows with content)
- **Contains multiple rows:**

#### B1. Deadline Row (56dp)
- **Text:** "12 January" (Bold 16dp, `#B85C00` orange) + "से पहले नेट चालू करना है" (Regular 16dp, `#161021`)
- **Background:** `#FAF9FC`

#### B2. Call Instruction (purple info bar)
- **Text line 1:** "कस्टमर को कॉल करके, अपने पहुंचने का समय बता दें" (SemiBold 12dp, `#6D17CE`)
- **Text line 2:** "ताकि समय और जगह दोनों पक्की हो जाए" (Regular 12dp, `#6D17CE`)

#### B3. Customer Info V3 Instance (192dp)
- **Name:** "Himanshu Singh" — Bold 16dp, `#161021`
- **Address:** "G 69, Mata mandir, 3rd street, Ghaziabad Uttar Pradesh" — Display Regular 14dp, `#161021`

#### B4. Device Details Rows
Two device rows stacked:
- **Row 1:** Device ID "TP412365" | Speed "100 Mbps" | Connection "कॉपर"
- **Row 2:** Device ID "GX412365" | Speed "50 mbps" | Connection "फाइबर"
- Labels: Regular 12dp, `#A7A1B2` (hint text)
- Values: Regular 14dp, `#161021`
- **Row 2 bg:** `#F1E5FF` (light purple — `rgb(0.945, 0.898, 1)`)

#### B5. Aadhaar Section
- **Background:** `#F1E5FF` (light purple) — 96dp height
- **Title:** "आधार कार्ड" (Bold 16dp, `#161021`)
- **Subtitle:** "कस्टमर के आधार की जानकारी देखें" (Regular 12dp, `#665E75`)

#### B6. ISP Prefill Tip (green bar)
- **Background:** `#C9F0DD` (light green — `rgb(0.788, 0.941, 0.867)`)
- **Height:** 40dp
- **Text:** "15 मिनट बचाएँ" (SemiBold 14dp, `#FAF9FC`) — wait, this should be dark text on green bg
- **Subtext:** "अपने ऑफिस में ही ISP डिटेल्स भरकर" (Regular 16dp, `#161021`)

#### B7. Payment Status
- **Text:** "पेमेंट हो गई है" (Regular 14dp, `#008043` green)

### C. CTA (y=656dp)
- **Size:** 328×48dp
- **Background:** `#D9008D`
- **Text:** "सेटअप शुरू करें" — Bold 16dp, `#FAF9FC`

---

## SCREEN 07: Arrival Dialog (200:5119)
**Frame:** 312×344dp (DIALOG, not full screen) | **Background:** `#FAF9FC`

### A. Head Section (y=32dp, x=24dp, width=264dp)

#### A1. Graphic
- **Size:** 96×96dp circle
- **Background:** `#F1EDF7` (neutral/100 lavender)
- **Icon:** 72×72dp "house user" illustration (custom — export as asset)

#### A2. Headline
- **Text:** "क्या आप कस्टमर के घर पर हैं?"
- **Font:** Noto Sans Display Bold, 24dp
- **Color:** `#161021`

#### A3. Subtitle
- **Text:** "किसी भरोसेमंद व्यक्ति को अपने बिजनेस का मैनेजर बनाएं"
- **Font:** Noto Sans Regular, 16dp
- **Color:** `#161021`
- **Note:** This subtitle text seems like placeholder/wrong content for an arrival dialog

#### A4. Remaining Customers Warning
- **Text:** "02 recharges are still pending."
- **Font:** Noto Sans Display Bold, 16dp
- **Color:** `#B85C00` (warning orange)

### B. Action Bar (y=264dp, x=24dp, width=264dp)
- **Layout:** Vertical stack, 2 buttons

#### B1. Primary CTA
- **Size:** 264×48dp
- **Background:** `#D9008D` (brand pink)
- **Border radius:** 16dp
- **Text:** "हाँ, पहुँच गया हूँ" — Display Bold 16dp, `#FAF9FC`

#### B2. Secondary CTA
- **Size:** 264×48dp
- **Background:** `#FFE5F6` (light pink)
- **Border radius:** 16dp
- **Text:** "Cancel" — Display Bold 16dp, `#D9008D`

### Build Note
This is a **modal dialog** overlaid on the Transfer Info screen (S06). Build as an overlay/popup, not a separate page.

---

## SCREEN 08: 3-Pin Reminder Dialog (200:5286)
**Frame:** 312×480dp (DIALOG) | **Background:** `#FAF9FC`

### A. Head Section (y=32dp, x=24dp, width=264dp, height=344dp)

#### A1. Tick Icon
- **Size:** 48×48dp
- **Background:** `#FFE699` (warm yellow — `rgb(1, 0.902, 0.8)`)
- **Content:** Tick/check mark inside

#### A2. Main Text
- **Text:** "नेट बॉक्स के साथ 3-पिन प्लग लगाना भी ज़रूरी है"
- **Font:** Noto Sans Bold, 20dp
- **Color:** `#161021`

#### A3. Warning Text
- **Text:** "3-पिन के बिना सेट अप अधूरा रहेगा"
- **Font:** Noto Sans Regular, 16dp
- **Color:** `#B85C00` (warning orange)

#### A4. Additional Text
- "आगे बढ़ने के लिए नया Username डालें" (Regular 16dp, `#161021`)
- "Error Code : 255" (Display Bold 16dp, `#161021`)

#### A5. Remaining Customers
- Same warning pattern as S07

### B. Agree Checkbox (y=424dp, x=24dp)
- **Layout:** Row — [Checkbox 24×24] [8dp gap] [Text]
- **Text:** "मैंने 3-पिन प्लग रख लिया है" — Display Bold 16dp, `#161021`

### Build Note
Dialog overlay. No separate CTA button — checking the checkbox auto-advances.

---

## SCREEN 09: Transfer Info 2 — Post-Arrival (200:5201)
**Frame:** 360×720dp | **Background:** `#FAF9FC`

### Identical layout to S06
Same customer info, same device rows, same Aadhaar section, same CTA "सेटअप शुरू करें".

### Build Note
S06 and S09 are the same screen shown at different points in the flow. **Reuse the same HTML** — the dialog (S07) appears between them.

---

## SCREEN 10: 3-Pin Plug Dialog with Image (200:5259)
**Frame:** 312×480dp (DIALOG) | **Background:** `#FAF9FC`

### Identical structure to S08
Same text content, same checkbox label "मैंने 3-पिन प्लग रख लिया है".

### Difference from S08
- S08 is the initial warning (text only)
- S10 includes a **photo of the 3-pin plug** (the `threepin_plug.webp` asset)
- The photo sits in the text area between the warning and the checkbox

### Build Note
Build S08 and S10 as the **same dialog component** with a `showImage` flag. When image is shown, display `threepin_plug.webp` between warning text and checkbox.

---

## SCREEN 11: Selfie Camera (200:5853)
**Frame:** 360×720dp

### A. Camera Viewfinder (full screen background)
- **Frame:** 360×720dp
- **Background:** `#4D4D4D` (dark grey placeholder — in APK, this is the live camera feed)
- **Contains:** Shutter button at bottom-center

### B. Top App Bar (176dp — taller than usual!)
- Status Bar (24dp): light text on dark
- Header (56dp): back navigation
- **Title section (96dp):** — extra tall!
  - **Instruction:** "व्योम एक्सपर्ट टी-शर्ट पहने हुए एक अच्छी सी सेल्फी लीजिए"
  - **Font:** Noto Sans Display Bold, 24dp, `#FAF9FC` (white on camera bg)
  - This is a **transparent overlay** on top of the camera feed

### C. Shutter Button (bottom-center, y≈644dp)
- **Group:** 64×64dp total

#### C1. Outer Ring (Ellipse 765)
- **Size:** 64×64dp
- **Fill:** `#D9D9D9` (light grey)
- **Stroke:** `#E8E4F0` (lavender), weight 2dp
- **Shape:** Circle

#### C2. Inner Circle (Ellipse 764)
- **Size:** 54×54dp
- **Fill:** `#FAF9FC` (white)
- **Centered** inside the outer ring (5dp inset)

### D. Flash Icon (top-right)
- **Position:** x=320, y=40
- **Size:** 24×24dp
- **Icon:** `flash_on` — custom vector, fill `#FAF9FC`

### E. Timer Display
- **Text:** "01:25" — Poppins Regular, 16dp, `#FFFFFF`
- **Position:** In status bar area

### Shutter Button Correction
**Previous audit said "pink ring outline"** — WRONG. Actual:
- Outer ring fill is `#D9D9D9` (grey) with `#E8E4F0` (lavender) stroke at 2dp
- Inner circle is white `#FAF9FC`
- **No pink at all in the shutter button**

---

## SCREEN 12: Selfie with Pink T-shirt (200:5769)
**Frame:** 360×720dp

### Same camera layout as S11 but:
- Camera shows the technician in a **pink Wiom T-shirt** (live photo/placeholder)
- **Additional instruction overlay:** "मुस्कुराइए और एक अच्छी सी फोटो लीजिए" (Bold 20dp, `#FFFFFF`)
- Counter badge: "1" in circle (Bold 14dp, `#D92B90`)
- Placeholder text: "<Camera Viewfinder>" (Regular 16dp, `#FFDB96` golden)
- Same header text: "व्योम एक्सपर्ट टी-शर्ट पहने हुए एक अच्छी सी सेल्फी लीजिए"

### Build Note
S11 and S12 are the **same camera screen** — S12 just shows the live preview with the person in frame. In the APK, the camera feed handles this automatically. Build as **one screen**.

---

## SCREEN 13: Selfie Review — "रॉकस्टार" (200:6631)
**Frame:** 360×720dp | **Background:** `#FAF9FC`

### A. Header (136dp — extra tall)
- Status Bar (24dp)
- App Bar (56dp)
- Title (56dp): **"आप तो बिलकुल रॉकस्टार लग रहे हैं"** — Display Bold 24dp, `#161021`
  - Subtitle placeholder visible

### B. Photo Display (y=152dp)
- **Mask group:** 328×456dp (16dp margin each side)
- **Contains:**
  - Background rectangle 328×456dp (`#D9D9D9`)
  - IMAGE fill (captured selfie photo)
  - Close/retake button (24×24dp) in corner
  - Corner mask/decoration vector (110×110dp)

### C. Action Area (y=640dp)
- **Background:** `#FAF9FC`
- **Height:** 80dp
- **CTA:** 328×48dp, `#D9008D` background
- **Text:** "कस्टमर के आधार कार्ड की फोटो लें" — Display Bold 16dp, `#FAF9FC`

### Build Note
This is the selfie review/confirmation screen. Shows the captured photo with a compliment headline and a CTA to proceed to Aadhaar capture.

---

## SCREEN 14: Aadhaar Front Capture (200:6879)
**Frame:** 360×720dp

### Same camera layout pattern as S11
- Dark viewfinder background
- **Header instruction:** "कस्टमर से आधार कार्ड मांगे और उसकी एक अच्छी सी फोटो लें" (Display Bold 24dp, `#161021`)
- **Viewfinder overlay text:** "आधार कार्ड को इस क्षेत्र में रखें" (Display Bold 24dp, `#FAF9FC`)
- **Guide rectangle:** Dashed/outlined rectangle overlay showing where to place the Aadhaar card
- Shutter button at bottom

### Build Note
Same camera component as S11, with an additional guide rectangle overlay for Aadhaar card placement.

---

## SCREEN 15: Aadhaar Front Captured (200:6954)
**Frame:** 360×720dp

### Same as S14 but:
- The Aadhaar card is now visible in the viewfinder (captured state)
- Same overlay text "आधार कार्ड को इस क्षेत्र में रखें"
- Auto-transitions to S16

---

## SCREEN 16: Aadhaar Review — Front Done + Back Pending (200:6748)
**Frame:** 360×720dp | **Background:** light

### A. Header
- Title area: "कस्टमर से आधार कार्ड मांगे और उसकी एक अच्छी सी फोटो लें" (Display Bold 24dp)
- Nav: "Add Device ID" (SemiBold 16dp, `#FFFFFF`) — likely a breadcrumb/step indicator

### B. Content
- **"आधार : Back"** — Display Bold 24dp, `#161021` (section heading for back side)
- **Front photo thumbnail:** "Device Photo" label (Regular 14dp, `#808080`) with captured front image
- **"+ Add"** — Bold 16dp, `#D92B90` (add/retake for front)
- **"+ फोटो लें"** — Display Bold 16dp, `#D9008D` (CTA to capture back side)

### Build Note
This is a review screen showing the captured front Aadhaar photo with a CTA to capture the back side. Two photo slots: front (filled) and back (empty with "फोटो लें" button).

---

## SCREEN 17: Aadhaar Back Capture (200:7118)
**Frame:** 360×720dp

### Identical to S14 (Aadhaar Front Capture)
Same camera layout, same overlay text "आधार कार्ड को इस क्षेत्र में रखें", same shutter button.

---

## SCREEN 18: Aadhaar Back Captured (200:7036)
**Frame:** 360×720dp

### Same as S15 — captured state of back side
Auto-transitions to S19.

---

## SCREEN 19: Aadhaar Both Sides Review (200:6814)
**Frame:** 360×720dp | **Background:** light

### Same layout as S16 but:
- Both front and back thumbnails filled
- **Headline:** "कस्टमर से आधार कार्ड मांगे और उसकी एक अच्छी सी फोटो लें" (Display Bold 20dp — note: 20dp not 24dp)
- **CTA:** "आगे बढ़ें" — Display Bold 16dp, `#FAF9FC` on brand bg

### Build Note
Review screen showing both Aadhaar sides with proceed CTA. Reuse S16 layout with both slots filled.

---

## SCREEN 20: Loading (200:6687)
**Frame:** 360×720dp

### No text content — pure loading/animation screen
- Likely shows a spinner or progress animation
- Auto-transitions to next screen
- Build: Simple loading screen with pink arc spinner animation

---

## SCREEN 21: Switch-On Net Box (200:5313)
**Frame:** 360×720dp | **Background:** `#FAF9FC`

### A. Header
- Standard light header: Title + "मदद" link
- **Headline:** "व्योम नेट बॉक्स को स्विच ऑन करें" (Display Bold 24dp, `#161021`)

### B. Content
- **Net Box photo:** the `netbox_power.webp` asset showing the power button
- **Booking status:** "Booking submitted" (Display Bold 16dp, `#A7A1B2` muted)
- **Reassurance:** "निश्चिंत रहे आगे की प्रक्रिया के लिए हमारी टीम आपको कॉल करेगी।" (Display Regular 14dp, `#161021`)

### C. Checkbox
- **Text:** "नेट बॉक्स ऑन कर दिया है" — Display Bold 16dp, `#161021`
- Same checkbox component as S04

### D. CTA
- **Text:** "कस्टमर की डिटेल्स कन्फर्म करें" — Display Bold 16dp, `#FAF9FC` on brand bg

---

## SCREEN 22: Customer Details (200:5924)
**Frame:** 360×720dp | **Background:** `#FAF9FC`

### A. Header
- Nav: "Add Device ID" (SemiBold 16dp, `#FFFFFF`)
- **Headline:** "कस्टमर डिटेल्स" (Display Bold 24dp, `#161021`)

### B. Customer Info Card
- **Name:** "Himanshu Singh" (Bold 16dp, `#161021`)
- **Address:** "G 69, Mata mandir, 3rd street, Ghaziabad Uttar Pradesh" (Display Regular 14dp, `#161021`)
- **Device ID:** "GX412365" (Regular 14dp) / label "डिवाइस ID" (Regular 12dp, `#A7A1B2`)
- **Family Number:** "फैमिली नंबर : 98765 43210" (Regular 14dp, `#161021`) / label "फैमिली नंबर" (Regular 12dp, `#A7A1B2`)
- **Speed:** "50 mbps" (Regular 14dp) / label "स्पीड" (Regular 12dp, `#A7A1B2`)
- **Connection:** "फाइबर" (Regular 14dp) / label "कनेक्शन" (Regular 12dp, `#A7A1B2`)

### C. Aadhaar Section
- **Title:** "आधार कार्ड" (Display SemiBold 14dp, `#161021`)
- **Thumbnails:** Front + Back Aadhaar photos (captured earlier)

### D. CTA
- **Text:** "ISP अकाउंट बनायें" — Display Bold 16dp, `#FAF9FC` on brand bg

---

## SCREEN 24: Checklist + PayG Jankari (200:7327)
**Frame:** 360×720dp | **Background:** `#FAF9FC`

### A. Header
- **Headline:** "Himanshu Singh जी से व्योम ऐप द्वारा पेमेंट करने को कहें" (Display Bold 24dp, `#161021`)

### B. Checklist Steps
Three steps in vertical list:

#### Step 1: WiFi — Done
- **Text:** "WiFi का नाम रख दिया" (Display Regular 16dp, `#161021`)
- **Status:** Completed (green tick)

#### Step 2: Aadhaar — Done
- **Text:** "आधार कार्ड और प्लग तैयार हैंं" (Regular 16dp, `#161021`)
- **Subtext:** "टेक्नीशियन 11 am से 9 pm के बीच आपके घर आएगा" (Display Regular 14dp)
- **Status:** Completed

#### Step 3: Payment — Pending
- **Text:** "पेमेंट करें" (Regular 16dp, `#161021`)
- **Subtext:** "टेक्नीशियन 11 am से 9 pm के बीच आपके घर आएगा" (Display Regular 14dp)
- **Status:** Spinner/pending (pink arc animation)

### C. PayG Info Cards (below checklist)

#### Card 1: "PayG – सिस्टम जानकारी"
- **Header:** SemiBold 14dp, `#FAF9FC` (white text on dark/brand bg)
- **Body:**
  - "₹300 सिक्योरिटी फीस होती है" (SemiBold 14dp, `#161021`)
  - "इसके अलावा कोई भी पेमेंट की अनुमति नहीं है" (SemiBold 14dp, `#161021`)
  - "कस्टमर अपनी ऐप में QR कोड से भी पेमेंट कर सकता है" (Regular 14dp, `#161021`)

#### Card 2: "PayG जरूरी सूचना"
- **Header:** SemiBold 14dp, `#FAF9FC`
- **Body:**
  - "₹300 सिक्योरिटी फीस होती है." (SemiBold 14dp, `#161021`)
  - "इसमें कस्टमर को 2 दिन का नेट मिलता है, उसके बाद कस्टमर अपनी मर्ज़ी से रिचार्ज कर सकते हैं" (Regular 14dp, `#161021`)

### Build Note
This is the key screen that was wrong in the current build — was missing the PayG Jankari cards and had the wrong step 3 text ("पेमेंट हो गयी" instead of "पेमेंट करें").

---

## SCREEN 25: Final / Post-Payment (200:7218)
**Frame:** 360×720dp | **Background:** `#FAF9FC`

### Same as S24 except:
- Step 3 text changes to **"पेमेंट हो गयी"** (payment completed)
- All three steps show completed state (green ticks)
- PayG info cards may still be visible
- Leads to Loading 2 → completion screens

---

## CROSS-SCREEN PATTERNS

### Header Variants
| Type | Height | Background | Text Color | Used In |
|------|--------|------------|------------|---------|
| Dark header | 80dp | `#443152` | `#FAF9FC` | S01, S02, S03 |
| Light header | 80dp+ | `#FAF9FC` | `#161021` | S04, S06, S13, S21, S22, S24 |
| Camera header | 176dp | Transparent | `#FAF9FC` | S11, S12, S14 |

### CTA Button Pattern
- **Width:** 328dp (360 - 16 - 16)
- **Height:** 48dp
- **Background:** `#D9008D`
- **Border radius:** 16dp
- **Text:** Noto Sans Bold/Display Bold, 16dp, `#FAF9FC`
- **Position:** y=656dp (16dp from bottom of 720dp frame)

### Dialog Pattern
- **Width:** 312dp
- **Background:** `#FAF9FC`
- **Padding:** 24dp
- **Head section:** Graphic (96×96 or 48×48) + headline + subtitle
- **Action bar:** Primary (brand pink) + Secondary (light pink) buttons, each 264×48dp, radius 16dp

### Checkbox Pattern
- **Size:** 24×24dp checkbox + text beside
- **Checkbox:** Custom `checkbox-tick` component instance
- **Label:** Display Bold 16dp, `#161021`
- **Gap:** 8dp between checkbox and text

### Camera Screen Pattern
- **Background:** `#4D4D4D` (viewfinder placeholder)
- **Shutter:** 64×64dp group (outer ring `#D9D9D9` with 2dp `#E8E4F0` stroke + inner 54dp `#FAF9FC` circle)
- **Flash:** 24×24dp `flash_on` icon, `#FAF9FC`, top-right
- **Instruction text:** Display Bold 24dp, `#FAF9FC` overlay

### Color Usage Summary
| Color | Hex | Role |
|-------|-----|------|
| Brand pink | `#D9008D` | CTAs, links, badges, active states |
| Info purple | `#6D17CE` | PayG headline, info text, call instruction |
| Warning orange | `#B85C00` | Deadline warnings, pending states |
| Success green | `#008043` | Payment confirmed, completed steps |
| Primary text | `#161021` | Main body text |
| Secondary text | `#665E75` | Hint text, descriptions |
| Muted text | `#A7A1B2` | Labels, disabled text |
| Dark header | `#443152` | App bar, status bar |
| Surface | `#FAF9FC` | Page background, card background |
| Light pink | `#FFE5F6` | Secondary CTA, card headers |
| Light purple | `#F1E5FF` | Device detail rows, Aadhaar sections |
| Light green | `#C9F0DD` | ISP prefill tip bar |

---

## KEY CORRECTIONS TO EXISTING BUILD

| # | Screen | What's Wrong | Figma Correct |
|---|--------|-------------|---------------|
| 1 | S11 | Shutter: "pink ring" | **Grey ring** (`#D9D9D9` fill, `#E8E4F0` 2dp stroke) |
| 2 | S04 | Missing screen | **New screen needed** — PayG acknowledgement with checkbox |
| 3 | S07 | Missing screen | **New dialog** — arrival confirmation with house icon |
| 4 | S08/S10 | Missing screen | **New dialog** — 3-pin plug reminder with checkbox |
| 5 | S13 | Missing screen | **New screen** — selfie review with "रॉकस्टार" compliment |
| 6 | S14-S19 | Missing screens | **6 screens** — Aadhaar capture flow (front+back+reviews) |
| 7 | S24 | Wrong step 3 text | "पेमेंट करें" not "पेमेंट हो गयी" (that's S25) |
| 8 | S24 | Missing PayG cards | Two info cards below checklist |
| 9 | S22 | Missing family number | "फैमिली नंबर : 98765 43210" row |
| 10 | S04 | Headline color | `#6D17CE` (purple) NOT brand pink |
