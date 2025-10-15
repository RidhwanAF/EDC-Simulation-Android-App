package com.raf.edcsimulation.navigation.routes

import kotlinx.serialization.Serializable

@Serializable
sealed class MainRoutes {
    @Serializable
    object Auth : MainRoutes() // Auth Parent

    @Serializable
    object Login : MainRoutes()

    @Serializable
    object Register : MainRoutes()

    @Serializable
    object MainMenu : MainRoutes() // Main Menu Parent

    @Serializable
    object Sale : MainRoutes()
}