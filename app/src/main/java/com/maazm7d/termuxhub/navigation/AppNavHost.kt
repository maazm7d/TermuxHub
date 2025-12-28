package com.maazm7d.termuxhub.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.maazm7d.termuxhub.ui.screens.about.AboutScreen
import com.maazm7d.termuxhub.ui.screens.details.ToolDetailScreen
import com.maazm7d.termuxhub.ui.screens.details.ToolDetailViewModel
import com.maazm7d.termuxhub.ui.screens.hall.HallOfFameScreen
import com.maazm7d.termuxhub.ui.screens.home.HomeScreen
import com.maazm7d.termuxhub.ui.screens.home.HomeViewModel
import com.maazm7d.termuxhub.ui.screens.saved.SavedScreen
import com.maazm7d.termuxhub.ui.screens.saved.SavedViewModel
import com.maazm7d.termuxhub.ui.screens.splash.SplashScreen
import com.maazm7d.termuxhub.ui.screens.whatsnew.WhatsNewScreen

/**
 * Single source of truth for navigation
 * Drawer-free, Play Store style bottom navigation
 * ALL animations are intentionally disabled
 */
@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Destinations.SPLASH,
        modifier = modifier
    ) {

        // ───────────────────────────
        // Splash (NO bottom bar)
        // ───────────────────────────
        composable(Destinations.SPLASH) {
            SplashScreen(
                onFinished = {
                    navController.navigate(Destinations.TOOLS) {
                        popUpTo(Destinations.SPLASH) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                }
            )
        }

        // ───────────────────────────
        // Bottom navigation destinations
        // ───────────────────────────

        composable(Destinations.TOOLS) {
            val vm: HomeViewModel = hiltViewModel()
            HomeScreen(
                viewModel = vm,
                onOpenDetails = { toolId ->
                    navController.navigate("${Destinations.DETAILS}/$toolId")
                }
            )
        }

        composable(Destinations.SAVED) {
    val vm: SavedViewModel = hiltViewModel()
    SavedScreen(
        viewModel = vm,
        onOpenDetails = { toolId ->
            navController.navigate("${Destinations.DETAILS}/$toolId")
        }
    )
        }
        composable(Destinations.HALL) {
            HallOfFameScreen()
        }

        composable(Destinations.WHATS_NEW) {
            WhatsNewScreen()
        }

        composable(Destinations.ABOUT) {
            AboutScreen()
        }

        // ───────────────────────────
        // Details screen (NO bottom bar)
        // ───────────────────────────
        composable("${Destinations.DETAILS}/{toolId}") { backStackEntry ->
            val vm: ToolDetailViewModel = hiltViewModel()
            ToolDetailScreen(
                toolId = backStackEntry.arguments?.getString("toolId").orEmpty(),
                viewModel = vm,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
