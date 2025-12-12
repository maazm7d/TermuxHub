package com.maazm7d.termuxhub.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tools")
data class ToolEntity(
    @PrimaryKey val id: String,
    val name: String,
    val description: String,
    val category: String,
    @ColumnInfo(name = "install_command") val installCommand: String?,
    @ColumnInfo(name = "repo_url") val repoUrl: String?,
    @ColumnInfo(name = "thumbnail") val thumbnail: String?,
    val version: String?,
    @ColumnInfo(name = "updated_at") val updatedAt: Long = 0L,
    @ColumnInfo(name = "is_favorite") val isFavorite: Boolean = false,
    @ColumnInfo(name = "author") val author: String? = "",
    @ColumnInfo(name = "require_root") val requireRoot: Boolean = false, 
    @ColumnInfo(name = "published_at") val publishedAt: String? = null
)
