package com.maazm7d.termuxhub.di

import com.maazm7d.termuxhub.domain.repository.HallOfFameRepository
import com.maazm7d.termuxhub.domain.repository.ToolRepository
import com.maazm7d.termuxhub.domain.usecase.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    @Singleton
    fun provideGetToolsUseCase(repository: ToolRepository): GetToolsUseCase =
        GetToolsUseCase(repository)

    @Provides
    @Singleton
    fun provideGetSavedToolsUseCase(repository: ToolRepository): GetSavedToolsUseCase =
        GetSavedToolsUseCase(repository)

    @Provides
    @Singleton
    fun provideToggleFavoriteUseCase(repository: ToolRepository): ToggleFavoriteUseCase =
        ToggleFavoriteUseCase(repository)

    @Provides
    @Singleton
    fun provideGetToolDetailsUseCase(repository: ToolRepository): GetToolDetailsUseCase =
        GetToolDetailsUseCase(repository)

    @Provides
    @Singleton
    fun provideRefreshToolsUseCase(repository: ToolRepository): RefreshToolsUseCase =
        RefreshToolsUseCase(repository)

    @Provides
    @Singleton
    fun provideGetStarsUseCase(repository: ToolRepository): GetStarsUseCase =
        GetStarsUseCase(repository)

    @Provides
    @Singleton
    fun provideGetHallOfFameMembersUseCase(repository: HallOfFameRepository): GetHallOfFameMembersUseCase =
        GetHallOfFameMembersUseCase(repository)
}
