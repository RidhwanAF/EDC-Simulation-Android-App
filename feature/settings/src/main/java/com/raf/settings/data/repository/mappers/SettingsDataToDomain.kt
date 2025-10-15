package com.raf.settings.data.repository.mappers

import com.raf.edcsimulation.core.domain.model.AppSettings
import com.raf.settings.data.models.DarkTheme
import com.raf.settings.data.models.Settings

object SettingsDataToDomain {
    fun Settings.toDomain() = AppSettings(
        darkTheme = darkTheme.toDomain(),
        dynamicColor = dynamicColor,
    )

    private fun DarkTheme.toDomain() = com.raf.edcsimulation.core.domain.model.DarkTheme.valueOf(name)
}