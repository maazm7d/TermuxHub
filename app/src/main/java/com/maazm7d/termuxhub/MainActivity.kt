package com.maazm7d.termuxhub

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.maazm7d.termuxhub.navigation.TermuxHubAppNav
import com.maazm7d.termuxhub.ui.theme.TermuxHubTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TermuxHubTheme {
                TermuxHubAppNav()
            }
        }
    }
}