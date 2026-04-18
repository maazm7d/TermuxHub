package com.maazm7d.termuxhub.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
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
import com.maazm7d.termuxhub.ui.screens.splash.SplashViewModel
import com.maazm7d.termuxhub.ui.screens.whatsnew.WhatsNewScreen

@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Destinations.SPLASH,
        modifier = modifier,
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None },
        popEnterTransition = { EnterTransition.None },
        popExitTransition = { ExitTransition.None }
    ) {

        composable(Destinations.SPLASH) {
            val vm: SplashViewModel = hiltViewModel()
            SplashScreen(
                viewModel = vm,
                onFinished = {
                    navController.navigate(Destinations.TOOLS) {
                        popUpTo(Destinations.SPLASH) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }

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

        composable(Destinations.HALL) { HallOfFameScreen() }
        composable(Destinations.WHATS_NEW) { WhatsNewScreen() }
        composable(Destinations.ABOUT) { AboutScreen() }

        composable("${Destinations.DETAILS}/{toolId}") {
            val vm: ToolDetailViewModel = hiltViewModel()
            ToolDetailScreen(
                toolId = it.arguments?.getString("toolId").orEmpty(),
                viewModel = vm,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
