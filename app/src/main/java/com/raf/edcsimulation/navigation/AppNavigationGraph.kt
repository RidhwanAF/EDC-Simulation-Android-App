package com.raf.edcsimulation.navigation

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

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun AppNavigationGraph(
    navController: NavHostController,
    startDestination: Routes,
) {
    SharedTransitionLayout(
        modifier = Modifier.fillMaxSize()
    ) {
        NavHost(
            navController = navController,
            startDestination = startDestination
        ) {
            navigation<Routes.Auth>(
                startDestination = Routes.Login,
            ) {
                composable<Routes.Login> {
                    LoginScreen(
                        animatedContentScope = this@composable,
                        onNavigateToRegister = {
                            navController.navigate(Routes.Register) {
                                launchSingleTop = true
                            }
                        },
                    )
                }
                composable<Routes.Register> {
                    RegisterScreen(
                        animatedContentScope = this@composable,
                        onNavigateToLogin = {
                            navController.navigate(Routes.Login) {
                                launchSingleTop = true
                                popUpTo(Routes.Register) {
                                    inclusive = true
                                }
                            }
                        }
                    )
                }
            }

            navigation<Routes.MainMenu>(
                startDestination = Routes.Sale,
            ) {
                composable<Routes.Sale> {
//                    SaleScreen()
                }
                composable<Routes.History> {
//                    HistoryScreen()
                }
                composable<Routes.Settlement> {
//                    SettlementScreen()
                }
            }
        }
    }
}