package com.maazm7d.termuxhub.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import kotlinx.coroutines.launch
import com.maazm7d.termuxhub.ui.components.*
import com.maazm7d.termuxhub.domain.model.Tool
import com.maazm7d.termuxhub.domain.model.getPublishedDate
import androidx.compose.material.icons.filled.GridView

enum class SortType(val label: String) {
    MOST_STARRED("Most starred"),
    LEAST_STARRED("Least starred"),
    NEWEST_FIRST("Newest first"),
    OLDEST_FIRST("Oldest first")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onOpenDetails: (String) -> Unit,
    onOpenSaved: () -> Unit,
    onOpenSettings: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()
    val stars by viewModel.starsMap.collectAsState()

    LaunchedEffect(Unit) { viewModel.refresh() }

    val query = remember { mutableStateOf("") }
    var selectedChip by remember { mutableStateOf(0) }
    var sortMenuExpanded by remember { mutableStateOf(false) }
    var currentSort by remember { mutableStateOf(SortType.MOST_STARRED) }
    var categoryMenuExpanded by remember { mutableStateOf(false) }

    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val categoryCounts = state.tools.groupingBy { it.category }.eachCount()
    val chipsWithCounts = listOf("All" to state.tools.size) +
            categoryCounts.keys.sorted().map { it to (categoryCounts[it] ?: 0) }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(280.dp)
                    .background(Color.White)
            ) {


                
               AppDrawer { action ->
    scope.launch {
        drawerState.close()   
        when (action) {
            "saved" -> onOpenSaved()
            "about" -> onOpenSettings()
        }
    }
               } 

            
            }
        }
    ) {
        Scaffold(topBar = {}) { padding ->

            Column(
                modifier = Modifier
                    .padding(padding)
                    .padding(horizontal = 4.dp)
            ) {

                // SEARCH + DRAWER + SORT IN ONE LINE
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = { scope.launch { drawerState.open() } }
                    ) {
                        Icon(Icons.Default.Menu, contentDescription = "Menu")
                    }

                    SearchBar(
                        queryState = query,
                        modifier = Modifier.weight(1f)
                    )

                    Box {
                        IconButton(onClick = { sortMenuExpanded = true }) {
                            Icon(Icons.Default.FilterList, contentDescription = "Sort & Filter")
                        }

                        DropdownMenu(
                            expanded = sortMenuExpanded,
                            onDismissRequest = { sortMenuExpanded = false }
                        ) {
                            SortType.values().forEach { sort ->
                                DropdownMenuItem(
                                    text = { Text(sort.label) },
                                    onClick = {
                                        currentSort = sort
                                        sortMenuExpanded = false
                                    }
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(2.dp))

                Row(
    modifier = Modifier.fillMaxWidth(),
    verticalAlignment = Alignment.CenterVertically
) {

    // CATEGORY MENU ICON
    Box {
        IconButton(
            onClick = { categoryMenuExpanded = true }
        ) {
            Icon(
                imageVector = Icons.Default.GridView,
                contentDescription = "Categories"
            )
        }

        DropdownMenu(
            expanded = categoryMenuExpanded,
            onDismissRequest = { categoryMenuExpanded = false }
        ) {
            chipsWithCounts.forEachIndexed { index, item ->
                DropdownMenuItem(
                    text = {
                        Text("${item.first} (${item.second})")
                    },
                    onClick = {
                        selectedChip = index
                        categoryMenuExpanded = false
                    }
                )
            }
        }
    }

    // EXISTING CHIPS (UNCHANGED)
    CategoryChips(
        chips = chipsWithCounts,
        selectedIndex = selectedChip,
        onChipSelected = { selectedChip = it }
    )
                }

                // FILTER + SORT TOOLS
                val filteredTools = state.tools.filter { tool ->
                    val byQuery = query.value.isBlank() ||
                            tool.name.contains(query.value, true) ||
                            tool.description.contains(query.value, true)

                    val byCategory = selectedChip == 0 ||
                            tool.category.equals(chipsWithCounts[selectedChip].first, true)

                    byQuery && byCategory
                }.let { list ->
                    when (currentSort) {
                        SortType.MOST_STARRED -> list.sortedByDescending { stars[it.id] ?: 0 }
                        SortType.LEAST_STARRED -> list.sortedBy { stars[it.id] ?: 0 }
                        SortType.NEWEST_FIRST -> list.sortedByDescending { it.getPublishedDate() }
                        SortType.OLDEST_FIRST -> list.sortedBy { it.getPublishedDate() }
                    }
                }

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 2.dp)
                ) {
                    items(filteredTools) { tool ->
                        ToolCard(
                            tool = tool,
                            stars = stars[tool.id],
                            onOpenDetails = onOpenDetails,
                            onToggleFavorite = { viewModel.toggleFavorite(it) },
                            onSave = { viewModel.toggleFavorite(it) },
                            onShare = {}
                        )
                    }
                }
            }
        }
    }
}
