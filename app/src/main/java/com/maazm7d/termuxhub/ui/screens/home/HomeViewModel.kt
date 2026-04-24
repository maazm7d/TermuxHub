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

data class HomeUiState(
    val tools: List<com.maazm7d.termuxhub.domain.model.Tool> = emptyList(),
    val starsMap: Map<String, Int> = emptyMap(),
    val isRefreshing: Boolean = false
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getToolsUseCase: GetToolsUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase,
    private val refreshToolsUseCase: RefreshToolsUseCase,
    private val getStarsUseCase: GetStarsUseCase
) : ViewModel() {

    private val _isRefreshing = MutableStateFlow(false)
    private val _starsMap = MutableStateFlow<Map<String, Int>>(emptyMap())

    val uiState: StateFlow<UiState<HomeUiState>> = combine(
        getToolsUseCase(),
        _starsMap,
        _isRefreshing
    ) { tools, stars, refreshing ->
        UiState.Success(HomeUiState(tools, stars, refreshing)) as UiState<HomeUiState>
    }.catch { e ->
        emit(UiState.Error(e.message ?: "Failed to load tools"))
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = UiState.Loading
    )

    init {
        fetchStarsOnce()
    }

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

    private fun fetchStarsOnce() {
        viewModelScope.launch {
            try {
                _starsMap.value = getStarsUseCase()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
