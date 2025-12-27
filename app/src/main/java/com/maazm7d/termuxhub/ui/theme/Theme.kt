package com.maazm7d.termuxhub.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.fillMaxSize

@Composable
fun TermuxHubTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = lightColorScheme(
            primary = md_theme_light_primary,
            onPrimary = md_theme_light_onPrimary,
            background = md_theme_light_background,
            surface = md_theme_light_surface
        ),
        typography = Typography,
        shapes = Shapes
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            content()
        }
    }
}
