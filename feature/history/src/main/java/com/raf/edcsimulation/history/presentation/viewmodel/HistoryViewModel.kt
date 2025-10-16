package com.raf.edcsimulation.history.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.raf.edcsimulation.core.domain.model.APIResult
import com.raf.edcsimulation.history.domain.usecase.FetchHistoryUseCase
import com.raf.edcsimulation.history.domain.usecase.GetHistoryLocalUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val fetchHistoryUseCase: FetchHistoryUseCase,
    private val getHistoryLocalUseCase: GetHistoryLocalUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(HistoryUiState())
    val uiState = _uiState.asStateFlow()

    init {
        fetchHistory()
        getLocalHistory()
    }

    fun sortData() {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update { currentState ->
                currentState.copy(
                    sortDataAsc = !currentState.sortDataAsc,
                )
            }

            _uiState.update {
                it.copy(
                    data = if (uiState.value.sortDataAsc) {
                        uiState.value.data.sortedBy { historyData ->
                            historyData.id
                        }
                    } else {
                        uiState.value.data.sortedByDescending { historyData ->
                            historyData.id
                        }
                    }
                )
            }
        }
    }

    private fun getLocalHistory() {
        viewModelScope.launch(Dispatchers.IO) {
            val history = getHistoryLocalUseCase().firstOrNull()
            Log.d(TAG, "getLocalHistory: $history")
            if (history == null) return@launch

            _uiState.update {
                it.copy(
                    data = if (it.sortDataAsc) history.sortedBy { historyData ->
                        historyData.id
                    } else history.sortedByDescending { historyData ->
                        historyData.id
                    },
                    settlementDataCount = history.count { historyData ->
                        historyData.status.equals("settled", true)
                    },
                    approvedDataCount = history.count { historyData ->
                        historyData.status.equals("approved", true)
                    }
                )
            }
        }
    }

    fun fetchHistory() {
        viewModelScope.launch(Dispatchers.IO) {
            fetchHistoryUseCase().collect { result ->
                when (result) {
                    is APIResult.Loading -> {
                        _uiState.update {
                            it.copy(
                                isLoading = true
                            )
                        }
                    }

                    is APIResult.Success -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = null,
                                data = if (it.sortDataAsc) result.data.sortedBy { historyData ->
                                    historyData.id
                                } else result.data.sortedByDescending { historyData ->
                                    historyData.id
                                },
                                settlementDataCount = result.data.count { historyData ->
                                    historyData.status.equals("settled", true)
                                },
                                approvedDataCount = result.data.count { historyData ->
                                    historyData.status.equals("approved", true)
                                }
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

    companion object {
        private const val TAG = "HistoryViewModel"
    }
}