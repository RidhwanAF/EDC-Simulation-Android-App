package com.raf.edcsimulation.core.domain.contracts

import com.raf.edcsimulation.core.domain.model.AppSettings
import kotlinx.coroutines.flow.Flow

interface AppSettingsProvider {
    suspend fun setAppSettings(appSettings: AppSettings)
    fun getAppSettings(): Flow<AppSettings>
    suspend fun resetSettings()
}