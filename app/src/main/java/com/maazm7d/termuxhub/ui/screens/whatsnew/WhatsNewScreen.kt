package com.maazm7d.termuxhub.ui.screens.whatsnew

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NewReleases
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@Composable
fun WhatsNewScreen() {
    val uriHandler = LocalUriHandler.current
    var textLayoutResult by remember { mutableStateOf<androidx.compose.ui.text.TextLayoutResult?>(null) }

    val whatsNewItems = listOf(
        "- App link support added — open tool details directly from a single shared link",
        "- Redesigned Tool Detail Screen with cleaner layout, better spacing, and smoother scrolling",
        "- Repository stats badges (stars, forks, etc.) added for quick insights",
        "- Migrated markdown rendering to multiplatform-markdown-renderer for improved compatibility and readability",
        "- Text selection enabled in the tool detail screen",
        "- Updated cards to a modern Outlined Material 3 style",
        "- Enhanced Saved Screen layout for a cleaner look",
        "- Refined typography across key screens for improved visual consistency"
    )

    val feedbackText = buildAnnotatedString {
        append("Have an idea or suggestion? Share it on the ")

        pushStringAnnotation(
            tag = "URL",
            annotation = "https://github.com/maazm7d/TermuxHub"
        )

        withStyle(
            style = SpanStyle(color = MaterialTheme.colorScheme.primary)
        ) {
            append("project repository")
        }

        pop()

        append(". Your feedback helps shape what comes next.")
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
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

            Text(
                text = "Recent updates and improvements",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(10.dp))

            HorizontalDivider(
                modifier = Modifier.width(120.dp),
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Version 3.2.4",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.75f),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(20.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    whatsNewItems.forEachIndexed { index, text ->
                        AnimatedWhatsNewItem(
                            text = text,
                            delayMillis = index * 120
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = feedbackText,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                onTextLayout = { textLayoutResult = it },
                modifier = Modifier
                    .padding(bottom = 8.dp)
                    .pointerInput(Unit) {
                        detectTapGestures { offset ->
                            textLayoutResult
                                ?.getOffsetForPosition(offset)
                                ?.let { index ->
                                    feedbackText
                                        .getStringAnnotations("URL", index, index)
                                        .firstOrNull()
                                        ?.let { uriHandler.openUri(it.item) }
                                }
                        }
                    }
            )
        }
    }
}

@Composable
private fun AnimatedWhatsNewItem(
    text: String,
    delayMillis: Int
) {
    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(delayMillis.toLong())
        visible = true
    }

    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(
            animationSpec = tween(durationMillis = 400)
        ) + slideInVertically(
            initialOffsetY = { it / 6 },
            animationSpec = tween(durationMillis = 400)
        )
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 8.dp)
        )
    }
}
