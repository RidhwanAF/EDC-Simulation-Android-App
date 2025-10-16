package com.raf.edcsimulation.history.domain.repository

import com.raf.edcsimulation.core.domain.model.APIResult
import com.raf.edcsimulation.history.domain.models.HistoryData
import kotlinx.coroutines.flow.Flow

interface HistoryRepository {
    fun fetchHistory(): Flow<APIResult<List<HistoryData>>>
    fun getHistoryLocal(): Flow<List<HistoryData>>
}