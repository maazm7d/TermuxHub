package com.maazm7d.termuxhub.data.remote

import com.maazm7d.termuxhub.data.remote.dto.MetadataDto
import retrofit2.Response

class MetadataClient(private val apiService: ApiService) {

    suspend fun fetchMetadata(): Response<MetadataDto> =
        apiService.getMetadata()

    suspend fun fetchReadme(toolId: String): Response<String> =
        apiService.getToolReadme(toolId)

    suspend fun fetchHallOfFameIndex() =
        apiService.getHallOfFameIndex()

    suspend fun fetchHallOfFameMarkdown(id: String) =
        apiService.getHallOfFameMarkdown(id)

    suspend fun fetchStars() =
        apiService.getStars()

    suspend fun fetchRepoStats() =
        apiService.getRepoStats()
}
