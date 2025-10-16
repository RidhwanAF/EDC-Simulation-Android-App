package com.raf.edcsimulation.history.data.repository.mappers

import com.raf.edcsimulation.history.data.models.HistoryItemData
import com.raf.edcsimulation.history.domain.models.HistoryData

object HistoryDataToDomain {
    fun HistoryItemData.toDomain() = HistoryData(
        id = id ?: 0,
        merchant = merchantId ?: "",
        amount = amount ?: 0,
        cardNumber = cardNumber ?: "",
        status = status ?: "",
    )
}