package com.maazm7d.termuxhub.ui.components

import android.net.Uri
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.maazm7d.termuxhub.domain.model.Tool
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.outlined.Security
import androidx.compose.ui.text.style.TextAlign
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext

@Composable
fun ToolCard(
    tool: Tool,
    stars: Int?,
    onOpenDetails: (String) -> Unit,
    onToggleFavorite: (String) -> Unit,
    onSave: (String) -> Unit,
    onShare: (Tool) -> Unit
) {

    val thumbnailUrl =
        "https://raw.githubusercontent.com/maazm7d/TermuxHub/main/metadata/thumbnail/${tool.id}.png"

    var isFav by remember { mutableStateOf(tool.isFavorite) }
    val favScale by animateFloatAsState(targetValue = if (isFav) 1.05f else 1f)

    val uriHandler = LocalUriHandler.current
    val context = LocalContext.current

    // dialog state for star action
    var showStarDialog by remember { mutableStateOf(false) }
    var pendingRepoUrl by remember { mutableStateOf<String?>(null) }

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

            // Thumbnail
            AsyncImage(
                model = thumbnailUrl,
                contentDescription = "${tool.name} thumbnail",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16f / 9f)
            )

            Column(modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)) {

                Text(
                    text = tool.name,
                    style = MaterialTheme.typography.titleMedium.copy(fontSize = 18.sp),
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
    text = tool.description.ifBlank { "No description available" },
    style = MaterialTheme.typography.bodyMedium.copy(fontSize = 13.sp),
    color = MaterialTheme.colorScheme.onSurfaceVariant,
    maxLines = 2,
                    modifier = Modifier.fillMaxWidth(),
    textAlign = TextAlign.Center
)

                Spacer(modifier = Modifier.height(8.dp))

                // DATE
                // DATE + AUTHOR on same line

Row(
    modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 12.dp, vertical = 6.dp),
    verticalAlignment = Alignment.CenterVertically
) {

    // LEFT — Published At
    Row(
        modifier = Modifier.weight(1f),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Filled.CalendarMonth,
            contentDescription = "Published At",
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(18.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = tool.publishedAt ?: "N/A",
            style = MaterialTheme.typography.bodySmall,
            fontSize = 13.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            maxLines = 1
        )
    }

    // CENTER — Author
    Row(
        modifier = Modifier.weight(1f),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Filled.Person,
            contentDescription = "Author",
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(18.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = tool.author ?: "Unknown",
            style = MaterialTheme.typography.bodySmall,
            fontSize = 13.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            maxLines = 1
        )
    }

    // RIGHT — Require Root
    Row(
        modifier = Modifier.weight(1f),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically
    ) {
        val isRoot = tool.requireRoot == true
        Icon(
            imageVector = if (isRoot) Icons.Filled.Security else Icons.Outlined.Security,
            contentDescription = if (isRoot) "Root Required" else "Non-Root",
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(18.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = if (isRoot) "Root" else "Non-Root",
            style = MaterialTheme.typography.bodySmall,
            fontSize = 13.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            maxLines = 1
        )
    }
}

                Spacer(modifier = Modifier.height(10.dp))

                // TAGS
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    tool.tags.take(2).forEach {
                        AssistChip(
                            onClick = {},
                            label = { Text(it, fontSize = 12.sp) },
                            shape = RoundedCornerShape(10.dp),
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // ACTION ROW
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    // STAR (shows count and allows "open repo to star")
                    Row(
                        modifier = Modifier
                            .clickable {
                                // open confirm dialog
                                pendingRepoUrl = tool.repoUrl
                                showStarDialog = true
                            },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = if (stars != null) Icons.Filled.Star else Icons.Outlined.StarBorder,
                            contentDescription = "Stars",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(stars?.toString() ?: "—", fontSize = 14.sp)
                    }

                    // SAVE (favorite)
                    Row(
    modifier = Modifier
        .clickable {
            isFav = !isFav
            onToggleFavorite(tool.id)

            Toast.makeText(
                context,
                if (isFav) "Saved" else "Removed from saved",
                Toast.LENGTH_SHORT
            ).show()
        }
        .scale(favScale),
    verticalAlignment = Alignment.CenterVertically
) {
                        Icon(
                            imageVector = if (isFav) Icons.Filled.Save else Icons.Filled.Save,
                            contentDescription = "Save",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Save", fontSize = 14.sp, color = MaterialTheme.colorScheme.primary)
                    }

                    // SHARE
                    Row(
                        modifier = Modifier.clickable { onShare(tool) },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Filled.Share, contentDescription = "Share", tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Share", fontSize = 14.sp, color = MaterialTheme.colorScheme.primary)
                    }
                }
            }
        }
    }

    // Star confirmation dialog
    if (showStarDialog) {
        AlertDialog(
            onDismissRequest = { showStarDialog = false },
            confirmButton = {
                TextButton(onClick = {
                    showStarDialog = false
                    pendingRepoUrl?.let { url ->
                        // open repo page so user can star it on GitHub
                        uriHandler.openUri(url)
                    }
                }) {
                    Text("Continue")
                }
            },
            dismissButton = {
                TextButton(onClick = { showStarDialog = false }) { Text("Cancel") }
            },
            title = { Text("Star on GitHub") },
            text = { Text("Do you want to open the repository on GitHub to star it?") }
        )
    }
}
