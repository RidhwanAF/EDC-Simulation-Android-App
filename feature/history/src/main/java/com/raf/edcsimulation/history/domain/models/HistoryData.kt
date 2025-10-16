package com.raf.edcsimulation.history.domain.models

data class HistoryData(
    val id: Int,
    val merchant: String,
    val amount: Int,
    val cardNumber: String,
    val status: String,
)
