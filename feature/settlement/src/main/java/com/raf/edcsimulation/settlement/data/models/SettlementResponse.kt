package com.raf.edcsimulation.settlement.data.models

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class SettlementResponse(
    @SerializedName("message")
    val message: String?,
    @SerializedName("error")
    val error: String?,
    @SerializedName("records_updated")
    val recordsUpdated: Int?,
)
