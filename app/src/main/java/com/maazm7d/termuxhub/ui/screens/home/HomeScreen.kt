package com.maazm7d.termuxhub.ui.screens.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material3.*
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.maazm7d.termuxhub.domain.model.getPublishedDate
import com.maazm7d.termuxhub.ui.components.CategoryChips
import com.maazm7d.termuxhub.ui.components.SearchBar
import com.maazm7d.termuxhub.ui.components.ToolCard
import com.maazm7d.termuxhub.utils.UiState

enum class SortType(val label: String) {
    NEWEST_FIRST("Newest first"),
    OLDEST_FIRST("Oldest first"),
    MOST_STARRED("Most starred"),
    LEAST_STARRED("Least starred")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
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
            HomeContent(
                state = state.data,
                onRefresh = { viewModel.refresh() },
                onToggleFavorite = { viewModel.toggleFavorite(it) },
                onOpenDetails = onOpenDetails
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeContent(
    state: HomeUiState,
    onRefresh: () -> Unit,
    onToggleFavorite: (String) -> Unit,
    onOpenDetails: (String) -> Unit
) {
    val searchQuery = rememberSaveable { mutableStateOf("") }
    var selectedCategoryIndex by rememberSaveable { mutableStateOf(0) }
    var currentSort by rememberSaveable { mutableStateOf(SortType.NEWEST_FIRST) }

    var sortMenuExpanded by remember { mutableStateOf(false) }
    var categoryMenuExpanded by remember { mutableStateOf(false) }

    val listState = rememberLazyListState()

    val categoryCounts = remember(state.tools) { state.tools.groupingBy { it.category }.eachCount() }
    val categories = remember(state.tools, categoryCounts) {
        listOf("All" to state.tools.size) + categoryCounts.keys.sorted().map { it to (categoryCounts[it] ?: 0) }
    }

    val filteredTools = remember(state.tools, searchQuery.value, selectedCategoryIndex, currentSort, state.starsMap) {
        state.tools
            .filter { tool ->
                val matchesQuery = searchQuery.value.isBlank() ||
                        tool.name.contains(searchQuery.value, true) ||
                        tool.description.contains(searchQuery.value, true)
                val matchesCategory = selectedCategoryIndex == 0 ||
                        tool.category.equals(categories[selectedCategoryIndex].first, true)
                matchesQuery && matchesCategory
            }
            .let { list ->
                when (currentSort) {
                    SortType.NEWEST_FIRST -> list.sortedByDescending { it.getPublishedDate() }
                    SortType.OLDEST_FIRST -> list.sortedBy { it.getPublishedDate() }
                    SortType.MOST_STARRED -> list.sortedByDescending { state.starsMap[it.id] ?: 0 }
                    SortType.LEAST_STARRED -> list.sortedBy { state.starsMap[it.id] ?: 0 }
                }
            }
    }

    PullToRefreshBox(
        isRefreshing = state.isRefreshing,
        onRefresh = onRefresh
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 6.dp)
        ) {
            // Search + sort row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                SearchBar(queryState = searchQuery, modifier = Modifier.weight(1f))
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
                                leadingIcon = {
                                    if (currentSort == sort) Icon(Icons.Default.Check, null)
                                },
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

            // Category row
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
                                leadingIcon = {
                                    if (selectedCategoryIndex == index) Icon(Icons.Default.Check, null)
                                },
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

            LazyColumn(
                state = listState,
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 12.dp)
            ) {
                items(filteredTools, key = { it.id }) { tool ->
                    ToolCard(
                        tool = tool,
                        stars = state.starsMap[tool.id],
                        onOpenDetails = onOpenDetails,
                        onToggleFavorite = { onToggleFavorite(it) },
                        onSave = { onToggleFavorite(it) }
                    )
                }
            }
        }
    }
}
