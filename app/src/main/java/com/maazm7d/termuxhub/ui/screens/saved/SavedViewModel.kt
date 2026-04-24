package com.maazm7d.termuxhub.ui.screens.saved

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maazm7d.termuxhub.domain.usecase.GetSavedToolsUseCase
import com.maazm7d.termuxhub.domain.usecase.ToggleFavoriteUseCase
import com.maazm7d.termuxhub.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SavedUiState(
    val tools: List<com.maazm7d.termuxhub.domain.model.Tool> = emptyList()
)

@HiltViewModel
class SavedViewModel @Inject constructor(
    private val getSavedToolsUseCase: GetSavedToolsUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase
) : ViewModel() {

    val uiState: StateFlow<UiState<SavedUiState>> = getSavedToolsUseCase()
        .map { tools -> UiState.Success(SavedUiState(tools)) as UiState<SavedUiState> }
        .catch { e -> emit(UiState.Error(e.message ?: "Failed to load saved tools")) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = UiState.Loading
        )

    fun unsave(toolId: String) {
        viewModelScope.launch {
            toggleFavoriteUseCase(toolId)
        }
    }
}
