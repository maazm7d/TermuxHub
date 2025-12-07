package com.maazm7d.termuxhub.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import com.maazm7d.termuxhub.ui.components.*
import com.maazm7d.termuxhub.ui.components.CategoryChips

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
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(280.dp)
                    .background(Color.White)
            ) {
                AppDrawer { action ->
                    when (action) {
                        "saved" -> onOpenSaved()
                        "settings" -> onOpenSettings()
                    }
                    scope.launch { drawerState.close() }
                }
            }
        },
        drawerState = drawerState
    ) {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = { Text("Termux Hub") },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.FilterList, contentDescription = "Menu")
                        }
                    }
                )
            }
        ) { padding ->

            Column(modifier = Modifier.padding(padding)) {

                // SEARCH BAR
                SearchBar(
                    queryState = query,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp)
                )

                // CATEGORY CHIPS
                CategoryChips(
                    chips = listOf("All") + state.chips,
                    selectedIndex = selectedChip,
                    onChipSelected = { selectedChip = it }
                )

                // FILTER LIST
                val filteredTools = state.tools.filter {
                    val matchesQuery = query.value.isBlank() ||
                            it.name.contains(query.value, ignoreCase = true) ||
                            it.description.contains(query.value, ignoreCase = true)

                    val matchesCategory = selectedChip == 0 ||
                            it.category.contains(state.chips[selectedChip - 1], ignoreCase = true)

                    matchesQuery && matchesCategory
                }

                // TOOL LIST
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 6.dp)
                ) {
                    items(filteredTools) { tool ->
                        ToolCard(
                            tool = tool,
                            onOpenDetails = onOpenDetails,
                            onLike = { viewModel.toggleFavorite(it) },
                            onSave = { viewModel.toggleFavorite(it) },
                            onShare = {}
                        )
                    }
                }
            }
        }
    }
}
