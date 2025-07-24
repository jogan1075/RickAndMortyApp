package com.jmc.rickandmortyapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.jmc.details.ui.DetailScreenView
import com.jmc.home.ui.HomeScreenView
import com.jmc.rickandmortyapp.SplashScreen

@Composable
fun AppNavHost(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "splash"
    ) {
        composable("splash") {
            SplashScreen(
                onTimeout = {
                    navController.navigate("home") {
                        popUpTo("splash") { inclusive = true }
                    }
                }
            )
        }

        // Home feature (desde el módulo feature-home)
        composable("home") {
            HomeScreenView(navController = navController) // hiltViewModel() interno
        }

        composable(
            route = "detail/{characterId}",
            arguments = listOf(navArgument("characterId") { type = NavType.IntType })
        ) {
            DetailScreenView(onBack = { navController.popBackStack() })
        }
    }
}