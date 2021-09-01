package com.bo.helper.data.repository.base

import com.bo.helper.data.*
import com.bo.helper.data.INTERNET_CONNECTION_ERROR
import com.bo.helper.data.entity.mapper.NetworkMapper
import com.google.gson.JsonSyntaxException
import retrofit2.Call
import retrofit2.Response
import retrofit2.awaitResponse
import timber.log.Timber
import java.io.IOException
import java.util.concurrent.atomic.AtomicInteger
import javax.inject.Inject

open class BaseRepositoryImpl @Inject constructor() {

    var count = AtomicInteger()

    @Suppress("UNCHECKED_CAST")
    suspend fun <T, K : Any> execute(action: T, mapper: NetworkMapper<T, K>?): PendingResult<K> =
        try {
            mapper?.map(action)?.let { SuccessResult(it) } ?: SuccessResult(null as K)
        } catch (ex: Throwable) {
            ErrorResult(ERROR_EXECUTION, ex.message)
        }
    @Suppress("UNCHECKED_CAST")
    suspend fun <T, K : Any> request(call: Call<T>, mapper: NetworkMapper<T, K>): PendingResult<K> =
        try {
            val response = call.awaitResponse()
            when (response.isSuccessful) {
                true -> {
                    val body = response.body()?.let { mapper.map(it) } ?: Unit as K
                    SuccessResult(body)
                }
                else -> getErrorModel(response)
            }
        } catch (ex: IOException) {
            ErrorResult(INTERNET_CONNECTION_ERROR, ex.message)
        }

    private fun <T, K : Any> getErrorModel(response: Response<T>?): PendingResult<K> {
        val responseBody = response?.errorBody()
        val errorCode = response?.code() ?: INTERNET_CONNECTION_ERROR
        return try {
            ErrorResult(errorCode, null, null)
        } catch (jsonEx: JsonSyntaxException) {
            Timber.e(jsonEx)
            Timber.e("messageText: ${responseBody?.toString()}")
            ErrorResult(errorCode, response?.message())
        }
    }
}