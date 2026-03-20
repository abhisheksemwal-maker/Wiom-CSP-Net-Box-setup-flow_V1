package com.wiom.partner.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.ExpandMore
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Router
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material.icons.rounded.Visibility
import androidx.compose.material.icons.rounded.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wiom.partner.components.CustomerDetailsBottomSheet
import com.wiom.partner.components.LightHeader
import com.wiom.partner.components.PrimaryCta
import com.wiom.partner.theme.*
import kotlinx.coroutines.delay

private enum class IspType(val label: String) {
    PPPOE("PPPoE"),
    STATIC_IP("Static IP"),
    DHCP("DHCP")
}

private enum class VlanType(val label: String) {
    TAG("TAG"),
    TRANSPARENT("TRANSPARENT"),
    UNTAG("UNTAG")
}

private enum class FieldValidation { IDLE, VALIDATING, VALID, ERROR }

@Composable
fun S16IspForm(
    onNext: () -> Unit,
    onClose: () -> Unit
) {
    var ispType by remember { mutableStateOf(IspType.PPPOE) }
    var showCustomerSheet by remember { mutableStateOf(false) }

    // PPPoE fields
    var username by remember { mutableStateOf("") }
    var usernameValidation by remember { mutableStateOf(FieldValidation.IDLE) }
    var password by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }
    val serviceName = "Auto"
    var vlanType by remember { mutableStateOf<VlanType?>(null) }
    var vlanExpanded by remember { mutableStateOf(false) }
    var vlanId by remember { mutableStateOf("") }
    var netBoxId by remember { mutableStateOf("") }
    var netBoxValidation by remember { mutableStateOf(FieldValidation.IDLE) }

    // Progressive reveal: Password + Service Name always visible for PPPoE.
    // VLAN appears after username validated. Net Box ID after VLAN selected.
    val showPasswordField = true
    val showServiceName = true
    val showVlan = usernameValidation == FieldValidation.VALID
    val showVlanId = vlanType == VlanType.TAG
    val showNetBoxId = vlanType != null && (!showVlanId || vlanId.isNotEmpty())
    val allComplete = netBoxValidation == FieldValidation.VALID

    // Username validation simulation
    LaunchedEffect(usernameValidation) {
        if (usernameValidation == FieldValidation.VALIDATING) {
            delay(1500)
            usernameValidation = FieldValidation.VALID
        }
    }

    // Net Box ID validation simulation
    LaunchedEffect(netBoxValidation) {
        if (netBoxValidation == FieldValidation.VALIDATING) {
            delay(1500)
            netBoxValidation = FieldValidation.VALID
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
    Column(
        modifier = Modifier.fillMaxSize().background(Bg)
    ) {
        // Light header: padding:4, close #352D42, flex:1, "मदद" fs:14 fw:600 tc:Brand
        LightHeader(onClose = onClose, showHelp = true)

        // Scrollable content
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {
            // Title row: padding:[8,16,16,16] → top:8, right:16, bottom:16, left:16
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                // "ISP की डिटेल्स भरें" fs:24 fw:700
                Text(
                    text = "ISP की डिटेल्स भरें",
                    color = Pri,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 32.sp,
                    modifier = Modifier.weight(1f)
                )
                // "कस्टमर डिटेल्स देखें" fs:12 fw:600 tc:Brand
                Text(
                    text = "कस्टमर डिटेल्स देखें",
                    color = Brand,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.clickable { showCustomerSheet = true }
                )
            }

            // Radio group: layout:H gap:16
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                IspType.entries.forEach { type ->
                    val selected = ispType == type
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .height(48.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Bg)
                            .border(
                                1.dp,
                                BorderDivider,
                                RoundedCornerShape(12.dp)
                            )
                            .clickable { ispType = type }
                            .padding(12.dp)
                    ) {
                        // Custom radio circle: matches WebView SVG 24×24 viewBox, circle r=11 stroke=2, dot r=6
                        Canvas(modifier = Modifier.size(24.dp)) {
                            val cx = size.width / 2
                            val cy = size.height / 2
                            val scale = size.width / 24f // scale factor from SVG viewBox to canvas
                            // Outer circle: r=11, stroke=2
                            drawCircle(
                                color = Color(0xFF352D42),
                                radius = 11f * scale,
                                center = Offset(cx, cy),
                                style = Stroke(width = 2f * scale)
                            )
                            if (selected) {
                                // Inner dot: r=6
                                drawCircle(
                                    color = Color(0xFFD9008D),
                                    radius = 6f * scale,
                                    center = Offset(cx, cy)
                                )
                            }
                        }
                        Spacer(Modifier.width(8.dp))
                        // Label: fs:14 fw:400 tc:#161021
                        Text(
                            text = type.label,
                            color = Pri,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Normal
                        )
                    }
                }
            }

            // PPPoE form fields: gap:24 between fields, margin-top:24
            Spacer(Modifier.height(24.dp))

            if (ispType == IspType.PPPOE) {
                Column(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    // Field 1: Username
                    IspInputField(
                        label = "Username",
                        value = username,
                        onValueChange = {
                            username = it
                            if (usernameValidation != FieldValidation.IDLE) {
                                usernameValidation = FieldValidation.IDLE
                            }
                        },
                        placeholder = "उदाहरण: Estell Networks",
                        icon = Icons.Rounded.Person,
                        validation = usernameValidation,
                        onSubmit = {
                            if (username.isNotEmpty()) {
                                usernameValidation = FieldValidation.VALIDATING
                            }
                        },
                        showSubmit = username.isNotEmpty() && usernameValidation == FieldValidation.IDLE
                    )

                    // Field 2: Password (progressive reveal)
                    AnimatedVisibility(
                        visible = showPasswordField,
                        enter = expandVertically() + fadeIn()
                    ) {
                        IspPasswordField(
                            label = "Password",
                            value = password,
                            onValueChange = { password = it },
                            placeholder = "Password डालें",
                            showPassword = showPassword,
                            onToggleVisibility = { showPassword = !showPassword }
                        )
                    }

                    // Field 3: Service Name (pre-filled, read-only)
                    AnimatedVisibility(
                        visible = showServiceName,
                        enter = expandVertically() + fadeIn()
                    ) {
                        IspReadOnlyField(
                            label = "Service Name",
                            value = serviceName
                        )
                    }

                    // Field 4: VLAN dropdown
                    AnimatedVisibility(
                        visible = showVlan,
                        enter = expandVertically() + fadeIn()
                    ) {
                        IspVlanDropdown(
                            label = "VLAN",
                            selected = vlanType,
                            expanded = vlanExpanded,
                            onExpandToggle = { vlanExpanded = !vlanExpanded },
                            onSelect = { vlanType = it; vlanExpanded = false }
                        )
                    }

                    // Field 5: VLAN ID (conditional when TAG)
                    AnimatedVisibility(
                        visible = showVlanId,
                        enter = expandVertically() + fadeIn()
                    ) {
                        IspInputField(
                            label = "VLAN ID (128 - 1492)",
                            value = vlanId,
                            onValueChange = { vlanId = it },
                            placeholder = "E.g 128",
                            icon = null,
                            validation = FieldValidation.IDLE,
                            onSubmit = {},
                            showSubmit = false,
                            keyboardType = KeyboardType.Number
                        )
                    }

                    // Field 6: Net Box ID
                    AnimatedVisibility(
                        visible = showNetBoxId,
                        enter = expandVertically() + fadeIn()
                    ) {
                        IspInputField(
                            label = "नेट बॉक्स ID डालें",
                            value = netBoxId,
                            onValueChange = {
                                netBoxId = it
                                if (netBoxValidation != FieldValidation.IDLE) {
                                    netBoxValidation = FieldValidation.IDLE
                                }
                            },
                            placeholder = "उदाहरण : 123456789",
                            icon = Icons.Rounded.Router,
                            validation = netBoxValidation,
                            onSubmit = {
                                if (netBoxId.isNotEmpty()) {
                                    netBoxValidation = FieldValidation.VALIDATING
                                }
                            },
                            showSubmit = netBoxId.isNotEmpty() && netBoxValidation == FieldValidation.IDLE
                        )
                    }
                }
            } else if (ispType == IspType.STATIC_IP) {
                // --- Static IP form: 5 fields ---
                StaticIpForm()
            } else {
                // --- DHCP readout card ---
                DhcpReadout()
            }

            Spacer(Modifier.height(24.dp))
        }

        // CTA: "नेट बॉक्स तैयार करें" hidden until all complete, padding:16, bg:Bg shadow:0 -2px 8px
        AnimatedVisibility(
            visible = allComplete,
            enter = fadeIn() + slideInVertically(initialOffsetY = { it / 2 })
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .drawBehind {
                        // shadow: 0 -2px 8px rgba(0,0,0,0.08)
                        drawRect(
                            color = Color.Black.copy(alpha = 0.08f),
                            topLeft = Offset(0f, -2.dp.toPx()),
                            size = Size(size.width, 2.dp.toPx())
                        )
                    }
                    .background(Bg)
                    .padding(16.dp)
            ) {
                PrimaryCta(
                    text = "नेट बॉक्स तैयार करें",
                    onClick = onNext
                )
            }
        }
    }

    // Customer details bottom sheet overlay
    CustomerDetailsBottomSheet(
        visible = showCustomerSheet,
        onDismiss = { showCustomerSheet = false }
    )
    } // end Box
}

// --- ISP Form Sub-Components (matching HTML spec exactly) ---

@Composable
private fun IspInputField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    icon: ImageVector?,
    validation: FieldValidation,
    onSubmit: () -> Unit,
    showSubmit: Boolean,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    val isFocusedOrFilled = value.isNotEmpty()

    Column {
        // Label: fs:16 fw:700 tc:#161021 (becomes fw:400 when active/filled)
        Text(
            text = label,
            color = Pri,
            fontSize = 16.sp,
            fontWeight = if (isFocusedOrFilled) FontWeight.Normal else FontWeight.Bold
        )
        Spacer(Modifier.height(8.dp))

        // Input container: h:48 bg:#FAF9FC stroke:#D7D3E0/1px r:12 p:[12,16,12,16] gap:12
        val borderColor = when (validation) {
            FieldValidation.VALID -> Pos         // stroke:#008043
            FieldValidation.ERROR -> Neg
            FieldValidation.VALIDATING -> BorderFocus
            FieldValidation.IDLE -> if (isFocusedOrFilled) BorderFocus else BorderDivider // #D7D3E0
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Bg) // bg:#FAF9FC
                .border(1.dp, borderColor, RoundedCornerShape(12.dp))
                .padding(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Leading icon: 24sp color:#A7A1B2
            if (icon != null) {
                Icon(
                    icon,
                    contentDescription = null,
                    tint = Hint, // #A7A1B2
                    modifier = Modifier.size(24.dp)
                )
            }

            // Text input
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier.weight(1f),
                textStyle = TextStyle(
                    color = Pri,        // #161021
                    fontSize = 16.sp,   // fs:16
                    fontWeight = FontWeight.Bold // fw:700
                ),
                singleLine = true,
                cursorBrush = SolidColor(Brand), // caret:Brand
                keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
                decorationBox = { inner ->
                    Box {
                        if (value.isEmpty()) {
                            // Placeholder: fs:16 fw:400 tc:#A7A1B2
                            Text(
                                placeholder,
                                color = Hint,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Normal
                            )
                        }
                        inner()
                    }
                }
            )

            // Trailing: spinner (20px Brand) / green check_circle / inline "सबमिट करें"
            when {
                validation == FieldValidation.VALIDATING -> {
                    // Spinner: 20px Brand — Canvas arc drawing
                    val brandColor = Brand
                    var sweepAngle by remember { mutableFloatStateOf(0f) }
                    LaunchedEffect(Unit) {
                        while (true) {
                            delay(16)
                            sweepAngle = (sweepAngle + 6f) % 360f
                        }
                    }
                    Canvas(modifier = Modifier.size(20.dp)) {
                        drawArc(
                            color = brandColor,
                            startAngle = sweepAngle,
                            sweepAngle = 270f,
                            useCenter = false,
                            style = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round),
                            topLeft = Offset(1.dp.toPx(), 1.dp.toPx()),
                            size = Size(size.width - 2.dp.toPx(), size.height - 2.dp.toPx())
                        )
                    }
                }
                validation == FieldValidation.VALID -> {
                    // Green check_circle icon
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(Pos),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Rounded.Check,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
                showSubmit -> {
                    // Inline "सबमिट करें" Brand fs:14 fw:600
                    Text(
                        "सबमिट करें",
                        color = Brand,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.clickable { onSubmit() }
                    )
                }
            }
        }
    }
}

@Composable
private fun IspPasswordField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    showPassword: Boolean,
    onToggleVisibility: () -> Unit
) {
    val isFocusedOrFilled = value.isNotEmpty()

    Column {
        // Label: fs:16 fw:700 → fw:400 when active/filled
        Text(
            text = label,
            color = Pri,
            fontSize = 16.sp,
            fontWeight = if (isFocusedOrFilled) FontWeight.Normal else FontWeight.Bold
        )
        Spacer(Modifier.height(8.dp))

        // Input container: h:48 bg:#FAF9FC stroke:#D7D3E0/1px r:12 p:[12,16,12,16] gap:12
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Bg)
                .border(
                    1.dp,
                    if (isFocusedOrFilled) BorderFocus else BorderDivider,
                    RoundedCornerShape(12.dp)
                )
                .padding(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Lock icon: 24sp #A7A1B2
            Icon(
                Icons.Rounded.Lock,
                contentDescription = null,
                tint = Hint,
                modifier = Modifier.size(24.dp)
            )

            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier.weight(1f),
                textStyle = TextStyle(
                    color = Pri,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                ),
                singleLine = true,
                cursorBrush = SolidColor(Brand),
                visualTransformation = if (showPassword) VisualTransformation.None
                    else PasswordVisualTransformation(),
                decorationBox = { inner ->
                    Box {
                        if (value.isEmpty()) {
                            Text(
                                placeholder,
                                color = Hint,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Normal
                            )
                        }
                        inner()
                    }
                }
            )

            // Eye toggle: visibility_off #A7A1B2
            Icon(
                if (showPassword) Icons.Rounded.Visibility else Icons.Rounded.VisibilityOff,
                contentDescription = "Toggle password",
                tint = Hint,
                modifier = Modifier.size(24.dp).clickable { onToggleVisibility() }
            )
        }
    }
}

@Composable
private fun IspReadOnlyField(
    label: String,
    value: String
) {
    Column {
        // Label: fs:16 fw:700
        Text(
            text = label,
            color = Pri,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.height(8.dp))

        // Container: h:48 bg:#FAF9FC stroke:#D7D3E0 r:12
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Bg)
                .border(1.dp, BorderDivider, RoundedCornerShape(12.dp))
                .padding(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Settings icon: 24sp #A7A1B2
            Icon(
                Icons.Rounded.Settings,
                contentDescription = null,
                tint = Hint,
                modifier = Modifier.size(24.dp)
            )

            // Pre-filled "Auto" in Brand color
            Text(
                text = value,
                color = Brand,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun IspVlanDropdown(
    label: String,
    selected: VlanType?,
    expanded: Boolean,
    onExpandToggle: () -> Unit,
    onSelect: (VlanType) -> Unit
) {
    Column {
        // Label: fs:16 fw:700
        Text(
            text = label,
            color = Pri,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.height(8.dp))

        // Dropdown trigger: h:48 bg:#FAF9FC stroke:#D7D3E0 r:12
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Bg)
                .border(
                    1.dp,
                    if (selected != null) BorderFocus else BorderDivider,
                    RoundedCornerShape(12.dp)
                )
                .clickable { onExpandToggle() }
                .padding(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = selected?.label ?: "UNTAG",
                color = if (selected != null) Pri else Pri,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f)
            )
            // expand_more chevron
            Icon(
                Icons.Rounded.ExpandMore,
                contentDescription = null,
                tint = Hint,
                modifier = Modifier.size(24.dp)
            )
        }

        // Options list: TAG/TRANSPARENT/UNTAG in bordered list
        AnimatedVisibility(visible = expanded) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .border(1.dp, BorderDivider, RoundedCornerShape(12.dp))
                    .background(Bg)
            ) {
                VlanType.entries.forEach { type ->
                    val isSelected = selected == type
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onSelect(type) }
                            .background(if (isSelected) N100 else Color.Transparent)
                            .padding(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 12.dp)
                    ) {
                        Text(
                            text = type.label,
                            color = Pri,
                            fontSize = 16.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                }
            }
        }
    }
}

// --- Static IP Form: 5 input fields ---
@Composable
private fun StaticIpForm() {
    var ipAddress by remember { mutableStateOf("") }
    var gateway by remember { mutableStateOf("") }
    var netmask by remember { mutableStateOf("255.255.255.0") }
    var primaryDns by remember { mutableStateOf("8.8.8.8") }
    var secondaryDns by remember { mutableStateOf("8.8.4.4") }

    Column(
        modifier = Modifier.padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        StaticIpField(
            label = "IP Address",
            value = ipAddress,
            onValueChange = { ipAddress = it },
            placeholder = "udaharan: 192.168.1.1"
        )
        StaticIpField(
            label = "IPv4 Gateway",
            value = gateway,
            onValueChange = { gateway = it },
            placeholder = "udaharan: 192.168.1.1"
        )
        StaticIpField(
            label = "IP Netmask",
            value = netmask,
            onValueChange = { netmask = it },
            placeholder = "255.255.255.0"
        )
        StaticIpField(
            label = "Primary DNS",
            value = primaryDns,
            onValueChange = { primaryDns = it },
            placeholder = "8.8.8.8"
        )
        StaticIpField(
            label = "Secondary DNS",
            value = secondaryDns,
            onValueChange = { secondaryDns = it },
            placeholder = "8.8.4.4"
        )
    }
}

@Composable
private fun StaticIpField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String
) {
    Column {
        // Label: fs:16 fw:700 tc:#161021
        Text(
            text = label,
            color = Pri,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.height(8.dp))

        // Input container: h:48 bg:#FAF9FC stroke:#D7D3E0/1dp r:12 p:[12,16]
        val borderColor = if (value.isNotEmpty()) BorderFocus else BorderDivider

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Bg)
                .border(1.dp, borderColor, RoundedCornerShape(12.dp))
                .padding(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier.weight(1f),
                textStyle = TextStyle(
                    color = Pri,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                ),
                singleLine = true,
                cursorBrush = SolidColor(Brand),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                decorationBox = { inner ->
                    Box {
                        if (value.isEmpty()) {
                            Text(
                                placeholder,
                                color = Hint,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Normal
                            )
                        }
                        inner()
                    }
                }
            )
        }
    }
}

// --- DHCP Readout Card ---
@Composable
private fun DhcpReadout() {
    Column(
        modifier = Modifier.padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Title: fs:16 fw:700 color:#1A1A1A lh:24
        Text(
            text = "Please Verify DHCP settings again",
            color = Color(0xFF1A1A1A),
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            lineHeight = 24.sp
        )

        // Card: bg:N100 (#F1EDF7), r:12, p:16, gap:8
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(N100)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val textStyle = TextStyle(
                color = Pri, // #161021
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal
            )
            Text("Interface", style = textStyle)
            Text("proto='dhcp'", style = textStyle)
            Text("device='eth0.2'", style = textStyle)
            Text("metric='10'", style = textStyle)
        }
    }
}
