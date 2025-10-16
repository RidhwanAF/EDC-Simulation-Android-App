package com.raf.edcsimulation.sale.data.model

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class SaleResponse(
    val error: String? = null,
    val status: String? = null,
    @SerializedName("transaction_id")
    val transactionId: String? = null,
)
