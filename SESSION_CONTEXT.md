# Wiom Technician APK — Session Context

## Project Goal
Build Android APK for Wiom technician installation flow.
Approach: Android WebView shell loading HTML/CSS/JS screens.
Screens must be: scalable to varying aspect ratios, faithful to Figma design, real interactive inputs, linear flow (no backend).

## Setup Status
- Java JDK 17: INSTALLED at C:/Program Files/Microsoft/jdk-17.0.18.8-hotspot
- Android Studio: INSTALLING (winget, may need completion)
- Project folder: C:/Users/Abhishek Semwal/wiom-apk/

## Figma Source
- File: PwrhO9KnvdZ4NNYwz3B85d (claude-playground)
- Section node: 200-7929 ("Test android flow")
- Token: REDACTED

## Design Language (from screenshots)
- Brand pink: #D9008D
- Header: dark purple #443152
- Font: Noto Sans Devanagari (Hindi primary)
- Status bar: system (dark bg)
- Cards: white with pink border/accent, border-radius ~16px
- CTA: full-width pink button at bottom
- Back: ← arrow in header | Close: × top-left
- "मदद" (Help) link top-right in pink

## All 38 Frame IDs + Image URLs
199:4938 → https://figma-alpha-api.s3.us-west-2.amazonaws.com/images/7fff579d-7311-46e8-8471-3933d8f0fb4a  [Only New Tasks - TASK LIST]
200:4961 → https://figma-alpha-api.s3.us-west-2.amazonaws.com/images/2324c7de-0070-4ee0-944d-aadcc55dffbf  [Variation 2 - Screen 14350 - TASK DETAIL]
200:5040 → https://figma-alpha-api.s3.us-west-2.amazonaws.com/images/4cabd3d3-c6c8-455e-88fb-e6e19fca0f24  [Variation 2 - Screen 14351]
200:5119 → https://figma-alpha-api.s3.us-west-2.amazonaws.com/images/152b6080-6486-4a60-8881-11cbeef0ad7e  [dialog]
200:5143 → https://figma-alpha-api.s3.us-west-2.amazonaws.com/images/227bbd3d-03e6-4a14-aac6-6ce6f4c006e5  [Transfer info - New Customer - Aadhar Available]
200:5201 → https://figma-alpha-api.s3.us-west-2.amazonaws.com/images/dfa81a74-801c-456b-93e7-4a7348057330  [Transfer info - New Customer - Aadhar Available 2]
200:5259 → https://figma-alpha-api.s3.us-west-2.amazonaws.com/images/476fdefd-1224-477e-b2e9-359998823aae  [dialog 2]
200:5286 → https://figma-alpha-api.s3.us-west-2.amazonaws.com/images/8c5ca50a-0fa9-405f-9543-94db3554c465  [dialog 3]
200:5313 → https://figma-alpha-api.s3.us-west-2.amazonaws.com/images/408be139-5cc9-4a50-814f-8e4f00a9e2c2  [Rohit App_Revised Installation 1]
200:5385 → https://figma-alpha-api.s3.us-west-2.amazonaws.com/images/2be357b9-8bf7-481f-ae5a-de18aab8aebe  [Variation 2 - Screen 14352]
200:5440 → https://figma-alpha-api.s3.us-west-2.amazonaws.com/images/7abe17a1-6c46-4833-b342-636c5b9a0423  [Variation 2 - Screen 14353]
200:5497 → https://figma-alpha-api.s3.us-west-2.amazonaws.com/images/28145cd3-10f0-4996-aaf6-41ff1c59c60c  [Rohit App_Revised Installation 2]
200:5565 → https://figma-alpha-api.s3.us-west-2.amazonaws.com/images/c168f517-1295-49f8-925b-3150e97a1893  [Rohit App_Revised Installation 3]
200:5633 → https://figma-alpha-api.s3.us-west-2.amazonaws.com/images/75872815-58cb-4349-8fd3-01efa3f7edd9  [Rohit App_Revised Installation 4]
200:5701 → https://figma-alpha-api.s3.us-west-2.amazonaws.com/images/09497386-a054-43c4-960b-ef8e4ac96496  [Rohit App_Revised Installation 5]
200:5769 → https://figma-alpha-api.s3.us-west-2.amazonaws.com/images/beca3a19-a8ed-4a9b-8dd0-bcadb00d9e01  [Rohit App_Revised Installation 6]
200:5853 → https://figma-alpha-api.s3.us-west-2.amazonaws.com/images/3f6c4638-730e-49d5-b279-c7a704daebfa  [Rohit App_Aadhar Capture 391]
200:5924 → https://figma-alpha-api.s3.us-west-2.amazonaws.com/images/d8048aab-3746-4c6c-8c9c-97c91a11edd9  [Variation 2 - Screen 14354]
200:6032 → https://figma-alpha-api.s3.us-west-2.amazonaws.com/images/dcc74df2-88b6-489a-9036-4c9733402247  [Rohit App_Revised Installation 7]
200:6144 → https://figma-alpha-api.s3.us-west-2.amazonaws.com/images/6c08a784-c219-4fa4-bdea-6e1ade5a3b79  [Rohit App_Revised Installation 8]
200:6256 → https://figma-alpha-api.s3.us-west-2.amazonaws.com/images/f8e2b833-4255-445b-b99d-9d6f9a88fc8a  [Rohit App_Revised Installation 9]
200:6365 → https://figma-alpha-api.s3.us-west-2.amazonaws.com/images/569fe10a-ca74-4f83-900e-70df87225ae9  [Rohit App_Revised Installation 10]
200:6474 → https://figma-alpha-api.s3.us-west-2.amazonaws.com/images/55edb9df-6093-4d87-a7d6-abd8522ed842  [Rohit App_Aadhar Capture 392]
200:6556 → https://figma-alpha-api.s3.us-west-2.amazonaws.com/images/4073122f-ec50-4e72-af55-d887c6de07f8  [Rohit App_Aadhar Capture 393]
200:6631 → https://figma-alpha-api.s3.us-west-2.amazonaws.com/images/af0f9929-115d-4c52-853c-6486d8a3c947  [Variation 2 - Screen 14355]
200:6687 → https://figma-alpha-api.s3.us-west-2.amazonaws.com/images/a4f7c30e-fe40-459a-95ae-60ab2a1a8b5b  [Loading 1]
200:6691 → https://figma-alpha-api.s3.us-west-2.amazonaws.com/images/4b292e62-1f2f-4b54-ae73-137905b77974  [Variation 2 - Screen 14356]
200:6748 → https://figma-alpha-api.s3.us-west-2.amazonaws.com/images/8c9fe94a-d1d3-4e7d-84e7-aafb29af8031  [Variation 2 - Screen 14357]
200:6814 → https://figma-alpha-api.s3.us-west-2.amazonaws.com/images/82e0e3f7-9e73-40fa-b4ec-09fd85933e26  [Variation 2 - Screen 14358]
200:6879 → https://figma-alpha-api.s3.us-west-2.amazonaws.com/images/d75abd77-5d4a-4626-a43a-689c678d6aa5  [Rohit App_Aadhar Capture 394]
200:6954 → https://figma-alpha-api.s3.us-west-2.amazonaws.com/images/d7f7e86a-ba66-46f6-a301-1c4fa750c1ce  [Rohit App_Aadhar Capture 395]
200:7036 → https://figma-alpha-api.s3.us-west-2.amazonaws.com/images/44ab8a69-5aab-4681-81f3-62fc6471cc17  [Rohit App_Aadhar Capture 396]
200:7118 → https://figma-alpha-api.s3.us-west-2.amazonaws.com/images/b5f7b6cd-0fe6-491f-b8c9-fbd6abf6141e  [Rohit App_Aadhar Capture 397]
200:7193 → https://figma-alpha-api.s3.us-west-2.amazonaws.com/images/703b093f-4643-46ef-97f0-fa4cbb3f65a3  [Acknowledgement 1]
200:7204 → https://figma-alpha-api.s3.us-west-2.amazonaws.com/images/3eb2e76f-463d-429d-b783-b607ef5bad99  [Acknowledgement 2]
200:7214 → https://figma-alpha-api.s3.us-west-2.amazonaws.com/images/343c4bc5-4404-494e-b1dc-8f23509c8cb9  [Loading 2]
200:7218 → https://figma-alpha-api.s3.us-west-2.amazonaws.com/images/0d6cb57d-30bb-42d1-802f-17d871f0ce05  [Rohit App_Revised Installation 11]
200:7327 → https://figma-alpha-api.s3.us-west-2.amazonaws.com/images/1850b062-d779-4b82-9ba4-0b437e267935  [Rohit App_Revised Installation 12]

## Prototype Flow (from vectors)
Only New Tasks → [CTA] → Screen 14350 (Task Detail)
Screen 14350 → [काम पूरा करें] → Acknowledgement
Acknowledgement → [agree] → Transfer info (New Customer, Aadhar Available)
Transfer info → [actions] → dialog
dialog → [agree] → Transfer info 2
Transfer info → [Dialog Action bar] → Aadhar Capture 391
Aadhar Capture 391 → [capture] → Rohit Revised Installation
Rohit Revised → multiple sequential steps → Loading → Screen 14353 → Screen 14352
Aadhar Capture 394 → 395 → [shutter] → Screen 14357
Aadhar Capture 397 → 396 → [shutter] → Screen 14358
Loading 2 → Rohit Revised Installation 11

## Next Steps for New Session
1. Check Android Studio install complete
2. Set JAVA_HOME + ANDROID_HOME env vars
3. Create Android WebView project skeleton
4. Download remaining screens
5. Build HTML for each screen (start with task list → task detail → acknowledgement)
6. Wire linear flow in JS
7. Build debug APK
