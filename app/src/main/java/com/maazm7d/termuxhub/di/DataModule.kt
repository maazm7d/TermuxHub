package com.maazm7d.termuxhub.di

import android.content.Context
import androidx.room.Room
import com.maazm7d.termuxhub.data.local.AppDatabase
import com.maazm7d.termuxhub.data.local.ToolDao
import com.maazm7d.termuxhub.data.remote.ApiService
import com.maazm7d.termuxhub.data.remote.MetadataClient
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

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    private const val METADATA_BASE_URL = "https://raw.githubusercontent.com/maazm7d/TermuxHub/main/"

    // --- Moshi ---
    @Provides
    @Singleton
    fun provideMoshi(): Moshi = Moshi.Builder()
        .addLast(KotlinJsonAdapterFactory())
        .build()

    // --- OkHttp ---
    @Provides
    @Singleton
    fun provideOkHttp(): OkHttpClient {
        val logger = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BASIC }
        return OkHttpClient.Builder()
            .addInterceptor(logger)
            .build()
    }

    // --- Retrofit ---
    @Provides
    @Singleton
    fun provideRetrofit(moshi: Moshi, okHttp: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(METADATA_BASE_URL)
            .client(okHttp)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()

    // --- ApiService ---
    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService =
        retrofit.create(ApiService::class.java)

    // --- MetadataClient ---
    @Provides
    @Singleton
    fun provideMetadataClient(apiService: ApiService): MetadataClient =
        MetadataClient(apiService)

    // --- Database ---
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext appContext: Context): AppDatabase =
        Room.databaseBuilder(appContext, AppDatabase::class.java, "termuxhub_db")
            .fallbackToDestructiveMigration()
            .build()

    // --- ToolDao ---
    @Provides
    fun provideToolDao(db: AppDatabase): ToolDao = db.toolDao()

    // --- Application Context ---
    @Provides
    @Singleton
    fun provideAppContext(@ApplicationContext context: Context): Context = context
}
