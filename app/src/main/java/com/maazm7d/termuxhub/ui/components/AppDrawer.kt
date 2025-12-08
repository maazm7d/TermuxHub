package com.maazm7d.termuxhub.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.NewReleases
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.safeDrawing
import com.maazm7d.termuxhub.R

@Composable
fun AppDrawer(onAction: (String) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .windowInsetsPadding(WindowInsets.safeDrawing)
            .background(MaterialTheme.colorScheme.surface)
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {

        // ---- HEADER ----
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 18.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // QUIRCLE SHAPE ICON
            Image(
                painter = painterResource(id = R.mipmap.ic_launcher),
                contentDescription = "App Icon",
                modifier = Modifier
                    .size(90.dp)
                    .clip(
                        RoundedCornerShape(
                            topStart = 50.dp,
                            topEnd = 50.dp,
                            bottomStart = 0.dp,
                            bottomEnd = 0.dp
                        )
                    )
            )

            Spacer(modifier = Modifier.height(14.dp))

            Text("Termux Hub", style = MaterialTheme.typography.headlineSmall)
            Text(
                "com.maazm7d.termuxhub",
                style = MaterialTheme.typography.bodySmall
            )
        }

        Spacer(modifier = Modifier.height(10.dp))
        Divider()
        Spacer(modifier = Modifier.height(16.dp))

        // ---- DRAWER ITEMS ----
        DrawerItem(
            icon = Icons.Default.Bookmark,
            label = "Saved List",
            desc = "Your saved tools and commands"
        ) { onAction("saved") }

        DrawerItem(
            icon = Icons.Default.EmojiEvents,
            label = "Hall of Fame",
            desc = "Most popular tools curated by users"
        ) { onAction("hall") }

        DrawerItem(
            icon = Icons.Default.NewReleases,
            label = "What's New",
            desc = "Latest tools, features & updates"
        ) { onAction("whats_new") }

        DrawerItem(
            icon = Icons.Default.Settings,
            label = "Settings",
            desc = "Themes, preferences & appearance"
        ) { onAction("settings") }

        Spacer(modifier = Modifier.weight(1f))

        Text("v1.0.0 (1)", style = MaterialTheme.typography.bodySmall)
    }
}

@Composable
fun DrawerItem(icon: ImageVector, label: String, desc: String, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 12.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, contentDescription = label)
            Spacer(modifier = Modifier.width(14.dp))

            Column {
                Text(label, style = MaterialTheme.typography.titleMedium)
                Text(desc, style = MaterialTheme.typography.bodySmall)
            }
        }

        Spacer(modifier = Modifier.height(14.dp))
        Divider()
    }
}
