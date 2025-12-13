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
    private val repository: ToolRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<ToolDetails?>(null)
    val uiState: StateFlow<ToolDetails?> = _uiState

    fun load(id: String) {
        viewModelScope.launch {
            _uiState.value = repository.getToolDetails(id)
        }
    }
}
