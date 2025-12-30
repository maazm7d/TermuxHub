package com.maazm7d.termuxhub.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import dev.jeziellago.compose.markdowntext.MarkdownText
import com.maazm7d.termuxhub.domain.model.HallOfFameMember
import androidx.compose.ui.graphics.Color

@Composable
fun HallOfFameCard(member: HallOfFameMember) {

    val uriHandler = LocalUriHandler.current
    val avatarUrl = "https://avatars.githubusercontent.com/${member.github}"

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 8.dp), // ✅ same spacing as ToolCard
        shape = RoundedCornerShape(12.dp),                // ✅ same radius
        elevation = CardDefaults.cardElevation(6.dp),     // ✅ same elevation
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface // ✅ EXACT MATCH
        )
    ) {

        Column(modifier = Modifier.padding(14.dp)) { // ✅ matches inner padding style

            Row {
                AsyncImage(
                    model = avatarUrl,
                    contentDescription = null,
                    modifier = Modifier
                        .size(52.dp)
                        .clip(CircleShape)
                )

                Spacer(Modifier.width(12.dp))

                Column {
                    Text(
                        text = member.github,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Text(
                        text = member.speciality,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(Modifier.height(12.dp))

            MarkdownText(
                markdown = member.contribution,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(Modifier.height(12.dp))

            OutlinedButton(
                onClick = { uriHandler.openUri(member.profileUrl) },
                modifier = Modifier.align(androidx.compose.ui.Alignment.End)
            ) {
                Text("View GitHub Profile")
            }
        }
    }
}
