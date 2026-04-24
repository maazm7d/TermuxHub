package com.maazm7d.termuxhub.di

import android.content.Context
import androidx.room.Room
import com.maazm7d.termuxhub.data.local.AppDatabase
import com.maazm7d.termuxhub.data.local.ToolDao
import com.maazm7d.termuxhub.data.local.HallOfFameDao
import com.maazm7d.termuxhub.data.remote.ApiService
import com.maazm7d.termuxhub.data.remote.MetadataClient
import com.maazm7d.termuxhub.data.source.local.LocalDataSource
import com.maazm7d.termuxhub.data.source.local.LocalDataSourceImpl
import com.maazm7d.termuxhub.data.source.remote.RemoteDataSource
import com.maazm7d.termuxhub.data.source.remote.RemoteDataSourceImpl
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
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    private const val METADATA_BASE_URL = com.maazm7d.termuxhub.utils.Constants.METADATA_BASE_URL

    @Provides
    @Singleton
    fun provideMoshi(): Moshi = Moshi.Builder()
        .addLast(KotlinJsonAdapterFactory())
        .build()

    @Provides
    @Singleton
    fun provideOkHttp(): OkHttpClient {
        val logger = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BASIC
        }
        return OkHttpClient.Builder()
            .addInterceptor(logger)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(
        moshi: Moshi,
        okHttp: OkHttpClient
    ): Retrofit = Retrofit.Builder()
        .baseUrl(METADATA_BASE_URL)
        .client(okHttp)
        .addConverterFactory(ScalarsConverterFactory.create())
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

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext appContext: Context): AppDatabase =
        Room.databaseBuilder(appContext, AppDatabase::class.java, "termuxhub_db")
            .fallbackToDestructiveMigration(true)
            .build()

    @Provides
    fun provideToolDao(db: AppDatabase): ToolDao = db.toolDao()

    @Provides
    fun provideHallOfFameDao(db: AppDatabase): HallOfFameDao = db.hallOfFameDao()

    @Provides
    @Singleton
    fun provideAppContext(@ApplicationContext context: Context): Context = context

    @Provides
    @Singleton
    fun provideRemoteDataSource(apiService: ApiService): RemoteDataSource =
        RemoteDataSourceImpl(apiService)

    @Provides
    @Singleton
    fun provideLocalDataSource(
        toolDao: ToolDao,
        hallOfFameDao: HallOfFameDao,
        @ApplicationContext context: Context,
        moshi: Moshi
    ): LocalDataSource = LocalDataSourceImpl(toolDao, hallOfFameDao, context, moshi)
}
