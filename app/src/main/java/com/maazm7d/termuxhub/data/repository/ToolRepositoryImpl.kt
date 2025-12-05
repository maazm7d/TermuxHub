package com.maazm7d.termuxhub.data.repository

import android.content.Context
import com.maazm7d.termuxhub.data.local.ToolDao
import com.maazm7d.termuxhub.data.local.entities.ToolEntity
import com.maazm7d.termuxhub.data.remote.MetadataClient
import com.maazm7d.termuxhub.data.remote.dto.MetadataDto
import com.maazm7d.termuxhub.data.remote.dto.ToolDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader
import javax.inject.Inject

/**
 * Concrete implementation of ToolRepository.
 * Uses Room (ToolDao) and MetadataClient (Retrofit + Moshi) and falls back to local asset JSON.
 */
class ToolRepositoryImpl @Inject constructor(
    private val toolDao: ToolDao,
    private val metadataClient: MetadataClient,
    private val appContext: Context,
    private val assetsFileName: String = "metadata.json"
) : ToolRepository {

    override fun observeAll(): Flow<List<ToolEntity>> = toolDao.getAllToolsFlow()

    override fun observeFavorites(): Flow<List<ToolEntity>> = toolDao.getFavoritesFlow()

    override suspend fun getToolById(id: String): ToolEntity? = toolDao.getToolById(id)

    override suspend fun setFavorite(toolId: String, isFav: Boolean) {
        val current = toolDao.getToolById(toolId) ?: return
        toolDao.update(current.copy(isFavorite = isFav))
    }

    override suspend fun refreshFromRemote(): Boolean {
        return try {
            val response = metadataClient.fetchMetadata()
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    val entities = body.tools.mapNotNull { it.toEntity() }
                    toolDao.insertAll(entities)
                    true
                } else {
                    loadFromAssets()
                }
            } else {
                loadFromAssets()
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
            loadFromAssets()
        }
    }

    private suspend fun loadFromAssets(): Boolean = withContext(Dispatchers.IO) {
        try {
            val input = appContext.assets.open(assetsFileName)
            val text = BufferedReader(InputStreamReader(input)).use { it.readText() }

            val moshi = com.squareup.moshi.Moshi.Builder()
                .addLast(com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory())
                .build()

            val adapter = moshi.adapter(MetadataDto::class.java)
            val dto = adapter.fromJson(text)

            if (dto != null) {
                val entities = dto.tools.mapNotNull { it.toEntity() }
                toolDao.insertAll(entities)
                true
            } else false
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    private fun ToolDto.toEntity(): ToolEntity? {
        if (id.isBlank() || name.isBlank()) return null
        return ToolEntity(
            id = id,
            name = name,
            description = description ?: "",
            category = category ?: "Uncategorized",
            installCommand = install,
            repoUrl = repo,
            thumbnail = thumbnail,
            version = version,
            updatedAt = updatedAt ?: 0L,
            isFavorite = false,
            likes = likes ?: 0,
            views = views ?: 0,
            publishedAt = publishedAt
        )
    }
}