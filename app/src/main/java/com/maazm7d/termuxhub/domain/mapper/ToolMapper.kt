package com.maazm7d.termuxhub.domain.mapper

import com.maazm7d.termuxhub.data.local.entities.ToolEntity
import com.maazm7d.termuxhub.domain.model.Tool

fun ToolEntity.toDomain() = Tool(
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
    publishedAt = publishedAt
)
