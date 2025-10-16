package com.raf.edcsimulation.history.data.models

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

//@Serializable
//data class HistoryResponse(
//    val listData: List<HistoryItemData>? = null,
//)

@Serializable
data class HistoryItemData(
    @SerializedName("ID")
    val id: Int?,
    @SerializedName("CreatedAt")
    val createdAt: String?,
    @SerializedName("UpdatedAt")
    val updatedAt: String?,
    @SerializedName("DeletedAt")
    val deletedAt: String?,
    @SerializedName("UserID")
    val userId: String?,
    @SerializedName("MerchantID")
    val merchantId: String?,
    @SerializedName("Amount")
    val amount: Int?,
    @SerializedName("CardNumber")
    val cardNumber: String?,
    @SerializedName("Status")
    val status: String?,
)
