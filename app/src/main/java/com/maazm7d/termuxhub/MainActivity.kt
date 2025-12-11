package com.maazm7d.termuxhub

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.maazm7d.termuxhub.navigation.TermuxHubAppNav
import com.maazm7d.termuxhub.ui.theme.TermuxHubTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Enable edge-to-edge (content goes under system bars)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        
        // Make system bars transparent
        makeSystemBarsTransparent()
        
        setContent {
            TermuxHubTheme {
                // Fill entire screen, no system bars padding
                TermuxHubAppNav(
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
    
    private fun makeSystemBarsTransparent() {
        // Make status bar and navigation bar transparent
        window.statusBarColor = android.graphics.Color.TRANSPARENT
        window.navigationBarColor = android.graphics.Color.TRANSPARENT
        
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            // Android 11+ - Control system bars behavior
            window.insetsController?.let { controller ->
                controller.hide(android.view.WindowInsets.Type.statusBars())
                controller.hide(android.view.WindowInsets.Type.navigationBars())
                controller.systemBarsBehavior = 
                    android.view.WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        } else {
            // For older versions
            @Suppress("DEPRECATION")
            window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            )
        }
        
        // Set the decor view to not fit system windows (so content goes under)
        WindowCompat.getInsetsController(window, window.decorView).apply {
            // Hide both bars
            hide(androidx.core.view.WindowInsetsCompat.Type.systemBars())
            // Set behavior to show bars transiently by swipe
            systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }
    
    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            makeSystemBarsTransparent()
        }
    }
}
