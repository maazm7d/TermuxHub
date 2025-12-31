package com.maazm7d.termuxhub.ui.screens.saved

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.maazm7d.termuxhub.domain.model.Tool

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SavedToolRow(
    tool: Tool,
    onOpenDetails: (String) -> Unit,
    onRemove: () -> Unit
) {

    val thumbnailUrl =
        "https://raw.githubusercontent.com/maazm7d/TermuxHub/main/metadata/thumbnail/${tool.id}.png"

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onOpenDetails(tool.id) },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f)
        )
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            // 🔥 APP ICON (thumbnail, not bookmark)
            AsyncImage(
                model = thumbnailUrl,
                contentDescription = "${tool.name} icon",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.08f)
                    )
            )

            Spacer(modifier = Modifier.width(14.dp))

            // TEXT CONTENT
            Column(
                modifier = Modifier.weight(1f)
            ) {

                // ⭐ Tool name — BIGGER & IMPORTANT
                Text(
                    text = tool.name,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(2.dp))

                // Description — max 2 lines
                Text(
                    text = tool.description.ifBlank { "No description available" },
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.width(6.dp))

            // REMOVE FROM SAVED
            IconButton(onClick = onRemove) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = "Remove",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
