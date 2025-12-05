package com.maazm7d.termuxhub.ui.screens.saved

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.maazm7d.termuxhub.ui.components.ToolCard
import com.maazm7d.termuxhub.ui.screens.saved.SavedViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import com.maazm7d.termuxhub.domain.model.Tool
import androidx.compose.foundation.layout.fillMaxSize

@Composable
fun SavedScreen(
    viewModel: SavedViewModel = viewModel(),
    onBack: () -> Unit
) {
    val savedTools by viewModel.savedTools.collectAsState()

    Column {
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