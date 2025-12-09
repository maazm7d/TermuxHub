package com.maazm7d.termuxhub.domain.model

data class Tool(
    val id: String,
    val name: String,
    val description: String,
    val category: String,
    val installCommand: String?,
    val repoUrl: String?,
    val thumbnail: String?,
    val version: String?,
    val updatedAt: Long,
    val isFavorite: Boolean,
    val likes: Int,
    val publishedAt: String?,
    val tags: List<String> = emptyList()  // <-- Add this
)
