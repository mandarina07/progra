package com.example.proyecto

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color

// Paleta de colores claro
private val LightColors = lightColorScheme(
    primary = Color(0xFF1C1F52),
    secondary = Color(0xFF6077A5),
    tertiary = Color(0xFFE5BCBC),
    background = Color(0xFFF8FAFC),
    surface = Color(0xFFFFFFFF),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black
)

// Paleta de colores oscuro
private val DarkColors = darkColorScheme(
    primary = Color(0xFF8EDDEE),
    secondary = Color(0xFF5FA0D0),
    tertiary = Color(0xFFB899C5),
    background = Color(0xFF03052B),
    surface = Color(0xFF1C1F52),
    onPrimary = Color.Black,
    onSecondary = Color.Black,
    onTertiary = Color.White,
    onBackground = Color.White,
    onSurface = Color.White
)

/**
 * App theme
 *
 * @param darkTheme
 * @param content
 * @receiver
 */
@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkColors else LightColors

    MaterialTheme(
        colorScheme = colors,
        typography = Typography(),
        content = content
    )
}
