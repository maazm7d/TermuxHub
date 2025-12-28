package com.maazm7d.termuxhub.ui.screens.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.maazm7d.termuxhub.domain.model.getPublishedDate
import com.maazm7d.termuxhub.ui.components.*

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
    onOpenDetails: (String) -> Unit
) {
    /* -------------------- STATE -------------------- */

    val uiState by viewModel.uiState.collectAsState()
    val starsMap by viewModel.starsMap.collectAsState()

    val searchQuery = rememberSaveable { mutableStateOf("") }
    var selectedCategoryIndex by rememberSaveable { mutableStateOf(0) }
    var currentSort by rememberSaveable { mutableStateOf(SortType.MOST_STARRED) }

    var sortMenuExpanded by remember { mutableStateOf(false) }
    var categoryMenuExpanded by remember { mutableStateOf(false) }

    /* -------------------- EFFECTS -------------------- */

    LaunchedEffect(Unit) {
        viewModel.refresh()
    }

    /* -------------------- DERIVED DATA -------------------- */

    val categoryCounts = remember(uiState.tools) {
        uiState.tools.groupingBy { it.category }.eachCount()
    }

    val categories = remember(uiState.tools, categoryCounts) {
        listOf("All" to uiState.tools.size) +
                categoryCounts.keys.sorted().map {
                    it to (categoryCounts[it] ?: 0)
                }
    }

    val filteredTools = remember(
        uiState.tools,
        searchQuery.value,
        selectedCategoryIndex,
        currentSort,
        starsMap
    ) {
        uiState.tools
            .filter { tool ->
                val matchesQuery =
                    searchQuery.value.isBlank() ||
                            tool.name.contains(searchQuery.value, true) ||
                            tool.description.contains(searchQuery.value, true)

                val matchesCategory =
                    selectedCategoryIndex == 0 ||
                            tool.category.equals(
                                categories[selectedCategoryIndex].first,
                                true
                            )

                matchesQuery && matchesCategory
            }
            .let { list ->
                when (currentSort) {
                    SortType.MOST_STARRED ->
                        list.sortedByDescending { starsMap[it.id] ?: 0 }
                    SortType.LEAST_STARRED ->
                        list.sortedBy { starsMap[it.id] ?: 0 }
                    SortType.NEWEST_FIRST ->
                        list.sortedByDescending { it.getPublishedDate() }
                    SortType.OLDEST_FIRST ->
                        list.sortedBy { it.getPublishedDate() }
                }
            }
    }

    /* -------------------- UI -------------------- */

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Tools") }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 6.dp)
        ) {

            /* ---------- SEARCH + SORT ---------- */

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                SearchBar(
                    queryState = searchQuery,
                    modifier = Modifier.weight(1f)
                )

                Box {
                    IconButton(onClick = { sortMenuExpanded = true }) {
                        Icon(Icons.Default.FilterList, contentDescription = "Sort")
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

            Spacer(modifier = Modifier.height(4.dp))

            /* ---------- CATEGORY ROW ---------- */

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box {
                    IconButton(onClick = { categoryMenuExpanded = true }) {
                        Icon(Icons.Default.GridView, contentDescription = "Categories")
                    }

                    DropdownMenu(
                        expanded = categoryMenuExpanded,
                        onDismissRequest = { categoryMenuExpanded = false }
                    ) {
                        categories.forEachIndexed { index, item ->
                            DropdownMenuItem(
                                text = { Text("${item.first} (${item.second})") },
                                onClick = {
                                    selectedCategoryIndex = index
                                    categoryMenuExpanded = false
                                }
                            )
                        }
                    }
                }

                CategoryChips(
                    chips = categories,
                    selectedIndex = selectedCategoryIndex,
                    onChipSelected = { selectedCategoryIndex = it }
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            /* ---------- TOOL LIST ---------- */

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 12.dp)
            ) {
                items(
                    items = filteredTools,
                    key = { tool -> tool.id }
                ) { tool ->
                    ToolCard(
                        tool = tool,
                        stars = starsMap[tool.id],
                        onOpenDetails = onOpenDetails,
                        onToggleFavorite = viewModel::toggleFavorite,
                        onSave = viewModel::toggleFavorite,
                        onShare = {}
                    )
                }
            }
        }
    }
}
