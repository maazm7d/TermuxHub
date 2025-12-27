package com.maazm7d.termuxhub.ui.screens.home

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.maazm7d.termuxhub.domain.model.getPublishedDate
import com.maazm7d.termuxhub.ui.components.*
import kotlinx.coroutines.launch

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

    /* -------------------- STATE -------------------- */

    val uiState by viewModel.uiState.collectAsState()
    val starsMap by viewModel.starsMap.collectAsState()

    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val searchQuery = rememberSaveable { mutableStateOf("") }
    var selectedCategoryIndex by rememberSaveable { mutableStateOf(0) }
    var currentSort by rememberSaveable { mutableStateOf(SortType.MOST_STARRED) }

    var sortMenuExpanded by remember { mutableStateOf(false) }
    var categoryMenuExpanded by remember { mutableStateOf(false) }

    // 🔒 Menu cooldown timestamp (THE FIX)
    var menuCooldownUntil by rememberSaveable { mutableLongStateOf(0L) }

    val isMenuEnabled =
        System.currentTimeMillis() > menuCooldownUntil

    /* -------------------- EFFECTS -------------------- */

    LaunchedEffect(Unit) {
        viewModel.refresh()
        drawerState.close()
    }

    BackHandler(drawerState.isOpen) {
        scope.launch { drawerState.close() }
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

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier.width(280.dp),
                drawerContainerColor = Color.White
            ) {
                AppDrawer { action ->
                    scope.launch {
                        // 🔒 START cooldown BEFORE leaving HomeScreen
                        menuCooldownUntil = System.currentTimeMillis() + 1500

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
        Scaffold { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 6.dp)
            ) {

                /* ---------- TOP ROW ---------- */

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        enabled = isMenuEnabled && !drawerState.isAnimationRunning,
                        onClick = {
                            scope.launch { drawerState.open() }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Menu",
                            tint = if (isMenuEnabled) {
                                MaterialTheme.colorScheme.onSurface
                            } else {
                                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                            }
                        )
                    }

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

                /* ---------- LIST ---------- */

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
                            onOpenDetails = {
                                // 🔒 Also start cooldown when opening details
                                menuCooldownUntil = System.currentTimeMillis() + 1500
                                onOpenDetails(it)
                            },
                            onToggleFavorite = viewModel::toggleFavorite,
                            onSave = viewModel::toggleFavorite,
                            onShare = {}
                        )
                    }
                }
            }
        }
    }
}
