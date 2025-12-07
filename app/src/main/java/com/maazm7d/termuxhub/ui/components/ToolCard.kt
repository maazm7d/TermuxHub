package com.maazm7d.termuxhub.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.filled.RemoveRedEye
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
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

    val thumbnailUrl =
        "https://raw.githubusercontent.com/maazm7d/TermuxHub/main/metadata/thumbnail/${tool.id}.png"

    var isLiked by remember { mutableStateOf(false) }
    val likeScale by animateFloatAsState(targetValue = if (isLiked) 1.2f else 1f)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 8.dp)
            .clickable { onOpenDetails(tool.id) },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {

        Column {

            AsyncImage(
                model = thumbnailUrl,
                contentDescription = "${tool.name} thumbnail",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16f / 9f)
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            )

            Column(modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)) {

                Text(
                    text = tool.name,
                    style = MaterialTheme.typography.titleMedium.copy(fontSize = 18.sp),
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = tool.category.ifBlank { "Utility" },
                    style = MaterialTheme.typography.bodyMedium.copy(fontSize = 13.sp),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Icon(
                        Icons.Default.RemoveRedEye,
                        contentDescription = "Views",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("${tool.views}", fontSize = 13.sp)

                    Spacer(modifier = Modifier.width(16.dp))

                    if (!tool.publishedAt.isNullOrBlank()) {
                        Icon(
                            Icons.Default.CalendarMonth,
                            contentDescription = "Published",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(tool.publishedAt, fontSize = 13.sp)
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

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    // LIKE
                    Row(
                        modifier = Modifier
                            .clickable {
                                isLiked = !isLiked
                                onLike(tool.id)
                            }
                            .scale(likeScale),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = if (isLiked) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                            contentDescription = "Like",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("${tool.likes + if (isLiked) 1 else 0}", fontSize = 14.sp)
                    }

                    // SAVE
                    Row(
                        modifier = Modifier.clickable { onSave(tool.id) },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.Save,
                            contentDescription = "Save",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Save", fontSize = 14.sp, color = MaterialTheme.colorScheme.primary)
                    }

                    // SHARE
                    Row(
                        modifier = Modifier.clickable { onShare(tool) },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.Share,
                            contentDescription = "Share",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Share", fontSize = 14.sp, color = MaterialTheme.colorScheme.primary)
                    }
                }
            }
        }
    }
}
