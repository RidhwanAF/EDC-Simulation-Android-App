package com.raf.edcsimulation.auth.data.models

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(
    @SerializedName("error")
    val error: String?,
    @SerializedName("message")
    val message: String?,
    @SerializedName("token")
    val token: String?,
)