package com.raf.settings.data.models

import kotlinx.serialization.Serializable

@Serializable
data class Settings(
    val darkTheme: DarkTheme = DarkTheme.FOLLOW_SYSTEM,
    val dynamicColor: Boolean = false,
)

@Serializable
enum class DarkTheme {
    FOLLOW_SYSTEM,
    LIGHT,
    DARK
}
