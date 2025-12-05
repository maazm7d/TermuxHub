package com.maazm7d.termuxhub.data.remote

import com.maazm7d.termuxhub.data.remote.dto.MetadataDto
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {
    // Single endpoint returning the main metadata JSON
    @GET("metadata/tools.json")
    suspend fun getMetadata(): Response<MetadataDto>
}