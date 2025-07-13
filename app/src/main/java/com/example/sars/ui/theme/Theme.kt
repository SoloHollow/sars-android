package com.example.jetbrainscomponents.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFFFFFFFF),// White - Buttons, top app bar, selected tabs/icons, highlights
    secondary = Color(0xFF999999),// Light Gray - Accent areas, toggles, switches, small highlights
    background = Color(0xFF0A0A0A), // Almost Black - Whole screen background
    surface = Color(0xFF121212), // Charcoal - Card, dialog, bottom bar, navigation bar backgrounds
    onPrimary = Color.Black, // Text/icon color on top of primary surfaces
    onSecondary = Color(0xFFFDFDFD), // Text/icon color on top of secondary
    onBackground = Color.White, // Text/icons that go on the background
    onSurface = Color.White, // Text/icons on surfaces (like in a Card or TopAppBar)
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF000000), // Pure Black - Buttons, top app bar, selected tabs/icons, highlights
    secondary = Color(0xFF666666), // Dim gray -	Accent areas, toggles, switches, small highlights
    background = Color(0xFFFDFDFD), // Off-White - Whole screen background
    surface = Color(0xFFFFFFFF), // White - Card, dialog, bottom bar, navigation bar backgrounds
    onPrimary = Color.White, // Text/icon color on top of primary surfaces
    onSecondary = Color(0xFFFDFDFD), // Text/icon color on top of secondary
    onBackground = Color(0xFF666666), // Text/icons that go on the background
    onSurface = Color.Black, // Text/icons on surfaces (like in a Card or TopAppBar)
)

@Composable
fun JetbrainsComponentsTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography(),
        shapes = Shapes(),
        content = content
    )
}
