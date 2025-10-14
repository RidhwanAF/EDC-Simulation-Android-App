package com.raf.edcsimulation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.raf.edcsimulation.core.ui.theme.EDCSimulationTheme
import com.raf.edcsimulation.navigation.AppNavigationGraph
import com.raf.edcsimulation.navigation.Routes
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            val startDestination = Routes.Auth

            EDCSimulationTheme(
                darkTheme = false,
                dynamicColor = false,
            ) {
                AppNavigationGraph(
                    navController = navController,
                    startDestination = startDestination,
                )
            }
        }
    }
}