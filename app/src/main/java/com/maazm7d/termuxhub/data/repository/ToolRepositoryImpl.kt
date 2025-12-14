package com.maazm7d.termuxhub.data.repository

import android.content.Context
import com.maazm7d.termuxhub.data.local.ToolDao
import com.maazm7d.termuxhub.data.local.entities.ToolEntity
import com.maazm7d.termuxhub.data.remote.GitHubClient
import com.maazm7d.termuxhub.data.remote.MetadataClient
import com.maazm7d.termuxhub.data.remote.dto.MetadataDto
import com.maazm7d.termuxhub.data.remote.dto.ToolDto
import com.maazm7d.termuxhub.domain.model.ToolDetails
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader
import javax.inject.Inject

class ToolRepositoryImpl @Inject constructor(
    private val toolDao: ToolDao,
    private val metadataClient: MetadataClient,
    private val githubClient: GitHubClient,
    private val appContext: Context,
    private val assetsFileName: String = "metadata.json"
) : ToolRepository {

    private val loading = MutableStateFlow(false)

    override fun observeLoading(): Flow<Boolean> = loading

    override fun setLoading(loading: Boolean) {
        this.loading.value = loading
    }

    override fun observeAll(): Flow<List<ToolEntity>> =
        toolDao.getAllToolsFlow()

    override fun observeFavorites(): Flow<List<ToolEntity>> =
        toolDao.getFavoritesFlow()

    override suspend fun getToolById(id: String): ToolEntity? =
        toolDao.getToolById(id)

    override suspend fun setFavorite(toolId: String, isFav: Boolean) {
        toolDao.getToolById(toolId)?.let {
            toolDao.update(it.copy(isFavorite = isFav))
        }
    }

    override suspend fun refreshFromRemote(): Boolean {
        return try {
            val response = metadataClient.fetchMetadata()
            if (response.isSuccessful) {
                response.body()?.tools?.forEach { dto ->
                    val existing = toolDao.getToolById(dto.id)
                    val entity = dto.toEntity(existing)
                    if (entity != null) {
                        toolDao.insert(entity)
                    }
                }
                true
            } else {
                loadFromAssets()
            }
        } catch (e: Exception) {
            loadFromAssets()
        }
    }

    override suspend fun fetchStarsForRepo(repoUrl: String): Int? =
        withContext(Dispatchers.IO) {
            try {
                val (owner, repo) = parseOwnerRepo(repoUrl) ?: return@withContext null
                githubClient.api
                    .getRepo(owner, repo)
                    .body()
                    ?.stargazers_count
            } catch (e: Exception) {
                null
            }
        }

    private fun parseOwnerRepo(raw: String): Pair<String, String>? {
        val s = raw.removeSuffix("/").removeSuffix(".git")
        val parts = s.substringAfter("github.com/", s).split("/")
        return if (parts.size >= 2) parts[0] to parts[1] else null
    }

    private suspend fun loadFromAssets(): Boolean =
        withContext(Dispatchers.IO) {
            try {
                val text = BufferedReader(
                    InputStreamReader(appContext.assets.open(assetsFileName))
                ).use { it.readText() }

                val moshi = com.squareup.moshi.Moshi.Builder()
                    .addLast(
                        com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory()
                    )
                    .build()

                val dto = moshi
                    .adapter(MetadataDto::class.java)
                    .fromJson(text)

                dto?.tools?.forEach {
                    val existing = toolDao.getToolById(it.id)
                    val entity = it.toEntity(existing)
                    if (entity != null) {
                        toolDao.insert(entity)
                    }
                }
                true
            } catch (e: Exception) {
                false
            }
        }

    private fun ToolDto.toEntity(existing: ToolEntity?): ToolEntity? {
        if (id.isBlank()) return null
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
            version = version,
            updatedAt = updatedAt ?: 0L,
            publishedAt = publishedAt,
            isFavorite = existing?.isFavorite ?: false
        )
    }

    override suspend fun getToolDetails(id: String): ToolDetails? {
        val tool = toolDao.getToolById(id) ?: return null
        val readme = metadataClient.fetchReadme(id).body().orEmpty()

        return ToolDetails(
            id = tool.id,
            title = tool.name,
            description = tool.description,
            readme = readme,
            installCommands = tool.installCommand ?: "",
            repoUrl = tool.repoUrl
        )
    }
}
