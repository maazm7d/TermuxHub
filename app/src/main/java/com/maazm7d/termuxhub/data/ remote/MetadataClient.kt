package com.maazm7d.termuxhub.data.remote

import com.maazm7d.termuxhub.data.remote.dto.MetadataDto
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import java.util.concurrent.TimeUnit

class MetadataClient(private val apiService: ApiService) {

    suspend fun fetchMetadata(): Response<MetadataDto> {
        return apiService.getMetadata()
    }

    suspend fun fetchReadme(toolId: String): Response<String> {
    return apiService.getToolReadme(toolId)
    }

    companion object {
        private const val METADATA_REMOTE_BASE_URL =
            "https://raw.githubusercontent.com/maazm7d/TermuxHub/main/"

        fun create(): MetadataClient {
            val logger = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BASIC
            }

            val client = OkHttpClient.Builder()
                .addInterceptor(logger)
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build()

            val moshi = Moshi.Builder()
                .addLast(KotlinJsonAdapterFactory())
                .build()

            val retrofit = Retrofit.Builder()
                .baseUrl(METADATA_REMOTE_BASE_URL)
                .client(client)
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .build()

            val api = retrofit.create(ApiService::class.java)
            return MetadataClient(api)
        }
    }
}
