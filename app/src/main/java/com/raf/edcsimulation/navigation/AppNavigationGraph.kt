package com.raf.edcsimulation.navigation

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.raf.edcsimulation.AppViewModel
import com.raf.edcsimulation.auth.presentation.screens.LoginScreen
import com.raf.edcsimulation.auth.presentation.screens.RegisterScreen
import com.raf.edcsimulation.auth.presentation.viewmodel.AuthViewModel
import com.raf.edcsimulation.utils.sharedViewModel

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun AppNavigationGraph(
    navController: NavHostController,
    startDestination: Routes,
    appViewModel: AppViewModel,
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
                    val authViewModel = it.sharedViewModel<AuthViewModel>(navController)

                    LoginScreen(
                        viewModel = authViewModel,
                        animatedContentScope = this@composable,
                        onNavigateToRegister = {
                            navController.navigate(Routes.Register) {
                                launchSingleTop = true
                            }
                        },
                        onLoginSuccess = {
                            navController.navigate(Routes.MainMenu) {
                                launchSingleTop = true
                                popUpTo(Routes.Login) {
                                    inclusive = true
                                }
                            }
                        }
                    )
                }
                composable<Routes.Register> {
                    val authViewModel = it.sharedViewModel<AuthViewModel>(navController)

                    RegisterScreen(
                        viewModel = authViewModel,
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
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Text(
                            text = "Sale"
                        )
                        Button(
                            onClick = appViewModel::logout
                        ) {
                            Text(text = "Logout")
                        }
                    }
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