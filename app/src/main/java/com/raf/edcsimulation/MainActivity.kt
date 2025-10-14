package com.raf.edcsimulation

import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.raf.edcsimulation.core.presentation.theme.EDCSimulationTheme
import com.raf.edcsimulation.navigation.AppNavigationGraph
import com.raf.edcsimulation.navigation.Routes
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private lateinit var appViewModel: AppViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Splash Screen
        val content: View = findViewById(android.R.id.content)
        content.viewTreeObserver.addOnPreDrawListener(
            object : ViewTreeObserver.OnPreDrawListener {
                override fun onPreDraw(): Boolean {
                    return if (appViewModel.appState.value is AppState.Loaded) {
                        content.viewTreeObserver.removeOnPreDrawListener(this)
                        true
                    } else false
                }
            }
        )

        setContent {
            appViewModel = hiltViewModel()
            val appState by appViewModel.appState.collectAsStateWithLifecycle()
            val uiState by appViewModel.uiState.collectAsStateWithLifecycle()

            val navController = rememberNavController()
            val startDestination = remember(appState) {
                when (appState) {
                    is AppState.Loaded -> {
                        if ((appState as AppState.Loaded).isLoggedIn) Routes.MainMenu else Routes.Auth
                    }

                    else -> Routes.Auth
                }
            }

            EDCSimulationTheme(
                darkTheme = uiState.isDarkTheme,
                dynamicColor = uiState.isDynamicColor,
            ) {
                AppNavigationGraph(
                    navController = navController,
                    startDestination = startDestination,
                    appViewModel = appViewModel
                )
            }
        }
    }
}