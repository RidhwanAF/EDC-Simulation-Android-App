package com.raf.edcsimulation.sale.data.models

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class SaleBody(
    val amount: Long,
    @SerializedName("CardNumber")
    val cardNumber: String,
    val description: String? = null,
    @SerializedName("MerchantID")
    val merchantId: String,
)
