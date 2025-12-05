package com.maazm7d.termuxhub.ui.screens.details

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ToolDetailScreen(
    toolId: String,
    viewModel: ToolDetailViewModel,
    onBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val ctx = LocalContext.current

    LaunchedEffect(toolId) { viewModel.load(toolId) }

    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text(uiState?.name ?: "") },
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }
            }
        )
        uiState?.let { tool ->
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = tool.description, style = MaterialTheme.typography.bodyLarge)
                Spacer(Modifier.height(8.dp))
                tool.repoUrl?.let { url ->
                    Button(onClick = { ctx.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url))) }) {
                        Text("Open GitHub Repo")
                    }
                }
            }
        }
    }
}
