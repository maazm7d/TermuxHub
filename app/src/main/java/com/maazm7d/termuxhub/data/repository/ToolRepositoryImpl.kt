package com.maazm7d.termuxhub.data.repository

import android.content.Context
import com.maazm7d.termuxhub.data.local.ToolDao
import com.maazm7d.termuxhub.data.local.entities.ToolEntity
import com.maazm7d.termuxhub.data.remote.MetadataClient
import com.maazm7d.termuxhub.data.remote.dto.MetadataDto
import com.maazm7d.termuxhub.data.remote.dto.RepoStatsDto
import com.maazm7d.termuxhub.data.remote.dto.ToolDto
import com.maazm7d.termuxhub.domain.model.ToolDetails
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader
import javax.inject.Inject

class ToolRepositoryImpl @Inject constructor(
    private val toolDao: ToolDao,
    private val metadataClient: MetadataClient,
    private val appContext: Context,
    private val assetsFileName: String = "metadata/metadata.json"
) : ToolRepository {

    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    override fun observeAll(): Flow<List<ToolEntity>> =
        toolDao.getAllToolsFlow()

    override fun observeFavorites(): Flow<List<ToolEntity>> =
        toolDao.getFavoritesFlow()

    override suspend fun getToolById(id: String): ToolEntity? =
        toolDao.getToolById(id)

    override suspend fun setFavorite(toolId: String, isFav: Boolean) {
        val current = toolDao.getToolById(toolId) ?: return
        toolDao.update(current.copy(isFavorite = isFav))
    }

    override suspend fun refreshFromRemote(): Boolean {
        return try {
            val response = metadataClient.fetchMetadata()
            if (response.isSuccessful && response.body() != null) {
                val repoStats = fetchRepoStats()
                val metadata = response.body()!!
                val entities = metadata.tools.mapNotNull { dto ->
                    val existing = toolDao.getToolById(dto.id)
                    dto.toEntity(existing, repoStats)
                }
                if (entities.isNotEmpty()) {
                    toolDao.insertAll(entities)   // batch insert
                }
                applyStars()
                true
            } else {
                loadFromAssets()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            loadFromAssets()
        }
    }

    override suspend fun fetchStars(): Map<String, Int> {
        return try {
            val resp = metadataClient.fetchStars()
            if (resp.isSuccessful) resp.body()?.stars ?: emptyMap()
            else emptyMap()
        } catch (e: Exception) {
            e.printStackTrace()
            emptyMap()
        }
    }

    private suspend fun fetchRepoStats(): Map<String, RepoStatsDto> {
        return try {
            val resp = metadataClient.fetchRepoStats()
            if (resp.isSuccessful) resp.body()?.stats ?: emptyMap()
            else emptyMap()
        } catch (_: Exception) {
            emptyMap()
        }
    }

    private suspend fun applyStars() {
        val starsMap = fetchStars()
        starsMap.forEach { (toolId, starCount) ->
            val tool = toolDao.getToolById(toolId)
            if (tool != null && tool.stars != starCount) {
                toolDao.update(tool.copy(stars = starCount))
            }
        }
    }

    private suspend fun loadFromAssets(): Boolean = withContext(Dispatchers.IO) {
        try {
            val repoStats = fetchRepoStats()
            val input = appContext.assets.open(assetsFileName)
            val text = BufferedReader(InputStreamReader(input)).use { it.readText() }
            val adapter = moshi.adapter(MetadataDto::class.java)
            val dto = adapter.fromJson(text)

            val entities = dto?.tools?.mapNotNull { t ->
                val existing = toolDao.getToolById(t.id)
                t.toEntity(existing, repoStats)
            } ?: emptyList()

            if (entities.isNotEmpty()) {
                toolDao.insertAll(entities)   // batch insert
            }

            applyStars()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    private fun ToolDto.toEntity(
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
            tags = tags,                     // directly assign List<String>? from DTO
            readme = existing?.readme         // preserve any previously cached readme
        )
    }

    override suspend fun getToolDetails(id: String): ToolDetails? {
        val tool = toolDao.getToolById(id) ?: return null
        var readme = tool.readme
        if (readme.isNullOrBlank()) {
            readme = try {
                metadataClient.fetchReadme(id).body() ?: ""
            } catch (e: Exception) {
                ""
            }
            if (readme.isNotBlank()) {
                toolDao.update(tool.copy(readme = readme))
            }
        }
        return ToolDetails(
            id = tool.id,
            title = tool.name,
            description = tool.description,
            readme = readme,
            installCommands = tool.installCommand ?: "",
            repoUrl = tool.repoUrl,
            stars = tool.stars,
            forks = tool.forks,
            issues = tool.issues,
            pullRequests = tool.pullRequests,
            license = tool.license,
            lastUpdated = tool.updatedAt
        )
    }
}
