package com.maazm7d.termuxhub.di

import android.content.Context
import com.maazm7d.termuxhub.data.source.local.LocalDataSource
import com.maazm7d.termuxhub.data.source.remote.RemoteDataSource
import com.maazm7d.termuxhub.domain.repository.ToolRepository
import com.maazm7d.termuxhub.data.repository.ToolRepositoryImpl
import com.maazm7d.termuxhub.domain.repository.HallOfFameRepository
import com.maazm7d.termuxhub.data.repository.HallOfFameRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideToolRepository(
        localDataSource: LocalDataSource,
        remoteDataSource: RemoteDataSource
    ): ToolRepository = ToolRepositoryImpl(
        localDataSource = localDataSource,
        remoteDataSource = remoteDataSource
    )

    @Provides
    @Singleton
    fun provideHallOfFameRepository(
        localDataSource: LocalDataSource,
        remoteDataSource: RemoteDataSource
    ): HallOfFameRepository = HallOfFameRepositoryImpl(
        localDataSource = localDataSource,
        remoteDataSource = remoteDataSource
    )
}
