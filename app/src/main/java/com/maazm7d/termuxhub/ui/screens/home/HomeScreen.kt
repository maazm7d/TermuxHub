package com.maazm7d.termuxhub.ui.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.maazm7d.termuxhub.domain.model.getPublishedDate
import com.maazm7d.termuxhub.ui.components.CategoryChips
import com.maazm7d.termuxhub.ui.components.SearchBar
import com.maazm7d.termuxhub.ui.components.ToolCard

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
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val starsMap by viewModel.starsMap.collectAsStateWithLifecycle()

    var searchQuery by rememberSaveable { mutableStateOf("") }
    var selectedCategoryIndex by rememberSaveable { mutableIntStateOf(0) }
    var currentSort by rememberSaveable { mutableStateOf(SortType.NEWEST_FIRST) }

    var sortMenuExpanded by remember { mutableStateOf(false) }
    var categoryMenuExpanded by remember { mutableStateOf(false) }

    val listState = rememberLazyListState()

    // Derive categories and counts
    val categories = remember(uiState.tools) {
        val allCount = uiState.tools.size
        val counts = uiState.tools.groupingBy { it.category }.eachCount()
        listOf("All" to allCount) + counts.entries.sortedBy { it.key }.map { it.key to it.value }
    }

    // Filter and sort tools
    val filteredTools by remember(uiState.tools, searchQuery, selectedCategoryIndex, currentSort, starsMap) {
        derivedStateOf {
            uiState.tools
                .filter { tool ->
                    val matchesQuery = searchQuery.isBlank() ||
                            tool.name.contains(searchQuery, ignoreCase = true) ||
                            tool.description.contains(searchQuery, ignoreCase = true)

                    val matchesCategory = selectedCategoryIndex == 0 ||
                            tool.category.equals(categories[selectedCategoryIndex].first, ignoreCase = true)

                    matchesQuery && matchesCategory
                }
                .let { list ->
                    when (currentSort) {
                        SortType.NEWEST_FIRST -> list.sortedByDescending { it.getPublishedDate() }
                        SortType.OLDEST_FIRST -> list.sortedBy { it.getPublishedDate() }
                        SortType.MOST_STARRED -> list.sortedByDescending { starsMap[it.id] ?: 0 }
                        SortType.LEAST_STARRED -> list.sortedBy { starsMap[it.id] ?: 0 }
                    }
                }
        }
    }

    // Scroll to top when filter/sort changes
    LaunchedEffect(currentSort, selectedCategoryIndex, searchQuery) {
        listState.scrollToItem(0)
    }

    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 6.dp)
                .padding(padding)
        ) {
            // Search and Sort Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                SearchBar(
                    queryState = remember { mutableStateOf(searchQuery) },
                    onQueryChange = { searchQuery = it },
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
                                    if (currentSort == sort) {
                                        Icon(Icons.Default.Check, contentDescription = null)
                                    }
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

            // Category Row
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
                        categories.forEachIndexed { index, (name, count) ->
                            DropdownMenuItem(
                                text = { Text("$name ($count)") },
                                leadingIcon = {
                                    if (selectedCategoryIndex == index) {
                                        Icon(Icons.Default.Check, contentDescription = null)
                                    }
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

            // Tool List
            LazyColumn(
                state = listState,
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 12.dp)
            ) {
                items(filteredTools, key = { it.id }) { tool ->
                    ToolCard(
                        tool = tool,
                        stars = starsMap[tool.id],
                        onOpenDetails = onOpenDetails,
                        onToggleFavorite = viewModel::toggleFavorite,
                        onSave = viewModel::toggleFavorite
                    )
                }
            }
        }
    }
}

// Updated SearchBar composable to accept onQueryChange instead of MutableState
@Composable
private fun SearchBar(
    queryState: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "Search tools..."
) {
    androidx.compose.material3.TextField(
        value = queryState,
        onValueChange = onQueryChange,
        modifier = modifier.fillMaxWidth(),
        placeholder = { Text(placeholder) },
        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
        trailingIcon = {
            if (queryState.isNotEmpty()) {
                IconButton(onClick = { onQueryChange("") }) {
                    Icon(Icons.Default.Clear, contentDescription = "Clear")
                }
            }
        },
        singleLine = true,
        colors = androidx.compose.material3.TextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.surface,
            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
            focusedIndicatorColor = androidx.compose.ui.graphics.Color.Transparent,
            unfocusedIndicatorColor = androidx.compose.ui.graphics.Color.Transparent,
            cursorColor = MaterialTheme.colorScheme.primary
        ),
        shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp)
    )
}
