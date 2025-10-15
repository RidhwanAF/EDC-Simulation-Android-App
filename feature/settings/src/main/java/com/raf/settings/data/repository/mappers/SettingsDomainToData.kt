package com.raf.settings.data.repository.mappers

import com.raf.edcsimulation.core.domain.model.AppSettings
import com.raf.edcsimulation.core.domain.model.DarkTheme
import com.raf.settings.data.models.Settings

object SettingsDomainToData {
    fun AppSettings.toSettings() = Settings(
        darkTheme = darkTheme.toDarkThemeSettings(),
        dynamicColor = dynamicColor,
    )

    private fun DarkTheme.toDarkThemeSettings() = com.raf.settings.data.models.DarkTheme.valueOf(name)
}