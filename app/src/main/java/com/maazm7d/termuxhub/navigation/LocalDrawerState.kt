package com.maazm7d.termuxhub.navigation

import androidx.compose.material3.DrawerState
import androidx.compose.runtime.staticCompositionLocalOf

val LocalDrawerState = staticCompositionLocalOf<DrawerState> {
    error("DrawerState not provided")
}
