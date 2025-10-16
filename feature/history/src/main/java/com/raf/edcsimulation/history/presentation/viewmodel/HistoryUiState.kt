package com.raf.edcsimulation.history.presentation.viewmodel

import com.raf.edcsimulation.history.domain.models.HistoryData

data class HistoryUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val sortDataAsc: Boolean = true,
    val data: List<HistoryData> = emptyList(),
    val settlementDataCount: Int = 0,
    val approvedDataCount: Int = 0,
)
