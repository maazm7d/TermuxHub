package com.maazm7d.termuxhub.ui.screens.home

import app.cash.turbine.test
import com.maazm7d.termuxhub.domain.model.Tool
import com.maazm7d.termuxhub.domain.usecase.GetStarsUseCase
import com.maazm7d.termuxhub.domain.usecase.GetToolsUseCase
import com.maazm7d.termuxhub.domain.usecase.RefreshToolsUseCase
import com.maazm7d.termuxhub.domain.usecase.ToggleFavoriteUseCase
import com.maazm7d.termuxhub.utils.UiState
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    private val getToolsUseCase: GetToolsUseCase = mockk()
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase = mockk()
    private val refreshToolsUseCase: RefreshToolsUseCase = mockk()
    private val getStarsUseCase: GetStarsUseCase = mockk()

    private lateinit var viewModel: HomeViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        coEvery { getToolsUseCase() } returns flowOf(emptyList())
        coEvery { getStarsUseCase() } returns emptyMap()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state is Loading`() = runTest {
        viewModel = HomeViewModel(getToolsUseCase, toggleFavoriteUseCase, refreshToolsUseCase, getStarsUseCase)
        assertTrue(viewModel.uiState.value is UiState.Loading)
    }

    @Test
    fun `when tools are loaded state becomes Success`() = runTest {
        val tools = listOf(
            Tool(id = "1", name = "Tool 1", description = "Desc 1", category = "Cat 1", updatedAt = 0L, isFavorite = false, thumbnail = null, installCommand = null, repoUrl = null, publishedAt = null)
        )
        coEvery { getToolsUseCase() } returns flowOf(tools)

        viewModel = HomeViewModel(getToolsUseCase, toggleFavoriteUseCase, refreshToolsUseCase, getStarsUseCase)

        viewModel.uiState.test {
            // Initial Loading
            assertTrue(awaitItem() is UiState.Loading)
            // Then Success
            val success = awaitItem() as UiState.Success
            assertEquals(tools, success.data.tools)
        }
    }
}

private fun assertEquals(expected: Any, actual: Any) {
    org.junit.Assert.assertEquals(expected, actual)
}
