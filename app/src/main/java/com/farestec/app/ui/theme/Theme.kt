package com.farestec.app.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = FaresPrimary,
    secondary = FaresSecondary,
    background = Color(0xFF12131A),
    surface = Color(0xFF1E2029)
)

private val LightColorScheme = lightColorScheme(
    primary = FaresPrimary,
    secondary = FaresSecondary,
    background = FaresBackground,
    surface = Color.White
)

@Composable
fun FaresTecTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme, content = content)
}
