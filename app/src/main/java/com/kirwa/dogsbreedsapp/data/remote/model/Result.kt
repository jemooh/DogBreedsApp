package com.kirwa.dogsbreedsapp.data.remote.model

/**
 * Sealed class for networking and UI states
 */
sealed class Result<out T> {
    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val exception: Exception) : Result<Nothing>()
    object Loading : Result<Nothing>()

    companion object {
        fun <T> success(data: T) = Success(data)
        fun error(exception: Exception) = Error(exception)
    }
}
