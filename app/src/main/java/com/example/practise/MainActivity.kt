package com.example.practise

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.example.practise.presentation.navigation.AppNavigation
import com.example.practise.ui.theme.PractiseTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PractiseTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

                    val navHost = rememberNavController()

                    AppNavigation(
                        navHost = navHost,
                        paddingValues = innerPadding,
                    )


                }
            }
        }
    }
}

