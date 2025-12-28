package com.maazm7d.termuxhub

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.core.view.WindowCompat
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.maazm7d.termuxhub.navigation.TermuxHubAppNav
import com.maazm7d.termuxhub.ui.theme.TermuxHubTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Edge-to-edge layout (required for modern bottom navigation)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            TermuxHubTheme {

                // System UI (status bar only – no animation/fade)
                val systemUiController = rememberSystemUiController()

                SideEffect {
                    systemUiController.setStatusBarColor(
                        color = Color.White,
                        darkIcons = true
                    )
                }

                // Root navigation host (Scaffold + BottomBar live inside)
                TermuxHubAppNav()
            }
        }
    }
}
