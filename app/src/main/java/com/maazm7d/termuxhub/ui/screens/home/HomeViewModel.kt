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

    fun toggleFavorite(toolId: String) {
        viewModelScope.launch {
            val tool = repository.getToolById(toolId) ?: return@launch
            repository.setFavorite(toolId, !tool.isFavorite)
        }
    }

    fun refresh() {
        viewModelScope.launch { repository.refreshFromRemote() }
    }
}

fun com.maazm7d.termuxhub.data.local.entities.ToolEntity.toDomain() = com.maazm7d.termuxhub.domain.model.Tool(
    id = id,
    name = name,
    description = description,
    category = category,
    installCommand = installCommand,
    repoUrl = repoUrl,
    thumbnail = thumbnail,
    version = version,
    updatedAt = updatedAt,
    isFavorite = isFavorite,
    likes = likes,
    views = views,
    publishedAt = publishedAt
)
