package com.maazm7d.termuxhub.ui.screens.blogs

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun BlogsScreen() {
    Column {
        TopAppBar(title = { Text("Blogs") })
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            "Blog content coming soon",
            modifier = Modifier.padding(16.dp),
            style = MaterialTheme.typography.bodyMedium
        )
    }
}