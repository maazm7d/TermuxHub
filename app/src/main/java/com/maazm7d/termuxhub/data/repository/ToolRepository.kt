package com.maazm7d.termuxhub.data.repository

import com.maazm7d.termuxhub.data.local.entities.ToolEntity
import com.maazm7d.termuxhub.domain.model.ToolDetails
import kotlinx.coroutines.flow.Flow

interface ToolRepository {
    fun observeAll(): Flow<List<ToolEntity>>
    fun observeFavorites(): Flow<List<ToolEntity>>

    fun observeLoading(): Flow<Boolean>
    fun setLoading(loading: Boolean)

    suspend fun getToolById(id: String): ToolEntity?
    suspend fun setFavorite(toolId: String, isFav: Boolean)
    suspend fun refreshFromRemote(): Boolean

    suspend fun fetchStarsForRepo(repoUrl: String): Int?
    suspend fun getToolDetails(id: String): ToolDetails?
}
