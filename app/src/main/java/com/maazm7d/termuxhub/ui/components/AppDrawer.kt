package com.maazm7d.termuxhub.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.clickable
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.foundation.shape.RoundedCornerShape

@Composable
fun AppDrawer(onAction: (String) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface) // or surfaceVariant / background
            .padding(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            horizontalArrangement = Arrangement.Start
        ) {
            Icon(Icons.Default.List, contentDescription = "App")
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text("Termux Hub", style = MaterialTheme.typography.titleLarge)
                Text("com.maazm7d.termuxhub", style = MaterialTheme.typography.bodySmall)
            }
        }

        Divider()
        Spacer(modifier = Modifier.height(12.dp))

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
            .padding(vertical = 12.dp),
    ) {
        Icon(icon, contentDescription = label)
        Spacer(modifier = Modifier.width(12.dp))
        Text(label, style = MaterialTheme.typography.bodyLarge)
    }
}
