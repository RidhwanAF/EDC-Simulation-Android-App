package com.raf.edcsimulation.navigation.navgraph

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.raf.edcsimulation.card.presentation.CardMenuView
import com.raf.edcsimulation.core.domain.model.AppSettings
import com.raf.edcsimulation.core.presentation.components.LogoutDialog
import com.raf.edcsimulation.core.presentation.components.SettingsDialog
import com.raf.edcsimulation.navigation.listSaleBottomBarMenu
import com.raf.edcsimulation.navigation.routes.SaleRoutes
import com.raf.edcsimulation.navigation.routes.isOnThisRoute
import com.raf.edcsimulation.sale.presentation.screens.SaleScreen
import com.raf.edcsimulation.ui.SaleBottomBar
import com.raf.edcsimulation.ui.SaleDialogState
import com.raf.edcsimulation.viewmodel.AppViewModel

@OptIn(ExperimentalSharedTransitionApi::class, ExperimentalMaterial3Api::class)
@Composable
fun SharedTransitionScope.SaleNavigationGraph(
    modifier: Modifier = Modifier,
    appViewModel: AppViewModel = hiltViewModel(),
    appSettings: AppSettings = AppSettings(),
) {
    val navController = rememberNavController()
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentMenu =
        listSaleBottomBarMenu.find { saleRoute ->
            saleRoute.route.isOnThisRoute(
                currentBackStackEntry
            )
        }

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    var dialogState by rememberSaveable {
        mutableStateOf(SaleDialogState.NONE)
    }

    var processedCardNumber by rememberSaveable {
        mutableStateOf("")
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .fillMaxSize()
    ) {
        Scaffold(
            topBar = {
                if (dialogState == SaleDialogState.FORM) return@Scaffold
                TopAppBar(
                    title = {
                        Text(
                            text = currentMenu?.title
                                ?: stringResource(com.raf.edcsimulation.R.string.app_name),
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                    },
                    actions = {
                        if (currentMenu?.route != SaleRoutes.Sale) return@TopAppBar
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            AnimatedContent(
                                targetState = dialogState != SaleDialogState.SETTINGS,
                                contentAlignment = Alignment.Center,
                            ) { targetState ->
                                if (targetState) {
                                    IconButton(
                                        onClick = {
                                            dialogState = SaleDialogState.SETTINGS
                                        },
                                        modifier = Modifier
                                            .sharedElement(
                                                sharedContentState = rememberSharedContentState("settings-dialog-key"),
                                                animatedVisibilityScope = this
                                            )
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Settings,
                                            contentDescription = "Settings"
                                        )
                                    }
                                } else Spacer(modifier = Modifier.width(48.dp))
                            }
                            AnimatedContent(
                                targetState = dialogState != SaleDialogState.LOGOUT,
                                contentAlignment = Alignment.Center,
                            ) { targetState ->
                                if (targetState) {
                                    IconButton(
                                        onClick = {
                                            dialogState = SaleDialogState.LOGOUT
                                        },
                                        colors = IconButtonDefaults.iconButtonColors(
                                            contentColor = MaterialTheme.colorScheme.error
                                        ),
                                        modifier = Modifier
                                            .sharedElement(
                                                sharedContentState = rememberSharedContentState("logout-dialog-key"),
                                                animatedVisibilityScope = this
                                            )
                                    ) {
                                        Icon(
                                            imageVector = Icons.AutoMirrored.Filled.Logout,
                                            contentDescription = "Logout"
                                        )
                                    }
                                } else Spacer(modifier = Modifier.width(48.dp))
                            }
                        }
                    },
                    scrollBehavior = scrollBehavior
                )
            },
            bottomBar = {
                if (dialogState == SaleDialogState.FORM) return@Scaffold
                SaleBottomBar(
                    currentRoute = currentMenu?.route ?: SaleRoutes.Sale,
                    onNavigate = {
                        navController.navigate(it.route) {
                            launchSingleTop = true
                            popUpTo(it.route) {
                                inclusive = true
                            }
                        }
                    }
                )
            },
            modifier = Modifier
                .fillMaxSize()
                .nestedScroll(scrollBehavior.nestedScrollConnection)
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = SaleRoutes.MainMenu,
                modifier = Modifier
                    .fillMaxSize()
            ) {
                navigation<SaleRoutes.MainMenu>(
                    startDestination = SaleRoutes.Sale,
                ) {
                    composable<SaleRoutes.Sale> {
                        if (dialogState == SaleDialogState.FORM) {
                            SaleScreen(
                                paddingValues = innerPadding,
                                cardNumber = processedCardNumber,
                                onDismiss = {
                                    dialogState = SaleDialogState.NONE
                                    processedCardNumber = ""
                                }
                            )
                        } else {
                            CardMenuView(
                                paddingValues = innerPadding,
                                onCardProcessed = { cardNumber ->
                                    dialogState = SaleDialogState.FORM
                                    processedCardNumber = cardNumber
                                }
                            )
                        }
                    }
                    composable<SaleRoutes.History> {
                        Text("aa")
                    }
                    composable<SaleRoutes.Settlement> {
                        Text("bb")
                    }
                }
            }
        }

        /**
         * Dialog
         */
        SettingsDialog(
            isShown = dialogState == SaleDialogState.SETTINGS,
            onDismissRequest = {
                dialogState = SaleDialogState.NONE
            },
            currentAppSettings = appSettings,
            onAppSettingsChange = {
                appViewModel.setAppSettings(it)
            },
        )
        LogoutDialog(
            isShown = dialogState == SaleDialogState.LOGOUT,
            onDismissRequest = {
                dialogState = SaleDialogState.NONE
            },
            onLogout = {
                appViewModel.logout()
            }
        )
    }
}