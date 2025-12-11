package com.maazm7d.termuxhub.domain.usecase

import com.maazm7d.termuxhub.domain.model.Tool
import com.maazm7d.termuxhub.data.local.entities.ToolEntity
import com.maazm7d.termuxhub.data.repository.ToolRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetToolsUseCase @Inject constructor(
    private val repository: ToolRepository
) {
    operator fun invoke(): Flow<List<Tool>> {
        return repository.observeAll().map { list ->
            list.map { it.toDomain() }
        }
    }
}

private fun ToolEntity.toDomain() = Tool(
    id = id,
    name = name,
    description = description,
    category = category,
    installCommand = installCommand,
    repoUrl = repoUrl,
    author = author ?: "",
    thumbnail = thumbnail,
    version = version,
    updatedAt = updatedAt,
    isFavorite = isFavorite,
    publishedAt = publishedAt
)
