package com.raf.edcsimulation.settlement.domain.usecase

import com.raf.edcsimulation.settlement.domain.repository.SettlementRepository

class ProcessSettlementUseCase(private val settlementRepository: SettlementRepository) {
    suspend operator fun invoke() = settlementRepository.processSettlement()
}