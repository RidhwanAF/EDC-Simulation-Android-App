package com.raf.edcsimulation.history.domain.usecase

import com.raf.edcsimulation.history.domain.repository.HistoryRepository

class GetHistoryLocalUseCase(private val historyRepository: HistoryRepository) {
    operator fun invoke() = historyRepository.getHistoryLocal()
}