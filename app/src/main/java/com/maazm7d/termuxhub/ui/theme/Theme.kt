package com.maazm7d.termuxhub.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable

@Composable
fun TermuxHubTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = androidx.compose.material3.lightColorScheme(
            primary = md_theme_light_primary,
            onPrimary = md_theme_light_onPrimary,
            background = md_theme_light_background,
            surface = md_theme_light_surface,
            onSurface = md_theme_light_onSurface,
            outline = md_theme_light_outline
        ),
        typography = Typography,
        shapes = Shapes,
        content = {
            Surface {
                content()
            }
        }
    )
}