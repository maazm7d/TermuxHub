package com.maazm7d.termuxhub.data.remote

import com.maazm7d.termuxhub.data.remote.dto.MetadataDto
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.Response
import java.util.concurrent.TimeUnit

class MetadataClient(private val apiService: ApiService) {

    suspend fun fetchMetadata(): Response<MetadataDto> {
        return apiService.getMetadata()
    }

    companion object {
        // Replace this with your GitHub raw base URL, for example:
        // "https://raw.githubusercontent.com/<username>/<repo>/main/"
        private const val METADATA_REMOTE_BASE_URL = "https://raw.githubusercontent.com/yourusername/yourrepo/main/"

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