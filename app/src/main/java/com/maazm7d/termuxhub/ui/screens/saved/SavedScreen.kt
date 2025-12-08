package com.maazm7d.termuxhub.ui.screens.saved

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.window.PopupProperties
import com.maazm7d.termuxhub.ui.components.ToolCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SavedScreen(
    viewModel: SavedViewModel,
    onBack: () -> Unit
) {
    val savedTools by viewModel.savedTools.collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {

        TopAppBar(
            title = { Text("Saved Tools") },
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }
            }
        )

        if (savedTools.isEmpty()) {
            // Show friendly message when no saved tools
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("Start by saving your favourite tool!", style = MaterialTheme.typography.bodyLarge)
            }
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(savedTools) { tool ->
                    var expanded by remember { mutableStateOf(false) }

                    Box {
                        ToolCard(
                            tool = tool,
                            onOpenDetails = {},
                            onLike = {},
                            onSave = {},
                            onShare = {}
                        )

                        // Three dots menu
                        IconButton(
                            onClick = { expanded = true },
                            modifier = Modifier.align(Alignment.TopEnd)
                        ) {
                            Icon(Icons.Default.MoreVert, contentDescription = "Menu")
                        }

                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false },
                            properties = PopupProperties(focusable = true)
                        ) {
                            DropdownMenuItem(
                                text = { Text("Remove") },
                                onClick = {
                                    viewModel.removeTool(tool)
                                    expanded = false
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}
