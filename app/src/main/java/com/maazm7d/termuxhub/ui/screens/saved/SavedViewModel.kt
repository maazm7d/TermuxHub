package com.maazm7d.termuxhub.ui.screens.saved

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maazm7d.termuxhub.data.repository.ToolRepository
import com.maazm7d.termuxhub.domain.model.Tool
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.maazm7d.termuxhub.domain.mapper.toDomain

@HiltViewModel
class SavedViewModel @Inject constructor(
    private val repository: ToolRepository
) : ViewModel() {

    private val _savedTools = MutableStateFlow<List<Tool>>(emptyList())
    val savedTools: StateFlow<List<Tool>> = _savedTools

    init {
        viewModelScope.launch {
            repository.observeFavorites()
                .map { list -> list.map { it.toDomain() } }
                .collect { _savedTools.value = it }
        }
    }
}

fun com.maazm7d.termuxhub.data.local.entities.ToolEntity.toDomain() = Tool(
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