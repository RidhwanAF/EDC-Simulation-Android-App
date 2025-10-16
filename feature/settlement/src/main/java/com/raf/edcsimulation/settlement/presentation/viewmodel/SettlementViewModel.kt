package com.raf.edcsimulation.settlement.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.raf.edcsimulation.core.domain.model.APIResult
import com.raf.edcsimulation.settlement.domain.usecase.ProcessSettlementUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettlementViewModel @Inject constructor(
    private val processSettlementUseCase: ProcessSettlementUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettlementUiState())
    val uiState = _uiState.asStateFlow()

    fun settleNow() {
        viewModelScope.launch {
            processSettlementUseCase().collect { result ->
                when (result) {
                    is APIResult.Loading -> {
                        _uiState.update { it.copy(isLoading = true) }
                    }

                    is APIResult.Success -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = null,
                                data = result.data
                            )
                        }
                    }

                    is APIResult.Error -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = result.message
                            )
                        }
                    }
                }
            }
        }
    }

    fun resetState() {
        _uiState.update { SettlementUiState() }
    }
}