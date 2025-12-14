package com.maazm7d.termuxhub.data.repository

import com.maazm7d.termuxhub.data.local.entities.ToolEntity
import kotlinx.coroutines.flow.Flow
import com.maazm7d.termuxhub.domain.model.ToolDetails

interface ToolRepository {
    fun observeAll(): Flow<List<ToolEntity>>
    fun observeFavorites(): Flow<List<ToolEntity>>
    suspend fun getToolById(id: String): ToolEntity?
    suspend fun setFavorite(toolId: String, isFav: Boolean)
    suspend fun refreshFromRemote(): Boolean

    // NEW: fetch github stars for a repo url (returns null on parse/error)
    suspend fun fetchStarsForRepo(repoUrl: String): Int?
    suspend fun getToolDetails(id: String): ToolDetails?
}
