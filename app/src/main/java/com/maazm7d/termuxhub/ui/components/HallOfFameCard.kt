package com.maazm7d.termuxhub.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.maazm7d.termuxhub.domain.model.HallOfFameMember
import com.mikepenz.markdown.m3.Markdown
import com.mikepenz.markdown.m3.markdownTypography
import com.mikepenz.markdown.coil3.Coil3ImageTransformerImpl

@Composable
fun HallOfFameCard(
    member: HallOfFameMember,
    modifier: Modifier = Modifier
) {
    val uriHandler = LocalUriHandler.current
    val avatarUrl = remember(member.github) { "https://avatars.githubusercontent.com/${member.github}" }
    val avatarShape = RoundedCornerShape(14.dp)

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(2.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
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

            Markdown(
                content = member.contribution,
                typography = markdownTypography(
                    text = TextStyle(
                        fontSize = 13.sp,
                        lineHeight = 18.sp
                    ),
                    h1 = TextStyle(fontSize = 18.sp),
                    h2 = TextStyle(fontSize = 16.sp),
                    h3 = TextStyle(fontSize = 15.sp),
                    h4 = TextStyle(fontSize = 14.sp),
                    h5 = TextStyle(fontSize = 13.sp),
                    h6 = TextStyle(fontSize = 13.sp),
                    code = TextStyle(
                        fontSize = 12.sp,
                        lineHeight = 16.sp
                    ),
                    bullet = TextStyle(fontSize = 13.sp),
                    quote = TextStyle(fontSize = 13.sp)
                ),
                imageTransformer = Coil3ImageTransformerImpl
            )

            Spacer(modifier = Modifier.height(12.dp))

            TextButton(
                onClick = { uriHandler.openUri(member.profileUrl) },
                modifier = Modifier.align(Alignment.End),
                contentPadding = androidx.compose.material3.ButtonDefaults.TextButtonContentPadding
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.OpenInNew,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text("GitHub")
            }
        }
    }
}
