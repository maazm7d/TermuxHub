package com.maazm7d.termuxhub.data.repository

import android.content.Context
import com.maazm7d.termuxhub.data.local.ToolDao
import com.maazm7d.termuxhub.data.local.entities.ToolEntity
import com.maazm7d.termuxhub.data.remote.MetadataClient
import com.maazm7d.termuxhub.data.remote.GitHubClient
import com.maazm7d.termuxhub.data.remote.dto.MetadataDto
import com.maazm7d.termuxhub.data.remote.dto.ToolDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
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
                    body.tools.forEach { dto ->
                        val existing = toolDao.getToolById(dto.id)
                        val entity = dto.toEntity(existing)
                        if (entity != null) toolDao.insert(entity)
                    }
                    true
                } else loadFromAssets()
            } else loadFromAssets()
        } catch (ex: Exception) {
            ex.printStackTrace()
            loadFromAssets()
        }
    }

    /**
     * Try to fetch star count from GitHub API for a given repo url.
     * Accepts common forms:
     *  - https://github.com/owner/repo
     *  - https://github.com/owner/repo/
     *  - git@github.com:owner/repo.git
     *  - owner/repo
     * Returns null if parse or network error.
     */
    override suspend fun fetchStarsForRepo(repoUrl: String): Int? = withContext(Dispatchers.IO) {
        try {
            val parts = parseOwnerRepo(repoUrl) ?: return@withContext null
            val (owner, repo) = parts
            val resp = githubClient.api.getRepo(owner, repo)
            if (resp.isSuccessful) {
                resp.body()?.stargazers_count
            } else null
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun parseOwnerRepo(raw: String): Pair<String, String>? {
        var s = raw.trim()
        if (s.isEmpty()) return null

        // common https urls
        if (s.startsWith("https://") || s.startsWith("http://")) {
            // strip trailing slashes and .git
            s = s.removeSuffix("/")
            s = s.removeSuffix(".git")
            val segments = s.split("/")
            // last two segments expected: owner/repo
            if (segments.size >= 2) {
                val owner = segments[segments.size - 2]
                val repo = segments.last()
                return owner to repo
            }
        }

        // git@github.com:owner/repo.git
        if (s.startsWith("git@")) {
            val afterColon = s.substringAfter(':')
            val cleaned = afterColon.removeSuffix(".git").trim()
            val seg = cleaned.split("/")
            if (seg.size == 2) return seg[0] to seg[1]
        }

        // owner/repo
        if (!s.contains("://") && s.contains("/")) {
            val seg = s.removeSuffix(".git").split("/")
            if (seg.size == 2) return seg[0] to seg[1]
        }

        return null
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
            dto?.tools?.forEach { t ->
                val existing = toolDao.getToolById(t.id)
                val entity = t.toEntity(existing)
                if (entity != null) toolDao.insert(entity)
            }
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    private fun ToolDto.toEntity(existing: ToolEntity? = null): ToolEntity? {
        if (id.isBlank() || name.isBlank()) return null
        return ToolEntity(
            id = id,
            name = name,
            description = description ?: "",
            category = category ?: "Uncategorized",
            installCommand = install,
            repoUrl = repo,
            author = author ?: "", 
            thumbnail = thumbnail,
            version = version,
            updatedAt = updatedAt ?: 0L,
            isFavorite = existing?.isFavorite ?: false,
            publishedAt = publishedAt
        )
    }
}
