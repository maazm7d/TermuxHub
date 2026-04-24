package com.maazm7d.termuxhub.data.source.remote

import com.maazm7d.termuxhub.data.remote.ApiService
import com.maazm7d.termuxhub.data.remote.dto.*
import retrofit2.Response
import javax.inject.Inject

interface RemoteDataSource {
    suspend fun fetchMetadata(): Response<MetadataDto>
    suspend fun fetchReadme(toolId: String): Response<String>
    suspend fun fetchHallOfFameIndex(): Response<HallOfFameIndexDto>
    suspend fun fetchHallOfFameMarkdown(id: String): Response<String>
    suspend fun fetchStars(): Response<StarsDto>
    suspend fun fetchRepoStats(): Response<RepoStatsRootDto>
}

class RemoteDataSourceImpl @Inject constructor(
    private val apiService: ApiService
) : RemoteDataSource {
    override suspend fun fetchMetadata(): Response<MetadataDto> = apiService.getMetadata()
    override suspend fun fetchReadme(toolId: String): Response<String> = apiService.getToolReadme(toolId)
    override suspend fun fetchHallOfFameIndex(): Response<HallOfFameIndexDto> = apiService.getHallOfFameIndex()
    override suspend fun fetchHallOfFameMarkdown(id: String): Response<String> = apiService.getHallOfFameMarkdown(id)
    override suspend fun fetchStars(): Response<StarsDto> = apiService.getStars()
    override suspend fun fetchRepoStats(): Response<RepoStatsRootDto> = apiService.getRepoStats()
}
