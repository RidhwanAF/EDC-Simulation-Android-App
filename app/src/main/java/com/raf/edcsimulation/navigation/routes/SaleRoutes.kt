package com.raf.edcsimulation.navigation.routes

import androidx.navigation.NavBackStackEntry
import com.raf.edcsimulation.BuildConfig
import kotlinx.serialization.Serializable

@Serializable
sealed class SaleRoutes {
    @Serializable
    data object MainMenu : SaleRoutes() // Parent

    @Serializable
    data object Sale : SaleRoutes()

    @Serializable
    data object History : SaleRoutes()

    @Serializable
    data object Settlement : SaleRoutes()
}

fun SaleRoutes.isOnThisRoute(currentBackStackEntry: NavBackStackEntry?): Boolean {
    val currentRoute = currentBackStackEntry?.destination?.route
    val thisRoute =
        "${BuildConfig.APPLICATION_ID}.navigation.routes.SaleRoutes.${javaClass.simpleName}"
    return currentRoute == thisRoute
}