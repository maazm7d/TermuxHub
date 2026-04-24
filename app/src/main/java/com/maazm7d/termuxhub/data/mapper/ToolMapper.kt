package com.maazm7d.termuxhub.data.mapper

import com.maazm7d.termuxhub.data.local.entities.ToolEntity
import com.maazm7d.termuxhub.data.remote.dto.RepoStatsDto
import com.maazm7d.termuxhub.data.remote.dto.ToolDto
import com.maazm7d.termuxhub.domain.model.Tool
import com.maazm7d.termuxhub.domain.model.ToolDetails

fun ToolDto.toEntity(
    existing: ToolEntity? = null,
    repoStats: Map<String, RepoStatsDto>
): ToolEntity? {
    if (id.isBlank() || name.isBlank()) return null

    val stats = repoStats[id]

    return ToolEntity(
        id = id,
        name = name,
        description = description ?: "",
        category = category ?: "Uncategorized",
        installCommand = install,
        repoUrl = repo,
        author = author ?: "",
        requireRoot = requireRoot ?: false,
        thumbnail = thumbnail,
        forks = stats?.forks ?: existing?.forks ?: 0,
        issues = stats?.issues ?: existing?.issues ?: 0,
        pullRequests = stats?.pullRequests ?: existing?.pullRequests ?: 0,
        license = stats?.license ?: existing?.license,
        stars = stats?.stars ?: existing?.stars ?: 0,
        updatedAt = stats?.lastUpdated
            ?: existing?.updatedAt
            ?: System.currentTimeMillis(),
        isFavorite = existing?.isFavorite ?: false,
        publishedAt = publishedAt,
        tags = tags,
        readme = existing?.readme
    )
}

fun ToolEntity.toDomain() = Tool(
    id = id,
    name = name,
    description = description,
    category = category,
    installCommand = installCommand,
    repoUrl = repoUrl,
    author = author ?: "",
    requireRoot = requireRoot,
    thumbnail = thumbnail,
    updatedAt = updatedAt,
    isFavorite = isFavorite,
    publishedAt = publishedAt,
    forks = forks,
    issues = issues,
    pullRequests = pullRequests,
    license = license,
    stars = stars
)

fun ToolEntity.toDetailDomain(readme: String) = ToolDetails(
    id = id,
    title = name,
    description = description,
    readme = readme,
    installCommands = installCommand ?: "",
    repoUrl = repoUrl,
    stars = stars,
    forks = forks,
    issues = issues,
    pullRequests = pullRequests,
    license = license,
    lastUpdated = updatedAt
)
