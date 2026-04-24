package com.maazm7d.termuxhub.data.repository

import com.maazm7d.termuxhub.data.mapper.toDomain
import com.maazm7d.termuxhub.data.mapper.toEntity
import com.maazm7d.termuxhub.data.source.local.LocalDataSource
import com.maazm7d.termuxhub.data.source.remote.RemoteDataSource
import com.maazm7d.termuxhub.domain.model.HallOfFameMember
import com.maazm7d.termuxhub.domain.repository.HallOfFameRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import timber.log.Timber
import javax.inject.Inject

class HallOfFameRepositoryImpl @Inject constructor(
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource
) : HallOfFameRepository {

    override fun observeMembers(): Flow<List<HallOfFameMember>> =
        localDataSource.getAllHallOfFameFlow().map { entities ->
            entities.map { it.toDomain() }
        }

    override suspend fun refresh(): Result<Unit> = runCatching {
        val indexResponse = remoteDataSource.fetchHallOfFameIndex()
        if (!indexResponse.isSuccessful) {
            throw Exception("Failed to fetch index: ${indexResponse.code()}")
        }
        val members = indexResponse.body()?.members ?: throw Exception("Empty index")

        val resolvedMembers = members.map { dto ->
            kotlinx.coroutines.coroutineScope {
                async {
                    val markdown = fetchMarkdownSafely(dto.id)
                    dto.toDomain(markdown)
                }
            }
        }.awaitAll()

        localDataSource.clearHallOfFame()
        localDataSource.insertHallOfFameMembers(resolvedMembers.map { it.toEntity() })

        Timber.i("Hall of Fame refreshed successfully")
    }.onFailure { error ->
        Timber.e(error, "Hall of Fame refresh failed")
    }

    private suspend fun fetchMarkdownSafely(id: String): String {
        return try {
            remoteDataSource.fetchHallOfFameMarkdown(id).body() ?: ""
        } catch (e: Exception) {
            Timber.w(e, "Failed to fetch markdown for $id")
            ""
        }
    }
}
