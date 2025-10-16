package com.raf.edcsimulation.auth.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserJWT(
    val exp: Long,
    @SerialName("user_id")
    val userId: Int,
)
