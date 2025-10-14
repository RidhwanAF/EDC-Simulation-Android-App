package com.raf.edcsimulation.auth.data.models

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class ErrorResponse(
    @SerializedName("error")
    val error: String? = null,

    @SerializedName("message")
    val message: String? = null
)
