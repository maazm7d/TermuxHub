package com.maazm7d.termuxhub.ui.screens.whatsnew

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NewReleases
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp

@Composable
fun WhatsNewScreen() {
    val uriHandler = LocalUriHandler.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // Title
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Filled.NewReleases,
                contentDescription = "What's New",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(22.dp)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = "What’s New",
                style = MaterialTheme.typography.headlineSmall
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Top dialogue (unchanged)
        Text(
            text = "Discover the latest improvements, enhancements, and refinements in TermuxHub.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Subtle divider
        Divider(
            modifier = Modifier
                .width(120.dp),
            thickness = 1.dp,
            color = MaterialTheme.colorScheme.surface
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Center content
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {

                Text(
                    text = "This section will be used for update highlights in future versions.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Clickable repository text
                val annotatedText = buildAnnotatedString {
                    append("Got a wild idea? Drop it in the ")

                    pushStringAnnotation(
                        tag = "URL",
                        annotation = "https://github.com/maazm7d/TermuxHub" // replace if needed
                    )
                    withStyle(
                        style = SpanStyle(
                            color = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        append("project repository")
                    }
                    pop()

                    append(".\nIf it makes sense (and doesn’t break everything), we might just build it.")
                }

                ClickableText(
                    text = annotatedText,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center
                    ),
                    onClick = { offset ->
                        annotatedText
                            .getStringAnnotations("URL", offset, offset)
                            .firstOrNull()
                            ?.let { uriHandler.openUri(it.item) }
                    }
                )
            }
        }
    }
}
