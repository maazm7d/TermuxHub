package com.maazm7d.termuxhub.ui.screens.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maazm7d.termuxhub.data.repository.ToolRepository
import com.maazm7d.termuxhub.domain.model.Tool
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.maazm7d.termuxhub.domain.mapper.toDomain

@HiltViewModel
class ToolDetailViewModel @Inject constructor(
    private val repository: com.maazm7d.termuxhub.data.repository.ToolRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<Tool?>(null)
    val uiState: StateFlow<Tool?> = _uiState

    fun load(id: String) {
        viewModelScope.launch {
            val entity = repository.getToolById(id)
            _uiState.value = entity?.toDomain()
        }
    }

    fun toggleFavorite(toolId: String) {
        viewModelScope.launch {
            val entity = repository.getToolById(toolId) ?: return@launch
            repository.setFavorite(toolId, !entity.isFavorite)
        }
    }
}