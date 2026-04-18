package com.maazm7d.termuxhub

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.*
import androidx.core.view.WindowCompat
import com.maazm7d.termuxhub.navigation.TermuxHubAppNav
import com.maazm7d.termuxhub.ui.theme.TermuxHubTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val deepLinkToolIdState = mutableStateOf<String?>(null)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (android.os.Build.VERSION.SDK_INT >= 34) {
            overrideActivityTransition(OVERRIDE_TRANSITION_OPEN, 0, 0)
            overrideActivityTransition(OVERRIDE_TRANSITION_CLOSE, 0, 0)
        } else {
            overridePendingTransition(0, 0)
        }
        enableEdgeToEdge()

        deepLinkToolIdState.value = extractToolId(intent)

        setContent {
            AppContent()
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        if (android.os.Build.VERSION.SDK_INT >= 34) {
            overrideActivityTransition(OVERRIDE_TRANSITION_OPEN, 0, 0)
            overrideActivityTransition(OVERRIDE_TRANSITION_CLOSE, 0, 0)
        } else {
            overridePendingTransition(0, 0)
        }

        deepLinkToolIdState.value = extractToolId(intent)
    }

    @Composable
    private fun AppContent() {
        val darkTheme = isSystemInDarkTheme()
        val deepLinkToolId by deepLinkToolIdState

        SideEffect {
            WindowCompat.getInsetsController(window, window.decorView).apply {
                isAppearanceLightStatusBars = !darkTheme
                isAppearanceLightNavigationBars = !darkTheme
            }
        }

        TermuxHubTheme(darkTheme = darkTheme) {
            TermuxHubAppNav(
                deepLinkToolId = deepLinkToolId
            )
        }
    }

    private fun extractToolId(intent: Intent?): String? {
        val data = intent?.data ?: return null

        if (
            data.scheme == "https" &&
            data.host == "maazm7d.github.io" &&
            data.path?.startsWith("/termuxhub/tool/") == true
        ) {
            return data.lastPathSegment
        }

        return null
    }
}
