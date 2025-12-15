package com.maazm7d.termuxhub.ui.screens.details

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.BugReport
import androidx.compose.material.icons.filled.Code
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import dev.jeziellago.compose.markdowntext.MarkdownText

@Composable
fun ToolDetailScreen(
    toolId: String,
    viewModel: ToolDetailViewModel,
    onBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(toolId) {
        viewModel.load(toolId)
    }

    uiState?.let { tool ->

        val uriHandler = LocalUriHandler.current

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {

            /* 🔹 Back Button */
            IconButton(
                onClick = onBack,
                modifier = Modifier.size(40.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back"
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            /* 🔹 Title */
            Text(
                text = tool.title,
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(10.dp))

            /* 🔹 Description */
            Text(
                text = tool.description,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            /* 🔹 README */
            if (tool.readme.isNotBlank()) {
                MarkdownText(
                    markdown = tool.readme,
                    modifier = Modifier.fillMaxWidth(),
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.height(24.dp))
            }

            /* 🔹 Installation */
            if (tool.installCommands.isNotBlank()) {

                Text(
                    text = "Installation",
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(8.dp))

                tool.installCommands
                    .lines()
                    .filter { it.isNotBlank() }
                    .forEach { cmd ->
                        InstallCommandRow(command = cmd)
                    }

                Spacer(modifier = Modifier.height(24.dp))
            }

            /* 🔹 Source Code / Report Issue */
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                OutlinedButton(
                    onClick = {
                        tool.repoUrl?.let { uriHandler.openUri(it) }
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Code,
                        contentDescription = "Source Code",
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Source Code")
                }

                OutlinedButton(
                    onClick = {
                        tool.repoUrl?.let {
                            uriHandler.openUri("$it/issues")
                        }
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        imageVector = Icons.Filled.BugReport,
                        contentDescription = "Report Issue",
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Report Issue")
                }
            }
        }
    }
}
