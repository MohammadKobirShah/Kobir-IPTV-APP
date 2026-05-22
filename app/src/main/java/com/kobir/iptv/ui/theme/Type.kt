package com.kobir.iptv.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

private val Default = TextStyle(
    fontFamily = androidx.compose.ui.text.font.FontFamily.SansSerif,
    letterSpacing = 0.sp
)

val TVTypography = Typography(
    displayLarge = Default.copy(fontSize = 64.sp, fontWeight = FontWeight.Bold, letterSpacing = (-2).sp),
    displayMedium = Default.copy(fontSize = 48.sp, fontWeight = FontWeight.Bold, letterSpacing = (-1).sp),
    displaySmall = Default.copy(fontSize = 36.sp, fontWeight = FontWeight.SemiBold),
    headlineLarge = Default.copy(fontSize = 32.sp, fontWeight = FontWeight.SemiBold),
    headlineMedium = Default.copy(fontSize = 28.sp, fontWeight = FontWeight.SemiBold),
    headlineSmall = Default.copy(fontSize = 24.sp, fontWeight = FontWeight.Medium),
    titleLarge = Default.copy(fontSize = 22.sp, fontWeight = FontWeight.Medium),
    titleMedium = Default.copy(fontSize = 20.sp, fontWeight = FontWeight.Medium),
    titleSmall = Default.copy(fontSize = 18.sp, fontWeight = FontWeight.Medium),
    bodyLarge = Default.copy(fontSize = 18.sp),
    bodyMedium = Default.copy(fontSize = 16.sp),
    bodySmall = Default.copy(fontSize = 14.sp),
    labelLarge = Default.copy(fontSize = 16.sp, fontWeight = FontWeight.Medium, letterSpacing = 0.5.sp),
    labelMedium = Default.copy(fontSize = 14.sp, letterSpacing = 0.5.sp),
    labelSmall = Default.copy(fontSize = 12.sp, letterSpacing = 0.5.sp)
)
