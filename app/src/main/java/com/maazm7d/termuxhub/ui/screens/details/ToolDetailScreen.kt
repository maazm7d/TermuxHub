package com.maazm7d.termuxhub.ui.screens.details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.foundation.border
import androidx.compose.material.icons.filled.BugReport
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.maazm7d.termuxhub.ui.components.DetailScreenThumbnail
import com.maazm7d.termuxhub.ui.components.shimmer
import com.maazm7d.termuxhub.ui.components.ToolRepoBadgesRow
import com.maazm7d.termuxhub.domain.model.ToolDetails
import kotlinx.coroutines.launch
import androidx.compose.foundation.text.selection.SelectionContainer
import com.mikepenz.markdown.m3.Markdown
import com.mikepenz.markdown.m3.markdownTypography
import com.mikepenz.markdown.coil3.Coil3ImageTransformerImpl
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import com.maazm7d.termuxhub.ui.screens.details.components.InstallCommandRow
import androidx.compose.foundation.shape.RoundedCornerShape
import com.maazm7d.termuxhub.utils.UiState

@Composable
fun ToolDetailScreen(
    toolId: String,
    viewModel: ToolDetailViewModel,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val uiStateWrapper by viewModel.uiState.collectAsState()
    val installState by viewModel.installState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    var showSecurityDialog by remember { mutableStateOf(false) }

    LaunchedEffect(toolId) {
        viewModel.loadToolDetails(toolId)
    }

    LaunchedEffect(installState) {
        when (val state = installState) {
            is InstallState.Launched -> {
                snackbarHostState.showSnackbar("Launched in Termux")
                viewModel.resetInstallState()
            }
            is InstallState.TermuxMissing -> {
                snackbarHostState.showSnackbar("Termux is not installed")
                viewModel.resetInstallState()
            }
            is InstallState.SecurityError -> {
                showSecurityDialog = true
                viewModel.resetInstallState()
            }
            is InstallState.Error -> {
                snackbarHostState.showSnackbar("Error: ${state.message}")
                viewModel.resetInstallState()
            }
            else -> {}
        }
    }

    if (showSecurityDialog) {
        AlertDialog(
            onDismissRequest = { showSecurityDialog = false },
            title = { Text("Permission Required") },
            text = {
                Column {
                    Text("Termux requires 'allow-external-apps' to be enabled to run commands from other apps.")
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "1. Open Termux\n2. Run: nano ~/.termux/termux.properties\n3. Set: allow-external-apps = true\n4. Save and run: termux-reload-settings",
                        style = MaterialTheme.typography.bodySmall,
                        fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = { showSecurityDialog = false }) {
                    Text("OK")
                }
            }
        )
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            when (val state = uiStateWrapper) {
                is UiState.Loading -> ToolDetailShimmer()
                is UiState.Success -> ToolDetailContent(
                    tool = state.data,
                    viewModel = viewModel,
                    onBack = onBack
                )
                is UiState.Error -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
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
}

@Composable
private fun ToolDetailContent(
    tool: ToolDetails,
    viewModel: ToolDetailViewModel,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val uriHandler = LocalUriHandler.current
    val scrollState = rememberScrollState()
    val scope = rememberCoroutineScope()

    Box(modifier = Modifier.fillMaxSize()) {
        SelectionContainer {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(16.dp)
            ) {
                IconButton(onClick = onBack, modifier = Modifier.size(40.dp)) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedCard(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.outlinedCardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(16.dp)
                    ) {
                        DetailScreenThumbnail(
                            toolId = tool.id,
                            modifier = Modifier.padding(horizontal = 8.dp)
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        ToolRepoBadgesRow(tool)

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = tool.title,
                            style = MaterialTheme.typography.headlineSmall,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = tool.description,
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center,
                            maxLines = 3,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.padding(horizontal = 12.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                if (tool.readme.isNotBlank()) {
                    Markdown(
                        content = tool.readme,
                        modifier = Modifier.fillMaxWidth(),
                        typography = markdownTypography(
                            text = TextStyle(fontSize = 13.sp, lineHeight = 18.sp),
                            h1 = TextStyle(fontSize = 20.sp),
                            h2 = TextStyle(fontSize = 18.sp),
                            h3 = TextStyle(fontSize = 16.sp),
                            h4 = TextStyle(fontSize = 15.sp),
                            h5 = TextStyle(fontSize = 14.sp),
                            h6 = TextStyle(fontSize = 13.sp),
                            code = TextStyle(fontSize = 12.sp, lineHeight = 16.sp),
                            bullet = TextStyle(fontSize = 13.sp),
                            quote = TextStyle(fontSize = 13.sp)
                        ),
                        imageTransformer = Coil3ImageTransformerImpl
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                }

                if (tool.installCommands.isNotBlank()) {
                    OutlinedCard(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.outlinedCardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        )
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Installation",
                                style = MaterialTheme.typography.titleMedium
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            if (tool.requireRoot) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(
                                            MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.3f),
                                            RoundedCornerShape(8.dp)
                                        )
                                        .border(
                                            1.dp,
                                            MaterialTheme.colorScheme.error.copy(alpha = 0.5f),
                                            RoundedCornerShape(8.dp)
                                        )
                                        .padding(8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Warning,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.error,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "This tool runs 'su' inside Termux.",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onErrorContainer,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                                Spacer(modifier = Modifier.height(12.dp))
                            }

                            tool.installCommands
                                .lines()
                                .filter { it.isNotBlank() }
                                .forEach { cmd ->
                                    InstallCommandRow(
                                        command = cmd,
                                        onRunClick = { viewModel.runInstall(context, tool) }
                                    )
                                }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = { tool.repoUrl?.let { uriHandler.openUri(it) } },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.outlinedButtonColors()
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
                        onClick = { tool.repoUrl?.let { uriHandler.openUri("$it/issues") } },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.outlinedButtonColors()
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

        if (scrollState.maxValue > 0 && scrollState.value < scrollState.maxValue) {
            FloatingActionButton(
                onClick = {
                    scope.launch {
                        scrollState.animateScrollTo(scrollState.maxValue)
                    }
                },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp),
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(
                    imageVector = Icons.Outlined.KeyboardArrowDown,
                    contentDescription = "Scroll to bottom"
                )
            }
        }
    }
}

@Composable
private fun ToolDetailShimmer() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .shimmer()
        )

        Spacer(Modifier.height(12.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(16f / 9f)
                .padding(horizontal = 8.dp)
                .shimmer()
        )

        Spacer(Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth(0.6f)
                .height(28.dp)
                .shimmer()
                .align(Alignment.CenterHorizontally)
        )

        Spacer(Modifier.height(10.dp))

        repeat(3) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(16.dp)
                    .padding(horizontal = 12.dp, vertical = 2.dp)
                    .shimmer()
            )
        }

        Spacer(Modifier.height(24.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth(0.4f)
                .height(20.dp)
                .shimmer()
        )

        Spacer(Modifier.height(8.dp))

        repeat(6) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(14.dp)
                    .padding(vertical = 3.dp)
                    .shimmer()
            )
        }

        Spacer(Modifier.height(24.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth(0.4f)
                .height(20.dp)
                .shimmer()
        )

        Spacer(Modifier.height(8.dp))

        repeat(2) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(32.dp)
                    .padding(vertical = 4.dp)
                    .shimmer()
            )
        }

        Spacer(Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
                    .shimmer()
            )
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
                    .shimmer()
            )
        }
    }
}
