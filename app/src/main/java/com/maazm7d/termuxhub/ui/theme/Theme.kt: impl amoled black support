package com.maazm7d.termuxhub.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun TermuxHubTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) {
        darkColorScheme(
            primary = md_theme_amoled_primary,
            onPrimary = md_theme_amoled_onPrimary,
            background = Color.Black,
            surface = Color.Black,
            onSurface = md_theme_amoled_onSurface,
            outline = md_theme_amoled_outline
        )
    } else {
        lightColorScheme(
            primary = md_theme_light_primary,
            onPrimary = md_theme_light_onPrimary,
            background = md_theme_light_background,
            surface = md_theme_light_surface,
            onSurface = md_theme_light_onSurface,
            outline = md_theme_light_outline
        )
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        shapes = Shapes
    ) {
        Surface(
            color = MaterialTheme.colorScheme.background
        ) {
            content()
        }
    }
}
