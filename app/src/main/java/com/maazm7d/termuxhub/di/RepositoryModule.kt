package com.maazm7d.termuxhub.di

import android.content.Context
import com.maazm7d.termuxhub.data.local.ToolDao
import com.maazm7d.termuxhub.data.remote.MetadataClient
import com.maazm7d.termuxhub.data.repository.ToolRepository
import com.maazm7d.termuxhub.data.repository.ToolRepositoryImpl
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
        dao: ToolDao,
        api: MetadataClient,
        context: Context
    ): ToolRepository = ToolRepositoryImpl(dao, api, context)
}