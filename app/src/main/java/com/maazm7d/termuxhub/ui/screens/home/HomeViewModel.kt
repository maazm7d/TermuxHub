package com.maazm7d.termuxhub.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maazm7d.termuxhub.domain.model.Tool
import com.maazm7d.termuxhub.data.repository.ToolRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.maazm7d.termuxhub.domain.mapper.toDomain
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

data class HomeUiState(
    val tools: List<Tool> = emptyList(),
    val isLoading: Boolean = false,
    val query: String = "",
    val selectedChipIndex: Int = 0
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: ToolRepository
) : ViewModel() {

    val uiState: StateFlow<HomeUiState> = repository.observeAll()
        .map { list -> HomeUiState(tools = list.map { it.toDomain() }) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), HomeUiState())

    private val _starsMap = MutableStateFlow<Map<String, Int>>(emptyMap())
    val starsMap: StateFlow<Map<String, Int>> = _starsMap.asStateFlow()

    init {
        viewModelScope.launch {
            fetchStarsForAllTools()
        }
    }

    fun toggleFavorite(toolId: String) {
        viewModelScope.launch {
            val tool = repository.getToolById(toolId) ?: return@launch
            repository.setFavorite(toolId, !tool.isFavorite)
        }
    }

    fun refresh() {
        viewModelScope.launch {
            repository.refreshFromRemote()
            fetchStarsForAllTools()
        }
    }

    private suspend fun fetchStarsForAllTools() {
        val tools = repository.observeAll().firstOrNull() ?: return
        val results = mutableMapOf<String, Int>()

        coroutineScope {
            val jobs = tools.map { entity ->
                async {
                    val repo = entity.repoUrl ?: ""
                    val stars = if (repo.isBlank()) null else repository.fetchStarsForRepo(repo)
                    if (stars != null) results[entity.id] = stars
                }
            }
            jobs.forEach { it.join() }
        }
        _starsMap.value = results
    }
}
