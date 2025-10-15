package com.raf.edcsimulation.core.domain.usecase

import com.raf.edcsimulation.core.domain.contracts.AppSettingsProvider
import com.raf.edcsimulation.core.domain.model.AppSettings

class SetAppSettingsUseCase(private val appSettingsProvider: AppSettingsProvider) {
    suspend operator fun invoke(appSettings: AppSettings) {
        appSettingsProvider.setAppSettings(appSettings)
    }
}