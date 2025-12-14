package com.maazm7d.termuxhub.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maazm7d.termuxhub.data.repository.ToolRepository
import com.maazm7d.termuxhub.domain.mapper.toDomain
import com.maazm7d.termuxhub.domain.model.Tool
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeUiState(
    val tools: List<Tool> = emptyList(),
    val isLoading: Boolean = false
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: ToolRepository
) : ViewModel() {

    val uiState: StateFlow<HomeUiState> =
        combine(
            repository.observeAll(),
            repository.observeLoading()
        ) { tools, loading ->
            HomeUiState(
                tools = tools.map { it.toDomain() },
                isLoading = loading
            )
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            HomeUiState()
        )

    private val _starsMap = MutableStateFlow<Map<String, Int>>(emptyMap())
    val starsMap: StateFlow<Map<String, Int>> = _starsMap.asStateFlow()

    fun toggleFavorite(toolId: String) {
        viewModelScope.launch {
            val tool = repository.getToolById(toolId) ?: return@launch
            repository.setFavorite(toolId, !tool.isFavorite)
        }
    }

    fun refresh() {
        viewModelScope.launch {
            repository.setLoading(true)
            repository.refreshFromRemote()
            fetchStarsForAllTools()
            repository.setLoading(false)
        }
    }

    private suspend fun fetchStarsForAllTools() {
        val tools = repository.observeAll().firstOrNull() ?: return
        val results = mutableMapOf<String, Int>()

        coroutineScope {
            tools.map { entity ->
                async {
                    val repo = entity.repoUrl ?: return@async
                    repository.fetchStarsForRepo(repo)?.let {
                        results[entity.id] = it
                    }
                }
            }.forEach { it.await() }
        }

        _starsMap.value = results
    }
}
