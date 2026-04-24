package com.maazm7d.termuxhub.domain.usecase

import com.maazm7d.termuxhub.domain.model.Tool
import com.maazm7d.termuxhub.domain.repository.ToolRepository
import com.maazm7d.termuxhub.data.mapper.toDomain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetToolsUseCase @Inject constructor(
    private val repository: ToolRepository
) {
    operator fun invoke(): Flow<List<Tool>> =
        repository.observeAll().map { entities ->
            entities.map { it.toDomain() }
        }
}
