package com.maazm7d.termuxhub.ui.screens.saved

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.maazm7d.termuxhub.ui.components.ToolCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SavedScreen(
    viewModel: SavedViewModel,
    onOpenDetails: (String) -> Unit
) {
    val savedTools by viewModel.savedTools.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Saved") }
            )
        }
    ) { padding ->

        if (savedTools.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No saved tools yet",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentPadding = PaddingValues(bottom = 12.dp)
            ) {
                items(
                    items = savedTools,
                    key = { it.id }
                ) { tool ->
                    ToolCard(
                        tool = tool,
                        stars = null,
                        onOpenDetails = onOpenDetails,
                        onToggleFavorite = { viewModel.removeTool(tool) },
                        onSave = { viewModel.removeTool(tool) },
                        onShare = {}
                    )
                }
            }
        }
    }
}
