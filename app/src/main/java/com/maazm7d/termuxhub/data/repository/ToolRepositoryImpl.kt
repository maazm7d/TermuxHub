package com.maazm7d.termuxhub.data.repository

import com.maazm7d.termuxhub.data.local.entities.ToolEntity
import com.maazm7d.termuxhub.data.mapper.toDetailDomain
import com.maazm7d.termuxhub.data.mapper.toEntity
import com.maazm7d.termuxhub.data.remote.dto.RepoStatsDto
import com.maazm7d.termuxhub.data.source.local.LocalDataSource
import com.maazm7d.termuxhub.data.source.remote.RemoteDataSource
import com.maazm7d.termuxhub.domain.model.ToolDetails
import com.maazm7d.termuxhub.domain.repository.ToolRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import timber.log.Timber
import javax.inject.Inject

class ToolRepositoryImpl @Inject constructor(
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource,
    private val assetsFileName: String = "metadata/metadata.json"
) : ToolRepository {

    override fun observeAll(): Flow<List<ToolEntity>> =
        localDataSource.getAllToolsFlow()

    override fun observeFavorites(): Flow<List<ToolEntity>> =
        localDataSource.getFavoritesFlow()

    override suspend fun getToolById(id: String): ToolEntity? =
        localDataSource.getToolById(id)

    override suspend fun setFavorite(toolId: String, isFav: Boolean) {
        try {
            val current = localDataSource.getToolById(toolId) ?: return
            localDataSource.updateTool(current.copy(isFavorite = isFav))
        } catch (e: Exception) {
            Timber.e(e, "Error setting favorite for tool: $toolId")
        }
    }

    override suspend fun refreshFromRemote(): Boolean {
        return try {
            kotlinx.coroutines.coroutineScope {
                val metadataDeferred = async { remoteDataSource.fetchMetadata() }
                val repoStatsDeferred = async { fetchRepoStats() }
                val starsDeferred = async { fetchStars() }

                val response = metadataDeferred.await()
                val repoStats = repoStatsDeferred.await()
                val starsMap = starsDeferred.await()

                if (response.isSuccessful && response.body() != null) {
                    val metadata = response.body()!!
                    val existingTools = localDataSource.getAllTools().associateBy { it.id }
                    val entities = metadata.tools.mapNotNull { dto ->
                        val existing = existingTools[dto.id]
                        dto.toEntity(existing, repoStats)?.copy(
                            stars = starsMap[dto.id] ?: existing?.stars ?: 0
                        )
                    }
                    if (entities.isNotEmpty()) {
                        localDataSource.insertTools(entities)
                    }
                    true
                } else {
                    loadFromAssets()
                }
            }
        } catch (e: Exception) {
            Timber.e(e, "Error refreshing tools from remote")
            loadFromAssets()
        }
    }

    override suspend fun fetchStars(): Map<String, Int> = runCatching {
        val resp = remoteDataSource.fetchStars()
        if (resp.isSuccessful) resp.body()?.stars ?: emptyMap()
        else emptyMap()
    }.getOrElse { e ->
        Timber.e(e, "Error fetching stars")
        emptyMap()
    }

    private suspend fun fetchRepoStats(): Map<String, RepoStatsDto> = runCatching {
        val resp = remoteDataSource.fetchRepoStats()
        if (resp.isSuccessful) resp.body()?.stats ?: emptyMap()
        else emptyMap()
    }.getOrElse { e ->
        Timber.e(e, "Error fetching repo stats")
        emptyMap()
    }

    private suspend fun applyStars() {
        val starsMap = fetchStars()
        if (starsMap.isEmpty()) return

        val allTools = localDataSource.getAllTools()
        val toolsToUpdate = allTools.mapNotNull { tool ->
            val remoteStars = starsMap[tool.id]
            if (remoteStars != null && tool.stars != remoteStars) {
                tool.copy(stars = remoteStars)
            } else {
                null
            }
        }

        if (toolsToUpdate.isNotEmpty()) {
            localDataSource.updateTools(toolsToUpdate)
        }
    }

    private suspend fun loadFromAssets(): Boolean {
        return try {
            val repoStats = fetchRepoStats()
            val dto = localDataSource.loadMetadataFromAssets(assetsFileName)
            val existingTools = localDataSource.getAllTools().associateBy { it.id }

            val entities = dto?.tools?.mapNotNull { t ->
                val existing = existingTools[t.id]
                t.toEntity(existing, repoStats)
            } ?: emptyList()

            if (entities.isNotEmpty()) {
                localDataSource.insertTools(entities)
            }

            applyStars()
            true
        } catch (e: Exception) {
            Timber.e(e, "Error loading tools from assets")
            false
        }
    }

    override suspend fun getToolDetails(id: String): ToolDetails? {
        val tool = localDataSource.getToolById(id) ?: return null
        var readme = tool.readme
        if (readme.isNullOrBlank()) {
            readme = try {
                remoteDataSource.fetchReadme(id).body() ?: ""
            } catch (e: Exception) {
                Timber.e(e, "Error fetching readme for $id")
                ""
            }
            if (readme.isNotBlank()) {
                localDataSource.updateTool(tool.copy(readme = readme))
            }
        }
        return tool.toDetailDomain(readme)
    }
}
