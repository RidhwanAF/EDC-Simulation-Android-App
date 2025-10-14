package com.raf.edcsimulation.core.domain.model

sealed class APIResult<out T> {
    data class Success<out T>(val data: T) : APIResult<T>()
    data class Error(val message: String) : APIResult<Nothing>()
    object Loading : APIResult<Nothing>()
}