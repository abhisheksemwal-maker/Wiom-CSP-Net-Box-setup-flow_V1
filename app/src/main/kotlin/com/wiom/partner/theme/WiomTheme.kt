package com.wiom.partner.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val WiomColorScheme = lightColorScheme(
    primary = Brand,
    onPrimary = Bg,
    surface = Bg,
    onSurface = Pri,
    surfaceVariant = N100,
    outline = Border,
    error = Neg,
)

@Composable
fun WiomTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = WiomColorScheme,
        content = content
    )
}
