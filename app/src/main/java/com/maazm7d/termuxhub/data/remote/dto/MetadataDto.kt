package com.maazm7d.termuxhub.data.remote.dto

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MetadataDto(
    val version: String?,
    val lastUpdated: String?,
    val tools_count: Int? = 0,
    val tools: List<ToolDto> = emptyList()
)