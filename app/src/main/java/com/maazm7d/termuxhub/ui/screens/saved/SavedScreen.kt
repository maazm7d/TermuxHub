package com.maazm7d.termuxhub.ui.screens.saved

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.maazm7d.termuxhub.ui.components.ToolCard
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack

@Composable
fun SavedScreen(
    viewModel: SavedViewModel,
    onBack: () -> Unit
) {
    val savedTools by viewModel.savedTools.collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(title = { Text("Saved Tools") }, navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
            }
        })

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(savedTools) { t ->
                ToolCard(
                    tool = t,
                    onOpenDetails = {},
                    onLike = {},
                    onSave = {},
                    onShare = {}
                )
            }
        }
    }
}
