package com.maazm7d.termuxhub.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.maazm7d.termuxhub.ui.screens.blogs.BlogsScreen
import com.maazm7d.termuxhub.ui.screens.details.ToolDetailScreen
import com.maazm7d.termuxhub.ui.screens.details.ToolDetailViewModel
import com.maazm7d.termuxhub.ui.screens.home.HomeScreen
import com.maazm7d.termuxhub.ui.screens.home.HomeViewModel
import com.maazm7d.termuxhub.ui.screens.saved.SavedScreen
import com.maazm7d.termuxhub.ui.screens.saved.SavedViewModel
import com.maazm7d.termuxhub.ui.screens.settings.SettingsScreen
import com.maazm7d.termuxhub.ui.screens.splash.SplashScreen

object Destinations {
    const val SPLASH = "splash"
    const val HOME = "home"
    const val DETAILS = "details"
    const val SAVED = "saved"
    const val BLOGS = "blogs"
    const val SETTINGS = "settings"
}

@Composable
fun TermuxHubAppNav(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    AppNavHost(navController = navController, modifier = modifier)
}

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

        composable(Destinations.SPLASH) {
            SplashScreen(onFinished = {
                navController.navigate(Destinations.HOME) {
                    popUpTo(Destinations.SPLASH) { inclusive = true }
                }
            })
        }

        composable(Destinations.HOME) {
            val vm: HomeViewModel = hiltViewModel()
            HomeScreen(
                viewModel = vm,
                onOpenDetails = { id -> navController.navigate("${Destinations.DETAILS}/$id") },
                onOpenSaved = { navController.navigate(Destinations.SAVED) },
                onOpenSettings = { navController.navigate(Destinations.SETTINGS) }
            )
        }

        composable("${Destinations.DETAILS}/{toolId}") { backStackEntry ->
            val toolId = backStackEntry.arguments?.getString("toolId") ?: ""
            val vm: ToolDetailViewModel = hiltViewModel()
            ToolDetailScreen(toolId = toolId, viewModel = vm, onBack = { navController.popBackStack() })
        }

        composable(Destinations.SAVED) {
            val vm: SavedViewModel = hiltViewModel()
            SavedScreen(viewModel = vm, onBack = { navController.popBackStack() })
        }

        composable(Destinations.BLOGS) { BlogsScreen() }
        composable(Destinations.SETTINGS) { SettingsScreen(onBack = { navController.popBackStack() }) }
    }
}
