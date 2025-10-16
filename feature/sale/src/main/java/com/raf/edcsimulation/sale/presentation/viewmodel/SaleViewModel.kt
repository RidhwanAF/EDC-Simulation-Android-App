package com.raf.edcsimulation.sale.presentation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.raf.edcsimulation.core.domain.model.APIResult
import com.raf.edcsimulation.core.domain.usecase.GetUserIdUseCase
import com.raf.edcsimulation.sale.domain.usecase.SubmitSaleUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SaleViewModel @Inject constructor(
    private val getUserIdUseCase: GetUserIdUseCase,
    private val submitSaleUseCase: SubmitSaleUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(SaleUiState())
    val uiState = _uiState.asStateFlow()

    var amount by mutableStateOf("")
        private set
    var cardNumber by mutableStateOf("")
        private set
    var description by mutableStateOf("")
        private set
    var merchantId by mutableStateOf("")
        private set

    init {
        getUserId()
    }

    private fun getUserId() {
        viewModelScope.launch {
            val userId = getUserIdUseCase()
            if (userId != null) {
                merchantId = "MRC$userId"
            }
        }
    }

    fun onSaleInputUpdate(
        amount: String? = null,
        cardNumber: String? = null,
        description: String? = null,
        merchantId: String? = null,
    ) {
        amount?.let { this.amount = it }
        cardNumber?.let { this.cardNumber = it }
        description?.let { this.description = it }
        merchantId?.let { this.merchantId = it }
    }

    fun processSale() {
        viewModelScope.launch {
            submitSaleUseCase(
                amount = amount.toLong(),
                cardNumber = cardNumber,
                description = description.ifBlank { null },
                merchantId = merchantId,
            ).collect { saleFlow ->
                when (saleFlow) {
                    is APIResult.Loading -> {
                        _uiState.update {
                            it.copy(isLoading = true)
                        }
                    }

                    is APIResult.Success -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = null,
                                saleData = saleFlow.data
                            )
                        }
                    }

                    is APIResult.Error -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = saleFlow.message,
                                saleData = null
                            )
                        }
                    }
                }
            }
        }
    }

    fun resetSaleState() {
        _uiState.update { SaleUiState() }
    }
}