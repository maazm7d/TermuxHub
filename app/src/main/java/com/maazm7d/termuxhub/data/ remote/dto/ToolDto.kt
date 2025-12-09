package com.maazm7d.termuxhub.data.remote.dto

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ToolDto(
    val id: String,
    val name: String,
    val description: String? = "",
    val category: String? = "",
    val install: String? = "",
    val repo: String? = "",
    val thumbnail: String? = null,
    val version: String? = null,
    val updatedAt: Long? = 0L,
    val tags: List<String>? = emptyList(),
    val likes: Int? = 0,
    val publishedAt: String? = null
)
