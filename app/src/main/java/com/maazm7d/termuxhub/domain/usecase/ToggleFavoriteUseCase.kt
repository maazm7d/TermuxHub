package com.maazm7d.termuxhub.domain.usecase

import com.maazm7d.termuxhub.domain.repository.ToolRepository
import javax.inject.Inject

class ToggleFavoriteUseCase @Inject constructor(
    private val repository: ToolRepository
) {
    suspend operator fun invoke(toolId: String) {
        val tool = repository.getToolById(toolId) ?: return
        repository.setFavorite(toolId, !tool.isFavorite)
    }
}
