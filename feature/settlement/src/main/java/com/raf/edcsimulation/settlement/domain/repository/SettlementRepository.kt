package com.raf.edcsimulation.settlement.domain.repository

import com.raf.edcsimulation.core.domain.model.APIResult
import com.raf.edcsimulation.settlement.domain.models.SettlementData
import kotlinx.coroutines.flow.Flow

interface SettlementRepository {
    suspend fun processSettlement(): Flow<APIResult<SettlementData>>
}