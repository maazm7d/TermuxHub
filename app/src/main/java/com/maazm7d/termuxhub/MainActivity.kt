package com.maazm7d.termuxhub

import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.maazm7d.termuxhub.navigation.TermuxHubAppNav
import com.maazm7d.termuxhub.ui.theme.TermuxHubTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Enable edge-to-edge first
        WindowCompat.setDecorFitsSystemWindows(window, false)
        
        // Set immersive/sticky immersive mode for full screen
        hideSystemBars()
        
        setContent {
            TermuxHubTheme {
                // System UI controller for Compose
                val systemUiController = rememberSystemUiController()
                
                SideEffect {
                    // Set transparent status and navigation bars
                    systemUiController.setStatusBarColor(
                        color = Color.Transparent,
                        darkIcons = true
                    )
                    systemUiController.setNavigationBarColor(
                        color = Color.Transparent,
                        darkIcons = true
                    )
                    
                    // Optional: hide system bars when keyboard appears
                    systemUiController.isSystemBarsVisible = false
                }
                
                // Wrap your app with padding for system bars
                TermuxHubAppNav(
                    modifier = Modifier.systemBarsPadding()
                )
            }
        }
    }
    
    private fun hideSystemBars() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            // For Android 11+ (API 30+)
            window.setDecorFitsSystemWindows(false)
            WindowInsetsControllerCompat(window, window.decorView).let { controller ->
                controller.hide(WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE)
                controller.hide(
                    WindowInsetsControllerCompat.TYPE_STATUS_BARS or
                    WindowInsetsControllerCompat.TYPE_NAVIGATION_BARS
                )
                controller.systemBarsBehavior = 
                    WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        } else {
            // For older versions
            @Suppress("DEPRECATION")
            window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_FULLSCREEN
            )
            
            // Make navigation bar transparent
            window.navigationBarColor = Color.Transparent.toArgb()
            window.statusBarColor = Color.Transparent.toArgb()
        }
        
        // Optional: keep screen on
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }
    
    // Handle system bars when activity regains focus
    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            hideSystemBars()
        }
    }
    
    // Optional: Handle back button to exit immersive mode if needed
    override fun onBackPressed() {
        // You can show system bars temporarily when back is pressed
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            WindowInsetsControllerCompat(window, window.decorView).show(
                WindowInsetsControllerCompat.TYPE_STATUS_BARS or
                WindowInsetsControllerCompat.TYPE_NAVIGATION_BARS
            )
        }
        
        // Delay the actual back press to give time for bars to show
        window.decorView.postDelayed({
            super.onBackPressed()
        }, 100)
    }
}
