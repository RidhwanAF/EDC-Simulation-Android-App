package com.raf.edcsimulation.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class Routes {
    @Serializable
    object Auth : Routes() // Auth Parent

    @Serializable
    object Login: Routes()

    @Serializable
    object Register: Routes()

    @Serializable
    object MainMenu : Routes() // Home Parent

    @Serializable
    object Sale : Routes()

    @Serializable
    object History : Routes()

    @Serializable
    object Settlement : Routes()
}