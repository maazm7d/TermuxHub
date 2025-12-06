package com.maazm7d.termuxhub.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.safeDrawing


@Composable
fun AppDrawer(onAction: (String) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .windowInsetsPadding(WindowInsets.safeDrawing)
            .background(MaterialTheme.colorScheme.surface)
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        // HEADER
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.List, contentDescription = "App")
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text("Termux Hub", style = MaterialTheme.typography.titleLarge)
                Text("com.maazm7d.termuxhub", style = MaterialTheme.typography.bodySmall)
            }
        }

        Divider()
        Spacer(modifier = Modifier.height(12.dp))

        // DRAWER ITEMS
        DrawerItem(icon = Icons.Default.Star, label = "Saved List") { onAction("saved") }
        DrawerItem(icon = Icons.Default.Settings, label = "Settings") { onAction("settings") }
        DrawerItem(icon = Icons.Default.Star, label = "Hall of Fame") { onAction("hall") }
        DrawerItem(icon = Icons.Default.Star, label = "What's New") { onAction("whats_new") }

        Spacer(modifier = Modifier.weight(1f))

        Text("v1.0.0 (1)", style = MaterialTheme.typography.bodySmall)
    }
}

@Composable
fun DrawerItem(icon: ImageVector, label: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 14.dp)
            .background(MaterialTheme.colorScheme.surface),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = label)
        Spacer(modifier = Modifier.width(14.dp))
        Text(label, style = MaterialTheme.typography.bodyLarge)
    }
}
