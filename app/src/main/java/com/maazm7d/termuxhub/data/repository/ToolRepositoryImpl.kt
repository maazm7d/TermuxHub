package com.maazm7d.termuxhub.data.repository

import com.maazm7d.termuxhub.data.local.entities.ToolEntity
import com.maazm7d.termuxhub.data.mapper.toDetailDomain
import com.maazm7d.termuxhub.data.mapper.toEntity
import com.maazm7d.termuxhub.data.remote.dto.RepoStatsDto
import com.maazm7d.termuxhub.data.source.local.LocalDataSource
import com.maazm7d.termuxhub.data.source.remote.RemoteDataSource
import com.maazm7d.termuxhub.domain.model.ToolDetails
import com.maazm7d.termuxhub.domain.repository.ToolRepository
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
        val current = localDataSource.getToolById(toolId) ?: return
        localDataSource.updateTool(current.copy(isFavorite = isFav))
    }

    override suspend fun refreshFromRemote(): Boolean {
        return try {
            val response = remoteDataSource.fetchMetadata()
            if (response.isSuccessful && response.body() != null) {
                val repoStats = fetchRepoStats()
                val metadata = response.body()!!
                val entities = metadata.tools.mapNotNull { dto ->
                    val existing = localDataSource.getToolById(dto.id)
                    dto.toEntity(existing, repoStats)
                }
                if (entities.isNotEmpty()) {
                    localDataSource.insertTools(entities)
                }
                applyStars()
                true
            } else {
                loadFromAssets()
            }
        } catch (e: Exception) {
            Timber.e(e, "Error refreshing tools from remote")
            loadFromAssets()
        }
    }

    override suspend fun fetchStars(): Map<String, Int> {
        return try {
            val resp = remoteDataSource.fetchStars()
            if (resp.isSuccessful) resp.body()?.stars ?: emptyMap()
            else emptyMap()
        } catch (e: Exception) {
            Timber.e(e, "Error fetching stars")
            emptyMap()
        }
    }

    private suspend fun fetchRepoStats(): Map<String, RepoStatsDto> {
        return try {
            val resp = remoteDataSource.fetchRepoStats()
            if (resp.isSuccessful) resp.body()?.stats ?: emptyMap()
            else emptyMap()
        } catch (e: Exception) {
            Timber.e(e, "Error fetching repo stats")
            emptyMap()
        }
    }

    private suspend fun applyStars() {
        val starsMap = fetchStars()
        starsMap.forEach { (toolId, starCount) ->
            val tool = localDataSource.getToolById(toolId)
            if (tool != null && tool.stars != starCount) {
                localDataSource.updateTool(tool.copy(stars = starCount))
            }
        }
    }

    private suspend fun loadFromAssets(): Boolean {
        return try {
            val repoStats = fetchRepoStats()
            val dto = localDataSource.loadMetadataFromAssets(assetsFileName)

            val entities = dto?.tools?.mapNotNull { t ->
                val existing = localDataSource.getToolById(t.id)
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
