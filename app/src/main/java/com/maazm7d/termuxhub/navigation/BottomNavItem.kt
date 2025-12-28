package com.maazm7d.termuxhub.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.NewReleases
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(
    val route: String,
    val icon: ImageVector
) {
    object Tools : BottomNavItem(Destinations.TOOLS, Icons.Filled.List)
    object Saved : BottomNavItem(Destinations.SAVED, Icons.Filled.Bookmark)
    object Hall : BottomNavItem(Destinations.HALL, Icons.Filled.EmojiEvents)
    object WhatsNew : BottomNavItem(Destinations.WHATS_NEW, Icons.Filled.NewReleases)
    object About : BottomNavItem(Destinations.ABOUT, Icons.Filled.Info)
}

val bottomNavItems = listOf(
    BottomNavItem.Tools,
    BottomNavItem.Saved,
    BottomNavItem.Hall,
    BottomNavItem.WhatsNew,
    BottomNavItem.About
)
