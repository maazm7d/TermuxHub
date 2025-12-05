package com.maazm7d.termuxhub.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.maazm7d.termuxhub.data.local.AppDatabase
import com.maazm7d.termuxhub.data.local.ToolDao
import com.maazm7d.termuxhub.data.remote.ApiService
import com.maazm7d.termuxhub.data.remote.MetadataClient
import com.maazm7d.termuxhub.data.repository.ToolRepository
import com.maazm7d.termuxhub.data.repository.ToolRepositoryImpl
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

/**
 * Provides DB, DAO, Retrofit, MetadataClient and binds ToolRepositoryImpl -> ToolRepository.
 * Edit METADATA_BASE_URL below to your GitHub raw base URL when ready.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    // Binds: ToolRepositoryImpl as ToolRepository
    @Binds
    @Singleton
    abstract fun bindToolRepository(impl: ToolRepositoryImpl): ToolRepository

    companion object {
        private const val METADATA_BASE_URL = "https://raw.githubusercontent.com/yourusername/yourrepo/main/"

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
                .build()
        }

        @Provides
        @Singleton
        fun provideRetrofit(moshi: Moshi, okHttp: OkHttpClient): Retrofit {
            return Retrofit.Builder()
                .baseUrl(METADATA_BASE_URL)
                .client(okHttp)
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .build()
        }

        @Provides
        @Singleton
        fun provideApiService(retrofit: Retrofit): ApiService =
            retrofit.create(ApiService::class.java)

        @Provides
        @Singleton
        fun provideMetadataClient(apiService: ApiService): MetadataClient =
            MetadataClient(apiService)

        @Provides
        @Singleton
        fun provideDatabase(@ApplicationContext appContext: Context): AppDatabase {
            return Room.databaseBuilder(appContext, AppDatabase::class.java, "termuxhub_db")
                .fallbackToDestructiveMigration()
                .build()
        }

        @Provides
        fun provideToolDao(db: AppDatabase): ToolDao = db.toolDao()

        @Provides
        @Singleton
        fun provideAppContext(@ApplicationContext context: Context): Context = context
    }
}