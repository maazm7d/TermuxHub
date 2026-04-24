package com.maazm7d.termuxhub.ui.screens.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maazm7d.termuxhub.domain.model.ToolDetails
import com.maazm7d.termuxhub.domain.usecase.GetToolDetailsUseCase
import com.maazm7d.termuxhub.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ToolDetailViewModel @Inject constructor(
    private val getToolDetailsUseCase: GetToolDetailsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<ToolDetails>>(UiState.Loading)
    val uiState: StateFlow<UiState<ToolDetails>> = _uiState.asStateFlow()

    fun loadToolDetails(toolId: String) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                val details = getToolDetailsUseCase(toolId)
                if (details != null) {
                    _uiState.value = UiState.Success(details)
                } else {
                    _uiState.value = UiState.Error("Tool not found")
                }
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "Failed to load tool details")
            }
        }
    }
}
