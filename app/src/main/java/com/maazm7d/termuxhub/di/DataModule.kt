package com.maazm7d.termuxhub.di

import android.content.Context
import androidx.room.Room
import com.maazm7d.termuxhub.data.local.AppDatabase
import com.maazm7d.termuxhub.data.local.ToolDao
import com.maazm7d.termuxhub.data.remote.MetadataClient
import com.maazm7d.termuxhub.data.remote.GitHubClient
import com.maazm7d.termuxhub.data.remote.ApiService
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton
import java.util.concurrent.TimeUnit

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    private const val METADATA_BASE_URL = "https://raw.githubusercontent.com/maazm7d/TermuxHub/main/"

    @Provides
    @Singleton
    fun provideMoshi(): Moshi = Moshi.Builder()
        .addLast(KotlinJsonAdapterFactory())
        .build()

    @Provides
    @Singleton
    fun provideOkHttp(): OkHttpClient {
        val logger = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BASIC }
        return OkHttpClient.Builder()
            .addInterceptor(logger)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    // Retrofit for metadata
    @Provides
    @Singleton
    fun provideRetrofit(moshi: Moshi, okHttp: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(METADATA_BASE_URL)
            .client(okHttp)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService =
        retrofit.create(ApiService::class.java)

    @Provides
    @Singleton
    fun provideMetadataClient(apiService: ApiService): MetadataClient =
        MetadataClient(apiService)

    // GitHub client (uses same okHttp & moshi)
    @Provides
    @Singleton
    fun provideGitHubClient(moshi: Moshi, okHttp: OkHttpClient): GitHubClient =
        GitHubClient.create(moshi, okHttp)

    // --- Database ---
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext appContext: Context): AppDatabase =
        Room.databaseBuilder(appContext, AppDatabase::class.java, "termuxhub_db")
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    fun provideToolDao(db: AppDatabase): ToolDao = db.toolDao()

    @Provides
    @Singleton
    fun provideAppContext(@ApplicationContext context: Context): Context = context
}
