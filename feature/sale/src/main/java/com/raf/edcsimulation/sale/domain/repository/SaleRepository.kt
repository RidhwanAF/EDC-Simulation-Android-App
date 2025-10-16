package com.raf.edcsimulation.sale.domain.repository

import com.raf.edcsimulation.core.domain.model.APIResult
import com.raf.edcsimulation.sale.domain.models.SaleData
import kotlinx.coroutines.flow.Flow

interface SaleRepository {
    suspend fun submitSale(
        amount: Long,
        cardNumber: String,
        description: String?,
        merchantId: String,
    ): Flow<APIResult<SaleData>>
}