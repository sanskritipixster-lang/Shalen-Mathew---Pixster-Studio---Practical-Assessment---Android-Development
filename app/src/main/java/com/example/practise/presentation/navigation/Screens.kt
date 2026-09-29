package com.example.practise.presentation.navigation

sealed class Screens(val route: String) {

    object SplashScreen: Screens("Splash")
    object DashboardScreen: Screens("Dashboard")
    object HomeScreen: Screens("Home")
}