package com.maazm7d.termuxhub.ui.components

import android.widget.Toast
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.maazm7d.termuxhub.domain.model.Tool

@Composable
fun ToolCard(
    tool: Tool,
    stars: Int?,
    onOpenDetails: (String) -> Unit,
    onToggleFavorite: (String) -> Unit,
    onSave: (String) -> Unit
) {
    val thumbnailUrl =
        "https://raw.githubusercontent.com/maazm7d/TermuxHub/main/metadata/thumbnail/${tool.id}.webp"

    var isFav by remember { mutableStateOf(tool.isFavorite) }
    val favScale by animateFloatAsState(targetValue = if (isFav) 1.05f else 1f)

    val uriHandler = LocalUriHandler.current
    val context = LocalContext.current

    var showStarDialog by remember { mutableStateOf(false) }
    var pendingRepoUrl by remember { mutableStateOf<String?>(null) }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onOpenDetails(tool.id) }
            .padding(horizontal = 16.dp, vertical = 8.dp),
        color = MaterialTheme.colorScheme.background
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Thumbnail
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(MaterialTheme.shapes.medium)
            ) {
                var imageLoading by remember { mutableStateOf(true) }
                if (imageLoading) {
                    Box(modifier = Modifier.fillMaxSize().shimmer())
                }
                AsyncImage(
                    model = thumbnailUrl,
                    contentDescription = "${tool.name} thumbnail",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize(),
                    onSuccess = { imageLoading = false },
                    onError = { imageLoading = false }
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Tool Info
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = tool.name,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = tool.category ?: "Tool",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (stars != null) {
                        Text(
                            text = stars.toString(),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Icon(
                            imageVector = Icons.Filled.Star,
                            contentDescription = null,
                            modifier = Modifier.size(12.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    if (tool.requireRoot == true) {
                        if (stars != null) {
                            Spacer(modifier = Modifier.width(8.dp))
                        }
                        Text(
                            text = "Root",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.error,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            // Actions
            IconButton(
                onClick = {
                    isFav = !isFav
                    onToggleFavorite(tool.id)
                    Toast.makeText(
                        context,
                        if (isFav) "Saved" else "Removed from saved",
                        Toast.LENGTH_SHORT
                    ).show()
                },
                modifier = Modifier.scale(favScale)
            ) {
                Icon(
                    imageVector = if (isFav) Icons.Filled.Bookmark else Icons.Outlined.BookmarkBorder,
                    contentDescription = "Save",
                    tint = if (isFav) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }

    if (showStarDialog) {
        AlertDialog(
            onDismissRequest = { showStarDialog = false },
            confirmButton = {
                TextButton(onClick = {
                    showStarDialog = false
                    pendingRepoUrl?.let { url -> uriHandler.openUri(url) }
                }) { Text("Continue") }
            },
            dismissButton = {
                TextButton(onClick = { showStarDialog = false }) { Text("Cancel") }
            },
            title = { Text("Star on GitHub") },
            text = { Text("Do you want to open the repository on GitHub to star it?") }
        )
    }
}
