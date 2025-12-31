package com.maazm7d.termuxhub.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.maazm7d.termuxhub.domain.model.HallOfFameMember
import dev.jeziellago.compose.markdowntext.MarkdownText

@Composable
fun HallOfFameCard(
    member: HallOfFameMember,
    modifier: Modifier = Modifier
) {
    val uriHandler = LocalUriHandler.current
    val avatarUrl = "https://avatars.githubusercontent.com/${member.github}"
    val avatarShape = RoundedCornerShape(14.dp) // 👈 squircle

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(2.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            /* ───── Header ───── */
            Row(verticalAlignment = Alignment.CenterVertically) {

                AsyncImage(
                    model = avatarUrl,
                    contentDescription = "${member.github} avatar",
                    modifier = Modifier
                        .size(60.dp)
                        .clip(avatarShape)
                        .border(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.outlineVariant,
                            shape = avatarShape
                        )
                )

                Spacer(modifier = Modifier.width(14.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = member.github,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold
                    )

                    Spacer(modifier = Modifier.height(2.dp))

                    Text(
                        text = member.speciality,
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            MarkdownText(
                markdown = member.contribution,
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(12.dp))

            TextButton(
                onClick = { uriHandler.openUri(member.profileUrl) },
                modifier = Modifier.align(Alignment.End),
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.OpenInNew,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text("GitHub")
            }
        }
    }
}
