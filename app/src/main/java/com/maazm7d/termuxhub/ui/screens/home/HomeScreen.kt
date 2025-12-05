package com.maazm7d.termuxhub.ui.screens.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.material3.icons.Icons
import androidx.compose.material3.icons.filled.FilterList
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.maazm7d.termuxhub.ui.components.*
import kotlinx.coroutines.launch
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.ArrowBack

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onOpenDetails: (String) -> Unit,
    onOpenSaved: () -> Unit,
    onOpenSettings: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()
    val query = remember { mutableStateOf("") }
    var selectedChip by remember { mutableStateOf(0) }

    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerContent = {
            AppDrawer { action ->
                when (action) {
                    "saved" -> onOpenSaved()
                    "settings" -> onOpenSettings()
                }
                scope.launch { drawerState.close() }
            }
        },
        drawerState = drawerState
    ) {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = { Text("Termux Tools & Commands") },
                    actions = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.FilterList, contentDescription = "Menu")
                        }
                    }
                )
            },
            bottomBar = {
                BottomNavBar(selectedIndex = 0, onSelected = { /* handle nav */ })
            },
            floatingActionButton = {
                FloatingActionButton(onClick = { /* filter */ }) {
                    Icon(Icons.Default.FilterList, contentDescription = "Filter")
                }
            }
        ) { padding ->
            Column(modifier = Modifier.padding(padding)) {
                Spacer(modifier = Modifier.height(8.dp))
                SearchBar(queryState = query, modifier = Modifier.padding(horizontal = 12.dp))
                ToolChipsRow(chips = state.chips, selectedIndex = selectedChip, onSelected = { idx -> selectedChip = idx })
                Spacer(modifier = Modifier.height(8.dp))
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    val filtered = state.tools.filter {
                        (query.value.isBlank() || it.name.contains(query.value, true) ||
                         it.description.contains(query.value, true)) &&
                        (selectedChip == 0 || it.category.contains(state.chips[selectedChip], true))
                    }
                    items(filtered) { tool ->
                        ToolCard(
                            tool = tool,
                            onOpenDetails = onOpenDetails,
                            onLike = { viewModel.toggleFavorite(it) },
                            onSave = { viewModel.toggleFavorite(it) },
                            onShare = { /* share */ }
                        )
                    }
                }
            }
        }
    }
}