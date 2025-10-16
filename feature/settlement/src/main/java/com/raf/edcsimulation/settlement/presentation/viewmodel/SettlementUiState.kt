package com.raf.edcsimulation.settlement.presentation.viewmodel

import com.raf.edcsimulation.settlement.domain.models.SettlementData

data class SettlementUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val data: SettlementData? = null,
)
