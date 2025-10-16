package com.raf.edcsimulation.sale.data.model

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class SaleBody(
    val amount: Long,
    @SerializedName("card_number")
    val cardNumber: String,
    val description: String? = null,
    @SerializedName("merchant_id")
    val merchantId: String,
)
