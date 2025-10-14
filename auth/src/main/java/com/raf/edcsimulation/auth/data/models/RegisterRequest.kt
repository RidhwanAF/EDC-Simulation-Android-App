package com.raf.edcsimulation.auth.data.models

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class RegisterRequest(
    @SerializedName("username")
    val username: String,
    @SerializedName("password")
    val password: String,
)
