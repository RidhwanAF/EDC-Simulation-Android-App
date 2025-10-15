package com.raf.edcsimulation.core.domain.model

data class AppSettings(
    val darkTheme: DarkTheme = DarkTheme.FOLLOW_SYSTEM,
    val dynamicColor: Boolean = false,
)

enum class DarkTheme {
    FOLLOW_SYSTEM,
    LIGHT,
    DARK
}
