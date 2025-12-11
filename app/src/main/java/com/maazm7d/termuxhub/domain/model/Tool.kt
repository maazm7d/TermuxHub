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
    val author: String? = "",
    val thumbnail: String?,
    val version: String?,
    val updatedAt: Long,
    val isFavorite: Boolean,
    val publishedAt: String?,
    val tags: List<String> = emptyList()  // <-- Add this
)

fun Tool.getPublishedDate(): Date? {
    return try {
        SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH).parse(this.publishedAt ?: "")
    } catch (e: Exception) {
        null
    }
}
