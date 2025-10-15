package com.raf.edcsimulation.core.domain.usecase

import com.raf.edcsimulation.core.domain.contracts.AppSettingsProvider

class GetAppSettingsUseCase(private val appSettingsProvider: AppSettingsProvider) {
    operator fun invoke() = appSettingsProvider.getAppSettings()
}