package com.bo.helper.domain.repository

import com.bo.helper.data.PendingResult
import com.bo.helper.data.entity.presentation.discount.IDiscount

interface IDiscountRepository {

    suspend fun getDiscount(limit: Long, offset: Long): PendingResult<List<IDiscount>>
    suspend fun delete(discountUI: IDiscount): PendingResult<Void>
    suspend fun update(discountUI: IDiscount): PendingResult<Void>
}