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

    // -------------------------------
    // TOOLS STATE
    // -------------------------------
    val uiState: StateFlow<HomeUiState> =
        repository.observeAll()
            .map { entities ->
                HomeUiState(
                    tools = entities.map { it.toDomain() }
                )
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = HomeUiState()
            )

    // -------------------------------
    // STARS STATE (SAFE & PERSISTENT)
    // -------------------------------
    private val _starsMap = MutableStateFlow<Map<String, Int>>(emptyMap())
    val starsMap: StateFlow<Map<String, Int>> = _starsMap.asStateFlow()

    // -------------------------------
    // INIT
    // -------------------------------
    init {
        // Reactively fetch stars when tool list changes
        viewModelScope.launch {
            uiState
                .map { it.tools }
                .distinctUntilChanged()
                .collect { tools ->
                    fetchStarsIfNeeded(tools)
                }
        }
    }

    // -------------------------------
    // FAVORITE TOGGLE
    // -------------------------------
    fun toggleFavorite(toolId: String) {
        viewModelScope.launch {
            val tool = repository.getToolById(toolId) ?: return@launch
            repository.setFavorite(toolId, !tool.isFavorite)
        }
    }

    // -------------------------------
    // REFRESH (REMOTE ONLY)
    // -------------------------------
    fun refresh() {
        viewModelScope.launch {
            repository.refreshFromRemote()
            // ⛔ Do NOT fetch stars here
            // Stars are fetched reactively above
        }
    }

    // -------------------------------
    // STAR FETCHING (SAFE)
    // -------------------------------
    private suspend fun fetchStarsIfNeeded(tools: List<Tool>) {
        if (tools.isEmpty()) return

        val currentStars = _starsMap.value.toMutableMap()

        coroutineScope {
            tools.forEach { tool ->
                if (currentStars.containsKey(tool.id)) return@forEach
                val repoUrl = tool.repoUrl ?: return@forEach

                async {
                    val stars = repository.fetchStarsForRepo(repoUrl)
                    if (stars != null) {
                        currentStars[tool.id] = stars
                    }
                }
            }
        }

        // 🔒 Never overwrite with empty map
        if (currentStars.isNotEmpty()) {
            _starsMap.value = currentStars
        }
    }
}
