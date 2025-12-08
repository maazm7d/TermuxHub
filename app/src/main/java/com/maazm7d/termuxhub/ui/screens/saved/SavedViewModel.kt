package com.maazm7d.termuxhub.ui.screens.saved

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maazm7d.termuxhub.data.repository.ToolRepository
import com.maazm7d.termuxhub.domain.model.Tool
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.maazm7d.termuxhub.domain.mapper.toDomain

@HiltViewModel
class SavedViewModel @Inject constructor(
    private val repository: ToolRepository
) : ViewModel() {

    val savedTools: StateFlow<List<Tool>> =
        repository.observeFavorites()
            .map { list -> list.map { it.toDomain() } }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun removeTool(tool: Tool) {
        viewModelScope.launch {
            repository.setFavorite(tool.id, false) // permanently remove favorite
        }
    }
}
