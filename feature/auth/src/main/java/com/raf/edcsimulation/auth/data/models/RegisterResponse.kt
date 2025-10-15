package com.raf.edcsimulation.auth.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RegisterResponse(
    @SerialName("error")
    val error: String?,
    @SerialName("message")
    val message: String?,
)