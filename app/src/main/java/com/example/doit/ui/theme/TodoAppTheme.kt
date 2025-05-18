package com.example.doit.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = Color(0xFFD4AF37),      // Deep Gold Premium
    secondary = Color(0xFF800020),    // Rich Burgundy Premium
    background = Color(0xFFefefee),
    surface = Color.White,
    onPrimary = Color.Black,
    onSecondary = Color.White,
    onBackground = Color.Black,
    onSurface = Color.Black
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFFD4AF37),
    secondary = Color(0xFF800020),
    background = Color.Black,
    surface = Color.Black,
    onPrimary = Color(0xFFebebeb),
    onSecondary = Color.White,
    onBackground = Color(0xFFCCCCCC),
    onSurface = Color(0xFFE0E0E0)
)

@Composable
fun TodoAppTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors = if (darkTheme) DarkColors else LightColors
    MaterialTheme(
        colorScheme = colors,
        typography = Typography(),
        shapes = Shapes(),
        content = content
    )
}
