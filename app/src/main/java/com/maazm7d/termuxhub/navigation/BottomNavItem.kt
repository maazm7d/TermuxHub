package com.maazm7d.termuxhub.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.NewReleases
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(
    val route: String,
    val icon: ImageVector,
    val isHome: Boolean = false
) {

    object Saved : BottomNavItem(
        route = Destinations.SAVED,
        icon = Icons.Filled.Bookmark
    )

    object Hall : BottomNavItem(
        route = Destinations.HALL,
        icon = Icons.Filled.EmojiEvents
    )

    object Tools : BottomNavItem(
        route = Destinations.TOOLS,
        icon = Icons.Filled.Home,
        isHome = true // ⭐ Home marker
    )

    object WhatsNew : BottomNavItem(
        route = Destinations.WHATS_NEW,
        icon = Icons.Filled.NewReleases
    )

    object About : BottomNavItem(
        route = Destinations.ABOUT,
        icon = Icons.Filled.Info
    )
}

/**
 * Order matters — Home is intentionally centered
 */
val bottomNavItems = listOf(
    BottomNavItem.Saved,
    BottomNavItem.Hall,
    BottomNavItem.Tools,   // HOME (center)
    BottomNavItem.WhatsNew,
    BottomNavItem.About
)
