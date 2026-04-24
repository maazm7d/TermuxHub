package com.maazm7d.termuxhub.ui.screens.saved

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.maazm7d.termuxhub.utils.UiState

@Composable
fun SavedScreen(
    viewModel: SavedViewModel,
    onOpenDetails: (String) -> Unit
) {
    val uiStateWrapper by viewModel.uiState.collectAsState()

    when (val state = uiStateWrapper) {
        is UiState.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        is UiState.Error -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = state.message, color = MaterialTheme.colorScheme.error)
            }
        }
        is UiState.Success -> {
            SavedContent(
                state = state.data,
                onOpenDetails = onOpenDetails,
                onRemove = { viewModel.unsave(it.id) }
            )
        }
    }
}

@Composable
private fun SavedContent(
    state: SavedUiState,
    onOpenDetails: (String) -> Unit,
    onRemove: (com.maazm7d.termuxhub.domain.model.Tool) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Filled.Bookmark,
                contentDescription = "Saved",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(22.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Saved",
                style = MaterialTheme.typography.headlineSmall
            )
            if (state.tools.isNotEmpty()) {
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "· ${state.tools.size}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Your bookmarked tools",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(10.dp))
        HorizontalDivider(
            modifier = Modifier
                .width(120.dp)
                .align(Alignment.CenterHorizontally),
            thickness = 1.dp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)
        )
        Spacer(modifier = Modifier.height(16.dp))

        if (state.tools.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    text = "No saved tools yet",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(vertical = 12.dp)
            ) {
                items(state.tools, key = { it.id }) { tool ->
                    SavedToolRow(
                        tool = tool,
                        onOpenDetails = onOpenDetails,
                        onRemove = { onRemove(tool) }
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }
        }
    }
}
