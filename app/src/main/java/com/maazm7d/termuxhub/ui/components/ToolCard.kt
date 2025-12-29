package com.maazm7d.termuxhub.ui.components

import android.widget.Toast
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.Security
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.maazm7d.termuxhub.domain.model.Tool

@Composable
fun ToolCard(
    tool: Tool,
    stars: Int?,
    onOpenDetails: (String) -> Unit,
    onToggleFavorite: (String) -> Unit,
    onShare: (Tool) -> Unit
) {
    val thumbnailUrl =
        "https://raw.githubusercontent.com/maazm7d/TermuxHub/main/metadata/thumbnail/${tool.id}.png"

    var isFav by remember(tool.id) { mutableStateOf(tool.isFavorite) }
    val favScale by animateFloatAsState(if (isFav) 1.08f else 1f)

    val uriHandler = LocalUriHandler.current
    val context = LocalContext.current

    var showStarDialog by remember { mutableStateOf(false) }
    var pendingRepoUrl by remember { mutableStateOf<String?>(null) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 8.dp)
            .clickable { onOpenDetails(tool.id) },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Column {

            AsyncImage(
                model = thumbnailUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16f / 9f)
            )

            Column(modifier = Modifier.padding(14.dp)) {

                Text(
                    text = tool.name,
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(4.dp))

                Text(
                    text = tool.description.ifBlank { "No description available" },
                    style = MaterialTheme.typography.bodySmall,
                    textAlign = TextAlign.Center,
                    maxLines = 2,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Icon(
                        Icons.Filled.CalendarMonth,
                        null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(Modifier.width(6.dp))
                    Text(tool.publishedAt ?: "N/A", fontSize = 13.sp)

                    Spacer(Modifier.weight(1f))

                    Icon(
                        if (tool.requireRoot == true)
                            Icons.Filled.Security
                        else
                            Icons.Outlined.Security,
                        null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(Modifier.width(6.dp))
                    Text(
                        if (tool.requireRoot == true) "Root" else "Non-Root",
                        fontSize = 13.sp
                    )
                }

                Spacer(Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    // ⭐ STARS
                    Row(
                        modifier = Modifier.clickable {
                            pendingRepoUrl = tool.repoUrl
                            showStarDialog = true
                        },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            if (stars != null) Icons.Filled.Star else Icons.Outlined.StarBorder,
                            null,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(Modifier.width(6.dp))
                        Text(stars?.toString() ?: "—")
                    }

                    // 🔖 SAVE / UNSAVE (SINGLE TOGGLE)
                    IconButton(
                        modifier = Modifier.scale(favScale),
                        onClick = {
                            isFav = !isFav
                            onToggleFavorite(tool.id)

                            Toast.makeText(
                                context,
                                if (isFav) "Saved" else "Removed from saved",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    ) {
                        Icon(
                            imageVector =
                                if (isFav) Icons.Filled.Bookmark
                                else Icons.Outlined.BookmarkBorder,
                            contentDescription = "Save"
                        )
                    }

                    // 🔗 SHARE
                    IconButton(onClick = { onShare(tool) }) {
                        Icon(Icons.Filled.Share, null)
                    }
                }
            }
        }
    }

    if (showStarDialog) {
        AlertDialog(
            onDismissRequest = { showStarDialog = false },
            confirmButton = {
                TextButton {
                    showStarDialog = false
                    pendingRepoUrl?.let(uriHandler::openUri)
                } { Text("Continue") }
            },
            dismissButton = {
                TextButton { showStarDialog = false } { Text("Cancel") }
            },
            title = { Text("Star on GitHub") },
            text = { Text("Open repository to star it on GitHub?") }
        )
    }
}
