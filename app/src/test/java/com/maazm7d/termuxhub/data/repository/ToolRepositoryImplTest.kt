package com.maazm7d.termuxhub.data.repository

import com.maazm7d.termuxhub.data.local.entities.ToolEntity
import com.maazm7d.termuxhub.data.source.local.LocalDataSource
import com.maazm7d.termuxhub.data.source.remote.RemoteDataSource
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class ToolRepositoryImplTest {

    private lateinit var repository: ToolRepositoryImpl
    private val localDataSource: LocalDataSource = mockk(relaxed = true)
    private val remoteDataSource: RemoteDataSource = mockk(relaxed = true)

    @Before
    fun setup() {
        repository = ToolRepositoryImpl(localDataSource, remoteDataSource)
    }

    @Test
    fun `observeAll returns tools from local data source`() = runTest {
        val tools = listOf(
            ToolEntity(
                id = "1",
                name = "Tool 1",
                description = "Desc 1",
                category = "Cat 1",
                updatedAt = 0L,
                installCommand = null,
                repoUrl = null,
                thumbnail = null
            )
        )
        coEvery { localDataSource.getAllToolsFlow() } returns flowOf(tools)

        repository.observeAll().collect { result ->
            assertEquals(tools, result)
        }
    }

    @Test
    fun `setFavorite updates tool in local data source`() = runTest {
        val toolId = "1"
        val tool = ToolEntity(
            id = toolId,
            name = "Tool 1",
            description = "Desc 1",
            category = "Cat 1",
            updatedAt = 0L,
            isFavorite = false,
            installCommand = null,
            repoUrl = null,
            thumbnail = null
        )
        coEvery { localDataSource.getToolById(toolId) } returns tool

        repository.setFavorite(toolId, true)

        coVerify { localDataSource.updateTool(tool.copy(isFavorite = true)) }
    }
}
