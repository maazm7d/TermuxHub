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

@Composable
fun HallOfFameCard(member: HallOfFameMember) {

    val uriHandler = LocalUriHandler.current
    val avatarUrl = "https://avatars.githubusercontent.com/${member.github}"
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {

        Column(Modifier.padding(16.dp)) {

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
                    Text(member.github, style = MaterialTheme.typography.titleMedium)
                    Text(
                        member.speciality,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(Modifier.height(12.dp))

            MarkdownText(
                markdown = member.contribution,
                style = MaterialTheme.typography.bodyMedium
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
