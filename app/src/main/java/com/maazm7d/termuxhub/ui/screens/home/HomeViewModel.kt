package com.maazm7d.termuxhub.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maazm7d.termuxhub.domain.usecase.GetStarsUseCase
import com.maazm7d.termuxhub.domain.usecase.GetToolsUseCase
import com.maazm7d.termuxhub.domain.usecase.RefreshToolsUseCase
import com.maazm7d.termuxhub.domain.usecase.ToggleFavoriteUseCase
import com.maazm7d.termuxhub.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

import com.maazm7d.termuxhub.domain.model.Tool
import com.maazm7d.termuxhub.domain.model.getPublishedDate

data class HomeUiState(
    val tools: List<Tool> = emptyList(),
    val categories: List<Pair<String, Int>> = emptyList(),
    val isRefreshing: Boolean = false,
    val searchQuery: String = "",
    val selectedCategoryIndex: Int = 0,
    val currentSort: SortType = SortType.NEWEST_FIRST
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getToolsUseCase: GetToolsUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase,
    private val refreshToolsUseCase: RefreshToolsUseCase
) : ViewModel() {

    private val _isRefreshing = MutableStateFlow(false)
    private val _searchQuery = MutableStateFlow("")
    private val _selectedCategoryIndex = MutableStateFlow(0)
    private val _currentSort = MutableStateFlow(SortType.NEWEST_FIRST)

    val uiState: StateFlow<UiState<HomeUiState>> = combine(
        getToolsUseCase(),
        _isRefreshing,
        _searchQuery,
        _selectedCategoryIndex,
        _currentSort
    ) { tools, refreshing, query, categoryIndex, sort ->
        val categoryCounts = tools.groupingBy { it.category }.eachCount()
        val categories = listOf("All" to tools.size) + categoryCounts.keys.sorted().map { it to (categoryCounts[it] ?: 0) }

        val filteredTools = tools
            .filter { tool ->
                val matchesQuery = query.isBlank() ||
                        tool.name.contains(query, true) ||
                        tool.description.contains(query, true)
                val matchesCategory = categoryIndex == 0 ||
                        tool.category.equals(categories.getOrNull(categoryIndex)?.first, true)
                matchesQuery && matchesCategory
            }
            .let { list ->
                when (sort) {
                    SortType.NEWEST_FIRST -> list.sortedByDescending { it.getPublishedDate() }
                    SortType.OLDEST_FIRST -> list.sortedBy { it.getPublishedDate() }
                    SortType.MOST_STARRED -> list.sortedByDescending { it.stars }
                    SortType.LEAST_STARRED -> list.sortedBy { it.stars }
                }
            }

        UiState.Success(
            HomeUiState(
                tools = filteredTools,
                categories = categories,
                isRefreshing = refreshing,
                searchQuery = query,
                selectedCategoryIndex = categoryIndex,
                currentSort = sort
            )
        ) as UiState<HomeUiState>
    }.catch { e ->
        emit(UiState.Error(e.message ?: "Failed to load tools"))
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = UiState.Loading
    )

    fun refresh() {
        viewModelScope.launch {
            _isRefreshing.value = true
            try {
                refreshToolsUseCase()
            } catch (e: Exception) {
                // error is handled by the flow catch or we could emit a side effect
            } finally {
                _isRefreshing.value = false
            }
        }
    }

    fun toggleFavorite(toolId: String) {
        viewModelScope.launch {
            toggleFavoriteUseCase(toolId)
        }
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    fun onCategorySelected(index: Int) {
        _selectedCategoryIndex.value = index
    }

    fun onSortTypeSelected(sort: SortType) {
        _currentSort.value = sort
    }
}
