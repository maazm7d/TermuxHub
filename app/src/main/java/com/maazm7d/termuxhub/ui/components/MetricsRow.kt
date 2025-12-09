package com.maazm7d.termuxhub.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width

@Composable
fun MetricsRow(likes: Int, published: String) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Icon(Icons.Default.Favorite, contentDescription = "likes")
        Spacer(modifier = Modifier.width(4.dp))
        Text(likes.toString(), style = MaterialTheme.typography.bodyMedium)
        Spacer(modifier = Modifier.weight(1f))
        Text(published, style = MaterialTheme.typography.bodySmall ?: MaterialTheme.typography.bodyMedium)
    }
}
