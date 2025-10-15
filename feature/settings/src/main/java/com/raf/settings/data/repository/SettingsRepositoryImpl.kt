package com.raf.settings.data.repository

import com.raf.edcsimulation.core.domain.contracts.AppSettingsProvider
import com.raf.edcsimulation.core.domain.model.AppSettings
import com.raf.settings.data.local.SettingsDataStore
import com.raf.settings.data.repository.mappers.SettingsDataToDomain.toDomain
import com.raf.settings.data.repository.mappers.SettingsDomainToData.toSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SettingsRepositoryImpl @Inject constructor(
    private val settingsDataStore: SettingsDataStore,
) : AppSettingsProvider {

    override suspend fun setAppSettings(appSettings: AppSettings) {
        settingsDataStore.saveSettings(appSettings.toSettings())
    }

    override fun getAppSettings(): Flow<AppSettings> {
        return settingsDataStore.getSettings().map { settings ->
            settings.toDomain()
        }
    }

    override suspend fun resetSettings() {
        settingsDataStore.clearSettings()
    }
}