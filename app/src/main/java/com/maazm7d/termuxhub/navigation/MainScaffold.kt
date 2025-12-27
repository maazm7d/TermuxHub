package com.maazm7d.termuxhub.navigation

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch
import com.maazm7d.termuxhub.ui.components.AppDrawer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScaffold(
    navController: NavHostController,
    content: @Composable () -> Unit
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = !drawerState.isAnimationRunning,
        drawerContent = {
            ModalDrawerSheet {
                AppDrawer { action ->
                    scope.launch {
                        drawerState.close()
                        when (action) {
                            "saved" -> navController.navigate("saved")
                            "about" -> navController.navigate("about")
                        }
                    }
                }
            }
        }
    ) {
        CompositionLocalProvider(
            LocalDrawerState provides drawerState
        ) {
            content()
        }
    }
}
