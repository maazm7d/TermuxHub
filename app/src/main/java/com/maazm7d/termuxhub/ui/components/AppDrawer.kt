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
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.maazm7d.termuxhub.R
import androidx.compose.foundation.layout.statusBarsPadding

@Composable
fun AppDrawer(onAction: (String) -> Unit) {
    Column(
        modifier = Modifier
            // Take the whole window so the drawer sheet reaches behind the status bar
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            // Add automatic padding so content does not overlap the status bar
            .statusBarsPadding()          // or .systemBarsPadding() if needed[web:3][web:22]
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {

        // ---------------- HEADER ---------------------------------
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_launcher),
                contentDescription = "App Icon",
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(24.dp)) // SQUIRCLE SHAPE
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text("Termux Hub", style = MaterialTheme.typography.headlineSmall)
            Text(
                "Your Ultimate Command Toolkit",
                style = MaterialTheme.typography.bodySmall
            )
        }

        Spacer(modifier = Modifier.height(18.dp))
        Divider()
        Spacer(modifier = Modifier.height(12.dp))

        // ---------------- DRAWER ITEMS ----------------------------
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
    icon = Icons.Default.Info,
    label = "About",
    desc = "App info, version & open-source details"
) { onAction("about") }

        Spacer(modifier = Modifier.weight(1f))
        Text("v1.0.0 (1)", style = MaterialTheme.typography.bodySmall)
    }
}

@Composable
fun DrawerItem(
    icon: ImageVector,
    label: String,
    desc: String,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 10.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                modifier = Modifier.size(24.dp)
            )

            Spacer(modifier = Modifier.width(14.dp))

            Column {
                Text(
                    text = label,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = desc,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))
        Divider()
    }
}
