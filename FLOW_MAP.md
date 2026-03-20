# Wiom Technician APK — Complete Flow Map
## Mapped from Figma prototype vectors (200:7929)

### COMPLETE FLOW (25 unique screens):

```
S01  199:4938  Task List (home)
  → [CTA arrow]
S02  200:4961  Task Detail (card on dark bg)
  → [काम पूरा करें]
S03  200:5040  Task Detail (tapped state, same but centered card)
  → [Service ticket actions]
S04  200:7193  PayG Acknowledgement 1 (₹300 checkbox, unchecked)
  → [agree checkbox]
S05  200:7204  PayG Acknowledgement 2 (checked, auto-transitions)
  → [auto]
S06  200:5143  Transfer Info (customer details + "सेटअप शुरू करें")
  → [actions]
S07  200:5119  Dialog: Arrival ("क्या आप कस्टमर के घर पर हैं?")
  → [हाँ, पहुँच गया हूँ]
S08  200:5286  Dialog: 3-pin confirm (with checkbox)
  → [agree]
S09  200:5201  Transfer Info 2 (same layout, post-arrival)
  → [Dialog Action bar]
S10  200:5259  Dialog: 3-pin plug (with image + warning)
  → [checkbox agree → auto]
S11  200:5853  Selfie Camera (viewfinder, "टी-शर्ट पहने हुए")
  → [shutter capture]
S12  200:5769  Selfie Review ("रॉकस्टार" compliment + photo)
  → [कस्टमर के आधार कार्ड की फोटो लें]
S13  200:6631  Selfie Review with CTA
  → [CTA]
S14  200:6879  Aadhaar Front Capture (camera viewfinder + guide rect)
  → [shutter]
S15  200:6954  Aadhaar Front Captured (card in viewfinder)
  → [auto]
S16  200:6748  Aadhaar Review: Front done + Back "फोटो लें"
  → [फोटो लें]
S17  200:7118  Aadhaar Back Capture viewfinder
  → [shutter]
S18  200:7036  Aadhaar Back Captured
  → [auto]
S19  200:6814  Aadhaar Both Sides Review
  → [actions]
S20  200:6687  Loading 1
  → [auto]
S21  200:5313  Switch-On Net Box (with photo + checkbox)
  → [checkbox → auto]
S22  200:5924  Customer Details (name, address, Aadhaar thumbs, "ISP अकाउंट बनायें")
  → [ISP अकाउंट बनायें]
  → Sequential Installation screens:
S23  200:5385→200:5497→200:5565→200:5633→200:5701 (installation steps)
  → 200:6032→200:6144→200:6256→200:6365
S24  200:7327  Checklist + PayG Jankari (WiFi✓, Aadhaar✓, Payment spinner + PayG info card)
  → [auto after payment]
S25  200:7218  Final Revised Installation
  → Loading 2 (200:7214)
  → 200:5440→200:5385 (completion loop)
```

### SCREENS I CURRENTLY HAVE (8):
1. Task List ✓
2. Task Detail ✓
3. PayG Ack ✓
4. Transfer Info ✓ (with arrival + 3-pin overlays)
5. Selfie Camera ✓
6. Switch-On ✓
7. Checklist ✓ (but wrong — missing PayG Jankari card, spinner is wrong)
8. Customer Details ✓ (but wrong CTA text, missing Aadhaar thumbs)

### MISSING SCREENS TO BUILD (17):
- Selfie Review ("रॉकस्टार" + photo display)
- Aadhaar Front Capture (camera + guide rect overlay)
- Aadhaar Front Captured (card visible)
- Aadhaar Review (front done + back camera CTA)
- Aadhaar Back Capture
- Aadhaar Back Captured
- Aadhaar Both Sides Review
- Loading screens (×2)
- Sequential Rohit Revised Installation screens (×8+)
- Corrected Checklist with PayG Jankari info card

### VISUAL FIXES NEEDED ON EXISTING SCREENS:
- Step indicator stroke (0/8 badge — SVG ring progress)
- Real device status bar (Android bridge done, needs HTML removal)
- Camera uses real device camera (WebChromeClient done)
- Task detail dark bg: radial/vignette, not gradient
- Checklist: pink arc spinner + PayG Jankari card below
- Customer Details: Aadhaar thumbnails + "ISP अकाउंट बनायें" CTA + "फैमिली नंबर" row
- Icon consistency: export custom icons from Figma as SVG where they deviate from Material
- Shutter button: pink ring outline (not solid white)
