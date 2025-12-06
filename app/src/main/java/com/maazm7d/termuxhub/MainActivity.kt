package com.maazm7d.termuxhub

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.core.view.WindowCompat
import androidx.compose.ui.graphics.Color
import com.maazm7d.termuxhub.navigation.TermuxHubAppNav
import com.maazm7d.termuxhub.ui.theme.TermuxHubTheme
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            TermuxHubTheme {

                // Status bar Controller
                val systemUiController = rememberSystemUiController()

                SideEffect {
                    // Set status bar white
                    systemUiController.setStatusBarColor(
                        color = Color.White,
                        darkIcons = true      // enable dark icons
                    )

                    // Remove the default scrim tint
                    window.statusBarColor = Color.White.toArgb()
                }

                TermuxHubAppNav()
            }
        }
    }
}
