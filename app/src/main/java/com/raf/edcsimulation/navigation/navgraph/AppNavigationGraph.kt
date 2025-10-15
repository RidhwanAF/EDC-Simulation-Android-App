package com.raf.edcsimulation.navigation.navgraph

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.raf.edcsimulation.auth.presentation.screens.LoginScreen
import com.raf.edcsimulation.auth.presentation.screens.RegisterScreen
import com.raf.edcsimulation.auth.presentation.viewmodel.AuthViewModel
import com.raf.edcsimulation.core.domain.model.AppSettings
import com.raf.edcsimulation.navigation.routes.MainRoutes
import com.raf.edcsimulation.utils.sharedViewModel
import com.raf.edcsimulation.viewmodel.AppViewModel

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun AppNavigationGraph(
    navController: NavHostController,
    startDestination: MainRoutes,
    appViewModel: AppViewModel,
    appSettings: AppSettings
) {
    SharedTransitionLayout(
        modifier = Modifier.fillMaxSize()
    ) {
        NavHost(
            navController = navController,
            startDestination = startDestination
        ) {
            navigation<MainRoutes.Auth>(
                startDestination = MainRoutes.Login,
            ) {
                composable<MainRoutes.Login> {
                    val authViewModel = it.sharedViewModel<AuthViewModel>(navController)

                    LoginScreen(
                        viewModel = authViewModel,
                        animatedContentScope = this@composable,
                        onNavigateToRegister = {
                            navController.navigate(MainRoutes.Register) {
                                launchSingleTop = true
                            }
                        },
                        onLoginSuccess = {
                            navController.navigate(MainRoutes.MainMenu) {
                                launchSingleTop = true
                                popUpTo(MainRoutes.Login) {
                                    inclusive = true
                                }
                            }
                        }
                    )
                }
                composable<MainRoutes.Register> {
                    val authViewModel = it.sharedViewModel<AuthViewModel>(navController)

                    RegisterScreen(
                        viewModel = authViewModel,
                        animatedContentScope = this@composable,
                        onNavigateToLogin = {
                            navController.navigate(MainRoutes.Login) {
                                launchSingleTop = true
                                popUpTo(MainRoutes.Register) {
                                    inclusive = true
                                }
                            }
                        }
                    )
                }
            }

            navigation<MainRoutes.MainMenu>(
                startDestination = MainRoutes.Sale,
            ) {
                composable<MainRoutes.Sale> {
                    SaleNavigationGraph(
                        appViewModel = appViewModel,
                        appSettings = appSettings
                    )
                }
            }
        }
    }
}