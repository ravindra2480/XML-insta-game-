package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = ArcaneGold,
    onPrimary = Color.Black,
    primaryContainer = ArcaneGoldDark,
    onPrimaryContainer = ArcaneGoldBright,
    secondary = MysticPurple,
    onSecondary = Color.White,
    secondaryContainer = MysticPurpleDark,
    onSecondaryContainer = MysticPurpleLight,
    tertiary = AstralCyan,
    onTertiary = Color.Black,
    background = MysticDarkBg,
    onBackground = TextPrimary,
    surface = MysticSurface,
    onSurface = TextPrimary,
    surfaceVariant = MysticSurfaceElevated,
    onSurfaceVariant = TextSecondary,
    outline = MysticBorder
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography,
        content = content
    )
}

