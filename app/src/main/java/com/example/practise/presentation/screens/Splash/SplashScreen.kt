package com.example.practise.presentation.screens.Splash

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.practise.R
import com.example.practise.data.UserPreferencesManager
import com.example.practise.presentation.navigation.Screens
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun SplashScreen(navHost: NavHostController, paddingValues: PaddingValues){
    val context = LocalContext.current
    val preferencesManager = UserPreferencesManager(context)

    Box(modifier = Modifier
        .fillMaxSize()
        .padding(paddingValues)
        .background(color = Color.White),
        contentAlignment = Alignment.Center,
    ){

        AsyncImage(model = R.drawable.splash,
            contentDescription = "Splash Image")

        LaunchedEffect(Unit) {
            delay(2000.milliseconds)
            val isStarted = preferencesManager.isGetStartedClicked.first()
            if (isStarted) {
                navHost.navigate(Screens.HomeScreen.route) {
                    popUpTo(Screens.SplashScreen.route) { inclusive = true }
                }
            } else {
                navHost.navigate(Screens.DashboardScreen.route) {
                    popUpTo(Screens.SplashScreen.route) { inclusive = true }
                }
            }
        }
    }
}
