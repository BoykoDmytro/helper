package com.bo.helper.data


const val INTERNET_CONNECTION_ERROR = -2
const val BAD_REQUEST = 400

sealed class PendingResult<T : Any?>

class SuccessResult<T: Any?>(val body: T) : PendingResult<T>()

class ErrorResult<T: Any?>(
    val code: Int,
    val message: String? = null,
    val error: Throwable? = null
) : PendingResult<T>()
