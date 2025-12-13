package com.maazm7d.termuxhub.ui.screens.settings

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onBack: () -> Unit
) {
    var darkMode by remember { mutableStateOf(false) }

    Column {
        TopAppBar(title = { Text("Settings") }, navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
            }
        })
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Enable dark mode", style = MaterialTheme.typography.bodyLarge)
            Switch(checked = darkMode, onCheckedChange = { darkMode = it })
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                "About: Termux Hub - an open source Termux tool index",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
