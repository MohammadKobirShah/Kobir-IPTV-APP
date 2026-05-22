package com.kobir.iptv.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val TVColorScheme = darkColorScheme(
    primary = TVAccent,
    onPrimary = TVOnAccent,
    primaryContainer = TVAccentGlow,
    secondary = TVSecondary,
    tertiary = TVTertiary,
    background = TVBackground,
    onBackground = TVOnBackground,
    surface = TVSurface,
    onSurface = TVOnSurface,
    surfaceVariant = TVSurfaceVariant,
    onSurfaceVariant = TVOnSurfaceVariant,
)

@Composable
fun KobirIPTVTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = TVColorScheme,
        typography = TVTypography,
        content = content
    )
}
