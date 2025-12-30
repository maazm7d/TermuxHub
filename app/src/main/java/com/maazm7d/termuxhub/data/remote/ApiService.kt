package com.maazm7d.termuxhub.data.remote

import com.maazm7d.termuxhub.data.remote.dto.MetadataDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import com.maazm7d.termuxhub.data.remote.dto.HallOfFameIndexDto

interface ApiService {
    @GET("metadata/metadata.json")
    suspend fun getMetadata(): Response<MetadataDto>

    @GET("metadata/readme/{id}.md")
    suspend fun getToolReadme(
        @Path("id") toolId: String
    ): Response<String>

@GET("metadata/halloffame/index.json")
suspend fun getHallOfFameIndex(): Response<HallOfFameIndexDto>

@GET("metadata/halloffame/{id}.md")
suspend fun getHallOfFameMarkdown(
    @Path("id") id: String
): Response<String>

}
