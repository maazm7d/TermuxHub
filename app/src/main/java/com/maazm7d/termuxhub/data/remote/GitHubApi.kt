package com.maazm7d.termuxhub.data.remote

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface GitHubApi {
    @GET("repos/{owner}/{repo}")
    suspend fun getRepo(
        @Path("owner") owner: String,
        @Path("repo") repo: String
    ): Response<GitHubRepoDto>
}

@JsonClass(generateAdapter = true)
data class GitHubRepoDto(
    @Json(name = "stargazers_count") val stargazers_count: Int = 0
)
