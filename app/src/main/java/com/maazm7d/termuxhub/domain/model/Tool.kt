package com.maazm7d.termuxhub.domain.model

import java.text.SimpleDateFormat
import java.util.*

data class Tool(
    val id: String,
    val name: String,
    val description: String,
    val category: String,
    val installCommand: String?,
    val repoUrl: String?,
    val author: String = "",
    val requireRoot: Boolean = false,
    val thumbnail: String?,
    val updatedAt: Long,
    val isFavorite: Boolean,
    val publishedAt: String?,
    val tags: List<String> = emptyList(),
    val stars: Int = 0,
    val forks: Int = 0,
    val issues: Int = 0,
    val pullRequests: Int = 0,
    val license: String? = null
)

private val publishedDateFormat =
    SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH)

fun Tool.getPublishedDate(): Date? =
    publishedAt?.let {
        runCatching { publishedDateFormat.parse(it) }.getOrNull()
    }

data class ToolDetails(
    val id: String,
    val title: String,
    val description: String,
    val readme: String,
    val installCommands: String,
    val repoUrl: String?,
    val stars: Int = 0,
    val forks: Int = 0,
    val issues: Int = 0,
    val pullRequests: Int = 0,
    val license: String? = null,
    val lastUpdated: Long = 0L,
    val requireRoot: Boolean = false
)
