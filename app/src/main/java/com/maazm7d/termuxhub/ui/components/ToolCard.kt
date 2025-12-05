package com.maazm7d.termuxhub.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import coil.compose.AsyncImage
import com.maazm7d.termuxhub.domain.model.Tool
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.layout.ContentScale

@Composable
fun ToolCard(tool: Tool, onOpenDetails: (String) -> Unit, onLike: (String) -> Unit, onSave: (String) -> Unit, onShare: (Tool) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 8.dp)
            .clickable { onOpenDetails(tool.id) },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column {
            if (!tool.thumbnail.isNullOrBlank()) {
                AsyncImage(
                    model = tool.thumbnail,
                    contentDescription = "${tool.name} thumbnail",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp)
                        .background(MaterialTheme.colorScheme.surface),
                    contentScale = ContentScale.Crop
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Column(modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)) {
                Text(tool.name, style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.height(6.dp))
                Row {
                    // tags preview (use first 2 or category)
                    Text(tool.category.ifBlank { "Utility" }, style = MaterialTheme.typography.bodyMedium)
                }
                Spacer(modifier = Modifier.height(8.dp))
                MetricsRow(likes = tool.likes, views = tool.views, published = tool.publishedAt ?: "")
                Spacer(modifier = Modifier.height(8.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    IconButton(onClick = { onLike(tool.id) }) {
                        Icon(Icons.Default.Favorite, contentDescription = "Like")
                    }
                    IconButton(onClick = { onSave(tool.id) }) {
                        Icon(Icons.Default.Save, contentDescription = "Save")
                    }
                    IconButton(onClick = { onShare(tool) }) {
                        Icon(Icons.Default.Share, contentDescription = "Share")
                    }
                }
            }
        }
    }
}