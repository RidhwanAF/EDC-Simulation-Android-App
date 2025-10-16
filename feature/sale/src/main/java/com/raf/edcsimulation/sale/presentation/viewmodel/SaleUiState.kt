package com.raf.edcsimulation.sale.presentation.viewmodel

import com.raf.edcsimulation.sale.domain.model.SaleData

data class SaleUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val saleData: SaleData? = null
)
