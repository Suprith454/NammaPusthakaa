package com.nammapusthakaa.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = PrimaryBlue,
    onPrimary = White,
    primaryContainer = Color(0xFFDBEAFE),
    onPrimaryContainer = PrimaryBlueDark,
    secondary = PrimaryGreen,
    onSecondary = White,
    secondaryContainer = Color(0xFFD1FAE5),
    onSecondaryContainer = PrimaryGreenDark,
    tertiary = AccentOrange,
    onTertiary = White,
    tertiaryContainer = Color(0xFFFEF3C7),
    onTertiaryContainer = Color(0xFF92400E),
    background = Background,
    onBackground = TextPrimary,
    surface = Surface,
    onSurface = TextPrimary,
    surfaceVariant = Color(0xFFF1F5F9),
    onSurfaceVariant = TextSecondary,
    error = Error,
    onError = White,
    outline = Color(0xFFCBD5E1)
)

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF93C5FD),
    onPrimary = Color(0xFF1E3A5F),
    primaryContainer = PrimaryBlueDark,
    onPrimaryContainer = Color(0xFFDBEAFE),
    secondary = Color(0xFF6EE7B7),
    onSecondary = Color(0xFF064E3B),
    secondaryContainer = PrimaryGreenDark,
    onSecondaryContainer = Color(0xFFD1FAE5),
    tertiary = AccentYellow,
    onTertiary = Color(0xFF78350F),
    background = DarkBackground,
    onBackground = Color(0xFFF1F5F9),
    surface = DarkSurface,
    onSurface = Color(0xFFF1F5F9),
    surfaceVariant = DarkCard,
    onSurfaceVariant = Color(0xFF94A3B8),
    error = Color(0xFFFCA5A5),
    onError = Color(0xFF7F1D1D),
    outline = Color(0xFF475569)
)

@Composable
fun NammaPusthakaaTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
