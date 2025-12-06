package com.maazm7d.termuxhub.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.RemoveRedEye
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.maazm7d.termuxhub.domain.model.Tool

@Composable
fun ToolCard(
    tool: Tool,
    onOpenDetails: (String) -> Unit,
    onLike: (String) -> Unit,
    onSave: (String) -> Unit,
    onShare: (Tool) -> Unit
) {

    // Build thumbnail URL based on tool.id
    val thumbnailUrl =
        "https://raw.githubusercontent.com/maazm7d/TermuxHub/main/metadata/thumbnail/${tool.id}.png"

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 8.dp)
            .clickable { onOpenDetails(tool.id) },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {

        Column {

            // Thumbnail 16:9
            AsyncImage(
                model = thumbnailUrl,
                contentDescription = "${tool.name} thumbnail",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16f / 9f)
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            )

            Column(
                modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)
            ) {

                // Title
                Text(
                    text = tool.name,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontSize = 18.sp
                    ),
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Category Subtitle
                Text(
                    text = tool.category.ifBlank { "Utility" },
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontSize = 13.sp
                    ),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Tags
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    tool.tags.take(2).forEach {
                        AssistChip(
                            onClick = {},
                            label = { Text(it, fontSize = 12.sp) },
                            shape = RoundedCornerShape(10.dp),
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Stats row
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(18.dp)
                    )
                    Text("${tool.likes}", fontSize = 12.sp)

                    Icon(
                        imageVector = Icons.Default.RemoveRedEye,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(18.dp)
                    )
                    Text("${tool.views}", fontSize = 12.sp)

                    if (!tool.publishedAt.isNullOrBlank()) {
                        Text(
                            tool.publishedAt,
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Bottom actions
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
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
