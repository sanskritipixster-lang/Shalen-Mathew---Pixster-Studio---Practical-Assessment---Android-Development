package com.example.practise.presentation.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.practise.data.UserPreferencesManager
import com.example.practise.presentation.screens.Dashboard.DashboardScreen
import com.example.practise.presentation.screens.Home.HomeScreen
import com.example.practise.presentation.screens.Splash.SplashScreen
import kotlinx.coroutines.launch


@Composable
fun AppNavigation(
    navHost: NavHostController,
    paddingValues: PaddingValues,
    startDestination: String = Screens.SplashScreen.route
){
    NavHost(navController = navHost, startDestination = startDestination) {

        composable(Screens.SplashScreen.route) {
            SplashScreen(navHost, paddingValues)
        }

        composable(Screens.DashboardScreen.route){
            val context = LocalContext.current
            val preferencesManager = remember { UserPreferencesManager(context) }
            val coroutineScope = rememberCoroutineScope()

            DashboardScreen(navHost, paddingValues, onGetStarted = {
                coroutineScope.launch {
                    preferencesManager.setGetStartedClicked(true)
                    navHost.navigate(Screens.HomeScreen.route) {
                        popUpTo(Screens.DashboardScreen.route) { inclusive = true }
                    }
                }
            })
        }

        composable(Screens.HomeScreen.route) {
            HomeScreen(navHost, paddingValues)
        }

    }
}
