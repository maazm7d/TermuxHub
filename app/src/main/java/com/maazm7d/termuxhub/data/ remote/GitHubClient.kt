package com.maazm7d.termuxhub.data.remote

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import com.squareup.moshi.Moshi
import java.util.concurrent.TimeUnit

class GitHubClient private constructor(val api: GitHubApi) {
    companion object {
        fun create(moshi: Moshi, okHttpClient: OkHttpClient): GitHubClient {
            val retrofit = Retrofit.Builder()
                .baseUrl("https://api.github.com/")
                .client(okHttpClient)
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .build()
            val api = retrofit.create(GitHubApi::class.java)
            return GitHubClient(api)
        }
    }
}
