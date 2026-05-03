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
                onOpenDetails = onOpenDetails,
                onSearchQueryChanged = { viewModel.onSearchQueryChanged(it) },
                onCategorySelected = { viewModel.onCategorySelected(it) },
                onSortTypeSelected = { viewModel.onSortTypeSelected(it) }
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
    onOpenDetails: (String) -> Unit,
    onSearchQueryChanged: (String) -> Unit,
    onCategorySelected: (Int) -> Unit,
    onSortTypeSelected: (SortType) -> Unit
) {
    var sortMenuExpanded by remember { mutableStateOf(false) }
    var categoryMenuExpanded by remember { mutableStateOf(false) }

    val listState = rememberLazyListState()

    // Scroll to top when filters change
    LaunchedEffect(state.searchQuery, state.selectedCategoryIndex, state.currentSort) {
        listState.animateScrollToItem(0)
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
                SearchBar(
                    query = state.searchQuery,
                    onQueryChange = onSearchQueryChanged,
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
                        SortType.entries.forEach { sort ->
                            DropdownMenuItem(
                                text = { Text(sort.label) },
                                leadingIcon = {
                                    if (state.currentSort == sort) Icon(Icons.Default.Check, null)
                                },
                                onClick = {
                                    onSortTypeSelected(sort)
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
                        state.categories.forEachIndexed { index, item ->
                            DropdownMenuItem(
                                text = { Text("${item.first} (${item.second})") },
                                leadingIcon = {
                                    if (state.selectedCategoryIndex == index) Icon(Icons.Default.Check, null)
                                },
                                onClick = {
                                    onCategorySelected(index)
                                    categoryMenuExpanded = false
                                }
                            )
                        }
                    }
                }

                CategoryChips(
                    chips = state.categories,
                    selectedIndex = state.selectedCategoryIndex,
                    onChipSelected = onCategorySelected
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            LazyColumn(
                state = listState,
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 12.dp)
            ) {
                items(state.tools, key = { it.id }) { tool ->
                    ToolCard(
                        tool = tool,
                        stars = tool.stars,
                        onOpenDetails = onOpenDetails,
                        onToggleFavorite = { onToggleFavorite(it) },
                        onSave = { onToggleFavorite(it) }
                    )
                }
            }
        }
    }
}
