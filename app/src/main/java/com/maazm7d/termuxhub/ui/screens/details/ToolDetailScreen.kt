package com.maazm7d.termuxhub.ui.screens.details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.BugReport
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Download
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.maazm7d.termuxhub.domain.model.ToolDetails
import com.maazm7d.termuxhub.ui.components.DetailScreenThumbnail
import com.maazm7d.termuxhub.ui.components.shimmer
import com.maazm7d.termuxhub.ui.screens.details.components.InstallCommandRow
import com.maazm7d.termuxhub.utils.UiState
import com.mikepenz.markdown.coil3.Coil3ImageTransformerImpl
import com.mikepenz.markdown.m3.Markdown
import com.mikepenz.markdown.m3.markdownTypography
import androidx.compose.foundation.text.selection.SelectionContainer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ToolDetailScreen(
    toolId: String,
    viewModel: ToolDetailViewModel,
    onBack: () -> Unit
) {
    val uiStateWrapper by viewModel.uiState.collectAsState()

    LaunchedEffect(toolId) {
        viewModel.loadToolDetails(toolId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { paddingValues ->
        when (val state = uiStateWrapper) {
            is UiState.Loading -> ToolDetailShimmer(paddingValues)
            is UiState.Success -> ToolDetailContent(
                tool = state.data,
                paddingValues = paddingValues
            )
            is UiState.Error -> {
                Box(
                    modifier = Modifier.fillMaxSize().padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Failed to load tool",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.error
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = state.message,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ToolDetailContent(
    tool: ToolDetails,
    paddingValues: PaddingValues
) {
    val uriHandler = LocalUriHandler.current
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .verticalScroll(scrollState)
    ) {
        // Header Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            DetailScreenThumbnail(
                toolId = tool.id,
                modifier = Modifier
                    .size(88.dp)
                    .clip(MaterialTheme.shapes.large)
            )

            Spacer(modifier = Modifier.width(24.dp))

            Column {
                Text(
                    text = tool.title,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = tool.id.split("/").firstOrNull() ?: "Unknown",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = tool.id.split("/").firstOrNull() ?: "",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // Action Button
        Button(
            onClick = { /* Could scroll to install section */ },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 8.dp),
            shape = MaterialTheme.shapes.medium
        ) {
            Icon(Icons.Default.Download, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Install")
        }

        Spacer(modifier = Modifier.height(16.dp))
        HorizontalDivider(modifier = Modifier.padding(horizontal = 24.dp), thickness = 0.5.dp)
        Spacer(modifier = Modifier.height(16.dp))

        // Description
        Text(
            text = tool.description,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(horizontal = 24.dp),
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Install Section
        if (tool.installCommands.isNotBlank()) {
            Text(
                text = "Installation",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(horizontal = 24.dp)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .clip(MaterialTheme.shapes.medium)
                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
                    .padding(16.dp)
            ) {
                tool.installCommands
                    .lines()
                    .filter { it.isNotBlank() }
                    .forEach { cmd ->
                        InstallCommandRow(command = cmd)
                    }
            }
            Spacer(modifier = Modifier.height(24.dp))
        }

        // Readme Section
        if (tool.readme.isNotBlank()) {
            Text(
                text = "About this tool",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(horizontal = 24.dp)
            )
            Spacer(modifier = Modifier.height(12.dp))
            SelectionContainer {
                Markdown(
                    content = tool.readme,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    typography = markdownTypography(
                        text = TextStyle(fontSize = 14.sp, lineHeight = 20.sp),
                        h1 = TextStyle(fontSize = 22.sp, fontWeight = FontWeight.Bold),
                        h2 = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold),
                        h3 = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    ),
                    imageTransformer = Coil3ImageTransformerImpl
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
        }

        // External Links
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedButton(
                onClick = { tool.repoUrl?.let { uriHandler.openUri(it) } },
                modifier = Modifier.weight(1f),
                shape = MaterialTheme.shapes.medium
            ) {
                Icon(Icons.Default.Code, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Source")
            }
            OutlinedButton(
                onClick = { tool.repoUrl?.let { uriHandler.openUri("$it/issues") } },
                modifier = Modifier.weight(1f),
                shape = MaterialTheme.shapes.medium
            ) {
                Icon(Icons.Default.BugReport, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Issues")
            }
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
private fun ToolDetailShimmer(paddingValues: PaddingValues) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .verticalScroll(rememberScrollState())
            .padding(24.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(88.dp).clip(MaterialTheme.shapes.large).shimmer())
            Spacer(modifier = Modifier.width(24.dp))
            Column {
                Box(modifier = Modifier.width(120.dp).height(24.dp).shimmer())
                Spacer(modifier = Modifier.height(8.dp))
                Box(modifier = Modifier.width(80.dp).height(16.dp).shimmer())
            }
        }
        Spacer(modifier = Modifier.height(24.dp))
        Box(modifier = Modifier.fillMaxWidth().height(48.dp).clip(MaterialTheme.shapes.medium).shimmer())
        Spacer(modifier = Modifier.height(32.dp))
        repeat(5) {
            Box(modifier = Modifier.fillMaxWidth().height(16.dp).padding(vertical = 4.dp).shimmer())
        }
    }
}
