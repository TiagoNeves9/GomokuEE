package com.example.gomokuee.ui.theme

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

private val darkColorScheme = darkColorScheme(
    primary = Color(0xFF0081A7), // Default button/notification bar color
    secondary = Color(0xFF036994),
    tertiary = Color(0xFF5880A4),
    background = Color(0xFF303646),
    onBackground = Color(0xFFFFFFFF),
    onPrimaryContainer = Color(0xFFFFFFFF),
    surfaceVariant = Color(0xFF2B2929),
    error = Color(0xFFE43850)
)

private val lightColorScheme = lightColorScheme(
    primary = Color(0xFF0081A7), // Default button/notification bar color
    secondary = Color(0xFF00A8C5),
    tertiary = Color(0xFFA5D3F6),
    background = Color(0xFF7CE9FF), // Softer blue background color
    onBackground = Color(0xFF2B2929), // Slightly darker text color for better contrast
    surfaceVariant = Color(0xFFFFFFFF),
    onPrimaryContainer = Color(0xFF00223D),
    error = Color(0xFFE43850)
)

@Composable
fun GomokuEETheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> darkColorScheme
        else -> lightColorScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}